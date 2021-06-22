package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class Notifications extends IntentService {

    double longitude = 0.0;
    double latitude = 0.0;

    int notificationId = 1;

    public Notifications() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            if (!MainActivity.getNotifications) {
                stopSelf();
                return;
            }
            try {
                if (MainActivity.isGpsGranted) {
                    getCurrentLocation();
                    GetDataByCoordinates data = new GetDataByCoordinates(longitude, latitude);
                    data.execute();
                    JSONObject jsonObject = data.get();

                    JSONArray weather = jsonObject.getJSONArray("weather");
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject jsonWeather = weather.getJSONObject(0);
                    String weather2 = jsonWeather.getString("main");
                    double temp = main.getDouble("temp");
                    double temp2 = kelvinToCelsius(temp);

                    String s = weather2 + ", " + temp2 + " °C";
                    postNotification(s);
                }
                Thread.sleep(1200000);
            } catch (Exception e) {
                return;
            }

        }
    }

    private void postNotification(String s) {
        NotificationManagerCompat notificationManager = MainActivity.notificationManagerCompat;

        NotificationCompat.Builder builder = null;

            builder = new NotificationCompat.Builder(this, String.valueOf(MainActivity.CHANNEL_ID))
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Wetter zur Zeit: ")
                    .setContentText(s)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
            notificationId++;
    }

    private void getCurrentLocation() {
        Location l = null;
        try {
            l = MainActivity.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (l != null) {
                longitude = l.getLongitude();
                latitude = l.getLatitude();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private double kelvinToCelsius(double kelvin){
        double celsius = kelvin - 273.15;
        return Math.round(celsius*100.00)/100.00;
    }
}
