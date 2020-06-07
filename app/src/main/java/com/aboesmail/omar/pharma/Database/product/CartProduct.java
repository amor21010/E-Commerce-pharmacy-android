package com.aboesmail.omar.pharma.Database.product;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "products_table")
public class CartProduct {

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    private String english_Name;

    private String arabic_Name;

    private String sci_Name;

    private String company;

    private String category;

    private double price;
    @SerializedName("photo")
    private String photoUrl;

    private boolean avilable;
    private int count;

    public CartProduct(@NotNull String id,
                       String english_Name,
                       String arabic_Name,
                       String sci_Name,
                       String company,
                       String category,
                       double price,
                       String photoUrl,
                       boolean avilable, int count) {
        this.id = id;
        this.english_Name = english_Name;
        this.arabic_Name = arabic_Name;
        this.sci_Name = sci_Name;
        this.company = company;
        this.category = category;
        this.price = price;
        this.photoUrl = photoUrl;
        this.avilable = avilable;
        this.count = count;
    }


    public String getId() {
        return id;
    }


    public String getEnglish_Name() {
        return english_Name;
    }


    public String getArabic_Name() {
        return arabic_Name;
    }


    public String getSci_Name() {
        return sci_Name;
    }


    public String getCompany() {
        return company;
    }


    public String getCategory() {
        return category;
    }


    public double getPrice() {
        return price;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }


    public boolean isAvilable() {
        return avilable;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
