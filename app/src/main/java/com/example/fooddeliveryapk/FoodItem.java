package com.example.fooddeliveryapk;

public class FoodItem {
    private int id;
    private String name;
    private String price;
    private String description;
    private double rating;
    private int imageResource;
    private int quantity;

    public FoodItem(int id, String name, double price, String description, double rating, int imageResource) {
        this.id = id;
        this.name = name;
        this.price = String.valueOf(price);
        this.description = description;
        this.rating = rating;
        this.imageResource = imageResource;
        this.quantity = 1; // Initialize quantity to 1 by default
    }

    // Getters and setters for all attributes
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        // Calculate the total price based on quantity
        return quantity * Double.parseDouble(price.replace("Rs.", ""));
    }
}
