package fu.prm391.sample.navigation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fu.prm391.sample.navigation.R;
import fu.prm391.sample.navigation.model.ingredients;

public class IngreAdapter extends RecyclerView.Adapter<IngreAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ingredients> ingre;

    public IngreAdapter(Context mContext, ArrayList<ingredients> ingres) {
        this.context = mContext;
        this.ingre = ingres;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView name;
        private TextView amount;
        private LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.text_name);
            amount = itemView.findViewById(R.id.amount);
            layout = itemView.findViewById(R.id.layoutingre);
        }
    }

    @NonNull
    @Override
    public IngreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.item_ingre, parent, false);
        IngreAdapter.ViewHolder viewHolder = new IngreAdapter.ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ingredients f = ingre.get(position);
        Glide.with(context)
                .load(f.getImg())
                .fitCenter()
                .into(holder.img);
//        Picasso.get()
//                .load(f.getImg())
//                .fit()
//                .into(holder.img_food);
        holder.name.setText(f.getName());
        holder.amount.setText(f.getAmount());
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
        return ingre.size();
    }

    customdetailListener detailListner;

    public interface customdetailListener {
        public void onDetailClickListner(int position, ingredients f);
    }

    public void setCustomdetailListner(customdetailListener listener) {
        this.detailListner = listener;
    }
}
