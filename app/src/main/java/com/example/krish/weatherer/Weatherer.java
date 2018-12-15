package com.example.krish.weatherer;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.location.*;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.JsonReader;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class Weatherer extends AppCompatActivity {
    /*public String city;
    public int lat,lon;
    Geocoder geocoder;*/
    private FusedLocationProviderClient fusedLocationProviderClient;
    /*public static JSONObject jsonObject = new JSONObject();
    private LocationManager locationManager = null;*/
    LinearLayout hourly_LinearLayout;
    TextView city_textView, weather_condition, temp , temp_min, temp_max, pressure, wind, rain, humidity, sunrise, sunset, day, current_condition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hourly_LinearLayout = findViewById(R.id.hourly_LinearLayout);
        city_textView = findViewById(R.id.city_name);
        weather_condition = findViewById(R.id.weather_condition);
        temp = findViewById(R.id.temp);
        temp_max = findViewById(R.id.temp_max);
        temp_min = findViewById(R.id.temp_min);
        pressure = findViewById(R.id.pressure_perc);
        wind = findViewById(R.id.wind_speed);
        rain = findViewById(R.id.rain_perc);
        humidity = findViewById(R.id.humidity_perc);
        sunrise = findViewById(R.id.sunrise_time);
        sunset = findViewById(R.id.sunset_time);
        day = findViewById(R.id.day);
        current_condition = findViewById(R.id.current_condition);
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location location = new Location(locationManager.GPS_PROVIDER);
        if (PermissionChecker.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PermissionChecker.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Please Provide Location Access")
                        .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(Weatherer.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},99);
                            }
                        }).create().show();
            }
            else {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                            System.out.println(location.getLatitude());
                    }
                });
            }
        }
        else{

            //System.out.println(location.getLatitude()+" "+location.getLongitude());
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
        }
        String da = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().getTime())+" "+"TODAY";
        SpannableString Today = new SpannableString(da);
        Today.setSpan(new RelativeSizeSpan(1.3f),0,da.lastIndexOf(' '),0);
        day.setText(Today);
        DailyData openWeatherData = new DailyData();
        openWeatherData.execute("32.74","-97.11");
        WeeklyData weeklyData = new WeeklyData();
        weeklyData.execute("32.74","-97.11");
    }
    /*private LocationListener locationListener = location.ge/*{
        @Override
        public void onLocationChanged(Location location) {
            List<Address> addresses;
            try{
                geocoder = new Geocoder(getBaseContext(),Locale.getDefault());
                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if (addresses.size()>0)
                {
                    city = addresses.get(0).getLocality();
                    System.out.println(addresses.get(0).getLocality());
                    //city_textView.setText(city);
                }
                else
                    city = "not found";
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };*/
    private class DailyData extends AsyncTask<String,Void,JSONObject> {
        JSONObject jsonObject = new JSONObject();

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                InputStream inputStream = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + strings[0] + "&lon=" + strings[1] + "&type=accurate&units=metric&appid=2ddabb5b479ae7e758a20caaa28e7743").openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String string = "", jsonString = "";
                while ((string = bufferedReader.readLine()) != null)
                    jsonString += string;
                jsonObject = new JSONObject(jsonString);
            }
            catch (IOException | JSONException e)
            {
                System.out.println(e);
            }
            return jsonObject;
        }
        protected void onPostExecute(JSONObject jsonObject){

            super.onPostExecute(jsonObject);
            try {
                Weather weather = new Weather(jsonObject);
                city_textView.setText(weather.getCity());
                weather_condition.setText(weather.getCondition());
                SpannableString sunr = new SpannableString(weather.getSunrise());
                sunr.setSpan(new RelativeSizeSpan(1.2f),0,weather.getSunrise().indexOf('A'),0);
                sunrise.setText(sunr);
                SpannableString suns = new SpannableString(weather.getSunset());
                suns.setSpan(new RelativeSizeSpan(1.2f),0,weather.getSunset().indexOf('P'),0);
                sunset.setText(suns);
                wind.setText(weather.getWind_direction()+"° "+weather.getWind_speed()+" mps");
                pressure.setText(weather.getPressure()+" in hPa");
                rain.setText(weather.getRain()+"%");
                humidity.setText(weather.getHumidity()+"%");
                temp.setText(weather.getTemp());
                temp_min.setText(weather.getMin_temp());
                temp_max.setText(weather.getMax_temp());
                current_condition.setText("TODAY: "+weather.getCondition()+" currently. The high will be "+weather.getMax_temp()+". The low will be "+weather.getMin_temp()+".");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class WeeklyData extends AsyncTask<String,Integer,JSONObject>{
        JSONObject jsonObject = new JSONObject();
        Weather[] forecast;
        LinearLayout[] hourly_LinearLayout_Array;
        protected JSONObject doInBackground(String... strings) {
            try {
                InputStream inputStream = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=" + strings[0] + "&lon=" + strings[1] + "&units=metric&type=accurate&appid=2ddabb5b479ae7e758a20caaa28e7743").openStream();
                Scanner sc = new Scanner(new InputStreamReader(inputStream));
                JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
                StringBuffer jsonString = new StringBuffer();
                while (sc.hasNext())
                    jsonString.append(sc.nextLine());
                jsonObject = new JSONObject(jsonString.toString());
                forecast = new Weather[jsonObject.getJSONArray("list").length()];
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                int len = jsonArray.length();
                hourly_LinearLayout_Array = new LinearLayout[len];
                TextView hour[] = new TextView[len];
                ImageView imageView[] = new ImageView[len];
                TextView temp_hourly[] = new TextView[len];
                for (int i=0;i<jsonObject.getJSONArray("list").length();i++) {
                    forecast[i] = new Weather(jsonObject.getJSONArray("list").getJSONObject(i), 0);
                    hourly_LinearLayout_Array[i] = new LinearLayout(Weatherer.this);
                    hourly_LinearLayout_Array[i].setOrientation(LinearLayout.VERTICAL);
                    hour[i] = new TextView(Weatherer.this);
                    hour[i].setWidth(128);
                    hour[i].setGravity(1);
                    hour[i].setText(forecast[i].getHour());
                    imageView[i] = new ImageView(Weatherer.this);
                    imageView[i].setPadding(0,30,0,30);
                    imageView[i].setImageBitmap(forecast[i].getIcon());
                    temp_hourly[i] = new TextView(Weatherer.this);
                    temp_hourly[i].setWidth(128);
                    temp_hourly[i].setGravity(1);
                    temp_hourly[i].setText(forecast[i].getTemp());
                    hourly_LinearLayout_Array[i].addView(hour[i]);
                    hourly_LinearLayout_Array[i].addView(imageView[i]);
                    hourly_LinearLayout_Array[i].addView(temp_hourly[i]);
                    publishProgress(i);
                }
            }
            catch (IOException | JSONException e)
            {
                System.out.println(e);
            }
            return jsonObject;
        }
        protected void onProgressUpdate(Integer... Int)
        {
            super.onProgressUpdate(Int);
            hourly_LinearLayout.removeAllViews();
            for (int i=0;i<Int[0];i++)
                hourly_LinearLayout.addView(hourly_LinearLayout_Array[i]);
        }
    }
}

