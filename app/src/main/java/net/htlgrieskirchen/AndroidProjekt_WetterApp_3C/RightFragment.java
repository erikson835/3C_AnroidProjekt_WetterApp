package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RightFragment extends Fragment {

    private ArrayList<Adresse> adresslist = LeftFragment.adresslist;
    static LinearLayout linearLayout;
    private String citynameString;

    private TextView cityname;
    private TextView mainweather;
    private TextView anmerkung;
    private TextView temp;
    private TextView temp_min_max;
    private ImageView weatherIcon;
    private TextView right_sunset;
    private TextView right_sunrise;
    private TextView right_druck;
    private TextView right_luftfeuchtigkeit;
    private TextView right_wind;
    private TextView right_feelslike;
    //-----API-------------------
    String main;
    String description;
    String icon;
    //Main
    double tempMain;
    double feelslike;
    double temp_min;
    double temp_max;
    double druck;
    double luftfeuchtigkeit;
    //Wind
    double speed;
    double direction;
    //Clouds
    double percent;
    //Rain
    double rain1H;
    double rain3H;
    //Snow
    double snow1H;
    double snow3H;
    //Sys
    String countrycode;
    String sunrise;
    String sunset;
    //Timezone
    double timezone;
    //Name
    String cityname_1;
    //---------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rightfragment, container, false);
        intializeViews(view);
        linearLayout = view.findViewById(R.id.rightLayout);
        linearLayout.setBackgroundColor(MainActivity.color);
        return view;
    }

    private void intializeViews(View view) {
        cityname = (TextView) view.findViewById(R.id.right_cityname);
        mainweather = (TextView) view.findViewById(R.id.right_mainweather);
        anmerkung = (TextView) view.findViewById(R.id.right_description);
        temp = (TextView) view.findViewById(R.id.right_temp);
        temp_min_max = (TextView) view.findViewById(R.id.right_temp_min_max);
        weatherIcon = (ImageView) view.findViewById(R.id.right_weather_icon);
        right_sunrise = (TextView) view.findViewById(R.id.right_sunrise);
        right_sunset = (TextView) view.findViewById(R.id.right_sunset);
        right_druck = (TextView) view.findViewById(R.id.right_druck);
        right_luftfeuchtigkeit = (TextView) view.findViewById(R.id.right_luftfeuchtigkeit);
        right_wind = (TextView) view.findViewById(R.id.right_wind);
        right_feelslike = (TextView) view.findViewById(R.id.right_feelslike);
    }

    private double hpaToBar(double hpa){
        double bar = hpa / 1013.25;
        return Math.round(bar*100.0)/100.0;
    }

    public JSONObject startAsyncTask(String cityname) {
        GetData task = new GetData(cityname);
        task.execute("AsyncTask Thread ready");
        try {
            return task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void weatherData(String cityname){
        JSONObject jsonObject;
        try {
            jsonObject = startAsyncTask(cityname);
            //Weather
            JSONArray jsonWeather = jsonObject.getJSONArray("weather");
            main = jsonWeather.getJSONObject(0).getString("main");
            description = jsonWeather.getJSONObject(0).getString("description");
            icon = jsonWeather.getJSONObject(0).getString("icon");
            //Main
            JSONObject jsonMain = jsonObject.getJSONObject("main");
            tempMain = jsonMain.getDouble("temp");
            feelslike = jsonMain.getDouble("feels_like");
            temp_min = jsonMain.getDouble("temp_min");
            temp_max = jsonMain.getDouble("temp_max");
            druck = jsonMain.getDouble("pressure");
            luftfeuchtigkeit = jsonMain.getDouble("humidity");
            //Wind
            JSONObject jsonWind = jsonObject.getJSONObject("wind");
            speed = jsonWind.getDouble("speed");
            direction = jsonWind.getDouble("deg");
            //Clouds
            JSONObject jsonClouds = jsonObject.getJSONObject("clouds");
            percent = jsonClouds.getDouble("all");
            //Rain
            if(jsonObject.has("rain")){
                JSONObject jsonRain = jsonObject.getJSONObject("rain");
                rain1H = jsonRain.getDouble("1h");
                rain3H = jsonRain.getDouble("3h");
            }
            //Snow
            if(jsonObject.has("snow")){
                JSONObject jsonSnow = jsonObject.getJSONObject("snow");
                snow1H = jsonSnow.getDouble("1h");
                snow3H = jsonSnow.getDouble("3h");
            }
            //Sys
            JSONObject jsonSys = jsonObject.getJSONObject("sys");
            countrycode = jsonSys.getString("country");
            //Sys - sunrise
            double unixTimeStampSunrise = jsonSys.getDouble("sunrise");
            double javaTimeStampSunrise = unixTimeStampSunrise*1000;
            Date dateSunrise = new Date((long) javaTimeStampSunrise);
            SimpleDateFormat formatterSunrise = new SimpleDateFormat("HH:mm:ss");
            sunrise = formatterSunrise.format(dateSunrise);
            //Sys - sunset
            double unxiTimeStampSunset = jsonSys.getDouble("sunset");
            double javaTimeStampSunset = unxiTimeStampSunset*1000;
            Date dateSunset = new Date((long) javaTimeStampSunset);
            SimpleDateFormat formatterSunset = new SimpleDateFormat("HH:mm:ss");
            sunset = formatterSunset.format(dateSunset);
            //Timezone
            timezone = jsonObject.getDouble("timezone");
            //Name
            cityname_1 = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double kelvinToCelsius(double kelvin){
        double celsius = kelvin - 273.15;
        return Math.round(celsius*100.00)/100.00;
    }

    public void show(int pos, Adresse item) {
        citynameString = adresslist.get(pos).getAdresse();
        weatherData(citynameString);
        cityname.setText(cityname_1);
        mainweather.setText(main);
        anmerkung.setText(description);
        temp.setText("Temperatur: "+kelvinToCelsius(tempMain)+"°C");
        temp_min_max.setText("Temperaturzwischen: "+kelvinToCelsius(temp_min)+"°C und "+kelvinToCelsius(temp_max)+"°C");
        right_sunrise.setText("Sonnenaufgang: "+sunrise);
        right_sunset.setText("Sonnenuntergang: "+sunset);
        right_druck.setText("Atmosphärischer Druck: "+druck+"hPa, "+hpaToBar(druck)+"bar");
        right_luftfeuchtigkeit.setText("Luftfeuchtigkeit: "+luftfeuchtigkeit+"%");
        right_wind.setText("Wind: "+speed+"km/h");
        right_feelslike.setText("Gefühlt: "+kelvinToCelsius(feelslike)+"°C");
        String link = "https://openweathermap.org/img/wn/"+icon+"@4x.png";
        new Thread(new Runnable() {
            public void run() {
                weatherIcon.post(new Runnable() {
                    public void run() {
                        Picasso.get().load(link).into(weatherIcon);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
