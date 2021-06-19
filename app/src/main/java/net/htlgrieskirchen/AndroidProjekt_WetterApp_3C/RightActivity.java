package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation != Configuration.ORIENTATION_PORTRAIT) {
            finish();
            return;
        }
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent == null) return;
        RightFragment rightFragment = (RightFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragRight);
        int pos = intent.getIntExtra("pos", -1);
        Adresse item = (Adresse) intent.getSerializableExtra("item");
        rightFragment.show(pos, item);
    }
}
