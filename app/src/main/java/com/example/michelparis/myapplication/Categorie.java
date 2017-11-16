package com.example.michelparis.myapplication;

/**
 * Created by TANGUY on 16-11-17. z
 */

public class Categorie {

    private int id_Categorie;
    private String nom_Categorie;
    private String descriptif;

    public Categorie(int id_Categorie, String categorie, String descriptif) {
        this.id_Categorie = id_Categorie;
        this.nom_Categorie = nom_Categorie;
        this.descriptif = descriptif;

    }
    public String getNom_Categorie() {

        return nom_Categorie;
    }

    public void setNom_Categorie(String  nom_Categorie) {
        this.nom_Categorie = nom_Categorie;
    }
    public String getDescriptif(){
        return  descriptif;
    }
    public void setDescriptif(String descriptif){
        this.descriptif = descriptif;
    }
}