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
import android.widget.TextView;

class CustomGridNearby extends BaseAdapter {
    private Context mContext;
    private final int[] Imageid;
    private final String[] textlist;


    CustomGridNearby(Context c, int[] Imageid, String[] textlist) {
        mContext = c;
        this.Imageid = Imageid;
        this.textlist = textlist;
    }

    @Override
    public int getCount() {
        return Imageid.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.custom_grid_nearby, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            imageView.setImageResource(Imageid[position]);
            TextView textView = (TextView)grid.findViewById(R.id.grid_text);
            textView.setText(textlist[position]);
        } else {
            grid = convertView;
        }
        return grid;
    }
}
