package com.example.fooddeliveryapk;




import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.fooddeliveryapk.utils.AndroidUtil;

import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CustomerReg extends AppCompatActivity {

    EditText usernameInput,AddressInput;
    Button letMeInBtn;
    ProgressBar progressBar;
    String phoneNumber;

    FirebaseAuth fAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg);
        AddressInput=findViewById(R.id.login_address);
        usernameInput = findViewById(R.id.login_username);
        letMeInBtn = findViewById(R.id.login_let_me_in_btn);
        progressBar =findViewById(R.id.login_progress_bar);
        phoneNumber = Objects.requireNonNull(getIntent().getExtras()).getString("phone");

        fAuth=FirebaseAuth.getInstance();




        letMeInBtn.setOnClickListener((v -> {
            insertdata();

        }));


    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void insertdata()
    {

        Map<String,String> items=new HashMap<>();
        items.put("name",usernameInput.getText().toString().trim());
        items.put("address",AddressInput.getText().toString().trim());
        items.put("phoneNo",phoneNumber.trim());
            db.collection("users").add(items)
                .addOnCompleteListener(task -> {
                    usernameInput.setText("");
                    AddressInput.setText("");
                    Intent hi=new Intent(CustomerReg.this,  menu.class);
                    startActivity(hi);



                    AndroidUtil.showToast(getApplicationContext(), "Letting you in!");
                });


    }

}