package com.mapmyindia.smartcity;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<String> title, url;
    ArrayList<Bitmap> image;
    String city;
    Double lat, lng;
    ProgressDialog pd;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_layout);
        Log.d("News", "Running");
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading");

        Bundle extras = getIntent().getExtras();
        lat = extras.getDouble("Lat");
        lng = extras.getDouble("Lng");

        Log.d("Lat", String.valueOf(lat));
        Log.d("Lng", String.valueOf(lng));

        this.getSupportActionBar().hide();
        new fetchData().execute();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        title = new ArrayList<>();
        image = new ArrayList<>();
        url = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //https://webhose.io/search?token=12b9a9e0-eb8d-4ff8-9f7c-bc4c38cbcf33&format=json&q=Nagpur

    private class fetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            /******CITY******/
            HttpHandler sh = new HttpHandler();

            //http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=%09utuf3sdqIVGjQtDLUziuqKin8Tv68LFe&q=21%2C79

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?" +
                    "apikey=%09utuf3sdqIVGjQtDLUziuqKin8Tv68LFe&q=" +
                    lat +
                    "%2C"
                    + lng);


            Log.e("JSON", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    city = jsonObj.getString("EnglishName");

                } catch (final JSONException e) {
                    Log.e("LocationKey", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Some error has occurred while accessing your city.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e("LocationKey", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Some error has occurred while accessing your city.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            /*****NEWS****/

            Log.d("Run", "1");

            DefaultHttpClient client = new DefaultHttpClient();
            URL url1 = null;
            try {
                url1 = new URL("https://webhose.io/search?token=12b9a9e0-eb8d-4ff8-9f7c-bc4c38cbcf33&format=json&" +
                        "q=" + city);
                Log.d("Run", "2");
                } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpGet req = new HttpGet(String.valueOf(url1));
            req.setHeader("Accept", "text/plain");
            InputStream in = null;
            Log.d("Run", "3");
            try {
                HttpResponse res = client.execute(req);

                Log.d("Run", "4");
                assert res != null;
                HttpEntity jsonentity = res.getEntity();
                in = jsonentity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                Log.d("Run", "5");
                JSONObject jsonobj = new JSONObject(JSONParser.convertStreamToString(in));
                Log.e("JSON", "Response from url: " + jsonobj);

                title.clear();
                image.clear();
                url.clear();

                if (jsonobj != null) {
                    try {

                        Log.d("Run", "6");
                        JSONArray post = jsonobj.getJSONArray("posts");
                        for(int i = 0; i < 10; i++)  {
                            JSONObject obj = (JSONObject) post.get(i);
                            JSONObject thread = obj.getJSONObject("thread");
                            title.add(i, thread.getString("title"));
                            url.add(i, thread.getString("url"));
                            Bitmap u = getBitmapFromURL(thread.getString("main_image"));
                            if(u != null)   image.add(i, u);
                            else image.add(i, null);
                        }
                    } catch (final JSONException e) {
                        Log.e("News", "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Some error has occurred while accessing the news data.",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                } else {
                    Log.e("News", "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Some error has occurred while accessing the news data.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pd.hide();
            flag = true;
            adapter = new RecyclerNewsAdapter(getApplicationContext(), title, image, url);
            recyclerView.setAdapter(adapter);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            if(src.contains("null"))    {

            }

            URL url2 = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }


    @Override
    protected void onResume() {
        super.onPause();
        if(!flag)   new fetchData().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pd.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pd.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pd.dismiss();
    }
}
