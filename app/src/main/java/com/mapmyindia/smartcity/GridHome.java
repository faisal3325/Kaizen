package com.mapmyindia.smartcity;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridHome extends AppCompatActivity {

    Intent intent;
    TextView autoComplete;
    GridView grid;
    LinearLayout search;
    public static final String PREFS_NAME = "CollisionPrefs";
    public static boolean col  = false;

    int[] imageId = {
            R.drawable.nearby,
            R.drawable.issues_icon,
            R.drawable.col,
            R.drawable.gov_office
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_home);
        this.getSupportActionBar().hide();
        final NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(settings.getString("Yes", "").equals("Yes")) {
            col = true;
            Intent intent = new Intent(getApplicationContext()
                    , SensorService.class);
            this.startService(intent);
            Log.d("CheckBox", "true");
        }
        search = (LinearLayout) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridHome.this.onPause();
                Intent intent = new Intent(GridHome.this, Search.class);
                startActivity(intent);
            }
        });
        autoComplete = (TextView) findViewById(R.id.searchText);
        autoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridHome.this.onPause();
                Intent intent = new Intent(GridHome.this, Search.class);
                startActivity(intent);
            }
        });

        CustomGridHome adapter = new CustomGridHome(GridHome.this, imageId);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(GridHome.this, GridNearby.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(GridHome.this, KrumbsActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        final View checkBoxView = View.inflate(getApplicationContext(), R.layout.dialog_checkbox, null);
                        final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.collDialog));
                        if(col) {
                            checkBox.setEnabled(true);
                            checkBox.toggle();
                            checkBox.setChecked(true);
                            if(col) {
                                Intent intent = new Intent(getApplicationContext(), SensorService.class);
                                startService(intent);
                            }   else stopService(intent);
                        }
                        checkBox.setText(R.string.agree);

                        builder.setTitle("Collision Detector")
                                .setMessage(R.string.collision)
                                .setView(checkBoxView)
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(checkBox.isChecked())    {
                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putString("Yes", "Yes")
                                                    .apply();
                                            Intent intent = new Intent(GridHome.this, SensorService.class);
                                            startService(intent);
                                            Log.d("Sensor", "true");
                                            col = true;
                                        }   else    {
                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.clear()
                                                    .apply();
                                            Intent intent = new Intent(getApplicationContext(), SensorService.class);
                                            stopService(intent);
                                            Log.d("Sensor", "false");
                                            col = false;
                                            mNotifyMgr.cancelAll();
                                        }
                                    }
                                })
                                .show();
                        break;
                    case 3:
                        Intent intent = new Intent(GridHome.this, KrumbsReport.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(GridHome.this, SensorService.class);
        if(col) {
            startService(intent);
        }   else stopService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(GridHome.this, SensorService.class);
        if(col) {
            startService(intent);
        }   else stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(GridHome.this, SensorService.class);
        if(col) {
            startService(intent);
        }   else stopService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(GridHome.this, SensorService.class);
        if(col) {
            startService(intent);
        }   else stopService(intent);
    }
}