package com.mapmyindia.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Faisal on 16-Oct-16.
 */
public class SplashActivity extends AppCompatActivity {

    String place = null;
    Double lat;
    Double lng;

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

        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
        finish();
    }
}