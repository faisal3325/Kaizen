package com.mapmyindia.smartcity;

/**
 * Created by Faisal on 15-Oct-16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomGridNearby extends BaseAdapter {
    private Context mContext;
    private final int[] Imageid;


    public CustomGridNearby(Context c,int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.custom_grid_nearby, parent, false);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
