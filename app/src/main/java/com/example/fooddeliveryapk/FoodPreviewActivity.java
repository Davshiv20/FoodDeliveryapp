package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fooddeliveryapk.FoodItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;


public class FoodPreviewActivity extends AppCompatActivity {
    private RecyclerView foodPreviewRecyclerView;
    private FoodPreviewAdapter foodPreviewAdapter;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_preview);
        List<FoodItem> foodItemList = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get the current user's UID
        if (mAuth.getCurrentUser() != null) {
            customerId = mAuth.getCurrentUser().getUid();
        }
        // Receive data from the previous activity (menu.java)
        Intent intent = getIntent();
        if (intent != null) {
            // You can retrieve the food item data here and add it to the foodItemList
            // For example:
            String foodName = intent.getStringExtra("foodName");
            String foodDescription = intent.getStringExtra("foodDescription");
            double foodRating = intent.getDoubleExtra("foodRating", 0.0);
            double pricePerItem = intent.getDoubleExtra("pricePerItem", 0.0);
            FoodItem foodItem = new FoodItem(1, foodName, Double.parseDouble(String.valueOf(pricePerItem)), foodDescription, foodRating, R.drawable.food_img_1);
;

// Update this line

            // Create a FoodItem object with the received data

            // Add the food item to the list
            foodItemList.add(foodItem);
        }

        foodPreviewRecyclerView = findViewById(R.id.foodPreviewRecyclerView);
        foodPreviewAdapter = new FoodPreviewAdapter(foodItemList);
        foodPreviewRecyclerView.setAdapter(foodPreviewAdapter);
        foodPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call a method to save the order details to Firestore
                saveOrderDetailsToFirestore(customerId, foodItemList);
            }
        });

        // Set a click listener for the "Order" button

    }



    private class FoodPreviewAdapter extends RecyclerView.Adapter<FoodPreviewViewHolder> {
        private List<FoodItem> dataList;

        public FoodPreviewAdapter(List<FoodItem> dataList) {
            this.dataList = dataList;
        }

        @Override
        public FoodPreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_preview, parent, false);
            return new FoodPreviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FoodPreviewViewHolder holder, final int position) {
            final FoodItem foodItem = dataList.get(position);

            holder.foodNameTextView.setText(foodItem.getName());
            holder.foodDescriptionTextView.setText(foodItem.getDescription());
            holder.foodRatingTextView.setText(String.valueOf(foodItem.getRating()));

            // Handle quantity increase button click
            holder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentQuantity = foodItem.getQuantity();

                    // Increase the quantity by 1 (you can modify this logic as needed)
                    currentQuantity++;

                    // Update the quantity in the FoodItem
                    foodItem.setQuantity(currentQuantity);

                    // Update the quantity TextView to display the new quantity
                    holder.quantityTextView.setText(String.valueOf(currentQuantity));
                    // Implement quantity increase logic here
                    // You can update the quantity in the foodItem and notify the adapter
                    double totalPrice = foodItem.getTotalPrice();
                    holder.totalPriceTextView.setText(String.format(Locale.US, "Total Price: %.2f", totalPrice));
                }
            });

            // Handle quantity decrease button click
            holder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentQuantity = foodItem.getQuantity();

                    // Ensure the quantity is greater than 1 before decreasing
                    if (currentQuantity > 1) {
                        // Decrease the quantity by 1
                        currentQuantity--;

                        // Update the quantity in the FoodItem
                        foodItem.setQuantity(currentQuantity);

                        // Update the quantity TextView to display the new quantity
                        holder.quantityTextView.setText(String.valueOf(currentQuantity));
                        double totalPrice = foodItem.getTotalPrice();

                        // Update the total price TextView to display the new total price
                        holder.totalPriceTextView.setText(String.format(Locale.US, "Total Price: %.2f", totalPrice));
                    }
                    // Implement quantity decrease logic here
                    // You can update the quantity in the foodItem and notify the adapter
                }
            });

            // Handle back button click
            holder.backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Finish the activity to go back to the previous screen (menu.java)
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
    private void saveOrderDetailsToFirestore(String customerId, List<FoodItem> foodItemList) {
        // You can implement the logic to save the order details to Firestore here
        // For example, create a new document in the "orders" collection with order details

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a map to store order details (modify this as per your order data structure)
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("orderItems", foodItemList);
        orderDetails.put("orderDate", new Date()); // You can add a timestamp for the order date

// Reference to the currently logged-in user's document
        DocumentReference userRef = db.collection("users").document(customerId);

// Reference to the "orders" subcollection under the user's document
        CollectionReference ordersCollection = userRef.collection("orders");

// Add the order document to the "orders" subcollection
        ordersCollection
                .add(orderDetails)
                .addOnSuccessListener(documentReference -> {
                    // Order details saved successfully
                    // You can handle this as needed, such as displaying a confirmation message

                    // Show a confirmation message using a Toast
                    Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Start the payment activity here
                    Intent paymentIntent = new Intent(FoodPreviewActivity.this, PaymentActivity.class);
                    startActivity(paymentIntent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error in saving process!", Toast.LENGTH_SHORT).show();
                    // Handle any errors that occurred during the order saving process
                    // You can display an error message or handle the error as needed
                });
        // Add the order document to the "orders" collection
//        db.collection("orders")
//                .add(orderDetails)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        // Order details saved successfully
//                        // You can handle this as needed, such as displaying a confirmation message
//                        Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
//                        Intent paymentIntent = new Intent(FoodPreviewActivity.this, PaymentActivity.class);
//                        startActivity(paymentIntent);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Handle any errors that occurred during the order saving process
//                        // You can display an error message or handle the error as needed
//
//                    }
//                });
    }
    private class FoodPreviewViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView foodDescriptionTextView;
        TextView foodRatingTextView;
        Button increaseQuantityButton;
        TextView quantityTextView;
        Button decreaseQuantityButton;
        Button backButton;
        TextView totalPriceTextView;



        public FoodPreviewViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodDescriptionTextView = itemView.findViewById(R.id.foodDescriptionTextView);
            foodRatingTextView = itemView.findViewById(R.id.foodRatingTextView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            backButton = itemView.findViewById(R.id.backButton);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView); // Initialize totalPriceTextView



        }
    }
}

