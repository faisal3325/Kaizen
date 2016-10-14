package com.mapmyindia.smartcity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

/**
 * Created by Faisal on 12-Oct-16.
 */
public class NotificationActivity extends Activity {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String TYPE = "TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (Objects.equals(extras.getString(TYPE), "safe")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }   else if (Objects.equals(extras.getString(TYPE), "sos")) {
                Intent intent = new Intent(this, Injured.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Log.d("NotificationActivity", "");
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(TYPE, "sos");
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static PendingIntent getDismissIntent1(int notificationId, Context context) {
        Log.d("NotificationActivity", "");
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(TYPE, "safe");
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}