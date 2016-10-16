package com.mapmyindia.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridHome extends AppCompatActivity {

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
        setContentView(R.layout.activity_grid_home);
        this.getSupportActionBar().hide();
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
        grid = (GridView) findViewById(R.id.grid);
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