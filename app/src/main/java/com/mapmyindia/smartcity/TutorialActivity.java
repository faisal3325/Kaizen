package com.mapmyindia.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Faisal on 16-Oct-16.
 */

public class TutorialActivity extends AppIntro  {

    public static final String PREFS_NAME = "TutPrefs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().hide();
        addSlide(AppIntroFragment.newInstance("Kaizen", "A Smart India Application which connect you to the Map where you can find " +
                "out Nearest place details like Restaurants, Hospitals, Atm, Bus Stop..Etc", R.drawable.tut_nearby, Color.parseColor("#673AB7")));
        addSlide(AppIntroFragment.newInstance("Kaizen", "A Smart India Issue Box where you can take a Real Time Image of Complaint " +
                "with Exact Location and will be uploaded to Admin Panel ", R.drawable.issues_icon, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("Kaizen", "A Smart India Accident detection which will detect if you are involved in " +
                "an accident using the sensors and will send SMS to all you emergency contacts with your location", R.drawable.col, Color.parseColor("#9C27B0")));

        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));
        showSkipButton(true);
        setProgressButtonEnabled(true);
        setVibrate(true);
        setVibrateIntensity(30);/*Our application mainly focuses on the smart implications of map to make the citizens smarter thus contributing towards a smarter city.

The following modules have been implemented in our application:
-Nearby- Using the application a user can find out nearby places like hospitals, ATM, police stations, etc. enabling them to be able to find such locations near to them.
-Issues Report- The Android camera is used to point at issues around in the city. Issues like potholes, accidents, garbage, child labor, etc. are reported along with a picture of the issue and the location of the issue to the web dashboard(https://dashboard.krumbs.io/dashboard/appathon7) as well as the Issue Statistics in the Android application so that other users or the government authorities can see and solve them.
-Collision detection- Using accelerator sensor the application is detecting whether the citizen is involved in an accident. If he is a victim, an automated SMS will be sent to the emergency contacts of the citizen.*/
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Seen", "Seen");
        editor.apply();

        Intent intent = new Intent(this, GridHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Seen", "Seen");
        editor.apply();

        Intent intent = new Intent(this, GridHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}