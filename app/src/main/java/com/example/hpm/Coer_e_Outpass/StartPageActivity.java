package com.example.hpm.Coer_e_Outpass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.gms.ads.MobileAds;


public class StartPageActivity extends AppCompatActivity {
    TextView wardenText,studentText,adminText;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        //Meaningless comment hehe ;p
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wardenText =(TextView)findViewById(R.id.wardenText);
        adminText=(TextView)findViewById(R.id.adminText);
        studentText =(TextView)findViewById(R.id.studentText);
        studentText.setTextColor(Color.BLACK);
        studentText.setTextSize(28f);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:

                        return new StudentLoginFragment();

                    case 1:
                        return new WardenLoginFragment();

                    case 2:
                        return new AdminLoginFragment();
                   // case 3:return null;
                    default:return null;

                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        wardenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        studentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences=getSharedPreferences("sharedprefstudentinfo", Context.MODE_PRIVATE);
                final String speamil=preferences.getString("email","");
                final String sppassword=preferences.getString("password","");


                if(!speamil.equals("")&&!sppassword.equals(""))
                {
                    startActivity(new Intent(StartPageActivity.this, WelcomePage.class));

                }
                else if(speamil.equals("")&&sppassword.equals(""))
                   viewPager.setCurrentItem(0);
            }

                    });
        adminText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               viewPager.setCurrentItem(2);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
              switch (position){
                  case 0:
                      studentText.setTextColor(Color.BLACK);
                      studentText.setTextSize(28f);
                      wardenText.setTextSize(20f);
                      adminText.setTextSize(20f);

                      break;
                  case 1:wardenText.setTextColor(Color.BLACK);
                      wardenText.setTextSize(28f);
                      studentText.setTextSize(20f);
                      adminText.setTextSize(20f);
                      break;
                  case 2:
                      wardenText.setTextSize(20f);
                      studentText.setTextSize(20f);
                      adminText.setTextColor(Color.BLACK);
                      adminText.setTextSize(28f);
                      break;
              }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
}
