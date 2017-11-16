package com.example.michelparis.myapplication;

/**
 * Created by TANGUY on 16-11-17.
 */

public class Personne {

    private String nom;
    private String prenom;
    private String mail;
    private String address;
    private String phoneNumber;
    private String date;

    public Personne(String nom, String prenom, String mail,
                    String address, String phoneNumber, String date) {

        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.date = date;

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

    public void setPrenomf(String prenom) {
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
}
