package com.liste;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.application.MainActivity;
import com.application.inventaire.R;
import com.bd.MySQLite;
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

public class ExportListe extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;
    //private Menu m;
    private ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    private Spinner spinnerCategorie;
    private Categorie categorieSelectionne;
    private Boolean dansListe1 = false;
    private Boolean dansListe2 = false;
    private Boolean dansListe3 = false;
    private String regexDate = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
    private Boolean checkPermissionActivite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exporter_liste);

        /*
        if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AjouterBien.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 4);
        }
        */

        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Exporter une liste");
        setSupportActionBar(myToolbar);

        spinnerCategorie = (Spinner) findViewById(R.id.select_categorie);
        spinnerCategorie.setOnItemSelectedListener(this);

        ArrayList<String> listeCategorieName = new ArrayList<String>();
        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
        categoriesList = categorieDAO.getAllCategorie();
        categorieDAO.close();

        ListeDAO listeDAO = new ListeDAO(this);

        listeDAO.open();

        ArrayList<Liste> listes = listeDAO.getallListe();

        listeDAO.close();

        final CheckedTextView ctvliste1 = (CheckedTextView) findViewById(R.id.checkListe1);
        final CheckedTextView ctvliste2 = (CheckedTextView) findViewById(R.id.checkListe2);
        final CheckedTextView ctvliste3 = (CheckedTextView) findViewById(R.id.checkListe3);

        ctvliste1.setText(listes.get(0).getLibelle_liste());
        ctvliste2.setText(listes.get(1).getLibelle_liste());
        ctvliste3.setText(listes.get(2).getLibelle_liste());

        ctvliste1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctvliste1.isChecked()) {
                    ctvliste1.setChecked(false);
                    dansListe1 = false;
                } else {
                    ctvliste1.setChecked(true);
                    dansListe1 = true;
                }
            }
        });

        ctvliste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctvliste2.isChecked()) {
                    ctvliste2.setChecked(false);
                    dansListe2 = false;
                } else {
                    ctvliste2.setChecked(true);
                    dansListe2 = true;
                }
            }
        });

        ctvliste3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctvliste3.isChecked()) {
                    ctvliste3.setChecked(false);
                    dansListe3 = false;
                } else {
                    ctvliste3.setChecked(true);
                    dansListe3 = true;
                }

                }});
    }

    public static void exportDB(Context context) {
        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
        Log.d("pouet",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        Log.d("pouet2",exportDir.toString());
        File file = new File(exportDir, ".csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = new MySQLite(context).getWritableDatabase();
            String selectQuery = "SELECT libelle FROM BIEN JOIN APPARTIENT ON APPARTIENT.id_bien=BIEN.id_bien WHERE id_liste = id_liste ORDER BY id_categorie";
            Cursor curCSV = db.rawQuery(selectQuery,null);

            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorieSelectionne = categoriesList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
    public void onClickExportListe(View view){
        TextView textViewNomCategorie = (TextView) findViewById(R.id.editCategorie);
        TextView textViewDescription = (TextView) findViewById(R.id.editDescription);

        String nomCategorie = textViewNomCategorie.getText().toString();
        String description = textViewDescription.getText().toString();

        Categorie categorie = new Categorie(1, nomCategorie, description);

        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
        categorieDAO.addCategorie(categorie);
        //categorieDAO.addCategorie(1,nomCategorie,description);
        categorieDAO.close();


    }
}