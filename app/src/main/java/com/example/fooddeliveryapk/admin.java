package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.fooddeliveryapk.model.UserModel;
import com.example.fooddeliveryapk.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "users" collection
        CollectionReference usersCollection = db.collection("users");

        // Reference to the TextView in your XML layout
        userDataTextView = findViewById(R.id.userDataTextView);

        // Create a list to store user data as Maps
        List<Map<String, Object>> userList = new ArrayList<>();

        // Retrieve data from all documents in the "users" collection
        usersCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder userDataBuilder = new StringBuilder();

                            for (DocumentSnapshot document : task.getResult()) {
                                // Convert each document to a Map
                                Map<String, Object> userData = document.getData();

                                // Add the user data to the list
                                userList.add(userData);

                                // Append the user data to the TextView
                                userDataBuilder.append("Name: ").append(userData.get("name")).append("\n");
                                userDataBuilder.append("Address: ").append(userData.get("address")).append("\n");
                                userDataBuilder.append("Phone Number: ").append(userData.get("phoneNo")).append("\n\n");
                            }

                            // Now, userList contains all the user data as Maps
                            // Display the user data in the TextView
                            userDataTextView.setText(userDataBuilder.toString());
                        } else {
                            // Handle errors
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            // You can display an error message to the admin or take other appropriate actions
                        }
                    }
                });
    }
}
