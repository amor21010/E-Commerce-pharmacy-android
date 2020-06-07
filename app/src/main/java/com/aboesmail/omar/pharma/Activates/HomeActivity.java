package com.aboesmail.omar.pharma.Activates;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aboesmail.omar.pharma.Activates.Adapers.HomeActivityAdapters.CatagoryRecyclerAdapter;
import com.aboesmail.omar.pharma.Activates.Adapers.HomeActivityAdapters.ProductRecyclerAdabter;

import com.aboesmail.omar.pharma.Api.Category.Category;
import com.aboesmail.omar.pharma.Api.Product.Product;
import com.aboesmail.omar.pharma.Api.User.User;
import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;
import com.aboesmail.omar.pharma.R;
import com.aboesmail.omar.pharma.Repository.product.ProductViewModel;
import com.aboesmail.omar.pharma.Repository.user.UserViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public UserViewModel userViewModel;
    ProductRecyclerAdabter productRecyclerAdabter;
    ViewFlipper viewFlipper;
    boolean doubleBackToExitPressedOnce = false;
    ProductViewModel viewModel;
    String userID;
    String productId;
    User user;


    SearchView searchView;
    RecyclerView catRecyclerView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//product flipper view
        viewFlipper = findViewById(R.id.flipper);
        int[] images = {R.drawable.backgroundbtn, R.drawable.backgroundbtn2, R.drawable.backgrounds};
        for (int image : images) {
            flipperImage(image);
        }


//Todo user info
        userViewModel = ViewModelProviders.of(HomeActivity.this).get(UserViewModel.class);
        user = getUserInfo();
        userID = user.getUserID();
//Todo products list
        productRecyclerAdabter = new ProductRecyclerAdabter();
        final RecyclerView productRecyclerView = findViewById(R.id.Recycler);


        viewModel = ViewModelProviders.of(HomeActivity.this).get(ProductViewModel.class);
//Products recycler view

        viewModel.getAllProduct().observe(HomeActivity.this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                buildProductsRecycler(productRecyclerView, productRecyclerAdabter);
                productRecyclerAdabter.setProductList(products, HomeActivity.this);
            }
        });
//category recycler view
        viewModel.getCategory().observe(HomeActivity.this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                inCategory(categories);
            }
        });


//TODO Live chat or upload photo(next update)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = "+02 01279509735"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = HomeActivity.this.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(HomeActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
//Todo drawer operations
        Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_black_24dp, getApplication().getTheme());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_home);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
/*
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setDrawerIndicatorEnabled(false);
*/

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);


//Todo set user image
        ImageView userImage = view.findViewById(R.id.user_photo_home);

        if (user != null) {
            Glide.with(view).asBitmap()
                    .load(user.getPhoto())
                    .into(userImage);

        }


/*        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                toggle.onDrawerSlide(drawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
// You can change this duration to more closely match that of the default animation.
        anim.setDuration(500);
        anim.start();*/

    }//onCreate end


    //TODO on back press handle
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                finish();
                moveTaskToBack(true);

                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;

                }
            }, 2000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productRecyclerAdabter.setFilterType("NAME");
                productRecyclerAdabter.getFilter().filter(newText);
                if (newText.trim().length() != 0) {
                    viewFlipper.setVisibility(View.GONE);
                    catRecyclerView.setVisibility(View.GONE);
                } else {
                    viewFlipper.setVisibility(View.VISIBLE);
                    catRecyclerView.setVisibility(View.VISIBLE);

                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            LocalUser localUser = userViewModel.getLocalById(userID);
            userViewModel.logOut(localUser);

            navigate(MainActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {


        } else if (id == R.id.nav_deals) {


        } else if (id == R.id.nav_Chekout) {

            navigate(CartActivity.class);


        } else if (id == R.id.nav_history) {

            navigate(HistoryActivity.class);

        } else if (id == R.id.nav_profile) {
            navigate(ProfileActivity.class);
        } else if (id == R.id.Logout) {
            LocalUser user =userViewModel.getAllLocalUsers().getValue();
            userViewModel.logOut(user);
            navigate(MainActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //TODO flipper view build
    public void flipperImage(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

    }

    //TODO Category recycler view build
    private void inCategory(List<Category> list) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        catRecyclerView = findViewById(R.id.category);
        catRecyclerView.setLayoutManager(linearLayoutManager);
        final CatagoryRecyclerAdapter adapter = new CatagoryRecyclerAdapter(this, list);
        catRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CatagoryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Category category = adapter.getmCategory(position);
                String categoryName = category.getCategory();
                productRecyclerAdabter.setFilterType("CATEGORY");
                productRecyclerAdabter.getFilter().filter(categoryName);
            }
        });

    }

    //TODO build customDialog
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


        viewModel.getProductInfo(productId).observe(HomeActivity.this, new Observer<Product>() {
            @SuppressLint({"SetTextI18n", "NewApi", "ResourceAsColor"})
            @Override
            public void onChanged(final Product product) {

                price.setText(String.valueOf(product.getPrice()));
                ProductName.setText(product.getEnglish_Name());

                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigate(ProductInfo.class);
                    }
                });

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
                            viewModel.addToCart(product.getId(), count, HomeActivity.this);
                            dialog.dismiss();

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

    //TODO Product recycler view build
    public void buildProductsRecycler(RecyclerView productRecyclerView,
                                      final ProductRecyclerAdabter adabter) {
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setHasFixedSize(true);

        productRecyclerView.setAdapter(adabter);
        productRecyclerView.setNestedScrollingEnabled(true);

        adabter.setOnItemClickListener(new ProductRecyclerAdabter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Product current = adabter.getProductPosition(position);
                productId = current.getId();
                if (productId != null)
                    navigate(ProductInfo.class);

            }

            @Override
            public void onAddToCartClick(int position) {
                Product current = adabter.getProductPosition(position);

                productId = current.getId();
                if (productId != null)
                    customDialog(productId);

            }

        });


    }

    //TODO get user info
    private User getUserInfo() {

        Intent intent = getIntent();
        userID = intent.getStringExtra("USERID");

        return userViewModel.getUserInfo(userID);
    }

    private void navigate(Class toActivity) {
        Intent intent = new Intent(HomeActivity.this, toActivity);
        intent.putExtra("USERID", userID);
        intent.putExtra("ProductID", productId);
        startActivity(intent);
    }
}


