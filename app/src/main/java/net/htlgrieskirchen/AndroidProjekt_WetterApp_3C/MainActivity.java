package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LeftFragment.OnSelectionChangedListener, View.OnClickListener {
    private RightFragment rightFragment;
    private boolean showRight = false;
    private static net.htlgrieskirchen.AndroidProjekt_WetterApp_3C.MainActivity instance;
    private ArrayList<Adresse> adresslist;
    private static final int RQ_PREFERENCES = 1;
    public static LocationManager lm;
    public static boolean isGpsGranted;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    static int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        instance = this;

        isGpsGranted = false;
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkPermissionGPS();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener = (sharedPrefs, key) -> preferenceChanged(sharedPrefs, key);
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void initializeView() {
        rightFragment = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.fragRight);
        showRight = rightFragment != null && rightFragment.isInLayout();
    }

    @Override
    public void onSelectionChanged(int pos, Adresse item) {
        if (showRight) {
            rightFragment.show(pos, item);
        } else callRightActivity(pos, item);
    }

    private void callRightActivity(int pos, Adresse item) {
        Intent intent = new Intent(this, RightActivity.class);
        intent.putExtra("pos", pos);
        intent.putExtra("item", item);
        startActivity(intent);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (adresslist != null) {
            savedInstanceState.putSerializable("adresslist", adresslist);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            adresslist = (ArrayList<Adresse>) savedInstanceState.getSerializable("adresslist");

            if (adresslist != null) {
                if (showRight) {
                    for (int i = 0; i < adresslist.size(); i++) {
                        rightFragment.show(i, adresslist.get(i));
                    }
                } else {
                    for (int i = 0; i < adresslist.size(); i++) {
                        callRightActivity(i, adresslist.get(i));
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                   //Action-Bar-Reaktion
        switch (item.getItemId()) {
            case R.id.menu_einstellungen:
                Intent intent = new Intent(this, MySettingsActivity.class);
                startActivityForResult(intent, RQ_PREFERENCES);
                break;
            case R.id.standort:
                if (isGpsGranted){
                    double lon = 0.0;
                    double lat = 0.0;
                    Location l = null;
                    try{
                        l = MainActivity.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(l != null){
                            lon = l.getLongitude();
                            lat = l.getLatitude();
                        }
                    }catch (SecurityException e){
                        e.printStackTrace();
                    }
                    String pos="geo:"+lat+","+lon+"?z=12";
                    Uri uri = Uri.parse(pos);
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(uri);
                    startActivity(intent2);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermissionGPS() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 321);
        } else {
            gpsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 321) return;
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS verweigert")
                    .setNeutralButton("Ok", null)
                    .show();
        } else {
            gpsGranted();
        }
    }

    private void gpsGranted() {
        isGpsGranted = true;
        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
            }
        };
    }

    public void openMap(double longitude, double latitude) {
        String pos="geo:"+latitude+","+longitude+"?z=18"; //vertauschen der Variable!
        Uri uri = Uri.parse(pos);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {               //Action-Bar-Menü
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void preferenceChanged(SharedPreferences sharedPrefs, String key) {
        // Entsprechende Preference akutalisieren
        switch (key) {
            case "selectedColorCode":
                String sValue = sharedPrefs.getString(key, "");
                color = Color.parseColor(sValue);
                LeftFragment.linearLayout.setBackgroundColor(color);
                int orientation = getResources().getConfiguration().orientation;
                if (orientation != Configuration.ORIENTATION_PORTRAIT){
                    RightFragment.linearLayout.setBackgroundColor(color);
                }
                break;
        }
    }
}