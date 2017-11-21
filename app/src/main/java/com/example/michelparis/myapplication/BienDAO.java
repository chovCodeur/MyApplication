package com.example.michelparis.myapplication;

/**
 * Created by Kevin on 16/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Kevin on 16/11/2017.
 */

/**
 * Classe BienDAO gère les interactions avec la base de données pour tout ce qui touche à la classe Bien
 */
public class BienDAO {

    public static final String TABLE_NAME = "BIEN";
    public static final String ID = "id_bien";
    public static final String NOM = "nom";
    public static final String DATESAISIE = "date_saisie";
    public static final String DATEACHAT = "date_achat";
    public static final String FACTURE = "facture";
    public static final String COMMENTAIRE = "commentaire";
    public static final String DESCRIPTION = "description";
    public static final String PRIX = "prix";
    public static final String NUMSERIE = "numero_serie";
    public static final String PHOTO_PRINCIPALE = "photo_principale";
    public static final String PHOTO_SEC1 = "photo_sec1";
    public static final String PHOTO_SEC2 = "photo_sec2";
    public static final String PHOTO_SEC3 = "photo_sec3";
    public static final String IDCATEGORIE = "id_categorie";

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    /**
     * Contructeur de la classe BienDAO
     * @param context le contexte
     */
    public BienDAO (Context context){
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
     * Méthode permettant d'ajouter un bien dans la table Bien
     * @param bien Bien : l'bien à ajouter
     * @return long l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
     */
    public long addBien(Bien bien){
        Log.e("MiPa","addBien"+bien.toString());
        ContentValues values = new ContentValues();
        values.put(NOM,bien.getNom_bien());
        values.put(DATESAISIE,bien.getDate_saisie_bien());
        values.put(DATEACHAT, bien.getDate_achat_bien());
        //values.put(FACTURE, bien.getMagasinBien());
        values.put(COMMENTAIRE, bien.getCommentaire_bien());
        values.put(DESCRIPTION, bien.getDescription_bien());
        values.put(PRIX, bien.getPrix_bien());
        values.put(NUMSERIE, bien.getNumeroSerie_bien());
        values.put(IDCATEGORIE, bien.getId_categorie_bien());


        return db.insert(TABLE_NAME,null,values);
    }

    /**
     * Méthode permettant de modifier un bien dans la table Bien
     * @param id long : l'id de l'bien
     * @param nom String : le nom de l'bien
     * @return int : le nombre de lignes affectées par la requête
     */
    public int modBien(int id, String nom, String dateSaisie, String dateAchat, String commentaire, int idCategorie,
                       String description, float prix, String numeroSerie){
        ContentValues values = new ContentValues();
        values.put(NOM,nom);
        values.put(DATESAISIE,dateSaisie);
        values.put(DATEACHAT, dateAchat);
        //values.put(FACTURE, bien.getMagasinBien());
        values.put(COMMENTAIRE, commentaire);
        values.put(DESCRIPTION, description);
        values.put(PRIX, prix);
        values.put(NUMSERIE, numeroSerie);
        values.put(IDCATEGORIE, idCategorie);

        String where = ID+" = ?";
        String[] whereArgs = {String.valueOf(id)};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }


    public Bien getBien(int id){
        Bien a=new Bien(0, "", "", "", "","", 0,null,null,null,null,0, "", "");

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
    }

    /**
     * Méthode permettant de supprimer un bien dans la table bien
     * @param bien Bien : le magasin à supprimer
     * @return int : le nombre de lignes affectées par la clause WHERE, 0 sinon
     */
    public int deleteBien(Bien bien) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = ID+" = ?";
        String[] whereArgs = {bien.getId_bien()+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    /**
     * Méthode permettant de retrouver tous les biens pour un magasin donné
     * @param id_liste long : l'id du magasin dans lequel il faut retrouver les biens
     * @return ArrayList<Bien> la liste des biens dans le magasin
     */
    public ArrayList<Bien> getBiensByListe(int id_liste){
        ArrayList<Bien> liste = new ArrayList<Bien>();
        Cursor curseurBien = db.rawQuery("SELECT * FROM "+TABLE_NAME+" JOIN APPARTIENT ON APPARTIENT.id_bien="+TABLE_NAME+"."+ID+" WHERE id_liste = "+id_liste, null);

        Bien bienTemp;
        if (curseurBien.moveToFirst()) {
            do {
                bienTemp = new Bien(
                        curseurBien.getInt(curseurBien.getColumnIndex(ID)),
                        curseurBien.getString(curseurBien.getColumnIndex(NOM)),
                        curseurBien.getString(curseurBien.getColumnIndex(DATESAISIE)),
                        curseurBien.getString(curseurBien.getColumnIndex(DATEACHAT)),
                        curseurBien.getString(curseurBien.getColumnIndex(FACTURE)),
                        curseurBien.getString(curseurBien.getColumnIndex(COMMENTAIRE)),
                        curseurBien.getFloat(curseurBien.getColumnIndex(PRIX)),
                        BitmapFactory.decodeByteArray(curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_PRINCIPALE)), 0, curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_PRINCIPALE)).length),
                        BitmapFactory.decodeByteArray(curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_SEC1)), 0, curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_SEC1)).length),
                        BitmapFactory.decodeByteArray(curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_SEC2)), 0, curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_SEC2)).length),
                        BitmapFactory.decodeByteArray(curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_SEC3)), 0, curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_SEC3)).length),
                        curseurBien.getInt(curseurBien.getColumnIndex(IDCATEGORIE)),
                        curseurBien.getString(curseurBien.getColumnIndex(DESCRIPTION)),
                        curseurBien.getString(curseurBien.getColumnIndex(NUMSERIE))
                );

                liste.add(bienTemp);
            } while (curseurBien.moveToNext());
        }
        curseurBien.close();
        return liste;
    }
}

