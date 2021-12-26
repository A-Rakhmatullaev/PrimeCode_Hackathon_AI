package com.example.hackathon_ai_v1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherClass
{
    final String API_KEY_STRING = "8b845140700350e09e6d7f88da317077";
    private String CITY_NAME_STRING = "Tashkent";
    HttpURLConnection urlConnection;
    String units = "metric";
    //String MAIN_URL_METRIC_STRING = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY_NAME_STRING + "&units=" + units + "&appid=" + API_KEY_STRING;
    URL MAIN_URL_METRIC;
    private static String temperature;

    public WeatherClass (String CITY_NAME_STRING)
    {
       this.CITY_NAME_STRING = CITY_NAME_STRING;
    }
    //Vars for adding city
    public void startEngine()
    {
        SearchOWMEngine searchOWMEngine = new SearchOWMEngine();
        searchOWMEngine.execute(CITY_NAME_STRING);
    }

    private class SearchOWMEngine extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... strings)
        {
            String MAIN_URL_METRIC_STRING = "https://api.openweathermap.org/data/2.5/weather?q="+strings[0]+"&units="+units+"&appid="+API_KEY_STRING;
            try
            {
                //assign value for URL
                MAIN_URL_METRIC = new URL(MAIN_URL_METRIC_STRING);

                //connect to MAIN_URL
                urlConnection = (HttpURLConnection)MAIN_URL_METRIC.openConnection();

                if(urlConnection.getResponseCode()==200)
                {
                    //String where all json file will be written
                    String line = null;
                    //get all inputStream from connected HHTP
                    InputStream inputStream = urlConnection.getInputStream();
                    //Copy this inputStream to bufferedReader to read Stream of data
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    //initialize StringBuffer to get data from bufferedReader [consequently from Stream] since we don't exact length of data
                    //therefore of exact number of lines
                    StringBuffer stringBuffer = new StringBuffer();
                    //if line's value taken from bufferedReader equals to null, stop loop
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        //give one line string value of var Line to stringBuffer
                        stringBuffer.append(line);
                    }
                    //close bufferedReader since we don't need to handle Stream data anymore,
                    //so it closes the stream and frees PC's resources
                    bufferedReader.close();
                    //JSONObject/Array class to navigate and easily transform json file in string's view
                    //to the objects of java or just fetch data from string view of json file [or in other word StringBuffer] more conveniently
                    //get MainObj that includes all others from stringBuffer

                    final JSONObject mainObj = new JSONObject(stringBuffer.toString());

                    //>>Log.d("MyLog","LINE: "+ stringBuffer.toString() + "\n");

                    //since we need weather array we write JSONArray var and get it from mainObj in form of string
                    JSONArray weather_response_array = new JSONArray(mainObj.getString("weather"));

                    //>>Log.d("MyLog","LINE: "+ weather_response_array + "\n");

                    //inside of weather array we have only 1 object so it's written weather_response_array.getJSONObject(0).toString()
                    JSONObject weather_sub_response = new JSONObject(weather_response_array.getJSONObject(0).toString());


                    //pop-up custom dialog now
                    JSONObject sub_main_obj = new JSONObject(mainObj.getString("main"));

                    //temp - sub_main_obj.getString("temp")
                    //max_temp - sub_main_obj.getString("temp_max")
                    //min_temp - sub_main_obj.getString("temp_min")
                    //atmo_pressure - sub_main_obj.getString("pressure")
                    //humidity - sub_main_obj.getString("humidity")

                    //Assign dialog temp
                    Log.d("MyLog", "Temperature: " + sub_main_obj.getString("temp"));
                    temperature = sub_main_obj.getString("temp");

                    //Assign dialog city name and state
                    //JSONObject sysObj = new JSONObject(mainObj.getString("sys"));


                }
                else
                {
                    //NOW
                    //auto refresh if not downloaded

                }
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

    }





}
