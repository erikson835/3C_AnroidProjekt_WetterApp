package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDataByCoordinates extends AsyncTask<String, Integer, JSONObject> {
    private String api_key;
    private double lon;
    private double lat;

    public GetDataByCoordinates(double lon, double lat){
        this.lon = lon;
        this.lat = lat;
        this.api_key = "da2b27a6664826e8126b238194caaabf";
    }

    @Override
    protected void onPreExecute() {
        // here we could do some UI manipulation before the worker
        // thread starts
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // do some UI manipulation while progress is modified
        super.onProgressUpdate(values);
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        // workhorse methode
        String sJson = "";
        try {
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+api_key).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine())!=null){
                    sb.append(line);
                }
                sJson = sb.toString();
                return new JSONObject(sJson);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        // called after doInBackground finishes
        super.onPostExecute(json);
    }
}
