package com.mapmyindia.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.place.Place;
import com.mmi.apis.place.nearby.NearbyListener;
import com.mmi.apis.place.nearby.NearbyManager;
import com.mmi.layers.BasicInfoWindow;
import com.mmi.layers.Marker;
import com.mmi.util.GeoPoint;

import java.util.ArrayList;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    MapView mMapView;
    MapmyIndiaMapView mMap;
    Double lat, lng;
    FloatingActionButton fab;
    ArrayList<String> placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String place, name;
        Bundle extras = getIntent().getExtras();
        place = extras.getString("Place");
        lat = extras.getDouble("Lat");
        lng = extras.getDouble("Lng");
        name = extras.getString("Name");
        Log.d("Map", name);
        Log.d("Map", place);
        Log.d("Map", String.valueOf(lat));
        Log.d("Map", String.valueOf(lng));

        LicenceManager.getInstance().setRestAPIKey("zr39sem7c8i2ulwifya84ifbgmuvnj4y");
        LicenceManager.getInstance().setMapSDKKey("m68qj6audr8ko52ffbnis25lnygmtvls");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View v) {
                if (placesList != null) {
                    Log.d("placesList", "First place");
                    Intent intent = new Intent(MapActivity.this, CardLayout.class);
                    intent.putExtra("Place Name", placesList);
                    startActivity(intent);
                }
            }
        });

        if (Objects.equals(place, "search")) {
            Log.d("Map", "Search");
            Log.d("Marker", name);
            mMap = (MapmyIndiaMapView) findViewById(R.id.map);
            mMapView = mMap.getMapView();
            GeoPoint geoPoint2 = new GeoPoint(lat, lng);
            BasicInfoWindow infoWindow = new BasicInfoWindow(R.layout.tooltip, mMapView);
            infoWindow.setTipColor(getResources().getColor(R.color.aquamarine));

            Marker marker = new Marker(mMapView);
            marker.setTitle(name);
            marker.setDescription(name);
            marker.setSubDescription(name);
            marker.setPosition(geoPoint2);
            marker.setInfoWindow(infoWindow);

            mMapView.getOverlays().add(marker);
            mMapView.setCenter(geoPoint2);
            mMapView.setZoom(20);
        } else if (Objects.equals(place, "krumbs")) {
            Log.d("Map", "Krumbs");
            mMap = (MapmyIndiaMapView) findViewById(R.id.map);
            mMapView = mMap.getMapView();
            GeoPoint geoPoint2 = new GeoPoint(lat, lng);
            Marker marker = new Marker(mMapView);
            marker.setPosition(geoPoint2);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(marker);
            mMapView.setCenter(geoPoint2);
            mMapView.setZoom(20);
        } else if (!Objects.equals(place, "krumbs") || !Objects.equals(place, "search")) {
            Log.d("Map", "Nearby");
            mMap = (MapmyIndiaMapView) findViewById(R.id.map);
            mMapView = mMap.getMapView();
            GeoPoint geoPoint = new GeoPoint(lat, lng);
            NearbyManager nearbyManager = new NearbyManager();
            nearbyManager.getNearbyPlaces(null, place, geoPoint, 1, new NearbyListener() {
                @Override
                public void onResult(int code, final ArrayList places) {
                    //code:0 success, 1 exception, 2 no result
                    if (code == 1) Log.d("Nearby", "Exception");
                    else if (code == 2) Log.d("Nearby", "No result found");
                    else if (code == 0) {
                        for (int i = 0; i < places.size(); i++) {
                            Log.d("Nearby Places", String.valueOf(places.get(i)));
                            Place place1 = (Place) places.get(i);
                            placesList = new ArrayList<>();
                            placesList.add(place1.getDisplayName());
                            Log.d("Place", place1.getDisplayName());
                            BasicInfoWindow infoWindow = new BasicInfoWindow(R.layout.tooltip, mMapView);
                            infoWindow.setTipColor(getResources().getColor(R.color.aquamarine));
                            Marker marker = new Marker(mMapView);

                            marker.setTitle(place1.getDisplayName());
                            marker.setDescription(place1.desc);
                            marker.setSubDescription(place1.more_info);
                            marker.setInfoWindow(infoWindow);
                            marker.setPosition(place1.getGeoPoint());
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            mMapView.getOverlays().add(marker);
                            mMapView.setCenter(place1.getGeoPoint());
                        }
                    }
                }
            });
        }
    }
}