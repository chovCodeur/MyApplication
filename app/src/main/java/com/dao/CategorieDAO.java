package com.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bd.MySQLite;
import com.categorie.Categorie;

import java.util.ArrayList;

/**
 * Created by Thib on 16/11/2017.
 */

public class CategorieDAO {

    public static final String TABLE_NAME = "CATEGORIE";
    public static final String ID = "id_categorie";
    public static final String NOM = "nom";
    public static final String DESCRIPTION = "description";

    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    /**
     * Contructeur de la classe CategorieDAO
     *
     * @param context le contexte
     */
    public CategorieDAO(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    /**
     * Méthode permettant l'ouverture de la table en lecture/ecriture
     */
    public void open() {
        db = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Méthode permettant d'ajouter une catégorie dans la table Catégorie
     *
     * @param categorie Categorie : la catégorie à ajouter
     * @return long l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
     */
    public long addCategorie(Categorie categorie) {
        ContentValues values = new ContentValues();
        values.put(NOM, categorie.getNom_Categorie());
        values.put(DESCRIPTION, categorie.getDescription());
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * Méthode permettant de modifier une catégorie dans la table Catégorie
     *
     * @param id_categorie int : l'id de la catégorie
     * @param nom          String : le nom de la catégorie
     * @param description  String : la description de la catégorie
     * @return int : le nombre de lignes affectées par la requête
     */
    public int modCategorie(int id_categorie, String nom, String description) {
        ContentValues values = new ContentValues();
        values.put(NOM, nom);
        values.put(DESCRIPTION, description);

        String where = ID + " = ?";
        String[] whereArgs = {String.valueOf(id_categorie)};
        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    /**
     * Méthode permettant de supprimer une catégorie dans la table Catégorie
     *
     * @param categorie Categorie : la catégorie à supprimer
     * @return int : le nombre de lignes affectées par la clause WHERE, 0 sinon
     */
    public int deleteCategorie(Categorie categorie) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID + " = ?";
        String[] whereArgs = {categorie.getId_Categorie() + ""};
        return db.delete(TABLE_NAME, where, whereArgs);
    }

    /**
     * Méthode permettant de récupérer le nom d'une catégorie grâce à son identifiant.
     *
     * @param id int : identifiant de la catégorie recherchée.
     * @return String : contient le nom de la catégorie ou est vide en cas d'échec.
     */
    public String getNomCategorieByIdCategorie(int id) {
        String nom = "";
        String selectQuery = "SELECT " + NOM + " FROM " + TABLE_NAME + " WHERE " + ID + "= " + id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            nom = c.getString(c.getColumnIndex(NOM));
            c.close();
        }
        return nom;
    }

    /**
     * Méthode permettant de récupérer la description d'une catégorie grâce à son identifiant.
     *
     * @param id int : identifiant de la catégorie recherchée.
     * @return String : contient la description de la catégorie ou est vide en cas d'échec.
     */
    public String getDescriptionCategorieByIdCategorie(int id) {
        String description = "";
        String selectQuery = "SELECT " + DESCRIPTION + " FROM " + TABLE_NAME + " WHERE " + ID + "= " + id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            description = c.getString(c.getColumnIndex(DESCRIPTION));
            c.close();
        }
        return description;
    }

    /**
     * Méthode permettant de récupérer toutes les catégories de la table Catégorie.
     *
     * @return ArrayList<Categorie> contenant toutes les catégories en cas de réussite, vide sinon.
     */
    public ArrayList<Categorie> getAllCategorie() {
        ArrayList<Categorie> liste = new ArrayList<Categorie>();
        Cursor curseurCategorie = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        Categorie categorie;
        if (curseurCategorie.moveToFirst()) {
            do {
                categorie = new Categorie(
                        curseurCategorie.getInt(curseurCategorie.getColumnIndex(ID)),
                        curseurCategorie.getString(curseurCategorie.getColumnIndex(NOM)),
                        curseurCategorie.getString(curseurCategorie.getColumnIndex(DESCRIPTION))
                );

                liste.add(categorie);
            } while (curseurCategorie.moveToNext());
        }
        curseurCategorie.close();
        return liste;
    }

    /**
     * Méthode permettant de récuperer toutes les acatégories d'une liste
     * @param idListe
     * @return listeCategorie
     */
    public ArrayList<Categorie> getCategoriesByIdListe(int idListe) {
        ArrayList<Categorie> liste = new ArrayList<Categorie>();

        Cursor curseurCategorie = db.rawQuery("SELECT DISTINCT CATEGORIE.id_categorie, CATEGORIE.nom, CATEGORIE.description FROM " + TABLE_NAME + " JOIN BIEN ON BIEN.id_categorie = CATEGORIE.id_categorie JOIN APPARTIENT ON APPARTIENT.id_bien = BIEN.id_bien WHERE APPARTIENT.id_liste = " + idListe, null);

        Categorie categorie;
        if (curseurCategorie.moveToFirst()) {
            do {
                categorie = new Categorie(
                        curseurCategorie.getInt(curseurCategorie.getColumnIndex(ID)),
                        curseurCategorie.getString(curseurCategorie.getColumnIndex(NOM)),
                        curseurCategorie.getString(curseurCategorie.getColumnIndex(DESCRIPTION))
                );

                liste.add(categorie);
            } while (curseurCategorie.moveToNext());
        }
        curseurCategorie.close();
        return liste;
    }

    /**
     * Méthode permettant la fermeture de la table en lecture/ecriture
     */
    public void close() {
        db.close();
    }

}

