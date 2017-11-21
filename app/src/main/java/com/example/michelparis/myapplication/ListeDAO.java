package com.example.michelparis.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Thib on 16/11/2017.
 */

public class ListeDAO {

    public static final String TABLE_NAME = "LISTE";
    public static final String ID = "id_liste";
    public static final String LIBELLE = "libelle";
    public static final String COMMENTAIRE = "commentaire";


    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    /**
     * Contructeur de la classe ListeDAO
     * @param context le contexte
     */
    public ListeDAO(Context context){
        maBaseSQLite = MySQLite.getInstance(context);
    }

    /**
     * Méthode permettant l'ouverture de la table en lecture/ecriture
     */
    public void open(){
        db=maBaseSQLite.getWritableDatabase();
    }

    /**
     * Méthode permettant la fermeture de la base de données
     */
    public void close(){
        db.close();
    }


    /**
     * Méthode permettant de modifier une liste dans la table liste
     * @param id_liste int : l'id de la liste
     * @param libelle String : le libelle de la liste
     * @param commentaire String : commentaire sur la liste
     * @return int : le nombre de lignes affectées par la requête
     */
    public int modifierListe(int id_liste, String libelle, String commentaire){
        ContentValues values = new ContentValues();
        values.put(LIBELLE,libelle);
        values.put(COMMENTAIRE, commentaire);

        String where = ID+" = ?";
        String[] whereArgs = {String.valueOf(id_liste)};
        return db.update(TABLE_NAME, values, where, whereArgs);
    }

}