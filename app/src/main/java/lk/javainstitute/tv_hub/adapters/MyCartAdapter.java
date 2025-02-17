package lk.javainstitute.tv_hub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import lk.javainstitute.tv_hub.R;
import lk.javainstitute.tv_hub.models.MyCartModel;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCartModel> cartModelList;
    int totalPrice = 0;

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(cartModelList.get(position).getProductImg()).into(holder.img);

        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText("Rs. " +cartModelList.get(position).getProductPrice()+ ".00");
        holder.date.setText(cartModelList.get(position).getProductDate());
        holder.time.setText(cartModelList.get(position).getProductTime());
        holder.quantity.setText("Quantity : " +cartModelList.get(position).getProductTotalQty());
        holder.totalPrice.setText(String.valueOf("Rs. " +cartModelList.get(position).getProductTotalPrice()+ ".00"));

        // Calculate total price
        totalPrice = totalPrice + cartModelList.get(position).getProductTotalPrice();
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount",totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,date,time,quantity,totalPrice;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.cart_img);
            name = itemView.findViewById(R.id.cart_name);
            price = itemView.findViewById(R.id.cart_price);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            quantity = itemView.findViewById(R.id.cart_qty);
            totalPrice = itemView.findViewById(R.id.cart_totalprice);

        }
    }
}
