package com.example.hpm.Coer_e_Outpass;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {
    Animation animation1,animation2;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        image=(ImageView)findViewById(R.id.image);
      /*  Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {*/

                animation1=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                animation2=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.antirotate);


                image.startAnimation(animation1);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                     image.startAnimation(animation2);
                        animation2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Intent s= new Intent(SplashScreenActivity.this,StartPageActivity.class);
                                startActivity(s);
                                finish();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
               // image.startAnimation(animation2);

          //  }
      //  },5000);

    }
}
