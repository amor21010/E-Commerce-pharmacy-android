package com.aboesmail.omar.pharma.Api.Category;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("_id")
    private String category;

    private String icon;

    public Category(String category, String icon) {
        this.category = category;
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public String getIcon() {
        return icon;
    }
}
