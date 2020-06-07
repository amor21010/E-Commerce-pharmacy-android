package com.aboesmail.omar.pharma.Api.order;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("_id")
    private String Id;
    private String time;
    private String owner;
    private String status;
    @SerializedName("totalPrice")
    private String totalPrice;
    private String seller;
    private String delivary;

    private JsonArray products;



    public Order(String owner, String status, String totalPrice) {
        this.totalPrice = totalPrice;
        this.owner = owner;
        this.status = status;

    }


    public JsonArray getProduct() {
        return products;
    }


    public String getTotalPrice() {
        return totalPrice;
    }

    public String getId() {
        return Id;
    }

    public String getSeller() {
        return seller;
    }

    public String getDelivary() {
        return delivary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
