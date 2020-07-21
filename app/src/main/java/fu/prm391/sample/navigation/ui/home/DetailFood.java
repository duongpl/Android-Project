package fu.prm391.sample.navigation.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.Food;

public class DetailFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        Intent i = getIntent();
        Food f = (Food)i.getSerializableExtra("model");
        Toast.makeText(this,f.getId()+f.getName(),Toast.LENGTH_LONG).show();
    }
}
