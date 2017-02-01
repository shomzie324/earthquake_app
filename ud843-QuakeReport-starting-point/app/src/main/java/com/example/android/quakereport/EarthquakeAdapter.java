package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        /**--------------------------------------------------------------------------------------- */

        /** Find magnitude textView in the earthquake list item layout by locating its ID*/
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);

        /** create instance of decimal formater to standardize magnitude output*/
        DecimalFormat decimalFormater = new DecimalFormat("0.0");
        //apply the formater to the raw extracted magnitude
        String magnitudeToDisplay = decimalFormater.format(currentEarthquake.getMagnitude());

        /** use getMagnitude to set the text of the textView*/
        magnitudeView.setText(magnitudeToDisplay);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        //TODO(Done): implement getMagnitudeColor method as a private method in the adapter to get the correct color with a switch statement
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        /**--------------------------------------------------------------------------------------- */

        // Create constant for the string split point
        final String WORD_SEPARATOR = "of";
        final String LOCATION_OFFSET_DEFAULT = "Near the";

        //get the location and apply to the split method, to be stored in a String array
        String [] locationArray = currentEarthquake.getLocation().split(WORD_SEPARATOR);

        //set up Strings to hold each location
        String locationOffset;
        String primaryLocation;

        //set each array element to separate variables
       // checks to see if the string was split in 2 and sets default si fit was not
        if (locationArray.length != 2) {
           locationOffset = LOCATION_OFFSET_DEFAULT;
            primaryLocation = locationArray[0];
        }
        else {
            locationOffset = locationArray[0] + "of";
            primaryLocation = locationArray[1];
        }

        /** Find locattion textView  in the earthquake list item layout by location its ID*/
        TextView locationView = (TextView) listItemView.findViewById(R.id.location);

        /** Find locationOffSet TextView with its id*/
        TextView offsetView = (TextView) listItemView.findViewById(R.id.locationOffSet);

        /** use getLocation to set the text of the textView*/
        locationView.setText(primaryLocation);
        offsetView.setText(locationOffset);

        /**--------------------------------------------------------------------------------------- */

        /** extract time as long to format into a date*/
        long timeInMilliseconds = currentEarthquake.getTimeInMilliseconds();

        /** put time into a date object to format it*/
        Date dateObject = new Date(timeInMilliseconds);

        /** Find date textView in the earthquake list item layout by locating its id*/
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        /** initialize a date formatter to call its method on the date object*/
        SimpleDateFormat dateFormater = new SimpleDateFormat("LLL dd,yyyy");

        /** create variable to hold formatted date, then call dateFormater.format w/date object as input*/
        String dateToDisplay = dateFormater.format(dateObject);

        /** set formatted date onto the right TextView*/

        dateView.setText(dateToDisplay);

        /** find text view for time with its id */
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);

        /** Create simpleDateFOrmat object to format the time*/
        SimpleDateFormat timeFormater = new SimpleDateFormat("h:mm a");

        /** format the date object for just the time*/
        String timeToDisplay = timeFormater.format(dateObject);

        /** Set the formatted time on the correct textView*/
        timeView.setText(timeToDisplay);

        return listItemView;

    }
    private int getMagnitudeColor (double magnitude){

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }
}