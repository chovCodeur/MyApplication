package com.bien;

import android.graphics.Bitmap;

/**
 * Created by Kevin on 11/11/2017.
 */

public class Bien {

    private int id_bien;
    private String nom_bien;
    private String date_saisie_bien;
    private String date_achat_bien;
    private String facture_bien;
    private String commentaire_bien;
    private String prix_bien;
    private String photo_bien_principal;
    private String photo_bien_miniature1;
    private String photo_bien_miniature2;
    private String photo_bien_miniature3;
    private int id_categorie_bien;
    private String description_bien;
    private String numeroSerie_bien;

    public Bien(int id_bien, String nom_bien, String date_saisie_bien, String date_achat_bien, String facture_bien,
                String commentaire_bien, String prix_bien, String photo_bien_principal, String photo_bien_miniature1,
                String photo_bien_miniature2, String photo_bien_miniature3, int id_categorie_bien, String description_bien, String numeroSerie_bien) {
        this.id_bien = id_bien;
        this.nom_bien = nom_bien;
        this.date_saisie_bien = date_saisie_bien;
        this.date_achat_bien = date_achat_bien;
        this.facture_bien = facture_bien;
        this.commentaire_bien = commentaire_bien;
        this.prix_bien = prix_bien;
        this.photo_bien_principal = photo_bien_principal;
        this.photo_bien_miniature1 = photo_bien_miniature1;
        this.photo_bien_miniature2 = photo_bien_miniature2;
        this.photo_bien_miniature3 = photo_bien_miniature3;
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

    public String getPrix_bien() {
        return prix_bien;
    }

    public void setPrix_bien(String prix_bien) {
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

    public String getFacture_bien() {
        return facture_bien;
    }

    public void setFacture_bien(String facture_bien) {
        this.facture_bien = facture_bien;
    }

    public String getPhoto_bien_principal() {
        return photo_bien_principal;
    }

    public void setPhoto_bien_principal(String photo_bien_principal) {
        this.photo_bien_principal = photo_bien_principal;
    }

    public String getPhoto_bien_miniature1() {
        return photo_bien_miniature1;
    }

    public void setPhoto_bien_miniature1(String photo_bien_miniature1) {
        this.photo_bien_miniature1 = photo_bien_miniature1;
    }

    public String getPhoto_bien_miniature2() {
        return photo_bien_miniature2;
    }

    public void setPhoto_bien_miniature2(String photo_bien_miniature2) {
        this.photo_bien_miniature2 = photo_bien_miniature2;
    }

    public String getPhoto_bien_miniature3() {
        return photo_bien_miniature3;
    }

    public void setPhoto_bien_miniature3(String photo_bien_miniature3) {
        this.photo_bien_miniature3 = photo_bien_miniature3;
    }

    @Override
    public String toString() {
        return "Bien{" +
                "id_bien=" + id_bien +
                ", nom_bien='" + nom_bien + '\'' +
                ", date_saisie_bien='" + date_saisie_bien + '\'' +
                ", date_achat_bien='" + date_achat_bien + '\'' +
                ", facture_bien='" + facture_bien + '\'' +
                ", commentaire_bien='" + commentaire_bien + '\'' +
                ", prix_bien=" + prix_bien +
                ", photo_bien_principal='" + photo_bien_principal + '\'' +
                ", photo_bien_miniature1='" + photo_bien_miniature1 + '\'' +
                ", photo_bien_miniature2='" + photo_bien_miniature2 + '\'' +
                ", photo_bien_miniature3='" + photo_bien_miniature3 + '\'' +
                ", id_categorie_bien=" + id_categorie_bien +
                ", description_bien='" + description_bien + '\'' +
                ", numeroSerie_bien='" + numeroSerie_bien + '\'' +
                '}';
    }
}
