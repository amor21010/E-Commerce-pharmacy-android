package com.aboesmail.omar.pharma.Api.Category;



import com.aboesmail.omar.pharma.Api.Category.Category;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {


    @GET("category/api/")
    Call<List<Category>> getAllCat();
}
