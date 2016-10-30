package com.mapmyindia.smartcity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by Faisal on 12-Oct-16.
 */
public class NotificationActivity extends Activity {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String TYPE = "TYPE";
    public static final String PREFS_NUM = "AccidentPrefs";
    Double lat = 21d, lng = 79d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (Objects.equals(extras.getString(TYPE), "safe")) {
                Intent intent = new Intent(this, GridHome.class);
                startActivity(intent);
                finish();
            }   else if (Objects.equals(extras.getString(TYPE), "sos")) {
                SharedPreferences sh = getSharedPreferences(PREFS_NUM, 0);
                if(sh.getString("Num1", null) != null) {
                    Log.d("SMS", "");
                    sendSMS(sh.getString("Num1", null), "Help me! I am involved in an accident! At the following coordinates. " +
                            "Please search the coordinate on MapMyIndia Map. " + GPSTracker.getLatitude() + ", " + GPSTracker.getLongitude());
                    sendSMS(sh.getString("Num2", null), "Help me! I am involved in an accident! At the following coordinates. " +
                            "Please search the coordinate on MapMyIndia Map. " + GPSTracker.getLatitude() + ", " + GPSTracker.getLongitude());
                    sendSMS(sh.getString("Num3", null), "Help me! I am involved in an accident! At the following coordinates. " +
                            "Please search the coordinate on MapMyIndia Map. " + GPSTracker.getLatitude() + ", " + GPSTracker.getLongitude());
                }
            }
        }
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context)   {
        Log.d("NotificationActivity", "SOS");
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(TYPE, "sos");
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static PendingIntent getDismissIntent1(int notificationId, Context context)  {
        Log.d("NotificationActivity", "Safe");
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(TYPE, "safe");
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void sendSMS(String phoneNumber, String message)    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
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
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
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