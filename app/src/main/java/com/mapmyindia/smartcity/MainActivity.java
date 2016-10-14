package com.mapmyindia.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText place = (EditText) findViewById(R.id.place);
        Button go = (Button) findViewById(R.id.go);
        Button sensor = (Button) findViewById(R.id.sensor);
        Button krumbs = (Button) findViewById(R.id.krumbs);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("Place", String.valueOf(place.getText()));
                Log.d("Place", String.valueOf(place.getText()));
                startActivity(intent);
            }
        });

        sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Sensor.class);
                startActivity(intent);
            }
        });

        krumbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, KrumbsActivity.class);
                startActivity(intent);
            }
        });
    }
}
