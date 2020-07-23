package fu.prm391.sample.navigation.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

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

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.adapter.IngreAdapter;
import fu.prm391.sample.navigation.model.ingredients;

public class Ingredients extends AppCompatActivity implements IngreAdapter.customdetailListener{
    private ArrayList<ingredients> ingredients;
    private RecyclerView RecyclerIngre;
    private IngreAdapter ingreAdapter;
    private String id;
    public static int REQ = 100;
    public static int RES = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ingredients = new ArrayList<>();
        final Intent i = getIntent();
        id = i .getStringExtra("id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseFirestore ingre = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference folderr = storageRef.child("ingredient");
        db.collection("foodingre")
                .whereEqualTo("foodid",id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot docu : task.getResult()) {
                                final ingredients i = new ingredients();
                                i.setAmount(docu.get("amount").toString());
                                DocumentReference docRef = ingre.collection("ingredients").document(docu.get("ingreid").toString());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                i.setId(document.getId());
                                                i.setName(document.get("name").toString());
                                                folderr.child(document.get("img").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        i.setImg(uri.toString());
                                                        ingredients.add(i);
                                                        RecyclerIngre = findViewById(R.id.recyclerIngre);
                                                        RecyclerIngre.setHasFixedSize(true);
                                                        ingreAdapter = new IngreAdapter(Ingredients.this, ingredients);
                                                        ingreAdapter.setCustomdetailListner(Ingredients.this);
                                                        RecyclerIngre.setAdapter(ingreAdapter);
                                                        RecyclerIngre.setLayoutManager(new GridLayoutManager(Ingredients.this, 3));
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
    public void onDetailClickListner(int position, fu.prm391.sample.navigation.model.ingredients f) {
//        Intent i = new Intent(this, FoodByIngre.class);
//        i.putExtra("model",f);
//        startActivityForResult(i,REQ);
    }
}
