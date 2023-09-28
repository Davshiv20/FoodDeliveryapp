package com.example.fooddeliveryapk;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderScheduleActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText instructionsEditText;
    private Button confirmButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_schedule);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        instructionsEditText = findViewById(R.id.instructionsEditText);
        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected date and time
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                int hour = timePicker.getCurrentHour(); // Use getCurrentHour() for API 23 and above
                int minute = timePicker.getCurrentMinute(); // Use getCurrentMinute() for API 23 and above

                // Get the user-entered instructions
                String instructions = instructionsEditText.getText().toString();

                // Get the current user's UID
                String customerId = mAuth.getCurrentUser().getUid();

                // Get the current time as the order time
                long orderTime = System.currentTimeMillis();

                // Update the Firestore document with the order time
                db.collection("users").document(customerId)
                        .update("orderTime", orderTime)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Document updated successfully, start PaymentActivity
                                Intent intent = new Intent(OrderScheduleActivity.this, PaymentActivity.class);
                                intent.putExtra("year", year);
                                intent.putExtra("month", month);
                                intent.putExtra("day", day);
                                intent.putExtra("hour", hour);
                                intent.putExtra("minute", minute);
                                intent.putExtra("instructions", instructions);
                                startActivity(intent);
                            } else {
                                // Handle the error
                                // You can display an error message or take appropriate action here
                            }
                        });
            }
        });
    }
}