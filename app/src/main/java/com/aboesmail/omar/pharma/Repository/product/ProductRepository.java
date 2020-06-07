package com.aboesmail.omar.pharma.Repository.product;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aboesmail.omar.pharma.Api.BaseApiUrl;

import com.aboesmail.omar.pharma.Api.Category.Category;
import com.aboesmail.omar.pharma.Api.Category.CategoryInterface;
import com.aboesmail.omar.pharma.Api.Product.Product;

import com.aboesmail.omar.pharma.Api.Product.ProductInterface;
import com.aboesmail.omar.pharma.Database.Roomdb;
import com.aboesmail.omar.pharma.Database.product.CartProduct;
import com.aboesmail.omar.pharma.Database.product.ProductDao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ProductRepository {


    private final LiveData<List<CartProduct>> allProducts;

    BaseApiUrl baseApiUrl = new BaseApiUrl();
    private String BaseUrl = baseApiUrl.getBaseUrl();
    private ProductDao productDao;

    ProductRepository(Application application) {
        Roomdb DataBase = Roomdb.getInstance(application);
        productDao = DataBase.productDao();
        allProducts = productDao.getAllCartProducts();


    }


    LiveData<List<Product>> getAllProduct() {
        final MutableLiveData<List<Product>> data = new MutableLiveData<>();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();


        ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<List<Product>> call = productInterface.getAllProduct();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NotNull Call<List<Product>> call, @NotNull Response<List<Product>> response) {

                List<Product> products = response.body();
                data.setValue(products);
            }

            @Override
            public void onFailure(@NotNull Call<List<Product>> call, @NotNull Throwable t) {
                Log.d("errorCode", t.getMessage());
            }
        });


        return data;
    }


    LiveData<List<Category>> getCategory() {
        final MutableLiveData<List<Category>> data = new MutableLiveData<>();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();


        CategoryInterface Interface = retrofit.create(CategoryInterface.class);
        Call<List<Category>> call = Interface.getAllCat();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                Log.d("categorys", response.body().toString());
                data.setValue(categories);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });


        return data;
    }


    void addToCart(final String id, final int Count, final LifecycleOwner activity) {


        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<Product> call = productInterface.getByID(id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {

                final Product res = response.body();
                if (res != null) {
                    final String resID = res.getId();

                    insertProduct(new CartProduct(resID,
                            res.getEnglish_Name(),
                            res.getArabic_Name(),
                            res.getSci_Name(),
                            res.getCompany(),
                            res.getCategory(),
                            res.getPrice(),
                            res.getPhotoUrl(),
                            res.isAvilable(), Count));

                }
            }

            @Override
            public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {
                Log.d("failToGet", "failToGet " + id + "  " + t.getMessage());
            }
        });

    }

    LiveData<Product> getProductInfo(final String id) {

        final MutableLiveData<Product> data = new MutableLiveData<>();
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<Product> call = productInterface.getByID(id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {

                data.setValue(response.body());

            }

            @Override
            public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {
                Log.d("failToGet", "failToGet " + id + "  " + t.getMessage());
            }
        });

        return data;
    }


    void patchQuantity(final String id, final int orderedQuantity, final int oldQ) {


        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        final ProductInterface productInterface = retrofit.create(ProductInterface.class);
        Call<Product> call = productInterface.getByID(id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {
                Product product = response.body();


                product.setAvailableQuantity((int) (product.getAvailableQuantity() - (orderedQuantity - oldQ)));
                if (product != null) {
                    Call<Product> patchCall = productInterface.patchQuantity(id, product);
                    patchCall.enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {

                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<Product> call, @NotNull Throwable t) {
                Log.d("failToGet", "failToGet " + id + "  " + t.getMessage());
            }
        });


    }

    void insertProduct(CartProduct cartProduct) {
        new insertProductAsyncTask(productDao).execute(cartProduct);
    }

    void updateProduct(CartProduct cartProduct) {
        new updateProductAsyncTask(productDao).execute(cartProduct);
    }

    void deleteProduct(CartProduct cartProduct) {
        new deleteProductAsyncTask(productDao).execute(cartProduct);
    }

    void deleteAllProduct() {
        new deleteAllProductAsyncTask(productDao).execute();
    }

//product database Operating

    LiveData<List<CartProduct>> getAllCartProducts() {
        return allProducts;
    }


    //product Async Tasks

    @SuppressLint("StaticFieldLeak")
    class insertProductAsyncTask extends AsyncTask<CartProduct, Void, Void> {
        private ProductDao productDao;

        insertProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(CartProduct... cartProducts) {
            productDao.insert(cartProducts[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class deleteProductAsyncTask extends AsyncTask<CartProduct, Void, Void> {
        private ProductDao productDao;

        deleteProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(CartProduct... cartProducts) {
            productDao.delete(cartProducts[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class getCartProductAsyncTask extends AsyncTask<String, Void, CartProduct> {
        private ProductDao productDao;

        getCartProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected CartProduct doInBackground(String... strings) {
            productDao.getCartProductByID(strings[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class updateProductAsyncTask extends AsyncTask<CartProduct, Void, Void> {
        private ProductDao productDao;

        updateProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(CartProduct... cartProducts) {
            productDao.update(cartProducts[0]);
            return null;
        }
    }

    class deleteAllProductAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProductDao productDao;

        deleteAllProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            productDao.deleteAll();
            return null;
        }
    }
}
