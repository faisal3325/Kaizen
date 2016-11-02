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
    ArrayList<Double> placesCoordLat = new ArrayList<>();
    ArrayList<Double> placesCoordLng = new ArrayList<>();
    Double lat, lng;
    Context c;
    public static ArrayList<Trip> tripList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<String> placeName, Double lati, Double lngi, ArrayList<Double> placeLat, ArrayList<Double> placeLng, Context context) {
        Log.d("place", String.valueOf(placeName.size()));
        name = placeName;
        placesCoordLat = placeLat;
        placesCoordLng = placeLng;
        lat = lati;
        lng = lngi;
        c = context;
    }

    private int[] images = {R.drawable.bus_stop,
            R.drawable.atm_icon,
            R.drawable.nearby,
            R.drawable.hospital,
            R.drawable.police,
            R.drawable.firebrigade,
            R.drawable.issues_icon,
            R.drawable.public_toilets
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
        viewHolder.itemImage.setImageResource(images[i]);
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