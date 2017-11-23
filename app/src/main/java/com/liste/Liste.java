package com.liste;

/**
 * Created by Kevin on 11/11/2017.
 */

public class Liste {
    private int id_liste;
    private String libelle_liste;
    private String commentaire_liste;

    public Liste(int id_liste, String libelle_liste, String commentaire_liste) {
        this.id_liste = id_liste;
        this.libelle_liste = libelle_liste;
        this.commentaire_liste = commentaire_liste;
    }

    public int getId_liste() {
        return id_liste;
    }

    public void setId_liste(int id_liste) {
        this.id_liste = id_liste;
    }

    public String getLibelle_liste() {
        return libelle_liste;
    }

    public void setLibelle_liste(String libelle_liste) {
        this.libelle_liste = libelle_liste;
    }

    public String getCommentaire_liste() {
        return commentaire_liste;
    }

    public void setCommentaire_liste(String commentaire_liste) {
        this.commentaire_liste = commentaire_liste;
    }
}
