package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shomari on 1/18/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

public EarthquakeAdapter (Context context, List<Earthquake> earthquakes){
    super(context,0,earthquakes);
}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent,false);
        }

       /** Find current earthquake positon*/
        Earthquake currentEarthquake = getItem(position);

        /** Find magnitude textView in the earthquake list item layout by locating its ID*/
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        /** use getMagnitude to set the text of the textView*/
        magnitudeView.setText(currentEarthquake.getMagnitude());

        /** Find locattion textView  in the earthquake list item layout by location its ID*/
        TextView locationView = (TextView) listItemView.findViewById(R.id.location);

        /** use getLocation to set the text of the textView*/
        locationView.setText(currentEarthquake.getLocation());

        /** Find date textView in the earthquake list item layout by locating its id*/
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        /** use getDate to set the text of the dateView*/
        dateView.setText(currentEarthquake.getDate());

        return listItemView;

    }
}
