package fu.prm391.sample.navigation.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.adapter.Toastv2;
import fu.prm391.sample.navigation.model.Food;

public class DetailFood extends AppCompatActivity {
    public static int REQ = 100;
    public static int RES = 200;
    private TextView textView;
    private ImageView img;
    private TextView name;
    private String food_id;
    private CheckBox ck;
    private String fileName = "wishlist.txt";
    private String filePath = "/FoodRepice";
    private File myExternalFile;
    private Food temp;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        list = new ArrayList<>();
        File pathFile = new File(Environment.getExternalStorageDirectory() + filePath);
        myExternalFile = new File(pathFile, fileName);
        try{
            FileInputStream br = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(br);
            BufferedReader da = new BufferedReader(
                    new InputStreamReader(in));
            String strLine;
            while ((strLine = da.readLine()) != null) {
                if(!strLine.isEmpty())
                    list.add(strLine);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView = findViewById(R.id.textView);
        img = findViewById(R.id.imageView);
        name = findViewById(R.id.food_name);
        ck = findViewById(R.id.checkBox);
        Intent i = getIntent();
        Food f = (Food) i.getSerializableExtra("model");
        if(list.contains(f.getId())) ck.setChecked(true);
        temp = f;
        food_id = f.getId();
        textView.setText(f.getStep());
        name.setText(f.getName());
        Glide.with(DetailFood.this)
                .load(f.getImg())
                .fitCenter()
                .into(img);

    }

    public void getIn(View view) {
        Intent i = new Intent(this, Ingredients.class);
        i.putExtra("id", food_id);
        startActivityForResult(i, REQ);
    }

    public void addwishlist(View view) throws IOException {
        if(ck.isChecked()){
            FileOutputStream fos = new FileOutputStream(myExternalFile,true);
            fos.write(String.format("\n"+temp.getId()).getBytes());
            fos.close();
            Toastv2.makeText(DetailFood.this,"Yêu thích",Toastv2.LENGTH_SHORT,Toastv2.SUCCESS,true).show();
        }else{
            if(list.contains(temp.getId())){
                list.remove(temp.getId());
                String s ="";
                for(String t : list){
                    s += t+"\n";
                }
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write(s.getBytes());
                fos.close();
            }
        }

    }
}
