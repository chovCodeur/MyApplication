package com.example.michelparis.myapplication;

/**
 * Created by Kevin on 16/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


/**
 * Created by Kevin on 16/11/2017.
 */

/**
 * Classe BienDAO gère les interactions avec la base de données pour tout ce qui touche à la classe Bien
 */
public class PersonneDAO {

    public static final String TABLE_NAME = "PERSONNE";
    public static final String ID = "id_personne";
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String DATENAISSANCE = "date_naissance";
    public static final String ADRESSE = "adresse";
    public static final String MAIL = "mail";
    public static final String TELEPHONE = "telephone";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    /**
     * Contructeur de la classe BienDAO
     * @param context le contexte
     */
    public PersonneDAO(Context context){
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
     * Méthode permettant de modifier un bien dans la table Bien
     * @param id long : l'id de l'bien
     * @param nom String : le nom de l'bien
     * @return int : le nombre de lignes affectées par la requête
     */
    public int modPersonne(int id, String nom, String prenom, String dateNaissance, String adresse, String mail,
                       String telephone){
        ContentValues values = new ContentValues();
        values.put(NOM,nom);
        values.put(PRENOM,prenom);
        values.put(DATENAISSANCE, dateNaissance);
        values.put(ADRESSE, adresse);
        values.put(MAIL, mail);
        values.put(TELEPHONE, telephone);

        String where = ID+" = ?";
        String[] whereArgs = {String.valueOf(id)};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }


    /*public Bien getPersonne(int id){
        Bien a=new Bien(0, "", "", "", "", 0.0f, 0, "", "");

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID+"="+id, null);
        if (c.moveToFirst()) {
            a.setId_bien(c.getInt(c.getColumnIndex(ID)));
            a.setNom_bien(c.getString(c.getColumnIndex(NOM)));
            a.setDate_saisie_bien(c.getString(c.getColumnIndex(DATESAISIE)));
            a.setDate_achat_bien(c.getString(c.getColumnIndex(DATEACHAT)));
            a.setCommentaire_bien(c.getString(c.getColumnIndex(COMMENTAIRE)));
            a.setPrix_bien(c.getFloat(c.getColumnIndex(PRIX)));
            a.setId_categorie_bien(c.getInt(c.getColumnIndex(IDCATEGORIE)));
            a.setDescription_bien(c.getString(c.getColumnIndex(DESCRIPTION)));
            a.setNumeroSerie_bien(c.getString(c.getColumnIndex(NUMSERIE)));
            c.close();
        }

        return a;
    }*/
}

