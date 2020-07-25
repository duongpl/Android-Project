package fu.prm391.sample.navigation.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.adapter.FoodAdapter;
import fu.prm391.sample.navigation.model.Food;

public class HomeFragment extends Fragment implements FoodAdapter.customdetailListener{
    private ArrayList<Food> foods;
    private RecyclerView RecyclerFood;
    private FoodAdapter foodAdapter;
    private String fileName = "wishlist.txt";
    private String filePath = "/FoodRepice";
    private File myExternalFile;
    public static int REQ = 100;
    public static int RES = 200;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foods = new ArrayList<>();
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        File pathFile = new File(Environment.getExternalStorageDirectory()
                + filePath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        isStoragePermissionGranted();
        myExternalFile = new File(pathFile, fileName);
        if(!myExternalFile.exists()) {
            try {
                myExternalFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference folderr = storageRef.child("food");
        db.collection("food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot docu : task.getResult()) {
                                final Food c = new Food();
                                c.setId(docu.getId());
                                c.setName(docu.get("name").toString());
                                c.setCategory_type(docu.get("category_type").toString());
                                String builder = docu.get("step").toString().replaceAll("Bước","\n\nBước");
                                c.setStep(builder);
                                String im = "food/"+docu.get("img").toString();
                                folderr.child(docu.get("img").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        c.setImg(uri.toString());
                                        foods.add(c);
                                        RecyclerFood = root.findViewById(R.id.recyclerviewhome);
                                        RecyclerFood.setHasFixedSize(true);
                                        foodAdapter = new FoodAdapter(getContext(), foods);
                                        foodAdapter.setCustomdetailListner(HomeFragment.this);
                                        RecyclerFood.setAdapter(foodAdapter);
                                        RecyclerFood.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        //
                                    }
                                });
                            }
                        }
                    }
                });
        return root;
    }

    @Override
    public void onDetailClickListner(int position, Food f) {
        Intent i = new Intent(getContext(), DetailFood.class);
        i.putExtra("model",f);
        startActivityForResult(i,REQ);
    }

    public void isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CONTACTS}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
        }
    }
}
