package com.aboesmail.omar.pharma.Activates.Adapers.HomeActivityAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aboesmail.omar.pharma.Api.Product.Product;
import com.aboesmail.omar.pharma.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductRecyclerAdabter extends RecyclerView.Adapter<ProductRecyclerAdabter.ProductViewHolder>
        implements Filterable {
    private List<Product> ProductList = new ArrayList<>();
    private String filterType = null;
    private Context mContext;
    private List<Product> fullList;
    private OnItemClickListener mListener;
    private Filter productNameFilter = new Filter() {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredProducts = new ArrayList<>();
            if (constraint.length() == 0) {
                filteredProducts.addAll(fullList);
            } else {
                String filter = constraint.toString().toLowerCase();
                for (Product product : fullList) {
                    String englishName = product.getEnglish_Name().toLowerCase();
                    if (englishName.contains(filter)) {
                        filteredProducts.add(product);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredProducts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ProductList.clear();
            ProductList.addAll((List<Product>) results.values);
            notifyDataSetChanged();
        }
    };
    private Filter productCategoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredProducts = new ArrayList<>();
            if (constraint.length() == 0) {
                filteredProducts.addAll(fullList);
            } else {
                String filter = constraint.toString().toLowerCase();
                for (Product product : fullList) {
                    String category = product.getCategory().toLowerCase();
                    if (category.equals(filter)) {
                        filteredProducts.add(product);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredProducts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ProductList.clear();
            List<Product> res = new ArrayList<>((List) results.values);
            ProductList.addAll(res);
            notifyDataSetChanged();
        }
    };

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_list_item, viewGroup, false);

        return new ProductViewHolder(view, mListener);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        final Product currentProduct = ProductList.get(i);

        Log.d("name", currentProduct.getEnglish_Name());
        final String name = String.valueOf(currentProduct.getEnglish_Name());
        final String price = String.valueOf(currentProduct.getPrice());
        final String categoryName = String.valueOf(currentProduct.getCategory());
        productViewHolder.DrugName.setText(name);
        productViewHolder.DrugPrice.setText(price);

        productViewHolder.category.setText(categoryName);
        Glide.with(mContext).asBitmap()
                .load(currentProduct.getPhotoUrl())
                .into(productViewHolder.DrugPhoto);

        if (currentProduct.getAvailableQuantity() == 0) {
            productViewHolder.available.setVisibility(View.VISIBLE);
            productViewHolder.cardView.setBackgroundResource(R.color.gray);
        }
    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }

    public void setProductList(List<Product> ProductList, Context context) {
        this.ProductList = ProductList;
        mContext = context;
        fullList = new ArrayList<>(ProductList);
    }

    public Product getProductPosition(int position) {
        return ProductList.get(position);
    }

    @Override
    public Filter getFilter() {
        if (filterType.equals("NAME"))
            return productNameFilter;
        else return productCategoryFilter;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onAddToCartClick(int position);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardView;
        private ImageView DrugPhoto;
        private TextView DrugName;
        private TextView DrugPrice;
        private Button addToCart;
        private TextView available;
        private TextView category;


        ProductViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            DrugPhoto = itemView.findViewById(R.id.drugphoto);
            DrugName = itemView.findViewById(R.id.drugname);
            DrugPrice = itemView.findViewById(R.id.drugprice);
            addToCart = itemView.findViewById(R.id.showDialog);
            cardView = itemView.findViewById(R.id.card);
            available = itemView.findViewById(R.id.availableText);
            category = itemView.findViewById(R.id.drugCategory);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAddToCartClick(position);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


            //   addToCart=itemView.findViewById(R.id.addToCart);
        }

    }

}
