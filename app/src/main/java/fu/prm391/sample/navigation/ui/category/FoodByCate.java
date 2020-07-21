package fu.prm391.sample.navigation.ui.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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
import fu.prm391.sample.navigation.adapter.FoodAdapter;
import fu.prm391.sample.navigation.model.Category;
import fu.prm391.sample.navigation.model.Food;
import fu.prm391.sample.navigation.ui.home.DetailFood;

public class FoodByCate extends AppCompatActivity implements FoodAdapter.customdetailListener{
    private ArrayList<Food> foods;
    private RecyclerView RecyclerFood;
    private FoodAdapter foodAdapter;
    public static int REQ = 100;
    public static int RES = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_by_cate);
        foods = new ArrayList<>();
        Intent i = getIntent();
        Category category = (Category) i.getSerializableExtra("model");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference folderr = storageRef.child("food");
        db.collection("food")
                .whereEqualTo("category_type",category.getId())
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
                                String im = "food/"+docu.get("img").toString();
                                folderr.child(docu.get("img").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        c.setImg(uri.toString());
                                        foods.add(c);
                                        RecyclerFood = findViewById(R.id.recyclerviewfoodbycate);
                                        RecyclerFood.setHasFixedSize(true);
                                        foodAdapter = new FoodAdapter(FoodByCate.this, foods);
                                        foodAdapter.setCustomdetailListner(FoodByCate.this);
                                        RecyclerFood.setAdapter(foodAdapter);
                                        RecyclerFood.setLayoutManager(new GridLayoutManager(FoodByCate.this, 2));
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
    }

    public void returnview(View view){
        finishActivity(CategoryFragment.RES);
    }

    @Override
    public void onDetailClickListner(int position, Food f) {
        Intent i = new Intent(this, DetailFood.class);
        i.putExtra("model",f);
        startActivityForResult(i,REQ);
    }
}
