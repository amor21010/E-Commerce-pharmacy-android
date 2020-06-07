package com.aboesmail.omar.pharma.Activates.Adapers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;


import com.aboesmail.omar.pharma.Api.Product.Product;
import com.aboesmail.omar.pharma.R;
import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {


    private String currentID;
    private int NewQ;
    private int oldQ;
    private List<Product> products;
    private List<Integer> Quantities;


    private MutableLiveData<List<Integer>> liveDataQ = new MutableLiveData<>();
    private Context context;
    private OnItemClickListener mListener;


    public OrderDetailsAdapter(List<Product> products, List<Integer> quantities, Context context) {
        this.products = products;
        Quantities = quantities;
        this.context = context;
    }

    public List<Integer> getQuantities() {
        return Quantities;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_product, parent, false);

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Product product = products.get(position);
        final int Quantity = Quantities.get(position);
        currentID = product.getId();
        oldQ = Quantity;
        holder.drugName.setText(product.getEnglish_Name());
        holder.price.setText(String.valueOf(product.getPrice()));
        Glide.with(context).asBitmap().load(product.getPhotoUrl()).into(holder.drugPhoto);
        holder.picker.setMax((int) product.getAvailableQuantity());
        holder.picker.setProgress(Quantity);
        final TextDrawable drawable = TextDrawable.builder()
                .buildRoundRect(String.valueOf(Quantity), Color.parseColor("#99B0DC"), 30);


        holder.progress.setImageDrawable(drawable);
        NewQ = holder.picker.getProgress();
        holder.picker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress != Quantity) {
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRoundRect(String.valueOf(progress), Color.parseColor("#FFD01F"), 30);
                    NewQ = holder.picker.getProgress();
                    Quantities.remove(position);
                    Quantities.add(position, NewQ);
                    holder.progress.setImageDrawable(drawable);
                    liveDataQ.setValue(Quantities);

                } else {

                    holder.progress.setImageDrawable(drawable);

                }
                liveDataQ.setValue(Quantities);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                liveDataQ.setValue(Quantities);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                NewQ = holder.picker.getProgress();
                Quantities.remove(position);
                Quantities.add(position, holder.picker.getProgress());
                liveDataQ.setValue(Quantities);
            }
        });
//        NewQ = holder.picker.getProgress();
        //    holder.sum.setText(String.valueOf(holder.picker.getProgress()));
        Quantities.remove(position);
        Quantities.add(position, holder.picker.getProgress());
        liveDataQ.setValue(Quantities);
    }

    public MutableLiveData<List<Integer>> getLiveDataQ() {
        return liveDataQ;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public int getOldQ() {
        return oldQ;
    }

    public Integer getNewQuantity() {
        return NewQ;
    }

    public String getUpdated() {
        return currentID;
    }

    public interface OnItemClickListener {
        void OrderUpdateClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView drugName;
        ImageView drugPhoto;
        TextView price;
        //  TextView sum;
        ImageView progress;
        // Button button;
        SeekBar picker;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener Listener) {
            super(itemView);

            drugName = itemView.findViewById(R.id.drugname);
            drugPhoto = itemView.findViewById(R.id.odrugphoto);
            price = itemView.findViewById(R.id.odrugprice);
            progress = itemView.findViewById(R.id.current);
            picker = itemView.findViewById(R.id.updateNumber);
            //  button = itemView.findViewById(R.id.updateQ);
            /*            sum = itemView.findViewById(R.id.sum);*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int pos = getAdapterPosition();
                        mListener.OrderUpdateClick(pos);
                    }
                }
            });


        }
    }
}
