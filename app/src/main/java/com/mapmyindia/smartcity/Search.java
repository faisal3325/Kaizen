package com.mapmyindia.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.place.autosuggest.AutoSuggest;
import com.mmi.apis.place.autosuggest.AutoSuggestListener;
import com.mmi.apis.place.autosuggest.AutoSuggestManager;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    MapView mMapView;
    MapmyIndiaMapView mMap;
    EditText autoComplete;
    ImageView back;
    ArrayList<String> myDataset, lat, lng;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LicenceManager.getInstance().setRestAPIKey("zr39sem7c8i2ulwifya84ifbgmuvnj4y");
        LicenceManager.getInstance().setMapSDKKey("m68qj6audr8ko52ffbnis25lnygmtvls");
        setContentView(R.layout.activity_search);
        myDataset = new ArrayList<>();
        myDataset.clear();
        lat = new ArrayList<>();
        lat.clear();
        lng = new ArrayList<>();
        lng.clear();
        this.getSupportActionBar().hide();
        mMap = (MapmyIndiaMapView) findViewById(R.id.map);
        mMapView = mMap.getMapView();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.this.finish();
                Intent intent = new Intent(Search.this, GridHome.class);
                startActivity(intent);
            }
        });

        final AutoSuggestManager autoSuggestManager = new AutoSuggestManager();
        autoComplete = (EditText) findViewById(R.id.autoCompleteTextView);
        autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("Text", "before");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() <= 2)  {
                    myDataset.clear();
                    lat.clear();
                    lng.clear();
                    updateList();
                }
                autoSuggestManager.getSuggestions(String.valueOf(charSequence), new AutoSuggestListener() {
                    @Override
                    public void onResult(int code, ArrayList places) {
                        //code:0 success, 1 exception, 2 no result
                        // response in array of AutoSuggest class
                        if (code == 0) {
                            Log.d("AutoSuggest", "Success");
                            ArrayList<AutoSuggest> autoSuggests;
                            autoSuggests = places;
                            myDataset.clear();
                            lat.clear();
                            lng.clear();
                            updateList();
                            for (int i = 0; i < autoSuggests.size(); i++) {
                                Log.d("AutoSuggest", String.valueOf(autoSuggests.get(i)));
                                Log.d("AutoSuggest", String.valueOf(autoSuggests.get(i).getAddr()));
                                Log.d("AutoSuggest", String.valueOf(autoSuggests.get(i).getX()));
                                lat.add(String.valueOf(autoSuggests.get(i).getX()));
                                lng.add(String.valueOf(autoSuggests.get(i).getY()));
                                myDataset.add(autoSuggests.get(i).getAddr());
                                updateList();
                            }
                        }   else if (code == 1) Log.e("AutoSuggest", "Exception");
                        else if (code == 2) Log.d("AutoSuggest", "No Result");
                    }
                }, true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("Text", "after");
            }
        });
    }

    void updateList()   {
        mAdapter = new AutoSuggestAdapter(myDataset, lat, lng);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }
}
