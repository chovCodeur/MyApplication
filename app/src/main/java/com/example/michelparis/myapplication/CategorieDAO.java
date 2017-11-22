package com.example.michelparis.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

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

    public int recupCategeorieBien(Categorie categorie) {
        int resultat=0;
        String selectQuery = "SELECT " + NOM +" FROM " + TABLE_NAME + " WHERE " + ID + " = " +ID ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            resultat = cursor.getInt(0);
        }
        return resultat;
    }

}
