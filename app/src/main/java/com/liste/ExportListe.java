package com.liste;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.application.MainActivity;
import com.application.inventaire.R;
import com.bd.MySQLite;
import com.bien.AjouterBien;
import com.categorie.Categorie;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.opencsv.CSVWriter;

import android.widget.AdapterView;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by Thib on 06/12/2017.
 */

public class ExportListe extends AppCompatActivity {

    private String nomFichierCsv = "";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;
    //private Menu m;
    private ArrayList<Categorie> listeCategorieEnBase = new ArrayList<Categorie>();
    private Spinner spinnerCategorie;
    public EditText nomListe;
    RadioButton ctvliste1;
    RadioButton ctvliste2;
    RadioButton ctvliste3;
    private Menu m;
    private int idliste = 0;
    private Boolean dansListe1 = false;
    private Boolean dansListe2 = false;
    private Boolean dansListe3 = false;
    private String regexDate = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
    private Boolean checkPermissionActivite = false;

    private ArrayList<Categorie> listeCategorieSelected;
    private ListeAdapter myAdapter;
    private Boolean perm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exporter_liste);
        this.listeCategorieSelected = new ArrayList<>();

        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Exporter une liste");
        setSupportActionBar(myToolbar);

        ListeDAO listeDAO = new ListeDAO(this);
        listeDAO.open();
        ArrayList<Liste> listes = listeDAO.getallListe();
        listeDAO.close();

        final RadioButton liste1 = (RadioButton) findViewById(R.id.checkListe1);
        final RadioButton liste2 = (RadioButton) findViewById(R.id.checkListe2);
        final RadioButton liste3 = (RadioButton) findViewById(R.id.checkListe3);

        liste1.setText(listes.get(0).getLibelle_liste());
        liste2.setText(listes.get(1).getLibelle_liste());
        liste3.setText(listes.get(2).getLibelle_liste());

        liste1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liste1.isChecked()) {
                    liste1.setChecked(true);
                    liste2.setChecked(false);
                    liste3.setChecked(false);
                    if (idliste != 1) {
                        idliste = 1;
                        listeCategorieSelected = new ArrayList<>();
                    }
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
                    liste2.setChecked(true);
                    liste1.setChecked(false);
                    liste3.setChecked(false);
                    if (idliste != 2) {
                        idliste = 2;
                        listeCategorieSelected = new ArrayList<>();
                    }
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
                    liste3.setChecked(true);
                    liste1.setChecked(false);
                    liste2.setChecked(false);
                    if (idliste != 3) {
                        idliste = 3;
                        listeCategorieSelected = new ArrayList<>();
                    }
                    TextView tv = (TextView) findViewById(R.id.affichageCategorie);
                    tv.setText("");
                    annexe(v);
                }

            }
        });

    }

    public void annexe(View view) {
        //final ArrayList<Categorie> categorieSelected = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(ExportListe.this);
        builder.setTitle("Choix des catégories à exporter :");

        LinearLayout corps = new LinearLayout(getApplicationContext());
        corps.setOrientation(LinearLayout.VERTICAL);

        TextView tvSelected = (TextView) findViewById(R.id.affichageCategorie);


        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
        listeCategorieEnBase = categorieDAO.getCategoriesByIdListe(idliste);
        categorieDAO.close();

        if (listeCategorieSelected == null || listeCategorieSelected.isEmpty()){
            listeCategorieSelected = listeCategorieEnBase;
            afficherCategories(tvSelected);
            tvSelected.setVisibility(View.VISIBLE);
        }

        final ListeAdapter categorieChooserAdapter = new ListeAdapter(getApplicationContext(), 0, listeCategorieEnBase, listeCategorieSelected);
        final ListView listeCategorie = new ListView(getApplicationContext());
        listeCategorie.setAdapter(categorieChooserAdapter);
        builder.setView(listeCategorie);
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listeCategorieSelected = new ArrayList<Categorie>();
                for (int j = 0; j < listeCategorieEnBase.size(); j++) {
                    Categorie cat = (Categorie) listeCategorie.getItemAtPosition(j);
                    if (cat.isSelected()) {
                        listeCategorieSelected.add(cat);
                    }
                }


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

    public void afficherCategories(TextView tvSelected){
        String displaySelected = "";
        int cpt = 0;
        int dernierElt = listeCategorieSelected.size() -1;
        for (Categorie cat : listeCategorieSelected) {
            if (cpt != dernierElt) {
                displaySelected += cat.getNom_Categorie() + ", ";
            } else {
                displaySelected += cat.getNom_Categorie();
            }
            cpt ++;
        }
        tvSelected.setText(displaySelected.trim());
    }

    public void exportDB(Context context, String nom) {

        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
        Log.d("pouet", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        Log.d("pouet2", exportDir.toString());
        File file = new File(exportDir, nom + ".csv");
        Log.d("pouet2", exportDir.toString());
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            SQLiteDatabase db = new MySQLite(context).getWritableDatabase();

            String inWhereClause = "";
            int i = 0;
            int dernierElt = listeCategorieSelected.size() -1;
            for (Categorie cat : listeCategorieSelected) {
                if (i == 0){
                    inWhereClause += " (\"" + cat.getId_Categorie() + "\", ";
                } else if (i == dernierElt) {
                    inWhereClause += "\"" + cat.getId_Categorie() + "\")";
                } else {
                    inWhereClause += "\"" + cat.getId_Categorie() + "\", ";
                }
                i++;
            }

            //id_bien,"","","","","","","","","id_categorie","photo_principale","photo_sec1","photo_sec2","photo_sec3","id_bien","id_liste"
            String nomColonne = "nom_bien as Nom, date_saisie as Date_de_saisie, date_achat as Date_achat, numero_serie as Numéro_serie, prix as Prix, description as Description, commentaire as Commentaire";
            String selectQuery = "SELECT "+nomColonne+" FROM BIEN JOIN APPARTIENT ON APPARTIENT.id_bien = BIEN.id_bien WHERE BIEN.id_categorie IN"+inWhereClause;

            Log.e("miPa",""+selectQuery);
            Cursor curCSV = db.rawQuery(selectQuery, null);

            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4),curCSV.getString(5),curCSV.getString(6)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            Toast.makeText(this, "Votre fichier CSV se trouve dans le dossier téléchargement", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception sqlEx) {
            Log.e(sqlEx.getMessage(), sqlEx.toString());
        }
    }

    public void onClickExportListe(View view) {
        EditText editTextNomFichier = (EditText) findViewById(R.id.nomListeAexporter);
        nomFichierCsv = editTextNomFichier.getText().toString();
        if (nomFichierCsv != null && !nomFichierCsv.equals("")){
            if (idliste != 0) {
                if (!listeCategorieSelected.isEmpty()){
                    verifierPermission();
                    if (perm) {
                        exportDB(getApplicationContext(), nomFichierCsv);
                    }
                } else {
                    Toast.makeText(this, "Vous devez sélectionner au moins une categorie", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vous devez sélectionner une liste à exporter", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vous devez écrire un nom de fichier", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        m = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                Intent intenthome = new Intent(getApplicationContext(), com.application.MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);

                return true;

            case R.id.plus:

                intent = new Intent(this, com.bien.AjouterBien.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void verifierPermission() {
        if (ContextCompat.checkSelfPermission(ExportListe.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ExportListe.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            perm = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (ContextCompat.checkSelfPermission(ExportListe.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            perm = true;
            exportDB(getApplicationContext(), nomFichierCsv);
        }

        if (!perm) {
            Toast.makeText(this, "L'application n'est pas autorisée à accéder aux documents. Verifier les permissions dans les réglages de l'appareil.", Toast.LENGTH_LONG).show();
        }

    }

}