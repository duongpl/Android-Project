package fu.prm391.sample.navigation.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.adapter.FoodAdapter;
import fu.prm391.sample.navigation.model.Category;
import fu.prm391.sample.navigation.model.Food;

public class HomeFragment extends Fragment {
    String img_temp = "";
    private ArrayList<Food> foods;
    private RecyclerView RecyclerFood;
    private FoodAdapter foodAdapter;
    private ArrayList<Category> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
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
                                StorageReference imgre = folderr.child(docu.get("img").toString());
                                imgre.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        c.setImg(uri.toString());
                                        foods.add(c);
                                        Toast.makeText(getContext(), img_temp, Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            RecyclerFood = root.findViewById(R.id.recyclerviewhome);
                            foodAdapter = new FoodAdapter(getContext(), foods);
                            RecyclerFood.setAdapter(foodAdapter);
                            RecyclerFood.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        }


                    }
                });
        return root;
    }
}
