package com.liste;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.application.inventaire.R;
import com.bd.MySQLite;
import com.categorie.Categorie;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Thib on 06/12/2017.
 */

/**
 * Classe permettant d'exporter une liste de bien au format CSV
 */

public class ExportListe extends AppCompatActivity {

    private String nomFichierCsv = "";
    private ArrayList<Categorie> listeCategorieEnBase = new ArrayList<Categorie>();
    private int idliste = 0;
    private Menu m;
    private ArrayList<Categorie> listeCategorieSelected;
    private Boolean perm = false;

    /**
     * Méthode appelée à la creation de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exporter_liste);

        // nom dans le menu
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.toolbar_title_exporter_liste));
        setSupportActionBar(myToolbar);

        this.listeCategorieSelected = new ArrayList<>();
        ListeDAO listeDAO = new ListeDAO(this);
        listeDAO.open();
        ArrayList<Liste> listes = listeDAO.getallListe();
        listeDAO.close();

        //on récupére et on affiche le libellé des listes
        final RadioButton liste1 = (RadioButton) findViewById(R.id.checkListe1);
        final RadioButton liste2 = (RadioButton) findViewById(R.id.checkListe2);
        final RadioButton liste3 = (RadioButton) findViewById(R.id.checkListe3);

        //On récupère le nom de chacune des listes
        liste1.setText(listes.get(0).getLibelle_liste());
        liste2.setText(listes.get(1).getLibelle_liste());
        liste3.setText(listes.get(2).getLibelle_liste());

        //Lorsque l'on clique sur la liste1
        liste1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liste1.isChecked()) {
                    // une seule liste à la fois
                    liste1.setChecked(true);
                    liste2.setChecked(false);
                    liste3.setChecked(false);
                    if (idliste != 1) {
                        idliste = 1;
                        listeCategorieSelected = new ArrayList<>();
                    }
                    // catégorie selectionnées
                    TextView tv = (TextView) findViewById(R.id.affichageCategorie);
                    tv.setText("");
                    annexe(v);
                }

            }
        });

        liste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liste2.isChecked()) {
                    // une seule liste à la fois
                    liste2.setChecked(true);
                    liste1.setChecked(false);
                    liste3.setChecked(false);
                    if (idliste != 2) {
                        idliste = 2;
                        listeCategorieSelected = new ArrayList<>();
                    }
                    // catégories selectionnées
                    TextView tv = (TextView) findViewById(R.id.affichageCategorie);
                    tv.setText("");
                    annexe(v);
                }
            }
        });

        liste3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liste3.isChecked()) {
                    // une seule liste à la fois
                    liste3.setChecked(true);
                    liste1.setChecked(false);
                    liste2.setChecked(false);
                    if (idliste != 3) {
                        idliste = 3;
                        listeCategorieSelected = new ArrayList<>();
                    }
                    // catégories selectionnées
                    TextView tv = (TextView) findViewById(R.id.affichageCategorie);
                    tv.setText("");
                    annexe(v);
                }

            }
        });

    }

    /**
     * Méthode de création de l'AlertDialog
     * @param view
     */
    public void annexe(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExportListe.this);
        //nom de la dialog
        builder.setTitle(getResources().getString(R.string.categories_to_export));

        // propriétés de la dialog
        LinearLayout corps = new LinearLayout(getApplicationContext());
        corps.setOrientation(LinearLayout.VERTICAL);

        TextView tvSelected = (TextView) findViewById(R.id.affichageCategorie);
        CategorieDAO categorieDAO = new CategorieDAO(this);

        // toutes les catégories de cette liste
        categorieDAO.open();
        listeCategorieEnBase = categorieDAO.getCategoriesByIdListe(idliste);
        categorieDAO.close();

        // si la liste est vide
        if (listeCategorieSelected == null || listeCategorieSelected.isEmpty()) {
            listeCategorieSelected = listeCategorieEnBase;
            afficherCategories(tvSelected);
            tvSelected.setVisibility(View.VISIBLE);
        }

        // adapter pour afficher la liste View
        final ListeAdapter categorieChooserAdapter = new ListeAdapter(getApplicationContext(), 0, listeCategorieEnBase, listeCategorieSelected);
        final ListView listeCategorie = new ListView(getApplicationContext());
        listeCategorie.setAdapter(categorieChooserAdapter);
        builder.setView(listeCategorie);
        builder.setPositiveButton(R.string.exporter_liste_dialog_negative_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listeCategorieSelected = new ArrayList<Categorie>();

                // toute les catégories
                for (int j = 0; j < listeCategorieEnBase.size(); j++) {
                    Categorie cat = (Categorie) listeCategorie.getItemAtPosition(j);
                    if (cat.isSelected()) {
                        listeCategorieSelected.add(cat);
                    }
                }


                // On vide l'affichage précédent
                TextView tvSelected = (TextView) findViewById(R.id.affichageCategorie);
                tvSelected.setText(null);
                if (listeCategorieSelected.isEmpty()) {
                    tvSelected.setVisibility(View.INVISIBLE);
                } else {
                    afficherCategories(tvSelected);
                    tvSelected.setVisibility(View.VISIBLE);
                }
            }
        });
        builder.show();
    }

    /**
     * Méthode permettant d'afficher les catégories dans la classe d'export
     * @param tvSelected
     */
    public void afficherCategories(TextView tvSelected) {
        String displaySelected = "";
        int cpt = 0;
        int dernierElt = listeCategorieSelected.size() - 1;
        for (Categorie cat : listeCategorieSelected) {
            // on affiche le contenu de la liste
            if (cpt != dernierElt) {
                displaySelected += cat.getNom_Categorie() + ", ";
            } else {
                displaySelected += cat.getNom_Categorie();
            }
            cpt++;
        }
        tvSelected.setText(displaySelected.trim());
    }

    /**
     * Méthode permettant d'exporter en csv
     * @param context
     * @param nom
     */
    public void exportDB(Context context, String nom) {

        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
        //On vérifie si le fichier existe déjà
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, nom + ".csv");

        try {
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            //Permet d'avoir les accents dans le csv
            os.write(0xef);
            os.write(0xbb);
            os.write(0xbf);
            CSVWriter csvWrite = new CSVWriter(new OutputStreamWriter(os));

            SQLiteDatabase db = new MySQLite(context).getWritableDatabase();

            // clause WHERE (IN)
            // pour selectionnée uniquement les bonnes catégories
            String inWhereClause = "";
            int i = 0;
            int dernierElt = listeCategorieSelected.size() - 1;
            for (Categorie cat : listeCategorieSelected) {
                if (i == 0) {
                    inWhereClause += " (\"" + cat.getId_Categorie() + "\", ";
                } else if (i == dernierElt) {
                    inWhereClause += "\"" + cat.getId_Categorie() + "\")";
                } else {
                    inWhereClause += "\"" + cat.getId_Categorie() + "\", ";
                }
                i++;
            }


            // nom de colonnes personnalisées
            String nomColonne = "nom_bien as Nom, date_saisie as Date_de_saisie, date_achat as Date_achat, numero_serie as Numéro_serie, prix as Prix, description as Description, commentaire as Commentaire, '##INFOS##' ,PERSONNE.nom as Nom_proprietaire, PERSONNE.prenom as Prenom_proprietaire, PERSONNE.date_naissance as Date_naissance_proprietaire, PERSONNE.adresse as Adresse_proprietaire, PERSONNE.mail as Mail_proprietaire, PERSONNE.telephone as Telephone_personne, PERSONNE.numero_contrat as Numero_contrat ";
            String selectQuery = "SELECT " + nomColonne + " FROM PERSONNE, BIEN JOIN APPARTIENT ON APPARTIENT.id_bien = BIEN.id_bien WHERE BIEN.id_categorie IN" + inWhereClause;

            Cursor curCSV = db.rawQuery(selectQuery, null);

            csvWrite.writeNext(curCSV.getColumnNames());
            boolean premierPassage = true;
            while (curCSV.moveToNext()) {
                //Les colonnes que l'on souhaite exporter

                if (premierPassage) {
                    String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12), curCSV.getString(13)};
                    csvWrite.writeNext(arrStr);
                    premierPassage = false;
                } else {
                    String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6)};
                    csvWrite.writeNext(arrStr);
                }
            }
            csvWrite.close();
            curCSV.close();

            // confirmation
            Toast.makeText(this, R.string.location_csv_file, Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception sqlEx) {
            Log.e(sqlEx.getMessage(), sqlEx.toString());
        }
    }

    /**
     * Méthode permettant de faire l'export lorsque l'on click sur le boutton
     * @param view
     */
    public void onClickExportListe(View view) {
        EditText editTextNomFichier = (EditText) findViewById(R.id.nomListeAexporter);
        nomFichierCsv = editTextNomFichier.getText().toString();
        // contrôle du nom de fichier
        if (nomFichierCsv != null && !nomFichierCsv.equals("")) {
            // il faut selctionner une liste
            if (idliste != 0) {
                // il faut au moins une catégorie
                if (!listeCategorieSelected.isEmpty()) {
                    verifierPermission();
                    // on vérifie les permissions de lecture/ecriture
                    if (perm) {
                        exportDB(getApplicationContext(), nomFichierCsv);
                    }
                } else {
                    // il faut au moins une catégorie
                    Toast.makeText(this, R.string.select_one_categorie, Toast.LENGTH_SHORT).show();
                }
                // il faut au moins une liste
            } else {
                Toast.makeText(this, R.string.select_one_list, Toast.LENGTH_SHORT).show();
            }
            // le nom est vide
        } else {
            Toast.makeText(this, R.string.file_name, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Procédure gérant l'action du bouton physique retour du téléphone.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Méthode permettant d'assigner le menu et ses options à l'activité.
     * @param menu
     * @return true;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        m = menu;
        return true;
    }

    /**
     * Methode utilisée par le menu supérieur
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                //Permet de revenir sur le menu principal
                Intent intenthome = new Intent(getApplicationContext(), com.application.MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);
                return true;
            case R.id.plus:
                //Permet de basculer sur la page AjouterBien
                intent = new Intent(this, com.bien.AjouterBien.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode vérifiant les permissions de lecture sur le stockage interne
     */
    public void verifierPermission() {
        if (ContextCompat.checkSelfPermission(ExportListe.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ExportListe.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            perm = true;
        }
    }

    /**
     * Méthode appelée après la verification des permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (ContextCompat.checkSelfPermission(ExportListe.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // on verifie les permissions
            perm = true;
            exportDB(getApplicationContext(), nomFichierCsv);
        }

        // si les permissions sont KO, on affiche un message d'erreur
        if (!perm) {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
        }

    }

}