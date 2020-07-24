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

import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.Category;
import fu.prm391.sample.navigation.model.Food;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Category> category;

    public CategoryAdapter(Context mContext, ArrayList<Category> categories) {
        this.context = mContext;
        this.category = categories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private ImageView img_category;
        private TextView name_category;
        private LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            img_category = itemView.findViewById(R.id.image);
            name_category = itemView.findViewById(R.id.text_name);
            layout = itemView.findViewById(R.id.item_food);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.category_image, parent, false);
        CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, final int position) {
        final Category f = category.get(position);
        holder.id.setText(f.getId());
        Glide.with(context)
                .load(f.getImg())

                .into(holder.img_category);
//        Picasso.get()
//                .load(f.getImg())
//                .fit()
//                .into(holder.img_food);
        holder.name_category.setText(f.getName());
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
        return category.size();
    }

    customdetailListener detailListner;

    public interface customdetailListener {
        public void onDetailClickListner(int position, Category f);
    }

    public void setCustomdetailListner(customdetailListener listener) {
        this.detailListner = listener;
    }

}
