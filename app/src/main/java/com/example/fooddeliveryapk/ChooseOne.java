package com.example.fooddeliveryapk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class ChooseOne extends AppCompatActivity {


    private TextView optionTextView;
    private Button restaurantButton;
    private Button customerButton;

    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_one);

        setContentView(R.layout.activity_choose_one);
        optionTextView = findViewById(R.id.option);
        restaurantButton = findViewById(R.id.btnRestaurant);
        customerButton = findViewById(R.id.btnCustomer);
        phoneNumber = getIntent().getExtras().getString("phone");



        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInadmin  = new Intent(ChooseOne.this,admin.class);
                logInadmin.putExtra("phone",phoneNumber);

                startActivity(logInadmin);



            }
        });

        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginuser = new Intent(ChooseOne.this,CustomerReg.class);
                loginuser.putExtra("phone",phoneNumber);
                startActivity(loginuser);

            }
        });
    }
}