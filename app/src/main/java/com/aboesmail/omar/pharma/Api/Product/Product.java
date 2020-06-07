package com.aboesmail.omar.pharma.Api.Product;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("_id")
    private String id;

    private String English_Name;

    private String Arabic_Name;

    @SerializedName("sci_Name")
    private String sci_Name;

    private String company;

    @SerializedName("category")
    private String category;

    private double price;

    @SerializedName("photo")
    private String photoUrl;

    @SerializedName("avilableQantaty")
    private double availableQuantity;

    private boolean avilable;

    public Product(String id, String english_Name,
                   String arabic_Name, String sci_Name,
                   String company, String category,
                   double price, String photoUrl, double availableQuantity,
                   boolean avilable) {
        this.id = id;
        English_Name = english_Name;
        Arabic_Name = arabic_Name;
        this.sci_Name = sci_Name;
        this.company = company;
        this.category = category;
        this.price = price;
        this.photoUrl = photoUrl;
        this.availableQuantity = availableQuantity;
        this.avilable = avilable;
    }
    public Product(String english_Name,
                   String arabic_Name, String sci_Name,
                   String company, String category,
                   double price, String photoUrl, double availableQuantity,
                   boolean avilable) {
        English_Name = english_Name;
        Arabic_Name = arabic_Name;
        this.sci_Name = sci_Name;
        this.company = company;
        this.category = category;
        this.price = price;
        this.photoUrl = photoUrl;
        this.availableQuantity = availableQuantity;
        this.avilable = avilable;
    }

    public double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglish_Name() {
        return English_Name;
    }

    public void setEnglish_Name(String english_Name) {
        English_Name = english_Name;
    }

    public String getArabic_Name() {
        return Arabic_Name;
    }

    public void setArabic_Name(String arabic_Name) {
        Arabic_Name = arabic_Name;
    }

    public String getSci_Name() {
        return sci_Name;
    }

    public void setSci_Name(String sci_Name) {
        this.sci_Name = sci_Name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isAvilable() {
        return avilable;
    }

    public void setAvilable(boolean avilable) {
        this.avilable = avilable;
    }
}
