package com.mapmyindia.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

        if (settings.getString("Seen", "").equals("Seen")) {
            Log.d("Splash", "Activity");
            Intent intent = new Intent(SplashActivity.this, GridHome.class);
            startActivity(intent);
            finish();
        }   else    {
            Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
