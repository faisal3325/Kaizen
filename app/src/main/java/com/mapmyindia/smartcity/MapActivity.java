package com.mapmyindia.smartcity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.place.Place;
import com.mmi.apis.place.nearby.NearbyListener;
import com.mmi.apis.place.nearby.NearbyManager;
import com.mmi.apis.routing.Trip;
import com.mmi.layers.BasicInfoWindow;
import com.mmi.layers.Marker;
import com.mmi.layers.PathOverlay;
import com.mmi.util.GeoPoint;

import java.util.ArrayList;
import java.util.Objects;

public class MapActivity extends AppCompatActivity  {

    MapView mMapView;
    MapmyIndiaMapView mMap;
    Double lat = null, lng;
    FloatingActionButton fab;
    ArrayList<String> placesList;
    ArrayList<Double> placesCoordLat, placesCoordLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LicenceManager.getInstance().setMapSDKKey("m68qj6audr8ko52ffbnis25lnygmtvls");
        LicenceManager.getInstance().setRestAPIKey("zr39sem7c8i2ulwifya84ifbgmuvnj4y");

        setContentView(R.layout.activity_map);
        this.getSupportActionBar().hide();

        final String place;
        String name;
        Bundle extras = getIntent().getExtras();
        place = extras.getString("Place");
        lat = extras.getDouble("Lat");
        lng = extras.getDouble("Lng");
        name = extras.getString("Name");
        Log.d("Map", name);
        Log.d("Map", place);
        Log.d("Map", String.valueOf(lat));
        Log.d("Map", String.valueOf(lng));

        mMap = (MapmyIndiaMapView) findViewById(R.id.map);
        mMapView = mMap.getMapView();
        GeoPoint geoPoint = new GeoPoint(lat, lng);
        mMapView.setZoom(10);
        mMapView.setCenter(geoPoint);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View v) {
                if (placesList != null) {
                    Log.d("placesList", "First place");
                    Intent intent = new Intent(MapActivity.this, CardLayout.class);
                    intent.putExtra("Place Name", placesList);
                    intent.putExtra("Place Lat", placesCoordLat);
                    intent.putExtra("Place Lng", placesCoordLng);
                    intent.putExtra("Lat", lat);
                    intent.putExtra("Lng", lng);
                    intent.putExtra("Type", place);
                    startActivity(intent);
                }
            }
        });

        if (Objects.equals(place, "search")) {
            Log.d("Map", "Search");
            Log.d("Marker", name);
            Log.d("MapView", lat + ", " + lng);
            BasicInfoWindow infoWindow = new BasicInfoWindow(R.layout.tooltip, mMapView);
            infoWindow.setTipColor(getResources().getColor(R.color.aquamarine));

            Marker marker = new Marker(mMapView);
            marker.setTitle(name);
            marker.setDescription(name);
            marker.setSubDescription(name);
            marker.setPosition(geoPoint);
            marker.setInfoWindow(infoWindow);

            mMapView.getOverlays().add(marker);
            mMapView.setZoom(10);
            mMapView.setCenter(geoPoint);

            fab.setVisibility(View.INVISIBLE);
        }   else if (!Objects.equals(place, "search") && (!Objects.equals(place, "route"))) {
            Log.d("Map", "Nearby");
            Log.d("Place", place);
            Log.d("GeoPoint", String.valueOf(geoPoint));
            NearbyManager nearbyManager = new NearbyManager();
            nearbyManager.getNearbyPlaces(null, place, new GeoPoint(lat, lng), 1, new NearbyListener() {
                @Override
                public void onResult(int code, final ArrayList places) {
                    //code:0 success, 1 exception, 2 no result
                    Log.d("Nearby", "Function");
                    if (code == 1) {
                        Log.d("Nearby", "Exception");
                        Looper.prepare();
                        Intent intent = new Intent(MapActivity.this, GridNearby.class);
                        startActivity(intent);
                        MapActivity.this.finish();
                    }   else if (code == 2) {
                        Log.d("Nearby", "No result found");
                        Looper.prepare();
                        Intent intent = new Intent(MapActivity.this, GridNearby.class);
                        startActivity(intent);
                        MapActivity.this.finish();
                    }   else if (code == 0) {
                        Log.d("Nearby", "Function 2");
                        placesList = new ArrayList<>();
                        placesCoordLat = new ArrayList<>();
                        placesCoordLng = new ArrayList<>();
                        Place place1 = new Place();
                        for (int i = 0; i < places.size(); i++) {
                            Log.d("Nearby Places", String.valueOf(places.get(i)));
                            place1 = (Place) places.get(i);
                            placesList.add(place1.getDisplayName());
                            placesCoordLat.add(place1.latitude);
                            placesCoordLng.add(place1.longitude);
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
                        }
                        mMapView.setCenter(place1.getGeoPoint());
                    }
                }
            });
        }   else if (Objects.equals(place, "route"))    {
            Log.d("Map", "Route");
            fab.setVisibility(View.INVISIBLE);
            Trip t;
            t = RecyclerAdapter.tripList.get(0);

            ArrayList geoPoints = t.getPath();
            PathOverlay pathOverlay = new PathOverlay(MapActivity.this);
            pathOverlay.setColor(Color.BLUE);
            pathOverlay.setWidth(10);
            pathOverlay.setPoints(geoPoints);
            pathOverlay.setTitle("Route");

            BasicInfoWindow infoWindow = new BasicInfoWindow(R.layout.tooltip, mMapView);
            infoWindow.setTipColor(getResources().getColor(R.color.aquamarine));
            mMapView.getOverlays().add(pathOverlay);

            Marker marker = new Marker(mMapView);
            marker.setTitle("Your location");
            marker.setInfoWindow(infoWindow);
            marker.setPosition(new GeoPoint(lat, lng));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(marker);
            mMapView.setZoom(10);
            mMapView.setCenter(geoPoint);

            Log.d("Trip", String.valueOf(t.getAdvises()));
            Log.d("Trip", String.valueOf(t.getDistance()));
            Log.d("Trip", String.valueOf(t.getPath()));
        }   else if (Objects.equals(place, "sound"))    {
            Log.d("Map", "Sound");
            fab.setVisibility(View.INVISIBLE);

            Marker marker = new Marker(mMapView);
            marker.setTitle("Your location");
            marker.setPosition(new GeoPoint(lat, lng));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(marker);
            mMapView.setZoom(10);
            mMapView.setCenter(geoPoint);
        }
    }
}