package com.example.michelparis.myapplication;

/**
 * Created by Kevin on 11/11/2017.
 */

public class Bien {

    private int id_bien;
    private String nom_bien;
    private String date_saisie_bien;
    private String date_achat_bien;
    //private XXXX facture_bien;
    private String commentaire_bien;
    private int prix_bien;
    //private XXXX photo_bien;
    private int id_categorie_bien;
    private String description_bien;
    private String numeroSerie_bien;

    public Bien(int id_bien, String nom_bien, String date_saisie_bien, String date_achat_bien, String commentaire_bien, int prix_bien, int id_categorie_bien, String description_bien, String numeroSerie_bien) {
        this.id_bien = id_bien;
        this.nom_bien = nom_bien;
        this.date_saisie_bien = date_saisie_bien;
        this.date_achat_bien = date_achat_bien;
        this.commentaire_bien = commentaire_bien;
        this.prix_bien = prix_bien;
        this.id_categorie_bien = id_categorie_bien;
        this.description_bien = description_bien;
        this.numeroSerie_bien = numeroSerie_bien;
    }

    public int getId_bien() {
        return id_bien;
    }

    public void setId_bien(int id_bien) {
        this.id_bien = id_bien;
    }

    public String getNom_bien() {
        return nom_bien;
    }

    public void setNom_bien(String nom_bien) {
        this.nom_bien = nom_bien;
    }

    public String getDate_saisie_bien() {
        return date_saisie_bien;
    }

    public void setDate_saisie_bien(String date_saisie_bien) {
        this.date_saisie_bien = date_saisie_bien;
    }

    public String getDate_achat_bien() {
        return date_achat_bien;
    }

    public void setDate_achat_bien(String date_achat_bien) {
        this.date_achat_bien = date_achat_bien;
    }

    public String getCommentaire_bien() {
        return commentaire_bien;
    }

    public void setCommentaire_bien(String commentaire_bien) {
        this.commentaire_bien = commentaire_bien;
    }

    public int getPrix_bien() {
        return prix_bien;
    }

    public void setPrix_bien(int prix_bien) {
        this.prix_bien = prix_bien;
    }

    public int getId_categorie_bien() {
        return id_categorie_bien;
    }

    public void setId_categorie_bien(int id_categorie_bien) {
        this.id_categorie_bien = id_categorie_bien;
    }

    public String getDescription_bien() {
        return description_bien;
    }

    public void setDescription_bien(String description_bien) {
        this.description_bien = description_bien;
    }

    public String getNumeroSerie_bien() {
        return numeroSerie_bien;
    }

    public void setNumeroSerie_bien(String numeroSerie_bien) {
        this.numeroSerie_bien = numeroSerie_bien;
    }
}
