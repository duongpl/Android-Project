package fu.prm391.sample.navigation.ui.wishlist;

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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.adapter.FoodAdapter;
import fu.prm391.sample.navigation.model.Food;
import fu.prm391.sample.navigation.ui.home.DetailFood;

public class WishlistFragment extends Fragment implements FoodAdapter.customdetailListener {
    private TextView tv;
    private ArrayList<Food> foods;
    private RecyclerView RecyclerFood;
    private FoodAdapter foodAdapter;
    public static int REQ = 100;
    public static int RES = 200;
    private String fileName = "wishlist.txt";
    private String filePath = "/FoodRepice";
    private File myExternalFile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        foods = new ArrayList<>();
        tv = root.findViewById(R.id.textView2);
        RecyclerFood = root.findViewById(R.id.recyclerviewwishlist);
        RecyclerFood.setHasFixedSize(true);
        File pathFile = new File(Environment.getExternalStorageDirectory() + filePath);
        myExternalFile = new File(pathFile, fileName);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
        } else {
        }
        final ArrayList<String> listID = getWishlist();
        if(listID.size() == 0) RecyclerFood.setVisibility(View.INVISIBLE);
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
                                if (listID.contains(docu.getId())) {
                                    final Food c = new Food();
                                    c.setId(docu.getId());
                                    c.setName(docu.get("name").toString());
                                    c.setCategory_type(docu.get("category_type").toString());
                                    String builder = docu.get("step").toString().replaceAll("Bước", "\n\nBước");
                                    c.setStep(builder);
                                    String im = "food/" + docu.get("img").toString();
                                    folderr.child(docu.get("img").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            c.setImg(uri.toString());
                                            foods.add(c);
                                            foodAdapter = new FoodAdapter(getContext(), foods);
                                            foodAdapter.setCustomdetailListner(WishlistFragment.this);
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
                    }
                });
        return root;
    }

    @Override
    public void onDetailClickListner(int position, Food f) {
        Intent i = new Intent(getContext(), DetailFood.class);
        i.putExtra("model", f);
        startActivity(i);
    }


    public ArrayList<String> getWishlist() {
        ArrayList<String> list = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if(!strLine.isEmpty())
                    list.add(strLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    /*
     * Kiểmtra xem device có bộ nhớ ngoài không
     */
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
