package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LeftFragment extends Fragment {
    private ListView lv;
    static ArrayList<Adresse> adresslist = new ArrayList<>();
    private ArrayAdapter<Adresse> adapter;
    private OnSelectionChangedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.leftfragment, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        lv = view.findViewById(R.id.left_listView);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, adresslist);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view1, position, id) -> itemSelected(position));
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged( int pos, Adresse item);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnSelectionChangedListener){
            listener = (OnSelectionChangedListener) context;
        }
    }

    private void itemSelected(int position) {
        Adresse item = adresslist.get(position);
        listener.onSelectionChanged(position, item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
