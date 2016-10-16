package com.mapmyindia.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Faisal on 16-Oct-16.
 */
public class SplashActivity extends AppCompatActivity {

    String place = null;
    Double lat;
    Double lng;
    public static final String PREFS_NAME = "TutPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null)  {
            place = extras.getString("Place");
            lat = extras.getDouble("Lat");
            lng = extras.getDouble("Lng");
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("Seen", "").equals("Seen")) {
            Log.d("Splash", "Activity");
            Intent intent = new Intent(SplashActivity.this, GridHome.class);
            startActivity(intent);
        }   else    {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
            finish();
        }
    }
}