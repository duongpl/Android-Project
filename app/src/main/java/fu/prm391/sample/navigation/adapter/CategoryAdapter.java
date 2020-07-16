package fu.prm391.sample.navigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Category> category;

    public CategoryAdapter(Context mContext, ArrayList<Category> categories) {
        this.context = mContext;
        this.category = categories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_category;
        private TextView name_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_category = itemView.findViewById(R.id.image);
            name_category = itemView.findViewById(R.id.text_name);
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
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category f = category.get(position);
        Glide.with(context)
                .load(f.getImg())
                .fitCenter()
                .into(holder.img_category);
//        Picasso.get()
//                .load(f.getImg())
//                .fit()
//                .into(holder.img_food);
        holder.name_category.setText(f.getName());
    }

    @Override
    public int getItemCount() {
        return category.size();
    }
}
