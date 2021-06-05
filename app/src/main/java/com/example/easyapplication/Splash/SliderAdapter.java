package com.example.easyapplication.Splash;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.easyapplication.R;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int colors[] = {
            R.drawable.bg_yellow,
            R.drawable.bg_red,
            R.drawable.bg_blue,
            R.drawable.bg_purple

    };

    int Text_color[] = {
            0xFF000000,
            0xFFFFFFFF,
            0xFFFFFFFF,
            0xFFFFFFFF

    };

    int animations[] = {
            R.raw.login_facility,
            R.raw.budget_manager,
            R.raw.medicine_reminder,
            R.raw.prayer_reminder
    };


    int headings[] = {
            R.string.first_slide_title,
            R.string.second_slide_title,
            R.string.third_slide_title,
            R.string.fourth_slide_title
    };

    int descriptions[] = {
            R.string.first_slide_desc,
            R.string.second_slide_desc,
            R.string.third_slide_desc,
            R.string.fourth_slide_desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout,container,false);

        LottieAnimationView animationView = view.findViewById(R.id.animation);
        TextView heading = view.findViewById(R.id.slider_heading);
        TextView desc = view.findViewById(R.id.slider_desc);
        ConstraintLayout bg_color = view.findViewById(R.id.background_color);


        bg_color.setBackgroundResource(colors[position]);
        animationView.setAnimation(animations[position]);
        heading.setText(headings[position]);
        heading.setTextColor(Text_color[position]);
        desc.setText(descriptions[position]);
        desc.setTextColor(Text_color[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}