package com.aboesmail.omar.pharma.Activates;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.aboesmail.omar.pharma.Api.Product.Product;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.product.ProductViewModel;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductInfo extends AppCompatActivity {
    ImageView image;
    TextView arabic;
    TextView english;
    TextView sci;
    TextView price;
    TextView company;
    TextView category;
    TextView available;
    Button add;


    ProductViewModel viewModel;
    String userID;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        image = findViewById(R.id.productImage);
        arabic = findViewById(R.id.arabic);
        english = findViewById(R.id.english);
        sci = findViewById(R.id.sicName);
        price = findViewById(R.id.price);
        company = findViewById(R.id.company);
        category = findViewById(R.id.category);
        available = findViewById(R.id.available);
        add = findViewById(R.id.addToCart);

        Intent intent = getIntent();
        productId = intent.getStringExtra("ProductID");
        userID = intent.getStringExtra("USERID");

        viewModel = ViewModelProviders.of(ProductInfo.this).get(ProductViewModel.class);

        if (productId != null) {
            viewModel.getProductInfo(productId).observe(ProductInfo.this, new Observer<Product>() {
                @Override
                public void onChanged(Product product) {

                    Glide.with(ProductInfo.this).asBitmap().
                            load(product.getPhotoUrl()).into(image);
                    arabic.setText(product.getArabic_Name());
                    english.setText(product.getEnglish_Name());
                    sci.setText(product.getSci_Name());
                    price.setText(String.valueOf(product.getPrice()));
                    company.setText(product.getCompany());
                    category.setText(product.getCategory());
                    available.setText(String.valueOf(product.getAvailableQuantity()));
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            customDialog(productId);
                        }
                    });

                }
            });
        }

    }


    public void customDialog(String productId) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater()
                .inflate(R.layout.costum_dialoug_add_cart, null);
//custom dialog var
        final TextView total = view.findViewById(R.id.totalprice);
        final TextView ProductName = view.findViewById(R.id.drugname_dialog);
        final TextView price = view.findViewById(R.id.selected_item_price);
        final TextView more = view.findViewById(R.id.info);
        final NumberPicker picker = view.findViewById(R.id.product_number);
        final Button backDialog = view.findViewById(R.id.back);
        final Button save = view.findViewById(R.id.add_to_cart);
        final CircleImageView imageView = view.findViewById(R.id.product_image);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        picker.setMinValue(1);
        more.setVisibility(View.GONE);

        viewModel.getProductInfo(productId).observe(ProductInfo.this, new Observer<Product>() {
            @SuppressLint({"SetTextI18n", "NewApi", "ResourceAsColor"})
            @Override
            public void onChanged(final Product product) {

                price.setText(String.valueOf(product.getPrice()));
                ProductName.setText(product.getEnglish_Name());


                if (product.getAvailableQuantity() != 0) {
                    picker.setEnabled(true);
                    save.setEnabled(true);
                    Glide.with(view).asBitmap().load(product.getPhotoUrl()).into(imageView);

                    picker.setMaxValue((int) product.getAvailableQuantity());

                    total.setText("Total price : " + (picker.getValue() * product.getPrice()));
                    picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @SuppressLint({"SetTextI18n"})
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                            total.setText("Total price : " + picker.getValue() * product.getPrice());
                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int count = Integer.parseInt(String.valueOf(picker.getValue()));
                            viewModel.addToCart(product.getId(), count, ProductInfo.this);
                            dialog.dismiss();
                            navigate(HomeActivity.class);
                        }

                    });
                } else {
                    picker.setEnabled(false);
                    save.setEnabled(false);
                    save.setBackground(getDrawable(R.color.gray));
                    total.setVisibility(View.GONE);
                }

            }
        });


        backDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void navigate(Class toActivity) {
        Intent intent = new Intent(ProductInfo.this, toActivity);
        intent.putExtra("USERID", userID);
        intent.putExtra("ProductID", productId);
        startActivity(intent);
    }
}
