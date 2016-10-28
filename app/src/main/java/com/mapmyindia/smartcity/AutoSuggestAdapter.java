package com.mapmyindia.smartcity;

/**
 * Created by Faisal on 18-Oct-16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AutoSuggestAdapter extends RecyclerView.Adapter<AutoSuggestAdapter.ViewHolder> {
    private ArrayList<String> mDataset, latitude, longitude;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public RelativeLayout relativeLayout;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.textView2);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.layout);
        }
    }

    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AutoSuggestAdapter(ArrayList<String> myDataset, ArrayList<String> lat, ArrayList<String> lng) {
        mDataset = myDataset;
        latitude = lat;
        longitude = lng;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AutoSuggestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.autosuggest_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset.get(position));
        holder.relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Intent", mDataset.get(position));
                Log.d("Intent", latitude.get(position));
                Log.d("Intent", longitude.get(position));
                Intent intent = new Intent(v.getContext(), MapActivity.class);
                intent.putExtra("Place", "search");
                intent.putExtra("Name", mDataset.get(position));
                intent.putExtra("Lng", latitude.get(position));
                intent.putExtra("Lat", longitude.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}