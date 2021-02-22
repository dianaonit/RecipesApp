package com.example.deepflavours;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextInputLayout inputEmail, inputPass;
    Button btnLogIn;
    FirebaseAuth uFirebaseAuth;
    ProgressDialog pd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



         //trimitere la ui-ul OnBoarding
        ImageView left_arrow= findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, OnBoarding.class);
            Login.this.startActivity(intent);
        });

        TextView tv_back= findViewById(R.id.tv_back);
        tv_back.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, OnBoarding.class);
            Login.this.startActivity(intent);
        });


        //trimitere la ui-ul de Sign In
        TextView tv_SignUp= findViewById(R.id.tv_SignUp);
        tv_SignUp.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            Login.this.startActivity(intent);
        });

       uFirebaseAuth=FirebaseAuth.getInstance();
       btnLogIn=findViewById(R.id.btn_login);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        inputEmail=findViewById(R.id.editTextEmail);
        inputPass=findViewById(R.id.editTextPassword);


    }

    private void userLogin(){

        final String uEmail=inputEmail.getEditText().getText().toString().trim();
        final String uPass=inputPass.getEditText().getText().toString().trim();

        boolean error=false;

        if(uEmail.isEmpty()){
            inputEmail.setError(getResources().getString(R.string.empty_errEmail));
            inputEmail.requestFocus();
            error = true;
        }else{
            inputEmail.setError(null);
            inputEmail.requestFocus();
        }

        if(uPass.isEmpty()){
            inputPass.setError(getResources().getString(R.string.empty_errPass));
            inputPass.requestFocus();
            error=true;
        }else{
            inputPass.setError(null);
            inputPass.requestFocus();
        }


        if(!error) {
            pd = new ProgressDialog(Login.this,ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            pd.setMessage("Please wait..");
            pd.show();

            uFirebaseAuth.signInWithEmailAndPassword(uEmail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        pd.dismiss();
                        Toast.makeText(Login.this, getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        new Thread(new Runnable() {
                            public void run() {
                                startActivity(new Intent(Login.this,MainActivity.class));
                            }
                        }).start();
                    } else {
                        pd.dismiss();
                        Toast.makeText(Login.this,getResources().getString(R.string.login_failed) , Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



    }



}
