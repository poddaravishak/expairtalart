package com.eterces.expiryalert;

public class Product {
    private String name;
    private String date;
    private String daysRemaining;
    private String category;
    private String imageUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(String daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Product(String name, String date, String daysRemaining, String category, String imageUri) {
        this.name = name;
        this.date = date;
        this.daysRemaining = daysRemaining;
        this.category = category;
        this.imageUri = imageUri;
    }
}