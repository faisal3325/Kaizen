package com.mapmyindia.smartcity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;

import java.util.ArrayList;

public class CardLayout extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    MapView mMapView;
    MapmyIndiaMapView mMap;
    ArrayList<Double> placesCoordLat = new ArrayList<>();
    ArrayList<Double> placesCoordLng = new ArrayList<>();
    ArrayList<String> placesList = new ArrayList<>();
    Double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LicenceManager.getInstance().setRestAPIKey("zr39sem7c8i2ulwifya84ifbgmuvnj4y");
        LicenceManager.getInstance().setMapSDKKey("m68qj6audr8ko52ffbnis25lnygmtvls");
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_card_layout);
        mMap = (MapmyIndiaMapView) findViewById(R.id.map);
        mMapView = mMap.getMapView();

        Bundle extras = getIntent().getExtras();
        placesList = extras.getStringArrayList("Place Name");
        placesCoordLat = (ArrayList<Double>) getIntent().getSerializableExtra("Place Lat");
        placesCoordLng = (ArrayList<Double>) getIntent().getSerializableExtra("Place Lng");
        lat = extras.getDouble("Lat");
        lng = extras.getDouble("Lng");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("Card", String.valueOf(placesList.size()));

        adapter = new RecyclerAdapter(placesList, lat, lng, placesCoordLat, placesCoordLng, CardLayout.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}