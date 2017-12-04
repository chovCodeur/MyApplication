package com.dao;

/**
 * Created by Kevin on 16/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bien.Bien;
import com.bd.MySQLite;

import java.io.ByteArrayOutputStream;
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

    private static final String KEY_ID_LISTE = "id_liste";
    private static final String TABLE_APPARTIENT = "APPARTIENT";



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
    public long addBien(Bien bien, int idListe){
        //Log.e("MiPa","addBien"+bien.toString());
        ContentValues values = new ContentValues();
        values.put(NOM,bien.getNom_bien());
        values.put(DATESAISIE,bien.getDate_saisie_bien());
        values.put(DATEACHAT, bien.getDate_achat_bien());
        //values.put(FACTURE, bien.getMagasinBien());
        values.put(COMMENTAIRE, bien.getCommentaire_bien());
        if (bien.getPhoto_bien_principal() != null) {
            values.put(PHOTO_PRINCIPALE, convertBitmapAsByteArray(bien.getPhoto_bien_principal()));
        }
        values.put(DESCRIPTION, bien.getDescription_bien());
        values.put(PRIX, bien.getPrix_bien());
        values.put(NUMSERIE, bien.getNumeroSerie_bien());
        values.put(IDCATEGORIE, bien.getId_categorie_bien());

        long temp = db.insert(TABLE_NAME,null,values);
        addInAppartient(temp, idListe);
        return temp;
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
            a.setPhoto_bien_principal(convertByteArrayAsBitmap(c.getBlob(c.getColumnIndex(PHOTO_PRINCIPALE))));
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
        Cursor curseurBien = db.rawQuery("SELECT * FROM "+TABLE_NAME+" JOIN APPARTIENT ON APPARTIENT.id_bien="+TABLE_NAME+"."+ID+" WHERE id_liste = "+id_liste+" ORDER BY "+IDCATEGORIE, null);

        Bien bienTemp =new Bien(0, "", "", "", "","", 0,null,null,null,null,0, "", "");

        if (curseurBien.moveToFirst()) {
            do {
               /* bienTemp = new Bien(
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
                ); */

             /*
                bienTemp = new Bien(
                        curseurBien.getInt(curseurBien.getColumnIndex(ID)),
                        curseurBien.getString(curseurBien.getColumnIndex(NOM)),
                        curseurBien.getString(curseurBien.getColumnIndex(DATESAISIE)),
                        curseurBien.getString(curseurBien.getColumnIndex(DATEACHAT)),
                        null,
                        curseurBien.getString(curseurBien.getColumnIndex(COMMENTAIRE)),
                        curseurBien.getFloat(curseurBien.getColumnIndex(PRIX)),
                        convertByteArrayAsBitmap(curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_PRINCIPALE))),
                        null,
                        null,
                        null,
                        curseurBien.getInt(curseurBien.getColumnIndex(IDCATEGORIE)),
                        curseurBien.getString(curseurBien.getColumnIndex(DESCRIPTION)),
                        curseurBien.getString(curseurBien.getColumnIndex(NUMSERIE))
                ); */

                Log.e("MiPa","Num"+curseurBien.getColumnIndex(PHOTO_PRINCIPALE));

                bienTemp.setId_bien(curseurBien.getInt(curseurBien.getColumnIndex(ID)));
                bienTemp.setNom_bien(curseurBien.getString(curseurBien.getColumnIndex(NOM)));
                bienTemp.setDate_saisie_bien(curseurBien.getString(curseurBien.getColumnIndex(DATESAISIE)));
                bienTemp.setDate_achat_bien(curseurBien.getString(curseurBien.getColumnIndex(DATEACHAT)));
                bienTemp.setCommentaire_bien(curseurBien.getString(curseurBien.getColumnIndex(COMMENTAIRE)));
                bienTemp.setPhoto_bien_principal(convertByteArrayAsBitmap(curseurBien.getBlob(curseurBien.getColumnIndex(PHOTO_PRINCIPALE))));
                bienTemp.setPrix_bien(curseurBien.getFloat(curseurBien.getColumnIndex(PRIX)));
                bienTemp.setId_categorie_bien(curseurBien.getInt(curseurBien.getColumnIndex(IDCATEGORIE)));
                bienTemp.setDescription_bien(curseurBien.getString(curseurBien.getColumnIndex(DESCRIPTION)));
                bienTemp.setNumeroSerie_bien(curseurBien.getString(curseurBien.getColumnIndex(NUMSERIE)));



                liste.add(bienTemp);
                bienTemp =new Bien(0, "", "", "", "","", 0,null,null,null,null,0, "", "");
            } while (curseurBien.moveToNext());
        }
        curseurBien.close();
        return liste;
    }

    public long addInAppartient(long idBienInsere, int idListe){
        ContentValues values = new ContentValues();
        values.put(ID, idBienInsere);
        values.put(KEY_ID_LISTE,idListe);
        return db.insert(TABLE_APPARTIENT,null,values);
    }

    public long compterBienEnBase(){
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public int supprimerListeAppartenance(int idBien, int prevIdListe) {
        String where = "id_bien = ? AND id_liste = ?";
        String[] whereArgs = {idBien+"", prevIdListe+""};

        return db.delete("APPARTIENT", where, whereArgs);
    }

    public ArrayList<Integer> getAllIdListeByIdBien(int id) {
        ArrayList<Integer> idListes = new ArrayList<>();

        Cursor curseur = db.rawQuery("SELECT id_liste FROM APPARTIENT WHERE id_bien = "+id,null);

        if (curseur.moveToFirst()) {
            do {
                idListes.add(curseur.getInt(curseur.getColumnIndex("id_liste")));
            } while (curseur.moveToNext());
        }
        curseur.close();

        return idListes;
    }

    public byte[] convertBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap != null ) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return outputStream.toByteArray();
        } else {
            return null;
        }
    }

    public Bitmap convertByteArrayAsBitmap(byte[] bytes) {

        if (bytes != null){
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }

    }


}

