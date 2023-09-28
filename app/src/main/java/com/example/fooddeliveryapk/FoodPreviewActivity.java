package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fooddeliveryapk.FoodItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable;
public class FoodPreviewActivity extends AppCompatActivity {
    private double totalCartPrice;
    private String name;
    private String address;
    private String phoneNo;
    private String customerId;
    private List<FoodItem> selectedFoodItems; // Add this list


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_food_preview);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button orderButton = findViewById(R.id.orderButton);
        Intent intent = getIntent();
        totalCartPrice = intent.getDoubleExtra("totalCartPrice", 0.0);
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        phoneNo = intent.getStringExtra("phoneNo");
        // Deserialize JSON to a List<FoodItem>
        String selectedFoodItemsJson = getIntent().getStringExtra("selectedFoodItemsJson");
        Gson gson = new Gson();
        Type foodItemType = new TypeToken<List<FoodItem>>() {
        }.getType();
        selectedFoodItems = gson.fromJson(selectedFoodItemsJson, foodItemType);

        // Display the total cart price in a TextView
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        totalPriceTextView.setText(String.format("Total Price: Rs. %.2f", totalCartPrice));

        // Get the current user's UID
        if (mAuth.getCurrentUser() != null) {
            customerId = mAuth.getCurrentUser().getUid();
        }

        orderButton.setOnClickListener(view -> {
            // Call a method to save the order details to Firestore
            saveOrderDetailsToFirestore();
            Intent pay=new Intent(FoodPreviewActivity.this,PaymentActivity.class);
            startActivity(pay);
        });
        LinearLayout selectedItemsLayout = findViewById(R.id.selectedItemsLayout);

// Iterate through the selected food items and create TextViews for each item
        for (FoodItem foodItem : selectedFoodItems) {
            TextView itemTextView = new TextView(this);
            itemTextView.setTextColor(Color.BLACK);

            // Set text size to 16sp (adjust the size as needed)
            itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            itemTextView.setText(foodItem.getName() + " - " + foodItem.getPrice());

            // You can further customize the TextView's appearance if needed
            // itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            // itemTextView.setTextColor(Color.BLACK);

            // Add the TextView to the LinearLayout
            selectedItemsLayout.addView(itemTextView);
        }

    }
    private void saveOrderDetailsToFirestore() {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a map to store order details
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("name", name); // Add the name
        orderDetails.put("address", address); // Add the address
        orderDetails.put("phoneNo", phoneNo); // Add the phoneNo
        orderDetails.put("totalPrice", totalCartPrice);
        orderDetails.put("orderDate", new Date());
        orderDetails.put("selectedFoodItems", selectedFoodItems); // You can add a timestamp for the order date
        // Add more fields as needed, for example:
       // orderDetails.put("orderItems", foodItemList);

        // Reference to the currently logged-in user's document
        DocumentReference userRef = db.collection("users").document(customerId);

        // Set the order details directly in the user's document
        userRef
                .set(orderDetails)
                .addOnSuccessListener(aVoid -> {
                    // Order details saved successfully
                    // You can handle this as needed, such as displaying a confirmation message

                    // Show a confirmation message using a Toast
                    Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Start the OrderSummaryActivity here if needed
                    // Intent intent = new Intent(FoodPreviewActivity.this, Order_Summary.class);
                    // startActivity(intent);

                    // Finish the current activity to go back to the previous screen (menu.java)
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error in saving process!", Toast.LENGTH_SHORT).show();
                    // Handle any errors that occurred during the order saving process
                    // You can display an error message or handle the error as needed
                });
    }
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
            this.quantity = 0; // Initialize quantity to 1 by default
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

