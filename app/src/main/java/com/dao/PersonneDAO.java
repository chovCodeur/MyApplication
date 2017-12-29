package com.dao;

/**
 * Created by Kevin on 16/11/2017. ok
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bd.MySQLite;
import com.personne.Personne;

/**
 * Classe PersonneDAO gère les interactions avec la base de données pour tout ce qui touche à la classe Personne
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

    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    /**
     * Contructeur de la classe PersonneDao
     *
     * @param context le contexte
     */
    public PersonneDAO(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    /**
     * Méthode permettant l'ouverture de la table en lecture/ecriture
     */
    public void open() {
        db = maBaseSQLite.getWritableDatabase();
    }

    /**
     * Méthode permettant la fermeture de la base de données
     */
    public void close() {
        db.close();
    }


    /**
     *
     * Méthode permettant de modifier une personne dans la table Personne
     *
     * @param id
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @param adresse
     * @param mail
     * @param telephone
     * @return nombre de ligne affectées
     */
    public int modPersonne(int id, String nom, String prenom, String dateNaissance, String adresse, String mail, String telephone) {
        ContentValues values = new ContentValues();
        values.put(NOM, nom);
        values.put(PRENOM, prenom);
        values.put(DATENAISSANCE, dateNaissance);
        values.put(ADRESSE, adresse);
        values.put(MAIL, mail);
        values.put(TELEPHONE, telephone);

        String where = ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    /**
     *
     * Méthode permettant d'inserer une personne dans la table Personne
     * @param nom
     * @param prenom
     * @param dateNaissance
     * @param adresse
     * @param mail
     * @param telephone
     * @return l'id de la ligne ou -1 si erreur
     */

    public long insertPersonne(String nom, String prenom, String dateNaissance, String adresse, String mail, String telephone) {
        ContentValues values = new ContentValues();
        values.put(NOM, nom);
        values.put(PRENOM, prenom);
        values.put(DATENAISSANCE, dateNaissance);
        values.put(ADRESSE, adresse);
        values.put(MAIL, mail);
        values.put(TELEPHONE, telephone);


        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * Méthode permettant de récuperer les informations de la personne en fonction de son ID
     * @param id
     * @return null si pas trouvée ou la personne trouvée
     */

    public Personne getPersonne(int id) {
        Personne p = new Personne();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id, null);
        if (c.moveToFirst()) {
            p.setId_Personne(c.getInt(c.getColumnIndex(ID)));
            p.setNom(c.getString(c.getColumnIndex(NOM)));
            p.setPrenom(c.getString(c.getColumnIndex(PRENOM)));
            p.setDate(c.getString(c.getColumnIndex(DATENAISSANCE)));
            p.setAddress(c.getString(c.getColumnIndex(ADRESSE)));
            p.setMail(c.getString(c.getColumnIndex(MAIL)));
            p.setPhoneNumber(c.getString(c.getColumnIndex(TELEPHONE)));

            c.close();
        } else {
            p = null;
        }

        return p;
    }

    /**
     * Création d'une personne avec tous les champs vides
     */
    public void creerPersonnePremierLancement(){
        ContentValues values = new ContentValues();
        values.put(ID, 1);
        values.put(PRENOM, "");
        values.put(NOM, "");
        values.put(DATENAISSANCE, "");
        values.put(ADRESSE, "");
        values.put(MAIL, "");
        values.put(TELEPHONE, "");
        db.insert(TABLE_NAME, null, values);
    }
}

