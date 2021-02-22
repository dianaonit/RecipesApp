package com.example.deepflavours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.desert1,
            R.drawable.desert2,
            R.drawable.desert3
    };


    public String[] slide_headings = {

            "Deep Flavours",
            "Customise Your Dish",
            "Happy Cooking"
    };

    public String[] slide_desc = {
            "The ingredient that binds us together.",
            "Customise your dish by using the ingredients that are in your kitchen.",
            "Simply advice: always cook with passion!"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }


    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_slide_viewpager, container, false);


        TextView slideImageView = (TextView) view.findViewById(R.id.iv_viewpager);
       // ImageView slideImageView = (ImageView) view.findViewById(R.id.iv_viewpager);
        TextView slideHeading = (TextView) view.findViewById(R.id.tv_headings);
        TextView slideDescription = (TextView) view.findViewById(R.id.tv_desc);

       // slideImageView.setImageResource(slide_images[position]);
        slideImageView.setBackgroundResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
