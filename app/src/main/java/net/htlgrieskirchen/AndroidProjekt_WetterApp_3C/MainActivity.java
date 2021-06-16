package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LeftFragment.OnSelectionChangedListener, View.OnClickListener {
    private RightFragment rightFragment;
    private boolean showRight = false;
    private static net.htlgrieskirchen.AndroidProjekt_WetterApp_3C.MainActivity instance;
    private ArrayList<Adresse> adresslist;
    private static final int RQ_PREFERENCES = 1;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        instance = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener = (sharedPrefs, key) -> preferenceChanged(sharedPrefs, key);
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        //linearLayout = findViewById(R.id.background);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                   //Action-Bar-Reaktion
        if (item.getItemId() == R.id.menu_einstellungen) {
            Intent intent = new Intent(this, MySettingsActivity.class);
            startActivityForResult(intent, RQ_PREFERENCES);
        }
        return super.onOptionsItemSelected(item);
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
                int color = Color.parseColor(sValue);
                LeftFragment.linearLayout.setBackgroundColor(color);
                int orientation = getResources().getConfiguration().orientation;
                if (orientation != Configuration.ORIENTATION_PORTRAIT){
                    RightFragment.linearLayout.setBackgroundColor(color);
                }
                break;
        }
    }
}