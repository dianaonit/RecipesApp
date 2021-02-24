package com.example.deepflavours;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.deepflavours.Adapter.SliderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class ViewPagerSlides extends AppCompatActivity {

    //variables
    ViewPager viewPager;
    LinearLayout dots;
    SliderAdapter sliderAdapter;
    TextView[] mDots;
    Button letsGetStarted;
    Button backBtn;
    Button nextBtn;
    Button skipBtn;
    int mCurrentPage;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_viewpager);

        //hooks-functionality
        viewPager = findViewById(R.id.slideViewPager);
        dots = findViewById(R.id.dotsLayout);
        letsGetStarted = findViewById(R.id.getstarted_btn);
        backBtn = findViewById(R.id.back_btn);
        nextBtn = findViewById(R.id.next_btn);
        skipBtn = findViewById(R.id.skip_btn);


        //call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //dots
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(changeListener);


        letsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("connected",true);
                reference.updateChildren(hashMap);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }



    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        dots.removeAllViews();


        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.circle_indicator1));

            dots.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.circle_indicator2));

        }

        //onclickListeners
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage + 2);
            }
        });

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            mCurrentPage = position;

            if (position == 0) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setEnabled(true);
                skipBtn.setEnabled(true);
                backBtn.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                skipBtn.setVisibility(View.VISIBLE);


            } else if (position == 1) {
                letsGetStarted.setVisibility(View.INVISIBLE);
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                skipBtn.setEnabled(false);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                skipBtn.setVisibility(View.INVISIBLE);

            } else {
                letsGetStarted.setVisibility(View.VISIBLE);
                nextBtn.setEnabled(false);
                backBtn.setEnabled(true);
                skipBtn.setEnabled(false);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.INVISIBLE);
                skipBtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
