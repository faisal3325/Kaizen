package com.mapmyindia.smartcity;

/**
 * Created by Faisal on 15-Oct-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class GridNearby extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks   {
    GridView grid;
    int[] imageId = {
            R.drawable.firebrigade,
            R.drawable.hospital,
            R.drawable.police,
            R.drawable.atm_icon,
            R.drawable.public_toilets,
            R.drawable.restaurant,
            R.drawable.bus_stop,
            R.drawable.cargo,
            R.drawable.metro
    };
    GoogleApiClient mapGoogleApiClient;
    private double latiUser;
    private double lngiUser;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_nearby);

        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), GridNearby.this))
            fetchLocationData();
        else
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), GridNearby.this);

        CustomGridNearby adapter = new CustomGridNearby(GridNearby.this,  imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent;
                switch(position) {
                    case 0:
                        Log.d("Grid", "Clicked on first");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "firestation");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "firestation");
                        startActivity(intent);
                        break;
                    case 1:
                        Log.d("Grid", "Clicked on second");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "hospital");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        Log.d("Latitude: " + latiUser, "Longitude: " + lngiUser);
                        intent.putExtra("Name", "hospital");
                        startActivity(intent);
                        break;
                    case 2:
                        Log.d("Grid", "Clicked on third!");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "police");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "police");
                        startActivity(intent);
                        break;
                    case 3:
                        Log.d("Grid", "Clicked on third!");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "atm");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "atm");
                        startActivity(intent);
                        break;
                    case 4:
                        Log.d("Grid", "Clicked on third!");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "toilets");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "toilets");
                        startActivity(intent);
                        break;
                    case 5:
                        Log.d("Grid", "Clicked on third!");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "restaurants");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "restaurants");
                        startActivity(intent);
                        break;
                    case 6:
                        Log.d("Grid", "Clicked on third!");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "bus");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "bus");
                        startActivity(intent);
                        break;
                    case 7:
                        Log.d("Grid", "Clicked on third!");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "cargo");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "cargo");
                        startActivity(intent);
                        break;
                    case 8:
                        Log.d("Grid", "Clicked on third!");
                        intent = new Intent(GridNearby.this , MapActivity.class);
                        intent.putExtra("Place", "metro");
                        intent.putExtra("Lat", latiUser);
                        intent.putExtra("Lng", lngiUser);
                        intent.putExtra("Name", "metro");
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }

    public static boolean checkPermission(String strPermission, Context _c, Activity _a) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED)    return true;
        else  return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fetchLocationData();
                else
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void fetchLocationData() {
        mapGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
        mapGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult arg0) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle arg0) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), GridNearby.this);
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mapGoogleApiClient);
        if (mLastLocation != null) {
            latiUser = mLastLocation.getLatitude();
            lngiUser = mLastLocation.getLongitude();
            Log.d("Latitude: " + latiUser, "Longitude: " + lngiUser);
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
    }
}