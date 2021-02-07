package com.example.deepflavours;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class StartCooking extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    String languageCode;

    TextView tw_deepFlavours;
    TextView tw_deliciouslySimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("Language", MODE_PRIVATE);
        languageCode = sharedPreferences.getString("Selected_language","");
        if(!languageCode.isEmpty()){
            setLocale(languageCode);
        }

        setContentView(R.layout.activity_startcooking);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ImageView Language = (ImageView) findViewById(R.id.iv_language);
        Language.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });


        //fonturi ui StartCooking
        tw_deepFlavours= findViewById(R.id.deep_flavours);
        Typeface customFont_dF=Typeface.createFromAsset(getAssets(),"fonts/SEASRN.ttf");
        tw_deepFlavours.setTypeface(customFont_dF);

        tw_deliciouslySimple=findViewById(R.id.deliciously_simple);
        Typeface customFont_dS=Typeface.createFromAsset(getAssets(),"fonts/Dancing.otf");
        tw_deliciouslySimple.setTypeface(customFont_dS);


        Button startcooking_btn= findViewById(R.id.startcooking_btn);
        startcooking_btn.setOnClickListener(view -> {
            Intent intent = new Intent(StartCooking.this, OnBoarding.class);
            StartCooking.this.startActivity(intent);
        });

        constraintLayout = findViewById(R.id.id_startcooking);

    }

    private void showChangeLanguageDialog() {
        Resources resources = getResources();
        final String[] listItems = {resources.getString(R.string.languageTypeRo),resources.getString(R.string.languageTypeEn),resources.getString(R.string.languageTypeDe),resources.getString(R.string.languageTypeFr)};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartCooking.this);
        mBuilder.setTitle(R.string.Language);
        int checkedItem;
        if(languageCode.equalsIgnoreCase("ro")){
            checkedItem = 0;
        } else if( languageCode.equalsIgnoreCase("en")) {
            checkedItem = 1;
        } else if(languageCode.equalsIgnoreCase("de")){
            checkedItem = 2;
        } else if(languageCode.equalsIgnoreCase("fr")){
            checkedItem = 3;
        } else{
            checkedItem = -1;
        }
        mBuilder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("ro");
                } else if (i == 1) {
                    setLocale("en");
                } else if (i == 2) {
                    setLocale("de");
                } else if (i == 3) {
                    setLocale("fr");
                }
                dialogInterface.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String localeCode) {
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Language", MODE_PRIVATE).edit();
        editor.putString("Selected_language", localeCode);
        editor.apply();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }



}