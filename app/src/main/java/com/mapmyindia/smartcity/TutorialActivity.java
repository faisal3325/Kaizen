package com.mapmyindia.smartcity;

import android.content.Intent;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons./*
        /*addSlide(firstFragment);
        addSlide(secondFragment);
        addSlide(thirdFragment);
        addSlide(fourthFragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Kaizen", "A Smart city application", R.drawable.police, Color.YELLOW));
        addSlide(AppIntroFragment.newInstance("Kaizen", "A smart city ATM", R.drawable.atm_icon, Color.GREEN));
        addSlide(AppIntroFragment.newInstance("Kaizen", "A Smart city Fire station", R.drawable.firebrigade, Color.RED));
        addSlide(AppIntroFragment.newInstance("Kaizen", "A Smart city Bus Stop", R.drawable.bus_stop, Color.BLUE));
        addSlide(AppIntroFragment.newInstance("Kaizen", "A Smart city Cargo", R.drawable.cargo, Color.BLACK));
        // OPTIONAL METHODS
        // Override bar/separator color.
  /*      setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));
*/
        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Intent intent = new Intent(this, GridHome.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent intent = new Intent(this, GridHome.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}