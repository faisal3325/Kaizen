package com.mapmyindia.smartcity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GridHome extends AppCompatActivity {

    Intent intent;
    TextView autoComplete;
    GridView grid;
    ImageView dropbox;
    CardView card;
    ProgressBarCircularIndeterminate progress;
    LinearLayout search, weatherLayout;
    public static final String PREFS_NAME = "CollisionPrefs";
    public static final String PREFS_NUM = "AccidentPrefs";
    public static boolean col  = false, flag = false;
    Double lat, lng;
    String locationKey, city;

    int[] imageId = {
            R.drawable.nearby,
            R.drawable.issues_icon,
            R.drawable.col,
            R.drawable.report
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

        dropbox = (ImageView) findViewById(R.id.imageView3);
        weatherLayout = (LinearLayout) findViewById(R.id.weather);
        progress = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
        card = (CardView) findViewById(R.id.card_view);
        weatherLayout.setVisibility(View.INVISIBLE);

        GPSTracker gpsTracker = new GPSTracker(this);

        if(!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }   else {
            gpsTracker.getLocation();
            lat = GPSTracker.latitude;
            lng = GPSTracker.longitude;

            if (checkInternet()) {
                Log.d("Internet", "On");
                new getLocationKey().execute();
            } else {
                Log.d("Internet", "Off on Resume");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder
                        .setMessage("No internet connection on your device. Would you like to enable it?")
                        .setTitle("No Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton(" Enable Internet ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        flag = true;
                                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        GridHome.super.onPause();
                                        startActivity(dialogIntent);
                                    }
                                })
                        .setNegativeButton(" Cancel ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }

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

        dropbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GridHome.this, NewsActivity.class);
                intent.putExtra("Lat", lat);
                intent.putExtra("Lng", lng);
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

    private class getLocationKey extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            //http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=%09utuf3sdqIVGjQtDLUziuqKin8Tv68LFe&q=21%2C79

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?" +
                    "apikey=%09utuf3sdqIVGjQtDLUziuqKin8Tv68LFe&q=" +
                    lat +
                    "%2C"
                    + lng);


            Log.e("JSON", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    locationKey = jsonObj.getString("Key");
                    city = jsonObj.getString("EnglishName");

                    Log.d("LocationKey" , locationKey);
                } catch (final JSONException e) {
                    Log.e("LocationKey", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Some error has occurred while accessing the weather data.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e("LocationKey", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Some error has occurred while accessing the weather data.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            new getWeatherData().execute();
        }
    }

    private class getWeatherData extends AsyncTask<Void, Void, Void> {

        String wText, wIcon, tempValue, uri;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://dataservice.accuweather.com/currentconditions/v1/" +
                    locationKey +
                    "?apikey=%09utuf3sdqIVGjQtDLUziuqKin8Tv68LFe");

            Log.e("JSON", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    wText = jsonObject.getString("WeatherText");
                    wIcon = jsonObject.getString("WeatherIcon");
                    JSONObject temp = jsonObject.getJSONObject("Temperature");
                    JSONObject metric = temp.getJSONObject("Metric");
                    tempValue = metric.getString("Value");

                    Log.d("WeatherData", wText);
                    Log.d("WeatherData", wIcon);
                    Log.d("WeatherData", tempValue);

                    uri = "@drawable/s" + wIcon;

                } catch (final JSONException e) {
                    Log.e("LocationKey", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Some error has occurred while accessing the weather data.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e("LocationKey", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Some error has occurred while accessing the weather data.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            progress.setVisibility(View.INVISIBLE);
            weatherLayout.setVisibility(View.VISIBLE);
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());

            ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
            TextView weatherCond = (TextView) findViewById(R.id.weatherCondition);
            TextView tempe = (TextView) findViewById(R.id.temp);
            TextView city1 = (TextView) findViewById(R.id.textView3);

            weatherCond.setText(wText);
            city1.setText(city);
            tempe.setText(tempValue + (char) 0x00B0 + "C");
            Drawable res = getResources().getDrawable(imageResource);
            weatherIcon.setImageDrawable(res);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume", "");
        GPSTracker gpsTracker = new GPSTracker(this);

        if(!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }   else {
            gpsTracker.getLocation();
            lat = GPSTracker.latitude;
            lng = GPSTracker.longitude;

            if (checkInternet()) {
                Log.d("Internet", "On");
                new getLocationKey().execute();
            } else {
                Log.d("Internet", "Off on Resume");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder
                        .setMessage("No internet connection on your device. Would you like to enable it?")
                        .setTitle("No Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton(" Enable Internet ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        flag = true;
                                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        GridHome.super.onPause();
                                        startActivity(dialogIntent);
                                    }
                                })
                        .setNegativeButton(" Cancel ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
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

    boolean checkInternet()    {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}