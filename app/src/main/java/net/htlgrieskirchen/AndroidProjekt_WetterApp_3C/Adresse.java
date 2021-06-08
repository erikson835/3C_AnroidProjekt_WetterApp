package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import android.provider.Telephony;

import java.io.Serializable;
import java.util.ArrayList;

public class Adresse implements Serializable {
    String adresse;

    public Adresse(String adresse){
        this.adresse = adresse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return adresse;
    }
}
