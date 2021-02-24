package com.example.deepflavours;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.deepflavours.Fragment.HomeFragment;
import com.example.deepflavours.Fragment.ProfileFragment;
import com.example.deepflavours.Fragment.SaveFragment;
import com.example.deepflavours.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment;
    String profileid = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if(getIntent().getBooleanExtra("isShowAllPosts", false)){
            profileid = getIntent().getStringExtra("profileid");
            selectedFragment = new ProfileFragment(profileid);
            if(profileid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
            } else {
                bottomNavigationView.setSelectedItemId(R.id.nav_search);
            }
        } else {
            selectedFragment = new HomeFragment();
        }




        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            if (selectedFragment != null && !(selectedFragment instanceof HomeFragment)) {
                                selectedFragment = new HomeFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            }

                            break;

                        case R.id.nav_search:
                            if (selectedFragment != null && !(selectedFragment instanceof SearchFragment)) {
                                selectedFragment = new SearchFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            }

                            break;

                        case R.id.nav_add:
                            selectedFragment = null;
                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                            break;

                        case R.id.nav_saved:
                            if (selectedFragment != null && !(selectedFragment instanceof SaveFragment)) {
                                selectedFragment = new SaveFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            }

                            break;

                        case R.id.nav_profile:
                            if((profileid!=null && !profileid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) || (selectedFragment != null && !(selectedFragment instanceof ProfileFragment))) {
                                selectedFragment = new ProfileFragment(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                profileid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            }

                            break;
                    }

                    return true;
                }
            };

}
