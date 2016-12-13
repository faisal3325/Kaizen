package com.mapmyindia.smartcity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Sound extends Activity {

    TextView mStatusView;
    Button upload;
    ImageView listenButton;
    MediaRecorder mRecorder;
    FloatingActionButton fab;

    private ArrayList<Double> values = new ArrayList();
    private static double mEMA = 0.0, sum = 0, lat, lng;
    private static int i;
    private boolean listening = false, flag = false;
    static final private double EMA_FILTER = 0.6;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        mStatusView = (TextView) findViewById(R.id.textView);
        listenButton = (ImageView) findViewById(R.id.imageView4);
        fab = (FloatingActionButton) findViewById(R.id.map);
        upload = (Button) findViewById(R.id.upload);

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listening) {

                    values.clear();
                    sum = 0;
                    listening = true;
                    listenButton.setImageResource(R.drawable.start_button_highlighted);

                    new CountDownTimer(11000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            startRecorder();
                            Log.i("Noise", "Tock");
                            soundDb(40);
                        }

                        public void onFinish() {
                            listening = false;
                            stopRecorder();
                            updateTv();
                            listenButton.setImageResource(R.drawable.start_button);
                        }
                    }.start();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sound.this , MapActivity.class);
                intent.putExtra("Place", "sound");
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag) {
                    GPSTracker gpsTracker = new GPSTracker(Sound.this);
                    if (!gpsTracker.canGetLocation()) gpsTracker.showSettingsAlert();
                    else {
                        flag = false;
                        gpsTracker.getLocation();
                        lat = GPSTracker.latitude;
                        lng = GPSTracker.longitude;
                        Snackbar.make(view, lat + ", " + lng, Snackbar.LENGTH_SHORT);
                    }
                }   else    Snackbar.make(view, "Please listen first for noise levels.", Snackbar.LENGTH_SHORT);
            }
        });
    }

    public void onResume()  {
        super.onResume();
    }

    public void onPause()   {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder() {
        if (mRecorder == null)  {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try     {
                mRecorder.prepare();
            }   catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));
            }   catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }   try     {
                mRecorder.start();
            }   catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
        }
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTv(){
        Double a = avgValue();
        i = Integer.valueOf(a.intValue());
        mStatusView.setText(i + " dB");
        flag = true;
    }

    public double avgValue()  {
        for(int i = 0; i < values.size(); i++)
            sum = sum + values.get(i);
        return sum / 10;
    }

    public void soundDb(double ampl)    {
        if(20 * Math.log10(getAmplitudeEMA() / ampl) > 0)   values.add(20 * Math.log10(getAmplitudeEMA() / ampl));
        Log.d("Sound", String.valueOf(20 * Math.log10(getAmplitudeEMA() / ampl)));
    }

    public double getAmplitude() {
        if (mRecorder != null)  return  (mRecorder.getMaxAmplitude());
        else    return 0;
    }

    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
}