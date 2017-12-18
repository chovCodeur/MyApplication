package com.liste;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    public EditText nomListe;
    private Categorie categorieSelectionne;
    RadioButton ctvliste1;
    RadioButton ctvliste2;
    RadioButton ctvliste3;
    private Menu m;
    private int idliste=1;
    private Boolean dansListe1 = false;
    private Boolean dansListe2 = false;
    private Boolean dansListe3 = false;
    private String regexDate = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
    private Boolean checkPermissionActivite = false;

    private ArrayList<Categorie> listeSelected;
    private ListeAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exporter_liste);
        this.listeSelected=new ArrayList<>();

        /*
        if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AjouterBien.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 4);
        }
        */

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
                    if (liste1.isChecked()){
                        liste1.setChecked(true);
                        liste2.setChecked(false);
                        liste3.setChecked(false);
                        if (idliste != 1){
                        idliste=1;
                        listeSelected = new ArrayList<>();
                        }
                        TextView tv = (TextView)findViewById(R.id.affichageCategorie);
                        tv.setText("");
                        annexe(v);
            }

            }
        });

        liste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (liste2.isChecked()){
                    liste2.setChecked(true);
                    liste1.setChecked(false);
                    liste3.setChecked(false);
                        if (idliste != 2) {
                            idliste = 2;
                            listeSelected = new ArrayList<>();
                        }
                        TextView tv = (TextView)findViewById(R.id.affichageCategorie);
                        tv.setText("");
                        annexe(v);
            }}
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
                            listeSelected = new ArrayList<>();
                        }
                        TextView tv = (TextView)findViewById(R.id.affichageCategorie);
                        tv.setText("");
                        annexe(v);
                    }

                }});

    }

    public void annexe(View view){
        ArrayList<Categorie> categorieSelected = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(ExportListe.this);
        builder.setTitle("Choix des catégories à exporter :");
        LinearLayout corps = new LinearLayout(getApplicationContext());
        corps.setOrientation(LinearLayout.VERTICAL);
        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
        Log.d("idListe", String.valueOf(idliste));
        categoriesList = categorieDAO.getCategoriesByIdListe(idliste);
        categorieDAO.close();
        Log.d("niktamere", String.valueOf(listeSelected.size()));
        ListeAdapter categorieChooserAdapter = new ListeAdapter(getApplicationContext(), 0, categoriesList, listeSelected);
        final ListView listeCategorie = new ListView(getApplicationContext());
        listeCategorie.setAdapter(categorieChooserAdapter);
        builder.setView(listeCategorie);
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listeSelected=new ArrayList<Categorie>();
                for (int j=0; j<categoriesList.size(); j++){
                    Categorie cat = (Categorie) listeCategorie.getItemAtPosition(j);
                    if (cat.isSelected()){
                        listeSelected.add(cat);
                    }
                }
                TextView tvSelected = (TextView)findViewById(R.id.affichageCategorie);
                tvSelected.setText(null);
                if(listeSelected.isEmpty()){
                    tvSelected.setVisibility(View.INVISIBLE);
                } else {
                    String displaySelected="";
                    for (Categorie cat : listeSelected){
                       displaySelected+=cat.getNom_Categorie()+" ";
                        Log.d("listSelectedboucle", listeSelected.toString());
                    }
                    tvSelected.setText(displaySelected);
                    tvSelected.setVisibility(View.VISIBLE);
                }
                Log.d("listSelected", listeSelected.toString());
            }
        });
        builder.show();
    }


    public void exportDB(Context context, String nom) {

        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
        Log.d("pouet",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());

        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        Log.d("pouet2",exportDir.toString());
        File file = new File(exportDir, nom+".csv");
        Log.d("pouet2",exportDir.toString());
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = new MySQLite(context).getWritableDatabase();
            String selectQuery = "SELECT * FROM BIEN";
            Cursor curCSV = db.rawQuery(selectQuery,null);

            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to export
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

//    public void modifierSpinner(int idliste){
//
//        spinnerCategorie = (Spinner) findViewById(R.id.select_categorie);
//
//        CategorieDAO categorieDAO = new CategorieDAO(this);
//        categorieDAO.open();
//        categoriesList = categorieDAO.getCategoriesByIdListe(idliste);
//        categorieDAO.close();
//
//
//        //spinnerCategorie.setOnItemSelectedListener(this);
//
//        /*ArrayList<String> listeCategorieName = new ArrayList<String>();
//        CategorieDAO categorieDAO = new CategorieDAO(this);
//        categorieDAO.open();
//        categoriesList = categorieDAO.getCategoriesByIdListe(idliste);
//        categorieDAO.close();*/
//
//        myAdapter = new ListeAdapter(this, 0, categoriesList);
//
//       /* ArrayAdapter arrayAdapterListe = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listeCategorieName);
//        arrayAdapterListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
//        spinnerCategorie.setAdapter(myAdapter);
//
//
//
//    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){

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
        EditText nomListe = (EditText) findViewById(R.id.nomListeAexporter);

        ExportListe exportListe = new ExportListe();
        exportListe.exportDB(getApplicationContext(), nomListe.getText().toString());
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
    public boolean onOptionsItemSelected (MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
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


}