package fu.prm391.sample.navigation.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import fu.prm391.sample.navigation.adapter.FoodAdapter;
import fu.prm391.sample.navigation.adapter.FoodByIngreAdapter;
import fu.prm391.sample.navigation.adapter.IngreAdapter;
import fu.prm391.sample.navigation.model.Food;
import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.ingredients;

public class FoodByIngre extends AppCompatActivity implements FoodByIngreAdapter.customdetailListener {
    private ArrayList<Food> foods;
    private RecyclerView RecyclerFood;
    private FoodByIngreAdapter foodAdapter;
    private ImageView img;
    private TextView tv;
    public static int REQ = 100;
    public static int RES = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_by_ingre);
        foods = new ArrayList<>();
        Intent i = getIntent();
        ingredients ingre  = (ingredients) i.getSerializableExtra("model");
        tv = findViewById(R.id.repice);
        img = findViewById(R.id.img);
        Glide.with(this)
                .load(ingre.getImg())
                .fitCenter()
                .into(img);
        tv.setText(String.format("Các món ăn với %s",ingre.getName()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseFirestore f = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference folderr = storageRef.child("food");
        db.collection("foodingre")
                .whereEqualTo("ingreid",ingre.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot docu : task.getResult()) {
                                final Food fo = new Food();
                                DocumentReference docRef = f.collection("food").document(docu.get("foodid").toString());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                fo.setId(document.getId());
                                                fo.setName(document.get("name").toString());
                                                fo.setCategory_type(document.get("category_type").toString());
                                                String builder = document.get("step").toString().replaceAll("Bước","\n\nBước");
                                                fo.setStep(builder);
                                                folderr.child(document.get("img").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        fo.setImg(uri.toString());
                                                        foods.add(fo);
                                                        RecyclerFood = findViewById(R.id.recyclerfoodbyrepice);
                                                        RecyclerFood.setHasFixedSize(true);
                                                        foodAdapter = new FoodByIngreAdapter(FoodByIngre.this, foods);
                                                        foodAdapter.setCustomdetailListner(FoodByIngre.this);
                                                        RecyclerFood.setAdapter(foodAdapter);
                                                        RecyclerFood.setLayoutManager(new GridLayoutManager(FoodByIngre.this, 2));
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        //
                                                    }
                                                });
                                            } else { }
                                        } else { }
                                    }
                                });

                            }
                        }
                    }
                });
    }

    @Override
    public void onDetailClickListner(int position, Food f) {
        Intent i = new Intent(this, DetailFood.class);
        i.putExtra("model",f);
        startActivityForResult(i,REQ);
    }
}
