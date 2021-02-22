package com.example.deepflavours;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    TextInputLayout userName, userEmail, userPass, userVerPass;
    Button btnCreateAccount;

    FirebaseAuth uFirebaseAuth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //trimitere la ui-ul de OnBoarding
        ImageView left_arrow= findViewById(R.id.left_arrow);
        left_arrow.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, OnBoarding.class);
            SignUp.this.startActivity(intent);
        });

        TextView tv_back= findViewById(R.id.tv_back);
        tv_back.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, OnBoarding.class);
            SignUp.this.startActivity(intent);
        });


         //trimitere la ui-ul Login
        TextView tv_LogIn= findViewById(R.id.tv_LogIn);
        tv_LogIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            SignUp.this.startActivity(intent);
        });

        uFirebaseAuth=FirebaseAuth.getInstance();

        btnCreateAccount=findViewById(R.id.createAccount_btn);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });


        userName=(TextInputLayout) findViewById(R.id.editTextName);
        userEmail=(TextInputLayout)findViewById(R.id.editTextEmail);
        userPass=(TextInputLayout)findViewById(R.id.editTextPassword);
        userVerPass=(TextInputLayout)findViewById(R.id.editTextPasswordVer);


    }


    private void createAccount(){

        final String uName=userName.getEditText().getText().toString().trim();
        final String uEmail=userEmail.getEditText().getText().toString().trim();
        final String uPass=userPass.getEditText().getText().toString().trim();
        final String uVerPass=userVerPass.getEditText().getText().toString().trim();


        boolean error = false;

        if(uName.isEmpty()){
            userName.setError(getResources().getString(R.string.empty_errfName));
            userName.requestFocus();
            error = true;
        }else{
            userName.setError(null);
            userName.requestFocus();
        }

        if(uEmail.isEmpty()){
            userEmail.setError(getResources().getString(R.string.empty_errEmail));
            userEmail.requestFocus();
            error = true;
        }else{
            userEmail.setError(null);
            userEmail.requestFocus();
        }

        if(uPass.isEmpty()){
            userPass.setError(getResources().getString(R.string.empty_errPass));
            userPass.requestFocus();
            error=true;
        }else if(uPass.length() < 6){
            userPass.setError(getResources().getString(R.string.length_errPass));
            userPass.requestFocus();
            error=true;
        }else{
            userPass.setError(null);
            userPass.requestFocus();
        }


        if(uVerPass.isEmpty()){
            userVerPass.setError(getResources().getString(R.string.empty_errVerPass));
            userVerPass.requestFocus();
            error=true;

        }else if(!uVerPass.equals(uPass)){
            userVerPass.setError(getResources().getString(R.string.notequal_errVerPass));
            userVerPass.requestFocus();
            error=true;
        }else{
            userVerPass.setError(null);
            userVerPass.requestFocus();
        }



        if(!error) {

            pd = new ProgressDialog(SignUp.this,ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            pd.setMessage("Please wait..");
            pd.show();

            uFirebaseAuth.createUserWithEmailAndPassword(uEmail, uPass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //User user = new User(uName,uEmail,uPass,uVerPass);
                                FirebaseUser firebaseUser = uFirebaseAuth.getCurrentUser();
                                String userid = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("username", uName);
                                hashMap.put("useremail",uEmail);
                                hashMap.put("userpassword", uPass);
                                hashMap.put("bio", "");
                                hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/deep-flavours.appspot.com/o/userplaceholder.png?alt=media&token=720bddfa-38ae-48b3-94c2-413692c2e499");
                                hashMap.put("connected",false);


                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            pd.dismiss();
                                            Toast.makeText(SignUp.this, getResources().getString(R.string.signup_success), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(SignUp.this, Login.class));

                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(SignUp.this, getResources().getString(R.string.signup_failed), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                            }  else{
                                    pd.dismiss();
                                    Toast.makeText(SignUp.this, getResources().getString(R.string.email_exists), Toast.LENGTH_LONG).show();
                                 }
                        }

                    });

        }


    }

}
