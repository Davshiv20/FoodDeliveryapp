package com.example.fooddeliveryapk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.fooddeliveryapk.FoodItem; // Assuming you have a FoodItem class
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Order_Summary extends AppCompatActivity {

    private TextView orderDetailsTextView;
    private Button proceedToPaymentButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        // Initialize views
        orderDetailsTextView = findViewById(R.id.orderDetailsTextView);
        proceedToPaymentButton = findViewById(R.id.paymentButton);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get the current user's UID
        String customerId = mAuth.getCurrentUser().getUid();

        // Retrieve the entire user document from Firestore
        db.collection("users").document(customerId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && task.getResult().exists()) {
                            // Get the entire user document from Firestore
                            DocumentSnapshot userDocument = task.getResult();

                            // Parse and format the order details
                            String orderDetails = formatOrderDetails(userDocument);

                            // Display the formatted order details in the TextView
                            orderDetailsTextView.setText(orderDetails);
                        }
                    }
                });

        // Handle "Proceed to Payment" button click
        proceedToPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the PaymentActivity
                Intent paymentIntent = new Intent(Order_Summary.this, OrderScheduleActivity.class);
                startActivity(paymentIntent);
            }
        });
    }

    // Parse and format the order details
    private String formatOrderDetails(DocumentSnapshot userDocument) {
        StringBuilder formattedOrder = new StringBuilder();

        // Extract the order items list from the user document
        List<Map<String, Object>> orderItems = (List<Map<String, Object>>) userDocument.get("orderItems");
        long orderTime = userDocument.getLong("orderTime");

        if (orderItems != null && !orderItems.isEmpty()) {
            String formattedText = "Order Details:\n\n";
            orderDetailsTextView.setText(Html.fromHtml(formattedText));
            formattedOrder.append(formattedText);
            formattedOrder.append("Order Time: ");
            formattedOrder.append(formatOrderTime(orderTime));
            formattedOrder.append("\n\n");

            // Iterate through the order items and format them
            for (int i = 0; i < orderItems.size(); i++) {
                Map<String, Object> orderItem = orderItems.get(i);
                String foodName = orderItem.get("name").toString();
                int quantity = Integer.parseInt(orderItem.get("quantity").toString());
                double totalPrice = Double.parseDouble(orderItem.get("totalPrice").toString());

                formattedOrder.append("Item " + (i + 1) + ":\n");
                formattedOrder.append("Food Name: " + foodName + "\n");
                formattedOrder.append("Quantity: " + quantity + "\n");
                formattedOrder.append("Total Price: $" + totalPrice + "\n\n");
            }
        } else {
            formattedOrder.append("No order items found.");
        }

        return formattedOrder.toString();
    }
    private String formatOrderTime(long orderTime) {
        // You can use SimpleDateFormat or another method to format the timestamp
        // For example:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(orderTime));
    }
}
