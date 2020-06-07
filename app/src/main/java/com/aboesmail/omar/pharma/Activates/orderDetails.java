package com.aboesmail.omar.pharma.Activates;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aboesmail.omar.pharma.Activates.Adapers.OrderDetailsAdapter;

import com.aboesmail.omar.pharma.Api.Product.Product;
import com.aboesmail.omar.pharma.Api.order.Order;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.order.OrderViewModel;
import com.aboesmail.omar.pharma.Repository.product.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class orderDetails extends AppCompatActivity {
    List<Product> products = new ArrayList<>();
    List<Integer> Quantities = new ArrayList<>();

    OrderViewModel viewModel;
    ProductViewModel productViewModel;


    TextView time;
    TextView seller;
    TextView delivery;
    TextView status;
    TextView total;
    String orderID;

    double sum = 0;
    int newQ;
    int oldQ;
    String productId;
    String userID;

    OrderDetailsAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        Toolbar toolbar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);


        time = findViewById(R.id.oTime);
        seller = findViewById(R.id.oSeller);
        delivery = findViewById(R.id.oDelivary);
        status = findViewById(R.id.oStatus);
        total = findViewById(R.id.oTotal);


        productViewModel = ViewModelProviders.of(orderDetails.this).get(ProductViewModel.class);


        FloatingActionButton fab = findViewById(R.id.fab_order);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSum();
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);
                    String id = product.getId();
                    int nQ = Quantities.get(i);
                    productViewModel.patchQuantity(id, nQ, oldQ);
                    viewModel.updateTotal(orderID, sum);
                    viewModel.updateOrderQuantities(orderID, id, nQ);
                    // Log.e("value", String.valueOf(productViewModel.getProductInfo(productId).getValue().getAvailableQuantity()));
                    confirmDialog();
                }
            }
        });

        final Intent intent = getIntent();
        orderID = intent.getStringExtra("ORDERID");
        userID = intent.getStringExtra("USERID");

        viewModel = ViewModelProviders.of(orderDetails.this).get(OrderViewModel.class);
        final Order order = viewModel.getOrderDetails(orderID);

        time.setText(order.getTime());
        seller.setText(order.getSeller());
        delivery.setText(order.getDelivary());
        status.setText(order.getStatus());
        total.setText(String.valueOf(order.getTotalPrice()));

        JsonArray productsJsonArray = order.getProduct();
        for (int i = 0; i < productsJsonArray.size(); i++) {
            JsonObject jsonProduct = productsJsonArray.get(i).getAsJsonObject();
            JsonObject productID = jsonProduct.get("productName").getAsJsonObject();

            String id = productID.get("_id").getAsString();
            String English_Name = productID.get("English_Name").getAsString();
            String Arabic_Name = productID.get("Arabic_Name").getAsString();
            String sci_Name = productID.get("sci_Name").getAsString();
            String company = productID.get("company").getAsString();
            String category = productID.get("category").getAsString();
            double price = productID.get("price").getAsDouble();
            String photo = productID.get("photo").getAsString();
            double avilableQantaty = productID.get("avilableQantaty").getAsDouble();

            int Quantity = jsonProduct.get("Quantity").getAsInt();

            Product product = new Product(id, English_Name, Arabic_Name,
                    sci_Name, company, category, price, photo,
                    avilableQantaty, true);


            products.add(i, product);
            Quantities.add(i, Quantity);
        }


        RecyclerView recyclerView = findViewById(R.id.oRecycler);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new OrderDetailsAdapter(products, Quantities, orderDetails.this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OrderDetailsAdapter.OnItemClickListener() {

            @Override
            public void OrderUpdateClick(int position) {

                navigate(ProductInfo.class);
            }


        });
        getSum().observe(orderDetails.this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                total.setText(String.valueOf(aDouble));
                total.setTextColor(Color.parseColor("#ff0000"));
            }
        });


    }


    public void confirmDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater()
                .inflate(R.layout.confirmorder, null);
//custom dialog var
        Button confirm = view.findViewById(R.id.oconfirm);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(HistoryActivity.class);
            }
        });
        dialog.show();
    }

    private void navigate(Class toActivity) {
        Intent intent = new Intent(orderDetails.this, toActivity);
        intent.putExtra("USERID", userID);
        intent.putExtra("ProductID", productId);
        startActivity(intent);
    }

    private LiveData<Double> getSum() {
        final MutableLiveData<Double> liveSum = new MutableLiveData<>();
        adapter.getLiveDataQ().observe(orderDetails.this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                productId = adapter.getUpdated();
                oldQ = adapter.getOldQ();
                List<Integer> newQS = Quantities;
                if (adapter.getNewQuantity() != null) {
                    newQ = adapter.getNewQuantity();

                    if (adapter.getQuantities().size() != 0)
                        newQS = adapter.getQuantities();
                    int Quantity;
                    sum = 0;
                    for (int i = 0; i < newQS.size(); i++) {
                        double price = products.get(i).getPrice();
                        Quantity = newQS.get(i);
                        sum += price * Quantity;
                    }
                    if (sum != 0) {
                        sum += 20;
                    }

                    liveSum.setValue(sum);
                }
            }
        });
        return liveSum;
    }

}
