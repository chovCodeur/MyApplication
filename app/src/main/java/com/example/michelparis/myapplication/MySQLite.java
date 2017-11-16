package com.example.michelparis.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Thib on 16/11/2017.
 */

public class MySQLite extends SQLiteOpenHelper {

    // Version de la BDD
    private static final int DATABASE_VERSION = 1;
    // Nom de la BDD
    private static final String DATABASE_NAME = "listerBiens.sqlite";
    // Nom des Tables de la BDD
    private static final String TABLE_LISTE = "LISTE";
    private static final String TABLE_BIEN = "BIEN";
    private static final String TABLE_CATEGORIE = "CATEGORIE";
    private static final String TABLE_PERSONNE = "PERSONNE";
    private static final String TABLE_APPARTIENT = "APPARTIENT";

    // Nom des colonnes de la table Liste
    private static final String KEY_ID_LISTE = "id_liste";
    private static final String KEY_LIBELLE = "libelle";
    private static final String KEY_COMMENTAIRE= "commentaire";

    // Nom des colonnes de la table Bien
    private static final String KEY_ID_BIEN = "id_bien";
    private static final String KEY_NOM = "nom";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NUMERO_SERIE = "numéro_serie";
    private static final String KEY_FACTURE = "facture";
    private static final String KEY_DATESAISIE = "date_saisie";
    private static final String KEY_DATEACHAT = "date_achat";
    private static final String KEY_PRIX = "prix";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_CATEGORIE_BIEN= "id_categorie";

    // Nom des colonnes de la table Catégorie
    private static final String KEY_ID_CATEGORIE = "id_categorie";

    // Nom des colonnes de la table Personne
    private static final String KEY_ID_PERSONNE = "id_personne";
    private static final String KEY_PRENOM= "prenom";
    private static final String KEY_NAISSANCE = "date_naissance";
    private static final String KEY_ADRESSE = "adresse";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_TELEPHONE = "telephone";

    private static MySQLite sInstance;

    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) { sInstance = new MySQLite(context); }
        return sInstance;
    }

    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_LISTE = "CREATE TABLE " + TABLE_LISTE + "("
                + KEY_ID_LISTE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_LIBELLE + " TEXT, " + KEY_COMMENTAIRE + " TEXT )";
        db.execSQL(CREATE_TABLE_LISTE);

        String CREATE_TABLE_BIEN = "CREATE TABLE " + TABLE_BIEN + "("
                + KEY_ID_BIEN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_NOM + " TEXT, " + KEY_DATESAISIE + " TEXT, " + KEY_DATEACHAT + " TEXT," + KEY_NUMERO_SERIE + " TEXT, " + KEY_PRIX + " REAL, " + KEY_DESCRIPTION + " TEXT, " + KEY_CATEGORIE_BIEN + " INTEGER, FOREIGN KEY (" + KEY_CATEGORIE_BIEN + ") REFERENCES " + TABLE_CATEGORIE + "(" + KEY_ID_CATEGORIE + "))";
        db.execSQL(CREATE_TABLE_BIEN);

        String CREATE_TABLE_CATEGORIE = "CREATE TABLE " + TABLE_CATEGORIE + "("
                + KEY_ID_CATEGORIE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_NOM + " TEXT, " + KEY_DESCRIPTION + "TEXT )";
        db.execSQL(CREATE_TABLE_CATEGORIE);

        String CREATE_TABLE_PERSONNE = "CREATE TABLE " + TABLE_PERSONNE + "("
                + KEY_ID_PERSONNE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_NOM + " TEXT, " + KEY_PRENOM + " TEXT, " + KEY_NAISSANCE + " TEXT, " + KEY_ADRESSE + " TEXT, " + KEY_MAIL + " TEXT, " + KEY_TELEPHONE + " INTEGER )";
        db.execSQL(CREATE_TABLE_PERSONNE);

        String CREATE_TABLE_APPARTIENT = "CREATE TABLE " + TABLE_APPARTIENT + "("
                + KEY_ID_BIEN + " INTEGER, " + KEY_ID_LISTE + " INTEGER PRIMARY KEY (" + KEY_ID_BIEN + " , " + KEY_ID_LISTE + "))";
        db.execSQL(CREATE_TABLE_APPARTIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
