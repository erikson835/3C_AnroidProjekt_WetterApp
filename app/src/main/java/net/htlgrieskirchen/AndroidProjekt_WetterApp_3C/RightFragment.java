package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class RightFragment extends Fragment {
    private TextView cityname;
    private ArrayList<Adresse> adresslist = LeftFragment.adresslist;
    static LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rightfragment, container, false);
        intializeViews(view);
        linearLayout = view.findViewById(R.id.rightLayout);
        return view;
    }

    private void intializeViews(View view) {
        cityname = (TextView) view.findViewById(R.id.right_cityname);
    }

    public void show(int pos, Adresse item) {
        cityname.setText(adresslist.get(pos).getAdresse());
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
