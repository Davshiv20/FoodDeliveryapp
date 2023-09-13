package com.example.fooddeliveryapk;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fooddeliveryapk.model.UserModel;
import com.example.fooddeliveryapk.utils.AndroidUtil;
import com.example.fooddeliveryapk.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomerReg extends AppCompatActivity {

    EditText usernameInput,AddressInput;
    Button letMeInBtn;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    // phoneNumber = getIntent().getExtras().getString("phone");
//   getUsername();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg);
        AddressInput=findViewById(R.id.login_address);
        usernameInput = findViewById(R.id.login_username);
        letMeInBtn = findViewById(R.id.login_let_me_in_btn);
        progressBar =findViewById(R.id.login_progress_bar);
        phoneNumber = getIntent().getExtras().getString("phone");

        fAuth=FirebaseAuth.getInstance();




        letMeInBtn.setOnClickListener((v -> {
            insertdata();
        }));


    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public void insertdata()
    {

        Map<String,String> items=new HashMap<>();
        items.put("name",usernameInput.getText().toString().trim());
        items.put("address",AddressInput.getText().toString().trim());
        items.put("phoneNo",phoneNumber.trim());
            db.collection("users").add(items)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        usernameInput.setText("");
                        AddressInput.setText("");

                        Intent intent = new Intent(CustomerReg.this, menu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        AndroidUtil.showToast(getApplicationContext(), "Letting you in!");
                    }
                });


    }

//    void setUsername() {
//        String username = usernameInput.getText().toString();
//        String address = AddressInput.getText().toString();
//
//        if (username.isEmpty() || username.length() < 3) {
//            usernameInput.setError("Username length should be at least 3 chars");
//            return;
//        }
//
//        setInProgress(true);
//
//        if (userModel != null) {
//            userModel.setUsername(username);
//            userModel.setAddress(address);
//        } else {
//            // Make sure to properly initialize userModel if it's null
//            userModel = new UserModel(phoneNumber, username, address, Timestamp.now(), FirebaseUtil.currentUserId());
//        }
//
//        if (FirebaseUtil.currentUserDetails() != null) {
//            FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    setInProgress(false);
//                    if (task.isSuccessful()) {
//                        Intent intent = new Intent(CustomerReg.this, ChooseOne.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        AndroidUtil.showToast(getApplicationContext(), "Letting you in!");
//                    } else {
//                        AndroidUtil.showToast(getApplicationContext(), "Error adding data");
//                    }
//                }
//            });
//        }
//    }


//    void getUsername(){
//        setInProgress(true);
//        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                setInProgress(false);
//                if(task.isSuccessful())
//                {
//                    userModel=task.getResult().toObject(UserModel.class);
//                    if(userModel!= null)
//                    {
//                        usernameInput.setText(userModel.getUsername());
//                        AddressInput.setText(userModel.getAddress());
//                    }
//                }
//            }
//        });
//    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }
}