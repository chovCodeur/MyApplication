package com.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Thib on 16/11/2017.
 */

/**
 * Pour gérer les interactions avec la BD
 */
public class MySQLite extends SQLiteOpenHelper {

    // Version de la BDD
    private static final int DATABASE_VERSION = 1;
    // Nom de la BDD
    private static final String DATABASE_NAME = "database.sqlite";
    // Nom des Tables de la BDD
    private static final String TABLE_LISTE = "LISTE";
    private static final String TABLE_BIEN = "BIEN";
    private static final String TABLE_CATEGORIE = "CATEGORIE";
    private static final String TABLE_PERSONNE = "PERSONNE";
    private static final String TABLE_APPARTIENT = "APPARTIENT";

    // Nom des colonnes de la table Liste
    private static final String KEY_ID_LISTE = "id_liste";
    private static final String KEY_LIBELLE = "libelle";
    private static final String KEY_COMMENTAIRE = "commentaire";

    // Nom des colonnes de la table Bien
    private static final String KEY_ID_BIEN = "id_bien";
    private static final String KEY_NOM = "nom";
    private static final String KEY_NOM_BIEN = "nom_bien";

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NUMERO_SERIE = "numero_serie";
    private static final String KEY_FACTURE = "facture";
    private static final String KEY_DATESAISIE = "date_saisie";
    private static final String KEY_DATEACHAT = "date_achat";
    private static final String KEY_PRIX = "prix";
    private static final String KEY_PHOTO_PRINCIPALE = "photo_principale";
    private static final String KEY_PHOTO_SEC1 = "photo_sec1";
    private static final String KEY_PHOTO_SEC2 = "photo_sec2";
    private static final String KEY_PHOTO_SEC3 = "photo_sec3";
    private static final String KEY_CATEGORIE_BIEN = "id_categorie";

    // Nom des colonnes de la table Catégorie
    private static final String KEY_ID_CATEGORIE = "id_categorie";

    // Nom des colonnes de la table Personne
    private static final String KEY_ID_PERSONNE = "id_personne";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_NAISSANCE = "date_naissance";
    private static final String KEY_ADRESSE = "adresse";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_NUMERO_CONTRAT = "numero_contrat";

    private static MySQLite sInstance;

    /**
     * getteur  de l'instance
     * @param context
     * @return instance
     */
    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MySQLite(context);
        }
        return sInstance;
    }

    /**
     * Constructeur de l'objet
     * @param context
     */
    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Appelé à la création
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // script et création de la table liste
        String CREATE_TABLE_LISTE = "CREATE TABLE " + TABLE_LISTE + "("
                + KEY_ID_LISTE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_LIBELLE + " TEXT, " + KEY_COMMENTAIRE + " TEXT )";
        db.execSQL(CREATE_TABLE_LISTE);

        // script et création de la table bien
        String CREATE_TABLE_BIEN = "CREATE TABLE " + TABLE_BIEN + "("
                + KEY_ID_BIEN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_NOM_BIEN + " TEXT, " + KEY_DATESAISIE + " TEXT, " + KEY_DATEACHAT + " TEXT," + KEY_FACTURE + " TEXT," + KEY_NUMERO_SERIE + " TEXT, " + KEY_PRIX + " REAL, " + KEY_DESCRIPTION + " TEXT, " + KEY_COMMENTAIRE + " TEXT, " + KEY_CATEGORIE_BIEN + " INTEGER, " + KEY_PHOTO_PRINCIPALE + " TEXT," + KEY_PHOTO_SEC1 + " TEXT," + KEY_PHOTO_SEC2 + " TEXT," + KEY_PHOTO_SEC3 + " TEXT, FOREIGN KEY (" + KEY_CATEGORIE_BIEN + ") REFERENCES " + TABLE_CATEGORIE + "(" + KEY_ID_CATEGORIE + "))";
        db.execSQL(CREATE_TABLE_BIEN);

        // script et création de la table categorie
        String CREATE_TABLE_CATEGORIE = "CREATE TABLE " + TABLE_CATEGORIE + "("
                + KEY_ID_CATEGORIE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_NOM + " TEXT, " + KEY_DESCRIPTION + " TEXT )";
        db.execSQL(CREATE_TABLE_CATEGORIE);

        // script et création de la table personne
        String CREATE_TABLE_PERSONNE = "CREATE TABLE " + TABLE_PERSONNE + "("
                + KEY_ID_PERSONNE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + KEY_NOM + " TEXT, " + KEY_PRENOM + " TEXT, " + KEY_NAISSANCE + " TEXT, " + KEY_ADRESSE + " TEXT, " + KEY_MAIL + " TEXT, " + KEY_TELEPHONE + " TEXT, " + KEY_NUMERO_CONTRAT + " TEXT  )";
        db.execSQL(CREATE_TABLE_PERSONNE);

        // script et création de la table appartient
        String CREATE_TABLE_APPARTIENT = "CREATE TABLE " + TABLE_APPARTIENT + "("
                + KEY_ID_BIEN + " INTEGER, " + KEY_ID_LISTE + " INTEGER, PRIMARY KEY (" + KEY_ID_BIEN + " , " + KEY_ID_LISTE + "))";
        db.execSQL(CREATE_TABLE_APPARTIENT);

    }

    /**
     * Lors d'une mise à jour de la BD
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Suppression des anciennes tables si elles existent
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONNE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPARTIENT);
        onCreate(db);
    }
}
