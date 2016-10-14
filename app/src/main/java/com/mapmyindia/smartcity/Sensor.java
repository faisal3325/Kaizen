package com.mapmyindia.smartcity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

public class Sensor extends AppCompatActivity implements SensorEventListener    {

    SensorManager sensorManager;
    long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        Button gForce = (Button) findViewById(R.id.button);
        gForce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collision();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER)
            getAccelerometer(sensorEvent);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float gX = x / 9.8f;
        float gY = y / 9.8f;
        float gZ = z / 9.8f;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
        Log.d("GeForce", String.valueOf(gForce));
        if(gForce > 10) {
            collision();
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    void collision()    {
        final int mNotificationId = 1;
        final NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent safeIntent = NotificationActivity.getDismissIntent1(mNotificationId, this);
        PendingIntent sosIntent = NotificationActivity.getDismissIntent(mNotificationId, this);

        final NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.mipmap.ic_launcher, "I am safe", safeIntent)
                .addAction(R.mipmap.ic_launcher, "SOS", sosIntent)
                .setContentTitle("Collision")
                .setContentText("Are you dead?")
                .setColor(Color.BLACK)
                .setVibrate(new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500})
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(VISIBILITY_PUBLIC)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setOngoing(true);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mNotifyMgr.cancel(mNotificationId);
            }
        }.start();
        Toast.makeText(this, "Collision detected!", Toast.LENGTH_SHORT).show();
    }
}