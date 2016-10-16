package com.mapmyindia.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.place.autosuggest.AutoSuggestListener;
import com.mmi.apis.place.autosuggest.AutoSuggestManager;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    MapView mMapView;
    MapmyIndiaMapView mMap;
    AutoCompleteTextView autoComplete;
    ImageView back;
    private List<PlaceList> placeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AutoCompleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LicenceManager.getInstance().setRestAPIKey("zr39sem7c8i2ulwifya84ifbgmuvnj4y");
        LicenceManager.getInstance().setMapSDKKey("m68qj6audr8ko52ffbnis25lnygmtvls");
        setContentView(R.layout.activity_search);

        this.getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new AutoCompleteAdapter(placeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareList();
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        mMap = (MapmyIndiaMapView) findViewById(R.id.map);
        mMapView = mMap.getMapView();

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.this.finish();
                Intent intent = new Intent(Search.this, GridHome.class);
                startActivity(intent);
            }
        });

        AutoSuggestManager autoSuggestManager = new AutoSuggestManager();
        autoSuggestManager.getSuggestions(String.valueOf(autoComplete.getText()), new AutoSuggestListener() {
            @Override
            public void onResult(int code, ArrayList places) {
                //code:0 success, 1 exception, 2 no result
                // response in array of AutoSuggest class
                if (code == 0) {
                    Log.d("AutoSuggest", "Success");
                    for (int i = 0; i < places.size(); i++)
                        Log.d("AutoSuggest", String.valueOf(places.get(i)));
                } else if (code == 1) Log.e("AutoSuggest", "Exception");
                else if (code == 2) Log.d("AutoSuggest", "No Result");
            }
        }, true);
    }

    private void prepareList() {
        PlaceList place = new PlaceList("Mad Max: Fury Road");
        placeList.add(place);

        place = new PlaceList("Mad Max: Fury Road");
        placeList.add(place);

        place = new PlaceList("Mad Max: Fury Road");
        placeList.add(place);

        place = new PlaceList("Mad Max: Fury Road");
        placeList.add(place);

        place = new PlaceList("Mad Max: Fury Road");
        placeList.add(place);

        place = new PlaceList("Mad Max: Fury Road");
        placeList.add(place);

        mAdapter.notifyDataSetChanged();
    }
}
