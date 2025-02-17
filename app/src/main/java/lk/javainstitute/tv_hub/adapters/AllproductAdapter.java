package lk.javainstitute.tv_hub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.models.AllProductModel;
import lk.javainstitute.tv_hub.ui.Details.DetailedActivity;

public class AllproductAdapter extends RecyclerView.Adapter<AllproductAdapter.ViewHolder> {

    Context context;
    List<AllProductModel> allProductModelList;

    public AllproductAdapter(Context context, List<AllProductModel> allProductModelList) {
        this.context = context;
        this.allProductModelList = allProductModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(allProductModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(allProductModelList.get(position).getName());
        holder.price.setText(allProductModelList.get(position).getPrice());
        holder.qty.setText(allProductModelList.get(position).getQty());
        holder.qty.setText(allProductModelList.get(position).getQty());
        holder.description.setText(allProductModelList.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detail", allProductModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, price, qty, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pro_img);
            name = itemView.findViewById(R.id.pro_name);
            price = itemView.findViewById(R.id.pro_price);
            qty = itemView.findViewById(R.id.pro_qty);
            description = itemView.findViewById(R.id.pro_dec);

        }
    }
}
