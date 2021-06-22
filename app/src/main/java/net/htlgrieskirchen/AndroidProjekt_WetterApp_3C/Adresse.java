package net.htlgrieskirchen.AndroidProjekt_WetterApp_3C;

import java.io.Serializable;

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
