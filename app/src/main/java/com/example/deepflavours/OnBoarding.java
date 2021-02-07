package com.example.deepflavours;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnBoarding extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

         //trimitere la ui-ul login
        TextView tv_login= findViewById(R.id.tv_login);
           tv_login.setOnClickListener(view -> {
            Intent intent = new Intent(OnBoarding.this, Login.class);
            OnBoarding.this.startActivity(intent);
        });

           //trimitere la ui-ul signup
        Button createaccount_btn= findViewById(R.id.createaccount_btn);
        createaccount_btn.setOnClickListener(view -> {
            Intent intent = new Intent(OnBoarding.this, SignUp.class);
            OnBoarding.this.startActivity(intent);
        });

    }
}
