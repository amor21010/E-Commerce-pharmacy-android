package com.aboesmail.omar.pharma.Activates.Adapers.HomeActivityAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aboesmail.omar.pharma.Api.Category.Category;
import com.aboesmail.omar.pharma.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class CatagoryRecyclerAdapter extends RecyclerView.Adapter<CatagoryRecyclerAdapter.CategoryViewHolder> {

    //vars
    private List<Category> mCategory;
    private Context mContext;
    private OnItemClickListener mListener;


    public CatagoryRecyclerAdapter(Context mContext, List<Category> mCategory) {
        this.mCategory = mCategory;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_list, parent, false);

        return new CategoryViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.category.setText(mCategory.get(position).getCategory());
        Glide.with(mContext).asBitmap()
                .load(mCategory.get(position).getIcon())
                .into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public Category getmCategory(int pos) {
        return mCategory.get(pos);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView category;
        private ImageView icon;
        private Button addToCart;

        CategoryViewHolder(@NonNull View itemView, final OnItemClickListener Listener) {
            super(itemView);
            category = itemView.findViewById(R.id.catagory_name);
            icon = itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
