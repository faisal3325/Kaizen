package com.mapmyindia.smartcity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

/**
 * Created by Faisal on 23-Oct-16.
 */

public class SensorService extends Service implements SensorEventListener   {

    private SensorManager sensorManager = null;
    public static final String PREFS_NUM = "AccidentPrefs";
    Double lat = 21d, lng = 79d;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("SensorService", "onStartCommand");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("Sensor", "onCreate");
        collisionNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SensorService", "onStartCommand");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        collisionNotification();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SensorService", "onStartCommand");
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER)
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
        if (gForce >= 10) {
            collision();
            sensorManager.unregisterListener(this);
        }
    }

    void collision() {
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
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .setOngoing(true);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mNotifyMgr.cancel(mNotificationId);
                SharedPreferences sh = getSharedPreferences(PREFS_NUM, 0);
                Log.d("Tick", "");
                if (sh.getString("Num1", null) != null) {
                    sendSMS(sh.getString("Num1", null), "Help me! I am involved in an accident! At the following coordinates. " +
                            "Please search the coordinate on MapMyIndia Map. " + GPSTracker.getLatitude() + ", " + GPSTracker.getLongitude());
                    sendSMS(sh.getString("Num2", null), "Help me! I am involved in an accident! At the following coordinates. " +
                            "Please search the coordinate on MapMyIndia Map. " + GPSTracker.getLatitude() + ", " + GPSTracker.getLongitude());
                    sendSMS(sh.getString("Num3", null), "Help me! I am involved in an accident! At the following coordinates. " +
                            "Please search the coordinate on MapMyIndia Map. " + GPSTracker.getLatitude() + ", " + GPSTracker.getLongitude());
                }
            }
        }.start();
        Toast.makeText(this, "Collision detected!", Toast.LENGTH_SHORT).show();
    }

    void collisionNotification() {
        final NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.d("Collision", "Notification");
        final NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Collision Detector")
                .setContentText("Running")
                .setColor(Color.BLACK)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(VISIBILITY_PUBLIC)
                .setOngoing(true);
        mNotifyMgr.notify(1, mBuilder.build());
    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}