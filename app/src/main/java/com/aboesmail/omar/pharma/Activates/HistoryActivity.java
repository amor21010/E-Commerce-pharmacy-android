package com.aboesmail.omar.pharma.Activates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aboesmail.omar.pharma.Activates.Adapers.OrderHistoryAdapter;
import com.aboesmail.omar.pharma.Api.order.Order;
import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.order.OrderViewModel;
import com.aboesmail.omar.pharma.Repository.user.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String userID;
    String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
        }
        });


        */

        final RecyclerView orderRecycler = findViewById(R.id.historyRecycler);

        orderRecycler.setLayoutManager(new LinearLayoutManager(this));
        orderRecycler.setHasFixedSize(true);


        final OrderViewModel viewModel = ViewModelProviders.of(HistoryActivity.this).get(OrderViewModel.class);
        UserViewModel userViewModel = ViewModelProviders.of(HistoryActivity.this).get(UserViewModel.class);


        Intent intent = getIntent();
        userID = intent.getStringExtra("USERID");

        LocalUser localUser = null;

        localUser = userViewModel.getLocalById(userID);
        if (localUser != null) {
            Log.e("localUser", localUser.getId());
            final List<Order> orders = viewModel.getUserOrders(userID);

            OrderHistoryAdapter adapter = new OrderHistoryAdapter(orders);
            orderRecycler.setAdapter(adapter);
            adapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
                @Override
                public void OrderDetailsClick(int position) {
                    Order current = orders.get(position);
                    orderID = current.getId();
                    navigate(orderDetails.class);

                }
            });


        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_history);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View view = navigationView.getHeaderView(0);


//Todo set user image
        ImageView userImage = view.findViewById(R.id.user_photo_histroy);

        if (localUser != null) {
            Glide.with(view).asBitmap()
                    .load(localUser.getUserPhoto())
                    .into(userImage);
            Log.v("photo", localUser.getUserPhoto());
        }
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

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
            navigate(CartActivity.class);
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_profile) {
            navigate(ProfileActivity.class);
        } else if (id == R.id.Logout) {
            UserViewModel viewModel = ViewModelProviders.of(HistoryActivity.this).get(UserViewModel.class);

            viewModel.logOut(viewModel.getLocalById(userID));

            navigate(MainActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void navigate(Class toActivity) {
        Intent intent = new Intent(HistoryActivity.this, toActivity);
        intent.putExtra("USERID", userID);
        intent.putExtra("ORDERID", orderID);
        startActivity(intent);
    }

}
