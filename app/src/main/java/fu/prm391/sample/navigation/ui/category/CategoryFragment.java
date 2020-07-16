package fu.prm391.sample.navigation.ui.category;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.adapter.CategoryAdapter;
import fu.prm391.sample.navigation.adapter.FoodAdapter;
import fu.prm391.sample.navigation.model.Category;
import fu.prm391.sample.navigation.model.Food;

public class CategoryFragment extends Fragment {
    private ArrayList<Category> categories;
    private RecyclerView RecyclerCategory;
    private CategoryAdapter categoryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        categories = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference folderr = storageRef.child("category");
        db.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot docu : task.getResult()) {
                                final Category c = new Category();
                                c.setId(docu.getId());
                                c.setName(docu.get("name").toString());
                                String im = "category/"+docu.get("img").toString();
                                folderr.child(docu.get("img").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        c.setImg(uri.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        //
                                    }
                                });
                                categories.add(c);
                            }
                            RecyclerCategory = root.findViewById(R.id.recyclerviewcategory);
                            RecyclerCategory.setHasFixedSize(true);
                            categoryAdapter = new CategoryAdapter(getContext(), categories);
                            RecyclerCategory.setAdapter(categoryAdapter);
                            RecyclerCategory.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });
        return root;
    }
}
