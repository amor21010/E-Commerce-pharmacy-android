package com.aboesmail.omar.pharma.Api.Product;


import com.aboesmail.omar.pharma.Api.Product.Product;


import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductInterface {

    @GET("product/api/")
    Call<List<Product>> getAllProduct();

    @GET("product/{id}/api/")
    Call<Product> getByID(@Path("id") String id);

    @POST("product/create/api/")
    Call<Product>  addNewProduct(@Body Product product);

    @PATCH("product/{id}/patch/api/")
    Call<Product> patchQuantity(@Path("id") String id, @Body Product product);

    @DELETE("product/{id}/delete/api")
    Call deleteProduct(@Path("id") String id);



}
