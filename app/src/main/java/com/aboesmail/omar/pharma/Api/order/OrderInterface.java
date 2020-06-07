package com.aboesmail.omar.pharma.Api.order;

import com.aboesmail.omar.pharma.Api.order.Order;


import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderInterface {
    @POST("/order/api/create")
    Call<Order> makeOrder(@Body Order order);



    @GET("/order/history/{username}")
    Call<List<Order>> getUserOrders(@Path("username") String username);

    @GET("/order/{id}/api/")
    Call<Order> getOrderDetails(@Path("id") String id);

    @FormUrlEncoded
    @PATCH("/order/{id}/updateQ/and")
    Call<Order> updateQuantities(@Path("id") String id, @Field("productID") String ProductID, @Field("Quantity") int Quantity);

    @FormUrlEncoded
    @PATCH("/order/{id}/update/api")
    Call<Order> updateOrder(@Path("id") String id, @Field("totalPrice") double totalPrice);


    @FormUrlEncoded
    @PUT("/order/{id}/api/add")
    Call<Order> addProducts(@Path("id") String id, @Field("productID") String ProductID, @Field("Quantity") int Quantity);


}
