package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private static final String TAG = "AdminActivity";
    private TextView userDataTextView; // Reference to the TextView
    private LinearLayout adminLayout;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
                                // Create a LinearLayout to display user and order details
                                LinearLayout userDetailsLayout = new LinearLayout(admin.this);
                                userDetailsLayout.setOrientation(LinearLayout.VERTICAL);

                                // Create a TextView to display user details
                                TextView userTextView = new TextView(admin.this);
                                userTextView.setTextSize(18);
                                userTextView.setText("User Details:\n");
                                userDetailsLayout.addView(userTextView);

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
                                orderTextView.setText("Order Details:\n");
                                userDetailsLayout.addView(orderTextView);

                                // Reference to the "orders" subcollection under the user's document
                                CollectionReference ordersCollection = userDocument.getReference().collection("orders");

                                // Retrieve data from all documents in the "orders" subcollection
                                ordersCollection.get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> orderTask) {
                                                if (orderTask.isSuccessful()) {
                                                    for (DocumentSnapshot orderDocument : orderTask.getResult()) {
                                                        // Append order details to the orderTextView
                                                        Map<String, Object> orderData = orderDocument.getData();
                                                        String orderDate = orderData.get("orderDate").toString();
                                                        List<Map<String, Object>> orderItems = (List<Map<String, Object>>) orderData.get("orderItems");

                                                        orderTextView.append("Order Date: " + orderDate + "\n");
                                                        orderTextView.append("Order Items:\n");

                                                        // Loop through and append each order item
                                                        for (Map<String, Object> orderItem : orderItems) {
                                                            String itemName = orderItem.get("name").toString();
                                                            double itemPrice = Double.parseDouble(orderItem.get("price").toString());
                                                            int itemQuantity = Integer.parseInt(orderItem.get("quantity").toString());
                                                            orderTextView.append("   - " + itemName + ": $" + itemPrice + " x" + itemQuantity + "\n");
                                                        }
                                                        orderTextView.append("\n");
                                                    }
                                                } else {
                                                    // Handle errors
                                                    Log.e(TAG, "Error getting orders: ", orderTask.getException());
                                                }
                                            }
                                        });

                                // Add the userDetailsLayout to the adminLayout
                                adminLayout.addView(userDetailsLayout);
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
                // Finish the admin activity to go back
                Intent intent=new Intent(admin.this,ChooseOne.class);
                startActivity(intent);
            }
        });
    }

    // Method to fetch and append order details
    private void appendOrderDetails(Map<String, Object> userData, StringBuilder userDataBuilder) {
        // Check if the user has order details
        if (userData.containsKey("orderDetails")) {
            List<Map<String, Object>> orderDetailsList = (List<Map<String, Object>>) userData.get("orderDetails");
            if (orderDetailsList != null && !orderDetailsList.isEmpty()) {
                userDataBuilder.append("Order Details:\n");

                for (Map<String, Object> orderDetails : orderDetailsList) {
                    // Append order date, time, and price
                    userDataBuilder.append("Date: ").append(orderDetails.get("orderDate")).append("\n");
                    userDataBuilder.append("Time: ").append(orderDetails.get("orderTime")).append("\n");
                    userDataBuilder.append("Price: ").append(orderDetails.get("orderPrice")).append("\n\n");
                }
            }
        }
    }
}
