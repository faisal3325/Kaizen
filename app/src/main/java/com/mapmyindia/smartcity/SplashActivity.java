package com.mapmyindia.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by Faisal on 16-Oct-16.
 */
public class SplashActivity extends AppCompatActivity   {

    public static final String PREFS_NAME = "TutPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ImageView rotate_image =(ImageView) findViewById(R.id.imageView3);
        RotateAnimation rotate = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(9000);
        rotate_image.startAnimation(rotate);

        int SPLASH_TIME_OUT = 9000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (settings.getString("Seen", "").equals("Seen")) {
                    Log.d("Splash", "Activity");
                    Intent intent = new Intent(SplashActivity.this, GridHome.class);
                    startActivity(intent);
                }   else    {
                    Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                    startActivity(intent);
                    finish();
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}