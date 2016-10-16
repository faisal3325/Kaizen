package com.mapmyindia.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.util.GeoPoint;

public class GridHome extends AppCompatActivity {

    MapView mMapView;
    MapmyIndiaMapView mMap;
    TextView autoComplete;
    Intent intent;
    GridView grid;
    LinearLayout search;

    int[] imageId = {
            R.drawable.nearby,
            R.drawable.issues_icon,
            R.drawable.sensor
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LicenceManager.getInstance().setRestAPIKey("zr39sem7c8i2ulwifya84ifbgmuvnj4y");
        LicenceManager.getInstance().setMapSDKKey("m68qj6audr8ko52ffbnis25lnygmtvls");

        setContentView(R.layout.activity_grid_home);
        mMap = (MapmyIndiaMapView) findViewById(R.id.map);
        mMapView = mMap.getMapView();
        GeoPoint geoPoint= new GeoPoint(21.1770846, 79.0691993);
        mMapView.setCenter(geoPoint);

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

        CustomGridHome adapter = new CustomGridHome(GridHome.this,  imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)    {
                    case 0:
                        intent = new Intent(GridHome.this, GridNearby.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(GridHome.this, KrumbsActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(GridHome.this, Sensor.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}