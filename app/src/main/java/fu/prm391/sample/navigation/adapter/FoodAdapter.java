package fu.prm391.sample.navigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.Category;
import fu.prm391.sample.navigation.model.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Food> food;

    public FoodAdapter(Context mContext, ArrayList<Food> foods) {
        this.context = mContext;
        this.food = foods;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_food;
        private TextView name_food;
        private LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_food = itemView.findViewById(R.id.image);
            name_food = itemView.findViewById(R.id.text_name);
            layout = itemView.findViewById(R.id.layouthome);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Food f = food.get(position);
        Glide.with(context)
                .load(f.getImg())
                .fitCenter()
                .into(holder.img_food);
//        Picasso.get()
//                .load(f.getImg())
//                .fit()
//                .into(holder.img_food);
        holder.name_food.setText(f.getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailListner != null) {
                    detailListner.onDetailClickListner(position,f);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    FoodAdapter.customdetailListener detailListner;

    public interface customdetailListener {
        public void onDetailClickListner(int position, Food f);
    }

    public void setCustomdetailListner(FoodAdapter.customdetailListener listener) {
        this.detailListner = listener;
    }
}
