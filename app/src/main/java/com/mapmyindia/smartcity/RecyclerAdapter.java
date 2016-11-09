package com.mapmyindia.smartcity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmi.apis.routing.DirectionListener;
import com.mmi.apis.routing.DirectionManager;
import com.mmi.apis.routing.Trip;
import com.mmi.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by Faisal on 27-Oct-16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<String> name = new ArrayList<>();
    String type;
    ArrayList<Double> placesCoordLat = new ArrayList<>();
    ArrayList<Double> placesCoordLng = new ArrayList<>();
    Double lat, lng;
    Context c;
    public static ArrayList<Trip> tripList = new ArrayList<>();

    public RecyclerAdapter(String placeType, ArrayList<String> placeName, Double lati, Double lngi, ArrayList<Double> placeLat, ArrayList<Double> placeLng, Context context) {
        Log.d("place", String.valueOf(placeName.size()));
        type = placeType;
        name = placeName;
        placesCoordLat = placeLat;
        placesCoordLng = placeLng;
        lat = lati;
        lng = lngi;
        c = context;
    }

    private int[] images = {
            R.drawable.colleges,
            R.drawable.hospital,
            R.drawable.gov_office,
            R.drawable.police,
            R.drawable.atm_icon,
            R.drawable.metro,
            R.drawable.public_toilets,
            R.drawable.restaurant,
            R.drawable.bus_stop,
            R.drawable.courier,
            R.drawable.cinema,
            R.drawable.market
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView placesName;
        FloatingActionButton fab_direction;

        ViewHolder(final View itemView) {
            super(itemView);

            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            placesName = (TextView) itemView.findViewById(R.id.item_title);
            fab_direction = (FloatingActionButton) itemView.findViewById(R.id.fab_direction);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.placesName.setText(name.get(i));

        if(type.contains("college")) viewHolder.itemImage.setImageResource(images[0]);
        else if(type.contains("hospital")) viewHolder.itemImage.setImageResource(images[1]);
        else if(type.contains("government")) viewHolder.itemImage.setImageResource(images[2]);
        else if(type.contains("police")) viewHolder.itemImage.setImageResource(images[3]);
        else if(type.contains("atm")) viewHolder.itemImage.setImageResource(images[4]);
        else if(type.contains("metro")) viewHolder.itemImage.setImageResource(images[5]);
        else if(type.contains("toilets")) viewHolder.itemImage.setImageResource(images[6]);
        else if(type.contains("restaurants")) viewHolder.itemImage.setImageResource(images[7]);
        else if(type.contains("bus")) viewHolder.itemImage.setImageResource(images[8]);
        else if(type.contains("courier")) viewHolder.itemImage.setImageResource(images[9]);
        else if(type.contains("cinema")) viewHolder.itemImage.setImageResource(images[10]);
        else if(type.contains("market")) viewHolder.itemImage.setImageResource(images[11]);
        else viewHolder.itemImage.setImageResource(images[11]);

        viewHolder.fab_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Clicked", "Direction");
                DirectionManager directionManager =new DirectionManager();
                directionManager.getDirections(new GeoPoint(lat, lng), new GeoPoint(placesCoordLat.get(i), placesCoordLng.get(i)), null, new DirectionListener() {
                    @Override
                    public void onResult(int code, final ArrayList<Trip> trips) {
                        //code:0 success, 1 exception, 2 no result
                        // array of Trip class. a trip represents a Route
                        if(code == 0)   {
                            Log.d("Trip", String.valueOf(trips.get(0)));
                            tripList = trips;
                            Intent intent = new Intent(c, MapActivity.class);
                            intent.putExtra("Place", "route");
                            intent.putExtra("Lat", lat);
                            intent.putExtra("Lng", lng);
                            intent.putExtra("Name", "ROUTE");
                            c.startActivity(intent);
                        }
                    }

                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}