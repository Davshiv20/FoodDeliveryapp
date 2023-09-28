package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class admin extends AppCompatActivity {

    private static final String TAG = "admin";
    private TextView userDataTextView; // Reference to the TextView
    private LinearLayout adminLayout;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        backButton = findViewById(R.id.backButton);
        adminLayout= findViewById(R.id.adminLayout);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "users" collection
        CollectionReference usersCollection = db.collection("users");

        // Reference to the LinearLayout and ScrollView in your XML layout
        // Retrieve data from all documents in the "users" collection
        usersCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot userDocument : task.getResult()) {
                                // Create a CardView to display user and order details
                                CardView userCardView = new CardView(admin.this);
                                userCardView.setLayoutParams(new CardView.LayoutParams(
                                        CardView.LayoutParams.MATCH_PARENT,
                                        CardView.LayoutParams.WRAP_CONTENT
                                ));
                                userCardView.setCardBackgroundColor(Color.TRANSPARENT);
                               // userCardView.setRadius(getResources().getDimension(R.dimen.card_corner_radius));

                                // Create a LinearLayout inside the CardView
                                LinearLayout userLinearLayout = new LinearLayout(admin.this);
                                userLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                userLinearLayout.setOrientation(LinearLayout.VERTICAL);
                                userLinearLayout.setPadding(16, 16, 16, 16);

                                // Create a TextView to display user details
                                TextView userTextView = new TextView(admin.this);
                                userTextView.setTextSize(18);
                                userTextView.setTextColor(Color.BLACK);
                                userTextView.setText("User Details:\n");

                                // Append user details to the userTextView
                                String userName = userDocument.getString("name");
                                String userAddress = userDocument.getString("address");
                                String userPhoneNumber = userDocument.getString("phoneNo");
                                userTextView.append("Name: " + userName + "\n");
                                userTextView.append("Address: " + userAddress + "\n");
                                userTextView.append("Phone Number: " + userPhoneNumber + "\n\n");

                                // Create a TextView to display order details
                                TextView orderTextView = new TextView(admin.this);
                                orderTextView.setTextSize(18);
                                orderTextView.setTextColor(Color.BLACK);
                                orderTextView.setText("Order Details:\n");

                                // Retrieve the order items array from the user document
                                List<Map<String, Object>> orderItems = (List<Map<String, Object>>) userDocument.get("selectedFoodItems");

                                if (orderItems != null && !orderItems.isEmpty()) {
                                    double totalOrderPrice = 0.0;

                                    for (Map<String, Object> orderItem : orderItems) {
                                        String itemName = orderItem.get("name").toString();
                                        String itemPrice = orderItem.get("price").toString();
                                        int itemQuantity = (int) (long) orderItem.get("quantity");
                                        double totalPrice = Double.parseDouble(itemPrice.replace("Rs.", "")) * itemQuantity;

                                        // Append order item details to the orderTextView
                                        orderTextView.append("Item: " + itemName + "\n");
                                        orderTextView.append("Price: Rs. " + itemPrice + "\n");
                                        orderTextView.append("Quantity: " + itemQuantity + "\n");
                                        orderTextView.append("Total Price: Rs. " + totalPrice + "\n\n");

                                        // Update the total order price
                                        totalOrderPrice += totalPrice;
                                    }
                                    orderTextView.append("Total Order Price: Rs. " + totalOrderPrice + "\n\n");

                                }
                                else {
                                    // Handle the case where there are no order items for this user
                                    orderTextView.append("No order items found.\n\n");
                                }

                                // Add userTextView and orderTextView to the userLinearLayout
                                userLinearLayout.addView(userTextView);
                                userLinearLayout.addView(orderTextView);

                                // Add the userLinearLayout to the userCardView
                                userCardView.addView(userLinearLayout);

                                // Add the userCardView to the adminLayout
                                adminLayout.addView(userCardView);
                            }
                        } else {
                            // Handle errors
                            Log.e(TAG, "Error getting users: ", task.getException());
                        }
                    }
                });
        // Set a click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}