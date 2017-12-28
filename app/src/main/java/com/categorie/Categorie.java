package com.categorie;

/**
 * Created by TANGUY on 16-11-17. z
 */

/**
 * Classe permettant de gérer une catégorie
 */

public class Categorie {

    private int id_Categorie;
    private String nom_Categorie;
    private String description;
    private String title;
    private boolean selected;


    /**
     * Constructeur de l'objet Catégorie
     * @param id_Categorie
     * @param nom_Categorie
     * @param description
     */
    public Categorie(int id_Categorie, String nom_Categorie, String description) {
        this.id_Categorie = id_Categorie;
        this.nom_Categorie = nom_Categorie;
        this.description = description;

    }

    public int getId_Categorie() {
        return id_Categorie;
    }

    public void setId_Categorie(int id_Categorie) {
        this.id_Categorie = id_Categorie;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom_Categorie() {

        return nom_Categorie;
    }

    public void setNom_Categorie(String nom_Categorie) {
        this.nom_Categorie = nom_Categorie;
    }

    /**
     * Surcharge de la méthode toString()
     * @return string
     */


    public String toString() {
        return "Categorie{" +
                "id_Categorie=" + id_Categorie +
                ", nom_Categorie='" + nom_Categorie + '\'' +
                ", description='" + description + '\'' +
                ", selected='" + selected + '\'' +
                '}';
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}