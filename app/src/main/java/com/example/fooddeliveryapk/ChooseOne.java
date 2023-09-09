package com.example.fooddeliveryapk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ChooseOne extends AppCompatActivity {


    private TextView optionTextView;
    private Button restaurantButton;
    private Button customerButton;
    Intent intent;
    String type;

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
        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemail  = new Intent(ChooseOne.this,Restaurantphone.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(ChooseOne.this,RestaurantRegistration.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Register  = new Intent(ChooseOne.this,RestaurantRegistration.class);
                    startActivity(Register);
                }

            }
        });

        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemailcust  = new Intent(ChooseOne.this,CustomerLogin.class);
                    startActivity(loginemailcust);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphonecust  = new Intent(ChooseOne.this,CustomerPhone.class);
                    startActivity(loginphonecust);
                    finish();
                }
                if(type.equals("google")){
                    Intent Registercust  = new Intent(ChooseOne.this,CustomerReg.class);
                    startActivity(Registercust);
                }

            }
        });
    }
}