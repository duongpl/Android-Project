package fu.prm391.sample.navigation.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.Category;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Category c;
    private ArrayList<Category> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final ImageView imgview = root.findViewById(R.id.imageView2);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("category")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot docu : task.getResult()){
                                        Category c = new Category();
                                        c.setId(docu.getId());
                                        c.setName(docu.get("name").toString());
                                        c.setImg(docu.get("img").toString());
                                        list.add(c);
                                    }
                                }
                                textView.setText(String.valueOf(list.size()));
                                int id = getResources().getIdentifier(list.get(0).getImg(), "drawable", getContext().getPackageName());
                                imgview.setImageResource(id);
                            }
                        });
            }
        });
        return root;
    }
}
