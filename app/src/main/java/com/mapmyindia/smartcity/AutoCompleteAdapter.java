package com.mapmyindia.smartcity;

/**
 * Created by Faisal on 17-Oct-16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.MyViewHolder> {

    private List<PlaceList> placesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }


    public AutoCompleteAdapter(List<PlaceList> placesList) {
        this.placesList = placesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlaceList place = placesList.get(position);
        holder.title.setText(place.getTitle());
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }
}