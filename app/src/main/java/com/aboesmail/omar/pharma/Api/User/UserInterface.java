package com.aboesmail.omar.pharma.Api.User;

import android.graphics.drawable.Drawable;

import com.aboesmail.omar.pharma.Api.User.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserInterface {

    @POST("user/api/create")
    Call<User> Register(@Body User user);

    @FormUrlEncoded
    @POST("user/api/login")
    Call<User> Loggin(@Field("email") String email, @Field("password") String password);

    @PATCH("user/api/{id}/patch")
    Call<User> update(@Path("id") String id, @Body User user);

    @Multipart
    @PATCH("user/api/{id}/patchImage")
    Call<User> patchImage(@Path("id") String id, @Part("photo") Drawable image);

    @GET("user/api/{id}")
    Call<User> getUser(@Path("id") String id);

}

