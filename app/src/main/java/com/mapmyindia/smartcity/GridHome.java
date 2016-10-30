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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GridHome extends AppCompatActivity {

    Intent intent;
    TextView autoComplete;
    GridView grid;
    LinearLayout search;
    public static final String PREFS_NAME = "CollisionPrefs";
    public static final String PREFS_NUM = "AccidentPrefs";
    public static boolean col  = false;

    int[] imageId = {
            R.drawable.nearby,
            R.drawable.issues_icon,
            R.drawable.col,
            R.drawable.gov_office
    };

    String[] gridtext = {
            "Nearby",
             "Issues",
            "Collision Detection",
            "Issues Statistics"
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

        CustomGridHome adapter = new CustomGridHome(GridHome.this, imageId, gridtext);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
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
                                .setNeutralButton("Set Emergency Contacts", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        LayoutInflater li = LayoutInflater.from(getApplicationContext());
                                        View promptsView = li.inflate(R.layout.layout_emergency, null);

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.collDialog));
                                        alertDialogBuilder.setView(promptsView);

                                        final EditText emergency1 = (EditText) promptsView.findViewById(R.id.contact1);
                                        final EditText emergency2 = (EditText) promptsView.findViewById(R.id.contact2);
                                        final EditText emergency3 = (EditText) promptsView.findViewById(R.id.contact3);

                                        SharedPreferences sh = getSharedPreferences(PREFS_NUM, 0);
                                        if(sh.getString("Num1", null) != null) {
                                            emergency1.setText(sh.getString("Num1", null));
                                            emergency2.setText(sh.getString("Num2", null));
                                            emergency3.setText(sh.getString("Num3", null));
                                        }

                                        alertDialogBuilder
                                                .setCancelable(false)
                                                .setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog,int id) {
                                                                if(emergency1.getText().length() == 10
                                                                        && emergency2.getText().length() == 10
                                                                        && emergency3.getText().length() == 10)   {
                                                                    SharedPreferences sh = getSharedPreferences(PREFS_NUM, 0);
                                                                    SharedPreferences.Editor editor = sh.edit();
                                                                    editor.putString("Num1", String.valueOf(emergency1.getText()));
                                                                    editor.putString("Num2", String.valueOf(emergency2.getText()));
                                                                    editor.putString("Num3", String.valueOf(emergency3.getText()));
                                                                    editor.apply();
                                                                }   else    Toast.makeText(GridHome.this, "Please enter a valid 10 digit number.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                })
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(checkBox.isChecked()) {
                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putString("Yes", "Yes").apply();
                                            startService(new Intent(getApplicationContext(), SensorService.class));
                                            Log.d("Sensor", "true");
                                            col = true;
                                        }   else    {
                                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.clear().apply();
                                            stopService(new Intent(getApplicationContext(), SensorService.class));
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