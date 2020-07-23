package fu.prm391.sample.navigation.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.Food;

public class DetailFood extends AppCompatActivity {
    public static int REQ = 100;
    public static int RES = 200;
    private TextView textView;
    private ImageView img;
    private TextView name;
    private String food_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        textView = findViewById(R.id.textView);
        img = findViewById(R.id.imageView);
        name = findViewById(R.id.food_name);
        Intent i = getIntent();
        Food f = (Food) i.getSerializableExtra("model");
        food_id = f.getId();
        textView.setText(f.getStep());
        name.setText(f.getName());
        Glide.with(DetailFood.this)
                .load(f.getImg())
                .fitCenter()
                .into(img);

    }

    public void getIn(View view){
        Intent i = new Intent(this,Ingredients.class);
        i.putExtra("id",food_id);
        startActivityForResult(i,REQ);
    }
}
