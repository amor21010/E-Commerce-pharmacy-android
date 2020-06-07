package com.aboesmail.omar.pharma.Activates.Adapers;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.aboesmail.omar.pharma.Api.order.Order;
import com.aboesmail.omar.pharma.R;
import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

public class OrderHistoryAdapter extends Adapter<OrderHistoryAdapter.ViewHolder> {
    List<Order> orders;

    public OrderHistoryAdapter(List<Order> orders) {
        this.orders = orders;
    }


    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    public interface OnItemClickListener {
        void OrderDetailsClick(int position);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historyitem, parent, false);
        return new ViewHolder(view,mListener);
    }


    public Order getOrderInPosition(int position){
        return orders.get(position);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextDrawable drawable1;
        Order currentOrder = orders.get(position);
        String price = currentOrder.getTotalPrice();

        holder.totalPrice.setText("Total : " + price + " L.E");
        holder.time.setText(currentOrder.getTime());

        if (currentOrder.getDelivary() != null)
            holder.delivery.setText("delivery : " + currentOrder.getDelivary());
        else
            holder.delivery.setVisibility(View.GONE);
        if (currentOrder.getSeller() != null)
            holder.seller.setText("seller : " + currentOrder.getSeller());
        else
            holder.seller.setVisibility(View.GONE);

        switch (currentOrder.getStatus()) {
            case "shipped": {
                drawable1 = TextDrawable.builder()
                        .buildRoundRect((currentOrder.getStatus().substring(0, 2).toUpperCase()), Color.YELLOW, 30);
                break;
            }
            case ("waiting"): {
                drawable1 = TextDrawable.builder()
                        .buildRoundRect((currentOrder.getStatus().substring(0, 1).toUpperCase()), Color.BLUE, 30);
                break;
            }
            case "done": {
                drawable1 = TextDrawable.builder()
                        .buildRoundRect((currentOrder.getStatus().substring(0, 1).toUpperCase()), Color.GREEN, 30);
                break;
            }
            default:
                drawable1 = TextDrawable.builder()
                        .buildRoundRect((currentOrder.getStatus().substring(0, 1).toUpperCase()), Color.BLUE, 30);

        }
        holder.status.setImageDrawable(drawable1);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView status;
        TextView time;
        TextView totalPrice;
        TextView seller;
        TextView delivery;
        Button orderDetails;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
            totalPrice = itemView.findViewById(R.id.totalOrder);
            seller = itemView.findViewById(R.id.seller);
            orderDetails = itemView.findViewById(R.id.OrderDetails);
            delivery = itemView.findViewById(R.id.delivery);


            orderDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int pos=getAdapterPosition();
                        listener.OrderDetailsClick(pos);
                    }
                }
            });

        }
    }
}
