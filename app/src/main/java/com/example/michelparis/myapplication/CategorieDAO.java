package com.example.michelparis.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.Log;

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
     * Contructeur de la classe ListeDAO
     * @param context le contexte
     */
    public CategorieDAO(Context context){
        maBaseSQLite = MySQLite.getInstance(context);
    }

    /**
     * Méthode permettant l'ouverture de la table en lecture/ecriture
     */
    public void open(){
        db=maBaseSQLite.getWritableDatabase();
    }

    /**
     * Méthode permettant d'ajouter une catégorie dans la table Catégorie
     * @param categorie Categorie : la catégorie à ajouter
     * @return long l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
     */
    public long addCategorie(Categorie categorie){
        ContentValues values = new ContentValues();
        values.put(NOM,categorie.getNom_Categorie());
        values.put(DESCRIPTION, categorie.getDescription());
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * Méthode permettant de modifier une catégorie dans la table Catégorie
     * @param id_categorie int : l'id de la catégorie
     * @param nom String : le nom de la catégorie
     * @param description String : la description de la catégorie
     * @return int : le nombre de lignes affectées par la requête
     */
    public int modCategorie(int id_categorie, String nom, String description){
        ContentValues values = new ContentValues();
        values.put(NOM,nom);
        values.put(DESCRIPTION, description);

        String where = ID+" = ?";
        String[] whereArgs = {String.valueOf(id_categorie)};
        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    /**
     * Méthode permettant de supprimer une catégorie dans la table Catégorie
     * @param categorie Categorie : la catégorie à supprimer
     * @return int : le nombre de lignes affectées par la clause WHERE, 0 sinon
     */
    public int deleteCategorie(Categorie categorie) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID+" = ?";
        String[] whereArgs = {categorie.getId_Categorie()+""};
        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public String getNomCategorieByIdBien(int id) {
        String nom="";
        String selectQuery = "SELECT " + NOM +" FROM " + TABLE_NAME + " WHERE " + ID + "= " +id ;

        Log.e("MiPa","avant :"+selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            Log.e("MiPa","dans le IF");

            nom = c.getString(c.getColumnIndex(NOM));
            c.close();
        }

        return nom;
    }

    public ArrayList<Categorie> getAllCategorie(){
        ArrayList<Categorie> liste = new ArrayList<Categorie>();
        Cursor curseurCategorie = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);

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
     * Méthode permettant l'ouverture de la table en lecture/ecriture
     */
    public void close(){
        db.close();
    }

}
