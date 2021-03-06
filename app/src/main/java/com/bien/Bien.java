package com.bien;

/**
 * Created by Kevin on 11/11/2017.
 */

/**
 * Classe permettant de gérer un Bien
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

    /**
     * Constructeur de l'objet bien
     * @param id_bien
     * @param nom_bien
     * @param date_saisie_bien
     * @param date_achat_bien
     * @param facture_bien
     * @param commentaire_bien
     * @param prix_bien
     * @param photo_bien_principal
     * @param photo_bien_miniature1
     * @param photo_bien_miniature2
     * @param photo_bien_miniature3
     * @param id_categorie_bien
     * @param description_bien
     * @param numeroSerie_bien
     */
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



    /**
     * @return the id_bien
     */
    public int getId_bien() {
        return id_bien;
    }



    /**
     * @param id_bien the id_bien to set
     */
    public void setId_bien(int id_bien) {
        this.id_bien = id_bien;
    }



    /**
     * @return the nom_bien
     */
    public String getNom_bien() {
        return nom_bien;
    }



    /**
     * @param nom_bien the nom_bien to set
     */
    public void setNom_bien(String nom_bien) {
        this.nom_bien = nom_bien;
    }



    /**
     * @return the date_saisie_bien
     */
    public String getDate_saisie_bien() {
        return date_saisie_bien;
    }



    /**
     * @param date_saisie_bien the date_saisie_bien to set
     */
    public void setDate_saisie_bien(String date_saisie_bien) {
        this.date_saisie_bien = date_saisie_bien;
    }



    /**
     * @return the date_achat_bien
     */
    public String getDate_achat_bien() {
        return date_achat_bien;
    }



    /**
     * @param date_achat_bien the date_achat_bien to set
     */
    public void setDate_achat_bien(String date_achat_bien) {
        this.date_achat_bien = date_achat_bien;
    }



    /**
     * @return the facture_bien
     */
    public String getFacture_bien() {
        return facture_bien;
    }



    /**
     * @param facture_bien the facture_bien to set
     */
    public void setFacture_bien(String facture_bien) {
        this.facture_bien = facture_bien;
    }



    /**
     * @return the commentaire_bien
     */
    public String getCommentaire_bien() {
        return commentaire_bien;
    }



    /**
     * @param commentaire_bien the commentaire_bien to set
     */
    public void setCommentaire_bien(String commentaire_bien) {
        this.commentaire_bien = commentaire_bien;
    }



    /**
     * @return the prix_bien
     */
    public String getPrix_bien() {
        return prix_bien;
    }



    /**
     * @param prix_bien the prix_bien to set
     */
    public void setPrix_bien(String prix_bien) {
        this.prix_bien = prix_bien;
    }



    /**
     * @return the photo_bien_principal
     */
    public String getPhoto_bien_principal() {
        return photo_bien_principal;
    }



    /**
     * @param photo_bien_principal the photo_bien_principal to set
     */
    public void setPhoto_bien_principal(String photo_bien_principal) {
        this.photo_bien_principal = photo_bien_principal;
    }



    /**
     * @return the photo_bien_miniature1
     */
    public String getPhoto_bien_miniature1() {
        return photo_bien_miniature1;
    }



    /**
     * @param photo_bien_miniature1 the photo_bien_miniature1 to set
     */
    public void setPhoto_bien_miniature1(String photo_bien_miniature1) {
        this.photo_bien_miniature1 = photo_bien_miniature1;
    }



    /**
     * @return the photo_bien_miniature2
     */
    public String getPhoto_bien_miniature2() {
        return photo_bien_miniature2;
    }



    /**
     * @param photo_bien_miniature2 the photo_bien_miniature2 to set
     */
    public void setPhoto_bien_miniature2(String photo_bien_miniature2) {
        this.photo_bien_miniature2 = photo_bien_miniature2;
    }



    /**
     * @return the photo_bien_miniature3
     */
    public String getPhoto_bien_miniature3() {
        return photo_bien_miniature3;
    }



    /**
     * @param photo_bien_miniature3 the photo_bien_miniature3 to set
     */
    public void setPhoto_bien_miniature3(String photo_bien_miniature3) {
        this.photo_bien_miniature3 = photo_bien_miniature3;
    }



    /**
     * @return the id_categorie_bien
     */
    public int getId_categorie_bien() {
        return id_categorie_bien;
    }



    /**
     * @param id_categorie_bien the id_categorie_bien to set
     */
    public void setId_categorie_bien(int id_categorie_bien) {
        this.id_categorie_bien = id_categorie_bien;
    }



    /**
     * @return the description_bien
     */
    public String getDescription_bien() {
        return description_bien;
    }



    /**
     * @param description_bien the description_bien to set
     */
    public void setDescription_bien(String description_bien) {
        this.description_bien = description_bien;
    }



    /**
     * @return the numeroSerie_bien
     */
    public String getNumeroSerie_bien() {
        return numeroSerie_bien;
    }



    /**
     * @param numeroSerie_bien the numeroSerie_bien to set
     */
    public void setNumeroSerie_bien(String numeroSerie_bien) {
        this.numeroSerie_bien = numeroSerie_bien;
    }



    /**
     * Surcharge de la méthode toString()
     * @return string
     */
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
