package com.example.michelparis.myapplication;

/**
 * Created by TANGUY on 16-11-17.
 */

public class Categorie {

    private String categorie;
    private String descriptif;

    public Categorie(String categorie, String descriptif) {
        this.categorie = categorie;
        this.descriptif = descriptif;

    }
    public String getCategorie() {

        return categorie;
    }

    public void setCategorie(String  categorie) {
        this.categorie = categorie;
    }
    public String getDescriptif(){
        return  descriptif;
    }
    public void setDescriptif(String descriptif){
        this.descriptif = descriptif;
    }
}