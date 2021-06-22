package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LeftFragment extends Fragment implements View.OnClickListener {
    private ListView lv;
    static ArrayList<Adresse> adresslist = new ArrayList<>();
    private ArrayAdapter<Adresse> adapter;
    private OnSelectionChangedListener listener;
    private Button button;
    private static LeftFragment instance;
    static LinearLayout linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leftfragment, container, false);
        initializeViews(view);
        button = view.findViewById(R.id.left_button);
        button.setOnClickListener(this);
        linearLayout = view.findViewById(R.id.background);
        //adresslist = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, adresslist);
        lv.setAdapter(adapter);
        return view;
    }

    private void initializeViews(View view) {
        lv = view.findViewById(R.id.left_listView);
        lv.setOnItemClickListener((parent, view1, position, id) -> itemSelected(position));
    }

    @Override
    public void onClick(View v) {
        showAlertDialog(v);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged( int pos, Adresse item);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        instance = this;
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

    private void showAlertDialog(View v){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Standorteingabe");
        alertDialog.setMessage("Geben sie ihren gewünschten Standort ein:");

        final EditText input = new EditText(v.getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK", (dialog, which)->addToList(v, input.getText().toString()));
        alertDialog.setNegativeButton("CANCEL", null);
        alertDialog.show();
    }

    private void addToList(View v, String adress){
        adresslist.add(new Adresse(adress));
        adapter.notifyDataSetChanged();
    }

    public static LeftFragment getInstance(){
        return instance;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (adresslist != null) {
            savedInstanceState.putSerializable("adresslist", adresslist);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setData(ArrayList<Adresse> adresslist) {
        this.adresslist = adresslist;
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            adresslist = (ArrayList<Adresse>) savedInstanceState.getSerializable("adresslist");
            linearLayout.setBackgroundColor(MainActivity.color);
            if (adresslist != null) {
                setData(adresslist);
            }
        }
    }

}
