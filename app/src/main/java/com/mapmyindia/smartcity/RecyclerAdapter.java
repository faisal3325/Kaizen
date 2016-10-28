package com.mapmyindia.smartcity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Faisal on 27-Oct-16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<String> name = new ArrayList<>();
    Context c;

    public RecyclerAdapter(ArrayList<String> placeName, Context context) {
        Log.d("place", String.valueOf(placeName.size()));
        name = placeName;
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

        ViewHolder(final View itemView) {
            super(itemView);

            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            placesName = (TextView) itemView.findViewById(R.id.item_title);
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
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}