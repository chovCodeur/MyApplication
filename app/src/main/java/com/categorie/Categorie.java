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

    /**
     * @return the id_Categorie
     */
    public int getId_Categorie() {
        return id_Categorie;
    }
    /**
     * @param id_Categorie the id_Categorie to set
     */
    public void setId_Categorie(int id_Categorie) {
        this.id_Categorie = id_Categorie;
    }
    /**
     * @return the nom_Categorie
     */
    public String getNom_Categorie() {
        return nom_Categorie;
    }
    /**
     * @param nom_Categorie the nom_Categorie to set
     */
    public void setNom_Categorie(String nom_Categorie) {
        this.nom_Categorie = nom_Categorie;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }
    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}