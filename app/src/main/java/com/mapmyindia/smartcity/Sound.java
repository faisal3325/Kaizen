package com.mapmyindia.smartcity;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Sound extends Activity {

    TextView mStatusView, button;
    ImageView listenButton;
    MediaRecorder mRecorder;

    private ArrayList<Double> values = new ArrayList();
    private static double mEMA = 0.0, sum = 0, lat, lng;
    private static int i;
    private boolean listening = false;
    static final private double EMA_FILTER = 0.6;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        mStatusView = (TextView) findViewById(R.id.textView);
        listenButton = (ImageView) findViewById(R.id.imageView4);
        button = (TextView) findViewById(R.id.textView6);

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!listening) {

                    values.clear();
                    button.setText("LISTENING...");
                    button.setTextSize(30);
                    button.setTextColor(Color.BLUE);
                    sum = 0;
                    listening = true;
                    Log.d("", "jhgfydf");
                    listenButton.setImageResource(R.drawable.start_);

                    new CountDownTimer(11000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            startRecorder();
                            Log.i("Noise", "Tock");
                            soundDb(60);
                        }

                        public void onFinish() {
                            listening = false;
                            stopRecorder();
                            updateTv();
                        }
                    }.start();
                }
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
                android.util.Log.e("Sound", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));
            }   catch (java.lang.SecurityException e) {
                android.util.Log.e("Sound", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }   try     {
                mRecorder.start();
            }   catch (java.lang.SecurityException e) {
                android.util.Log.e("Sound", "SecurityException: " +
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
        i = a.intValue();
        mStatusView.setText(i + " dB");
        button.setText(i + " dB");
        if(i < 75)  button.setTextColor(Color.GREEN);
        else if(i > 75 && i < 100)  button.setTextColor(Color.YELLOW);
        else button.setTextColor(Color.RED);
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