package com.mapmyindia.smartcity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerNewsAdapter.ViewHolder> {

    private ArrayList<String> title, url;
    private ArrayList<Bitmap> image;
    Context context;

    RecyclerNewsAdapter(Context m, ArrayList<String> title1, ArrayList<Bitmap> image1, ArrayList<String> url1) {

        context = m;
        title = new ArrayList<>();
        image = new ArrayList<>();
        url = new ArrayList<>();

        title = title1;
        image = image1;
        url = url1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView itemTitle;

        public ViewHolder(View itemView) {

            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Log.d("Click", "");
                    int position = getAdapterPosition();
                    String url2 = url.get(position);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url2));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(title.get(i));
        if(image.get(i) == null) viewHolder.itemImage.setImageResource(R.drawable.image_not_avail);
        else viewHolder.itemImage.setImageBitmap(image.get(i));
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
}