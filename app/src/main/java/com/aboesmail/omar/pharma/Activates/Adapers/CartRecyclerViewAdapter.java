package com.aboesmail.omar.pharma.Activates.Adapers;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aboesmail.omar.pharma.Database.product.CartProduct;
import com.aboesmail.omar.pharma.R;
import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartVeiwHolder> {
    private String TAG;
    private List<CartProduct> cartProducts = new ArrayList<>();


    @NonNull
    @Override
    public CartVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_list, parent, false);
        return new CartVeiwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartVeiwHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        String name = cartProducts.get(position).getEnglish_Name();
        String price = String.valueOf(cartProducts.get(position).getPrice());

        holder.drugName.setText(name);
        holder.drugPrice.setText(price);
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(String.valueOf(cartProducts.get(position).getCount()), Color.RED, 30);
        holder.image.setImageDrawable(drawable1);

    }

    public CartProduct getCartAtPos(int position) {
        return cartProducts.get(position);
    }


    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public void setCartData(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
        notifyDataSetChanged();
    }

    class CartVeiwHolder extends RecyclerView.ViewHolder {
        TextView drugName;
        TextView drugPrice;
        ImageView image;

        CartVeiwHolder(@NonNull View itemView) {
            super(itemView);
            drugName = itemView.findViewById(R.id.cartdrugname);
            drugPrice = itemView.findViewById(R.id.cartdrugprice);
            image = itemView.findViewById(R.id.cartdrugphoto);

        }

    }


}
