package com.example.michelparis.myapplication;

/**
 * Created by TANGUY on 16-11-17. z
 */

public class Categorie {

    private int id_Categorie;
    private String nom_Categorie;
    private String description;

    public Categorie(int id_Categorie, String categorie, String description) {
        this.id_Categorie = id_Categorie;
        this.nom_Categorie = nom_Categorie;
        this.description = description;

    }
    public String getNom_Categorie() {

        return nom_Categorie;
    }

    public void setNom_Categorie(String  nom_Categorie) {
        this.nom_Categorie = nom_Categorie;
    }
    public String getDescription(){
        return  description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}