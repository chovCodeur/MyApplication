package com.personne;

/**
 * Created by TANGUY on 16-11-17.
 */
/////

public class Personne {

    private int id_Personne;
    private String nom;
    private String prenom;
    private String mail;
    private String address;
    private String phoneNumber;
    private String date;

    public Personne(){

    }
    public Personne(int id_Personne, String nom, String prenom, String mail,
                    String address, String phoneNumber, String date) {
        this.id_Personne = id_Personne;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.date = date;

    }

    public int getId_Personne(){

        return id_Personne;
    }
    public void setId_Personne(int id_Personne){
        this.id_Personne = id_Personne;
    }
    public String getNom() {

        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {

        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {

        return address;
    }
    public void setAddress (String address){
        this.address = address;
    }
    public String getPhoneNumber() {

        return phoneNumber;
    }
    public void setPhoneNumber(String  phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getDate() {

        return date;
    }
    public void setDate(String  date) {
        this.date = date;
    }

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
                '}';
    }
}
