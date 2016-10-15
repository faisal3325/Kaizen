package com.mapmyindia.smartcity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class GridHome extends Activity {

    Intent intent;
    GridView grid;
    int[] imageId = {
            R.drawable.nearby,
            R.drawable.issues_icon,
            R.drawable.sensor
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_home);

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