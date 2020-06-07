package com.aboesmail.omar.pharma.Repository.order;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.aboesmail.omar.pharma.Api.BaseApiUrl;
import com.aboesmail.omar.pharma.Api.order.Order;
import com.aboesmail.omar.pharma.Api.order.OrderInterface;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OrderRepository {
    Order order;
    List<Order> orderList;
    private BaseApiUrl baseApiUrl = new BaseApiUrl();
    private String BaseUrl = baseApiUrl.getBaseUrl();

    List<Order> getUserOrder(String userName) {

        OrderInterface orderInterface = createConnection();
        return getOrderList(orderInterface, userName);
    }

    void updateTotalPrice(String orderID, double totalPrice) {

        OrderInterface orderInterface = createConnection();
        Call<Order> call = orderInterface.updateOrder(orderID, totalPrice);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {

            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("uuuupp", "fall" + t.getCause());
            }
        });
    }

    void updateOrderQuantities(String orderID, String productID, int newQ) {

        OrderInterface orderInterface = createConnection();
        Call<Order> call = orderInterface.updateQuantities(orderID, productID, newQ);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.d("orderUpdate", "false" + response.body());

            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.d("orderUpdate", "false " + t.getCause());
            }
        });
    }


    Order getOrderDetails(String id) {
        OrderInterface orderInterface = createConnection();
        return getOrder(orderInterface, id);


    }

    private Order getOrder(OrderInterface orderInterface, String id) {
        try {
            return new getOrderAsysnc(orderInterface).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Order> getOrderList(OrderInterface orderInterface, String id) {
        try {
            return new getUserOrderAsysnc(orderInterface).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private OrderInterface createConnection() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();

        return retrofit.create(OrderInterface.class);
    }

    @SuppressLint("StaticFieldLeak")
    class getOrderAsysnc extends AsyncTask<String, Void, Order> {
        private OrderInterface orderInterface;

        getOrderAsysnc(OrderInterface orderInterface) {
            this.orderInterface = orderInterface;
        }

        @Override
        protected Order doInBackground(String... params) {
            try {
                order = orderInterface.getOrderDetails(params[0]).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return order;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class getUserOrderAsysnc extends AsyncTask<String, Void, List<Order>> {
        private OrderInterface orderInterface;

        getUserOrderAsysnc(OrderInterface orderInterface) {
            this.orderInterface = orderInterface;
        }

        @Override
        protected List<Order> doInBackground(String... params) {
            try {
                orderList = orderInterface.getUserOrders(params[0]).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return orderList;
        }
    }


}
