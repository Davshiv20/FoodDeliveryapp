package com.example.fooddeliveryapk;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView foodTitle1;
    TextView foodTitle2;
    TextView foodTitle3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.frontp);
        foodTitle1 = (TextView)findViewById(R.id.foodTitle1);
        foodTitle2 = (TextView)findViewById(R.id.foodTitle2);
        foodTitle3= (TextView)findViewById(R.id.foodTitle3);
        foodTitle1.animate().alpha(0f).setDuration(0);
        foodTitle2.animate().alpha(0f).setDuration(0);
        foodTitle3.animate().alpha(0f).setDuration(0);
        foodTitle1.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                foodTitle2.animate().alpha(1f).setDuration(400);
                foodTitle3.animate().alpha(1f).setDuration(1200);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,Signup.class);
                startActivity(intent);
                finish();

            }
        },3000);


    }
}