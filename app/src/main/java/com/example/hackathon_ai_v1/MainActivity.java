package com.example.hackathon_ai_v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WeatherClass.TempAPI
{
    //main viewpager, containing all other functions
    ViewPager2 viewPager2;

    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    //Weather Vars
    public static FusedLocationProviderClient fusedLocationProviderClient;
    public static String tempVal;

    //Location PART Vars
    public String address;

    //Battery PART Vars
    public String batteryVal;

    //Time PART Vars
    public String timeVal;

    //Date PART Vars
    public String dateVal;

    //API PART Vars



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //set up Uzbek locale
        setAppLocale("uz");

        setContentView(R.layout.activity_main);
        //input views
        Initialization();


        //Weather (Temperature + Address + City) PART for On-Create
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        getLocation();

        //Battery PART for On-Create
        getBattery();


        //Time PART for On-Create
        getTime();

        //Date PART for On-Create
        getDate();

        //API PART for On-Create

        //put sound on each swipe
        getFromApi(batteryVal);

        //getTemperature


    }

    //get all views from this method
    public void Initialization()
    {
        viewPager2 = findViewById(R.id.view_pager);
        int [] images = {R.drawable.ic_clock, R.drawable.ic_calendar, R.drawable.ic_battery, R.drawable.ic_weather,
                        R.drawable.ic_location};
        String [] description =
                {getString(R.string.time),
                getString(R.string.date),
                getString(R.string.battery),
                getString(R.string.weather),
                getString(R.string.street)};
        viewPagerItemArrayList = new ArrayList<>();
        for (int i =0; i< images.length; i++)
        {
            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i], description[i]);
            viewPagerItemArrayList.add(viewPagerItem);
        }

        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);
        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);


    }

    //method to setup any locale
    protected void setAppLocale(String localeCode)
    {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }


    /////////////////////////////////// Weather PART

    //method to get Location
    public void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>()
        {
            @Override
            public void onComplete(@NonNull Task<Location> task)
            {
                Location location = task.getResult();
                if (location != null)
                {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);

                    try
                    {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //Log.d("MyLog", "Country: " + addresses.get(0).getCountryCode());
                        //Log.d("MyLog", "City: " + addresses.get(0).getLocality());
                        //Log.d("MyLog", "Address: " + addresses.get(0).getAddressLine(0));
                        //temp = "\nCity: " + addresses.get(0).getLocality() + "\nAddress: "+ addresses.get(0).getAddressLine(0);
                        //CITY_NAME_STRING = addresses.get(0).getLocality();
                      // MAIN_URL_METRIC_STRING = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY_NAME_STRING + "&units=" + units + "&appid=" + API_KEY_STRING;
                        WeatherClass weatherClass = new WeatherClass(addresses.get(0).getLocality());
                        weatherClass.startEngine();
                        weatherClass.setTempAPI(MainActivity.this);


                        //Log.d("MyLog", temp);

                        address = addresses.get(0).getAddressLine(0);
                        Log.d("MyLog","Address: " + address);





                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    //////////////////////////////////////////////Battery PART

    public void getBattery()
    {
        BatteryManager bm = (BatteryManager) MainActivity.this.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        batteryVal = getString(R.string.battery) + " " +Integer.toString(batLevel)
                + " " +getString(R.string.percent);
        Log.d("MyLog","Battery: " + batteryVal);

    }

    //////////////////////////////////////////////Time PART

    public void getTime()
    {
        String currentHour = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String currentMinute = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());

        timeVal = getString(R.string.hour) + " " + currentHour + "dan " + currentMinute + " "
                + getString(R.string.minute) + " o'tdi";
        Log.d("MyLog","Hour: " + timeVal);
    }

    ///////////////////////////////////////////////////Date PART

    public void getDate()
    {
        String currentDate1 = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        String currentDate2 = new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date());
        String currentDate3 = new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date());
        switch (currentDate2.toLowerCase())
        {
            case "mon":
            {
                currentDate2 = getString(R.string.mon);
                break;
            }
            case "tue":
            {
                currentDate2 = getString(R.string.tue);
                break;
            }
            case "wed":
            {
                currentDate2 = getString(R.string.wed);
                break;
            }
            case "thu":
            {
                currentDate2 = getString(R.string.thu);
                break;
            }
            case "fri":
            {
                currentDate2 = getString(R.string.fri);
                break;
            }
            case "sat":
            {
                currentDate2 = getString(R.string.sat);
                break;
            }
            case "sun":
            {
                currentDate2 = getString(R.string.sun);
                break;
            }

        }


        //change MMM
        switch (currentDate3.toLowerCase())
        {
            case "jan":
            {
                currentDate3 = getString(R.string.jan);
                break;
            }
            case "feb":
            {
                currentDate3 = getString(R.string.feb);
                break;
            }
            case "mar":
            {
                currentDate3 = getString(R.string.mar);
                break;
            }
            case "apr":
            {
                currentDate3 = getString(R.string.apr);
                break;
            }
            case "may":
            {
                currentDate3 = getString(R.string.may);
                break;
            }
            case "jun":
            {
                currentDate3 = getString(R.string.jun);
                break;
            }
            case "jul":
            {
                currentDate3 = getString(R.string.jul);
                break;
            }
            case "aug":
            {
                currentDate3 = getString(R.string.aug);
                break;
            }
            case "sep":
            {
                currentDate3 = getString(R.string.sep);
                break;
            }
            case "oct":
            {
                currentDate3 = getString(R.string.oct);
                break;
            }
            case "nov":
            {
                currentDate3 = getString(R.string.nov);
                break;
            }
            case "dec":
            {
                currentDate3 = getString(R.string.dec);
                break;
            }

        }
        dateVal = currentDate1 + " "+ currentDate3 + ", " + currentDate2;
        Log.d("MyLog", "Date: " + dateVal);
    }

    ///////////////////////////////////////TTS PART

    public void getFromApi (String inputStr)
    {
        ApiCaller apiCaller = new ApiCaller(inputStr, this);
        apiCaller.startEngine();
    }

    @Override
    public void displayTemp(String temp)
    {
        tempVal = temp;
        Log.d("MyLog", "MAINTEMP: " + tempVal);
    }
}