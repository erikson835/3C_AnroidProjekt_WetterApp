package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LeftFragment.OnSelectionChangedListener, View.OnClickListener {
    private RightFragment rightFragment;
    private boolean showRight = false;
    private static net.htlgrieskirchen.AndroidProjekt_WetterApp_3C.MainActivity instance;
    private ArrayList<Adresse> adresslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        instance = this;
    }

    private void initializeView(){
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
}