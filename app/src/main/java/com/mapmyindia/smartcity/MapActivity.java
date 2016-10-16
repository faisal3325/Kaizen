package com.mapmyindia.smartcity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.place.Place;
import com.mmi.apis.place.nearby.NearbyListener;
import com.mmi.apis.place.nearby.NearbyManager;
import com.mmi.layers.Marker;
import com.mmi.util.GeoPoint;

import java.util.ArrayList;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    MapView mMapView;
    MapmyIndiaMapView mMap;
    Double lat;
    Double lng;
    ArrayList<String> placesName;
    ArrayList<String> placesDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesName = new ArrayList<>();
        placesName.clear();
        placesDesc = new ArrayList<>();
        placesDesc.clear();
        String place = null;
        Bundle extras = getIntent().getExtras();
        place = extras.getString("Place");
        lat = extras.getDouble("Lat");
        lng = extras.getDouble("Lng");
        Log.d("Map", place);
        Log.d("Map", String.valueOf(lat));
        Log.d("Map", String.valueOf(lng));
        
        LicenceManager.getInstance().setRestAPIKey("zr39sem7c8i2ulwifya84ifbgmuvnj4y");
        LicenceManager.getInstance().setMapSDKKey("m68qj6audr8ko52ffbnis25lnygmtvls");

        setContentView(R.layout.activity_map);
        mMap = (MapmyIndiaMapView) findViewById(R.id.map);
        mMapView = mMap.getMapView();
        GeoPoint geoPoint= new GeoPoint(21.1770846, 79.0691993);
        mMapView.setCenter(geoPoint);
        mMapView.setZoom(20);

        if(!Objects.equals(place, "krumbs")) {
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
                            Log.d("Place", place1.getDisplayName());
                            placesName.add(place1.getDisplayName());
                            placesDesc.add(place1.desc);

                            Marker marker = new Marker(mMapView);
                            marker.setPosition(place1.getGeoPoint());
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            mMapView.getOverlays().add(marker);
                            mMapView.setCenter(place1.getGeoPoint());
                        }
                    }
                }
            });
        }   else if(Objects.equals(place, "krumbs"))    {
            Log.d("Map", "Krumbs");
            GeoPoint geoPoint2= new GeoPoint(lat, lng);
            Marker marker = new Marker(mMapView);
            marker.setPosition(geoPoint2);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(marker);
            mMapView.setCenter(geoPoint2);
        }
    }
}