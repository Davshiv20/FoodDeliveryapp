package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodItemList;

    private List<FoodItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize RecyclerView and the data list
        recyclerView = findViewById(R.id.recyclerView);
        foodItemList = new ArrayList<>();
        cartItemList = new ArrayList<>();

        // Add sample food items (you can replace these with your actual data)
        foodItemList.add(new FoodItem(1, "Fruit Bowl", "Rs.70", "A delicious bowl of fresh fruits offers a delightful medley of nature's finest flavors and colors, a true feast for the senses. Each bite is a burst of vibrant sweetness, a symphony of textures, and a source of pure nourishment. This inviting bowl is a celebration of health and well-being, combining the lusciousness of ripe fruits with the crispness of freshly cut produce.", 4.5, R.drawable.food_img_1));
        foodItemList.add(new FoodItem(2, "Sprouts Bowl", "Rs.40", "A healthy bowl of sprouts is a wholesome delight that embodies vitality and nutrition. Packed with essential nutrients and a refreshing crunch, it's a nutritious choice that keeps you energized throughout the day. These sprouted gems offer a blend of earthy flavors and a satisfying bite, making them a perfect addition to your balanced diet.", 4.2 , R.drawable.food_img_2));


        // Create a FoodAdapter and set it to the RecyclerView
        foodAdapter = new FoodAdapter(foodItemList);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Adapter class for the RecyclerView
    private class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {

        private List<FoodItem> dataList;

        public FoodAdapter(List<FoodItem> dataList) {
            this.dataList = dataList;
        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FoodViewHolder holder, final int position) {
            final FoodItem foodItem = dataList.get(position);

            // Bind data to the ViewHolder
            holder.foodName.setText(foodItem.getName());
            holder.foodPrice.setText(foodItem.getPrice());
            holder.foodImage.setImageResource(foodItem.getImageResource());

            // Handle "Add to Cart" button click
            holder.addToCartButton.setOnClickListener(view -> {
                // Add the selected item to the cart
                cartItemList.add(foodItem);
                // Notify the user
                holder.addToCartButton.setEnabled(false);
                showFoodPreview(foodItem);
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
    private void showFoodPreview(FoodItem foodItem) {
        Intent intent = new Intent(menu.this, FoodPreviewActivity.class);
        intent.putExtra("foodName", foodItem.getName());
        intent.putExtra("foodDescription", foodItem.getDescription());
        intent.putExtra("foodRating", foodItem.getRating());
        intent.putExtra("pricePerItem", Double.parseDouble(foodItem.getPrice().replace("Rs.", "")));
        startActivity(intent);
    }

    // ViewHolder class for each item in the RecyclerView
    private static class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;

        Button addToCartButton;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }

    // Data model class for a food item
    public class FoodItem {
        private int id;
        private String name;
        private String price;
        private String description;
        private double rating;
        private int imageResource;
        private int quantity;

        public FoodItem(int id, String name, String price, String description, double rating, int imageResource) {
            this.id = id;
            this.name = name;
            this.price = price;
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

}