package com.personne;

/**
 * Created by TANGUY on 16-11-17.
 */

/**
 * Classe qui gère les informations de la personne
 */

public class Personne {

    private int id_Personne;
    private String nom;
    private String prenom;
    private String mail;
    private String address;
    private String phoneNumber;
    private String date;
    private String numero_contrat;

    /**
     * Constructeur par défaut
     */
    public Personne() {
    }

    /**
     * Constructeur avec les attributs
     * @param id_Personne
     * @param nom
     * @param prenom
     * @param mail
     * @param address
     * @param phoneNumber
     * @param date
     * @param numero_contrat
     */
    public Personne(int id_Personne, String nom, String prenom, String mail,
                    String address, String phoneNumber, String date, String numero_contrat) {
        this.id_Personne = id_Personne;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.numero_contrat = numero_contrat;
    }

    /**
     * @return the id_Personne
     */
    public int getId_Personne() {
        return id_Personne;
    }
    /**
     * @param id_Personne the id_Personne to set
     */
    public void setId_Personne(int id_Personne) {
        this.id_Personne = id_Personne;
    }
    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }
    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }
    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }
    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }
    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the number of contract
     */
    public String getNumero_contrat() {
        return numero_contrat;
    }

    /**
     * @param numero_contrat the number of contract to set
     */
    public void setNumero_contrat(String numero_contrat) {
        this.numero_contrat = numero_contrat;
    }

    /**
     * Surcharge de la méthode toString()
     * @return string
     */
    @Override
    public String toString() {
        return "Personne{" +
                "id_Personne=" + id_Personne +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", mail='" + mail + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date='" + date + '\'' +
                ", numero_contrat='" + numero_contrat + '\'' +
                '}';
    }
}
