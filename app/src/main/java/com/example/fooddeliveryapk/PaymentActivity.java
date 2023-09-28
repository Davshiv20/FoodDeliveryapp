package com.example.fooddeliveryapk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;




public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);



        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

//    private void startRazorpayPayment() {
//        // Initialize Razorpay checkout
//        Checkout checkout = new Checkout();
//        checkout.setKeyID("YOUR_RAZORPAY_API_KEY"); // Replace with your Razorpay API key
//
//        // Get the total price from Firebase
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//        if (mAuth.getCurrentUser() != null) {
//            String customerId = mAuth.getCurrentUser().getUid();
//
//            // Replace "users" and "currentOrder" with your Firestore collection and document names
//            DocumentReference currentOrderRef = db.collection("users").document(customerId).collection("currentOrder").document("orderDetails");
//
//            currentOrderRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document != null && document.exists()) {
//                            // Retrieve the total price from the document
//                            long totalPriceInPaise = document.getLong("totalPrice");
//
//                            // Convert the total price to rupees (assuming it's in paise)
//                            double totalPriceInRupees = totalPriceInPaise / 100.0;
//
//                            // Create a JSON object with payment details
//                            JSONObject options = new JSONObject();
//                            try {
//                                options.put("name", "Your App");
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                            try {
//                                options.put("description", "Payment for Order");
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                            try {
//                                options.put("currency", "INR");
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                            try {
//                                options.put("amount", totalPriceInRupees * 100); // Convert rupees to paise
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            // Set a callback URL if needed
//                            try {
//                                options.put("callback_url", "your_callback_url");
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            // Open Razorpay checkout activity
//                            checkout.open(PaymentActivity.this, options);
//                        } else {
//                            // Handle the case where the document doesn't exist
//                            Toast.makeText(PaymentActivity.this, "Error: Order details not found", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        // Handle errors
//                        Toast.makeText(PaymentActivity.this, "Error loading order details", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }

//    // Implement the `onPaymentSuccess` and `onPaymentError` methods
//    @Override
//    public void onPaymentSuccess(String razorpayPaymentID) {
//        // Handle successful payment
//        Toast.makeText(this, "Payment successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPaymentError(int code, String response) {
//        // Handle payment error
//        Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
//    }
//}
