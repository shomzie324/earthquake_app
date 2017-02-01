package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.QueryUtils.earthquakes;


/**
 * Created by Shomari Malcolm on 2017-01-27.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    //QUery url
    private String mUrl;


      public EarthquakeLoader(Context context, String url) {
        super(context);
        // TODO(Done): Finish implementing this constructor
          mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
       if (mUrl == null) {
           return null;
       }
        earthquakes = QueryUtils.retrieveEarthquakeData(mUrl);

        return earthquakes;
    }
}
