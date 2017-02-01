package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    //TODO: 1. Add CreateUrl() to make a string a url object
    //TODO: 2.  add makeHTTPRequest() to use the url to make a connection,send the request,
    //TODO: get the response as inputStream, into input stream reader, into buffered reader
    //TODO: read each line and append to a string built output which will contain JSONresponse

    /** Signature for earthquakes array list needed outside extractEarthquakes to be accessed by retrieveEarthquakeData as well ;they return the same result
     * but the logic processing and populating the list is handled by the extract method*/
    public static ArrayList<Earthquake> earthquakes = new ArrayList<>();
   //ArrayList<Earthquake> earthquakes = null;

    public static ArrayList<Earthquake> retrieveEarthquakeData(String mUrl){


    /** Create Url with input String*/
    URL url = createUrl(mUrl);
    /** make HTTP request with the url and process into JSON Response*/
    String jsonResponse = null;
    try {
        jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
        Log.e(LOG_TAG, "Error closing input stream", e);
    }
    /** parse JSON response and extract desired fields to create earthquake object and add to ArrayList of earthquakes*/
    ArrayList<Earthquake> earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String mUrl) {
        URL url = null;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
      //create jsonResponse variable to hold the result or null if the URL is null
        String jsonResponse = "";

        //If the URL is null return early
        if (url == null) {
            return jsonResponse;
        }

        //initialize HTTPURLConnection object and Input Stream object
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;


        try{
            // attempt to establish connection with the url given
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request is succeful (returns code 200) then read the input stream
            if (urlConnection.getResponseCode() == 200){
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);}

            else{
                Log.e(LOG_TAG, "Error response code:" +urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
        Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
       //initialize string builder to append to it each time the buffer reader reads a line
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            /** if inputStream is not null put it in an input stream read
             * put the input stream reader into a buffered reader
             * read the first line of the buffered read to add to immutable stirng called line
             * as long as line is not null, read from it, append to string builder and replace line by reading another line */
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // reads entire first line of input stream
            String line = bufferedReader.readLine();

            /** since we already checked that inputStream != null atleast one line will be read
             * as long as there are subsequent lines to put into the line String it will keep appending to output*/
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String jsonResponse) {

// Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO(DONE): Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject root = new JSONObject(jsonResponse);
            JSONArray featuresArray = root.getJSONArray("features");

            for(int i = 0;i < featuresArray.length();i++){
                JSONObject currentEarthquake = featuresArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                // Extract the value for the key called "url"
                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude,location,time, url);

                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}