package com.aboesmail.omar.pharma.Activates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aboesmail.omar.pharma.Activates.Adapers.CartRecyclerViewAdapter;

import com.aboesmail.omar.pharma.Api.BaseApiUrl;
import com.aboesmail.omar.pharma.Api.order.Order;
import com.aboesmail.omar.pharma.Api.order.OrderInterface;
import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;
import com.aboesmail.omar.pharma.Database.product.CartProduct;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.product.ProductViewModel;
import com.aboesmail.omar.pharma.Repository.user.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView total;
    UserViewModel userViewModel;
    Button procced;
    List<CartProduct> cartProducts;
    String userID;
    ProductViewModel model;
    LocalUser localUser;
    BaseApiUrl baseApiUrl = new BaseApiUrl();
    private String BaseUrl = baseApiUrl.getBaseUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //   FloatingActionButton fab = findViewById(R.id.fab);

        procced = findViewById(R.id.checkout);

        final RecyclerView cartRecclerView = findViewById(R.id.cart_recycler);
        final CartRecyclerViewAdapter cartRecyclerViewAdapter = new CartRecyclerViewAdapter();
        model = ViewModelProviders.of(CartActivity.this).get(ProductViewModel.class);

        total = findViewById(R.id.totalOrder);
        userViewModel = ViewModelProviders.of(CartActivity.this).get(UserViewModel.class);

        Intent i = getIntent();
        userID = i.getStringExtra("USERID");
        if (userID != null) {

            localUser = userViewModel.getLocalById(userID);

        }

        //Build view items
        buildRecycler(cartRecclerView, cartRecyclerViewAdapter);
        model.getAllCartProducts().observe(CartActivity.this, new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(final List<CartProduct> list) {
                Log.d("Observing", "Observing");
                //getAndCheck(intent, model, list);

                cartRecyclerViewAdapter.setCartData(list);
                int sum = 0;
                if (list.size() > 0)
                    for (int i = 0; i < list.size(); i++) {
                        CartProduct product = list.get(i);
                        sum += product.getPrice() * product.getCount();

                    }
                total.setText(String.valueOf(sum));

                getCartList(list);


            }
        });


// making order
        procced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localUser != null) {
                    makeOrder(cartProducts);
                    model.deleteAll();
                }
            }
        });

//swap to modify and delete items form cart
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                CartProduct cartProduct = cartRecyclerViewAdapter.getCartAtPos(viewHolder.getAdapterPosition());
                model.delete(cartProduct);
            }
        }).attachToRecyclerView(cartRecclerView);


        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */

//drawer build
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_cart);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View view = navigationView.getHeaderView(0);


//Todo set user image
        ImageView userImage = view.findViewById(R.id.user_photo_home);

        if (localUser != null) {
            Glide.with(view).asBitmap()
                    .load(localUser.getUserPhoto())
                    .into(userImage);
            Log.v("photo", localUser.getUserPhoto());
        }
    }

    private void getCartList(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    //On back pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            navigate(HomeActivity.class);
            super.onBackPressed();
        }
    }


    //create drawer menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_activity_final, menu);
        return true;
    }


    //drawer item select
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navigate(HomeActivity.class);
        } else if (id == R.id.nav_deals) {

        } else if (id == R.id.nav_Chekout) {

        } else if (id == R.id.nav_history) {
            navigate(HistoryActivity.class);

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_profile) {
            navigate(ProfileActivity.class);
        } else if (id == R.id.Logout) {

            userViewModel.logOut(localUser);
            navigate(MainActivity.class);

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void buildRecycler(RecyclerView cartRecclerView, CartRecyclerViewAdapter cartRecyclerViewAdapter) {


        cartRecclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecclerView.setHasFixedSize(true);

        cartRecclerView.setNestedScrollingEnabled(true);
        cartRecclerView.setAdapter(cartRecyclerViewAdapter);


    }

    //get data from home activity
/*    public void getAndCheck(Intent intent, ProductViewModel model,
                            List<CartProduct> list) {

        if (intent.hasExtra("id")) {
            ArrayList<String> id = (intent.getExtras().getStringArrayList("id"));

            if (id != null) {
                for (int i = 0; i < id.size(); i++) {
                    String current = id.get(i);
                    model.addToCart(current,);
                }
                intent.removeExtra("id");
            }
            intent.removeExtra("id");
        }
    }*/

    //make order
    private void makeOrder(final List<CartProduct> cartProducts) {

        Order order = new Order(localUser.getId(),
                "waiting", String.valueOf(total.getText()));

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient).build();

        final OrderInterface orderInterface = retrofit.create(OrderInterface.class);
        Call<Order> makeOrder = orderInterface.makeOrder(order);
        makeOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order orderBody = response.body();
                for (int i = 0; i < cartProducts.size(); i++) {

                    final String ID = cartProducts.get(i).getId();
                    final int Quantity = cartProducts.get(i).getCount();
                    Call<Order> addProducts = orderInterface.addProducts(orderBody.getId(), ID, Quantity);
                    addProducts.enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            if (response.code() == 200) {
                                Toast.makeText(CartActivity.this,
                                        "Order in review",
                                        Toast.LENGTH_SHORT).show();

                                model.patchQuantity(ID, Quantity, 0);
                                navigate(HistoryActivity.class);

                            } else
                                Toast.makeText(CartActivity.this,
                                        "Order failed Please Call us" + response.message(),
                                        Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText(CartActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }

        });

    }

    private void navigate(Class toActivity) {
        Intent intent = new Intent(CartActivity.this, toActivity);
        intent.putExtra("USERID", userID);
        startActivity(intent);
    }

}
