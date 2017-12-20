package com.liste;

/**
 * Created by Kevin on 11/11/2017.
 */


/**
 * Classe permettant de gérer un objet Liste
 */
public class Liste {
    private int id_liste;
    private String libelle_liste;
    private String commentaire_liste;


    /**
     * Constructeur de la liste
     * @param id_liste
     * @param libelle_liste
     * @param commentaire_liste
     */
    public Liste(int id_liste, String libelle_liste, String commentaire_liste) {
        this.id_liste = id_liste;
        this.libelle_liste = libelle_liste;
        this.commentaire_liste = commentaire_liste;
    }

    /**
     * Getteur de l'idListe
     * @return id
     */
    public int getId_liste() {
        return id_liste;
    }

    /**
     * Setteur de l'id de la liste
     */
    public void setId_liste(int id_liste) {
        this.id_liste = id_liste;
    }

    /**
     * Getteur du libelle liste
     * @return libelle
     */
    public String getLibelle_liste() {
        return libelle_liste;
    }

    /**
     * Setteur du libelle liste
     */
    public void setLibelle_liste(String libelle_liste) {
        this.libelle_liste = libelle_liste;
    }

    /**
     * Getteur du commentaire de la liste
     * @return commentaire
     */
    public String getCommentaire_liste() {
        return commentaire_liste;
    }

    /**
     * Setteur du commentaire liste
     */
    public void setCommentaire_liste(String commentaire_liste) {
        this.commentaire_liste = commentaire_liste;
    }


}
