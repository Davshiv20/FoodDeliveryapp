package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {
    private static final String TAG = "AdminActivity";
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodItemList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize RecyclerView and the data list
        recyclerView = findViewById(R.id.recyclerView);
        foodItemList = new ArrayList<>();

        // Add sample food items (you can replace these with your actual data)
        foodItemList.add(new FoodItem("Fruit Bowl", "Rs.70", R.drawable.food_img_1));
        foodItemList.add(new FoodItem("Sprouts Bowl", "Rs.40", R.drawable.food_img_2));

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

        @NonNull
        @Override
        public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
            return new FoodViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
            FoodItem foodItem = dataList.get(position);

            // Bind data to the ViewHolder
            holder.foodName.setText(foodItem.getName());
            holder.foodPrice.setText(foodItem.getPrice());
            holder.foodImage.setImageResource(foodItem.getImageResource());
        }
        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    // ViewHolder class for each item in the RecyclerView
    private static class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
        }
    }

    // Data model class for a food item
    private class FoodItem {
        private String name;
        private String price;
        private int imageResource;

        public FoodItem(String name, String price, int imageResource) {
            this.name = name;
            this.price = price;
            this.imageResource = imageResource;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public int getImageResource() {
            return imageResource;
        }
    }
}