package com.jns.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final String[] action;
    private int[] images = null;
    public CustomGrid(Context c,String[] web,int[] images ) {
        mContext = c;
        this.images = images;
        this.action = web;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return action.length;
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
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_image_text, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(action[position]);
            imageView.setImageResource(images[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}