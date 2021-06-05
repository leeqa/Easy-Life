package com.example.easyapplication.Splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easyapplication.LogIn.UserLogin;
import com.example.easyapplication.Main.Home.MainActivity;
import com.example.easyapplication.R;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dots_Layout;

    SliderAdapter sliderAdapter;

    TextView[] dots;

    Button letsGetStarted;

    Animation animation;

    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        viewPager =findViewById(R.id.slider);
        dots_Layout =findViewById(R.id.dots);
        letsGetStarted =findViewById(R.id.get_started_btn);

        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);


        letsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n1 = new Intent(OnBoarding.this,UserLogin.class);
                startActivity(n1);
                finish();
            }
        });

    }


    public void skip(View view){
        startActivity(new Intent(this, UserLogin.class));
        finish();
    }

    public void next(View view){
        viewPager.setCurrentItem(currentPosition + 1);

    }

    private void addDots(int position){
        dots = new TextView[4];
        dots_Layout.removeAllViews();

        for(int i=0;i<dots.length;i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dots_Layout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            currentPosition = position;

            if(position == 0) {

                letsGetStarted.setVisibility(View.INVISIBLE);

            } else if(position == 1){

                letsGetStarted.setVisibility(View.INVISIBLE);

            } else if(position == 2){

                letsGetStarted.setVisibility(View.INVISIBLE);

            } else {

                animation = AnimationUtils.loadAnimation(OnBoarding.this,R.anim.bottom_anim);
                letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}