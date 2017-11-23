package com.example.michelparis.myapplication;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AjouterBien extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private DrawerLayout drawer;
    ArrayList<Categorie> categorieName = new ArrayList<Categorie>();
    String[] listeName={"L1","L2","L3"};
    Spinner spinnerCategorie;
    Spinner spinnerListe;
    String nomCategorieSelectionne = "";
    Categorie categorieSelectionne;
    String nomListeSelectionne = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_bien);

        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
        Log.e("MiPa","Salut"+categorieDAO.getNomCategorieByIdBien(1));
        if (categorieDAO.getNomCategorieByIdBien(1).equals("")) {

            Categorie cuisine = new Categorie(0, "Cuisine", "Tous mes objets de la cuisine");
            Categorie salon = new Categorie(0, "Salon", "Tous mes objets du Salon");
            categorieDAO.addCategorie(cuisine);
            categorieDAO.addCategorie(salon);

        }


        Log.e("MiPa",categorieDAO.getNomCategorieByIdBien(1));


        ArrayList<Categorie> listeCategorieEnBase = new ArrayList<Categorie>();

        listeCategorieEnBase = categorieDAO.getAllCategorie();

       // int nbCategorie = listeCategorieEnBase.size();

        categorieName.addAll(listeCategorieEnBase);


        //categorieName = new String[]{"A","B"};

        categorieDAO.close();
/*
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.toolbarTitle));
        setSupportActionBar(myToolbar);
*/

        spinnerCategorie =(Spinner) findViewById(R.id.select_categorie);
        spinnerCategorie.setOnItemSelectedListener(this);

        ArrayList<String> listeCategorieName = new ArrayList<String>();
        int i =0;
        for (Categorie categorie: categorieName) {
            listeCategorieName.add(i,categorie.getNom_Categorie());
        }

        ArrayAdapter arrayAdapterListe = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listeCategorieName);
        arrayAdapterListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(arrayAdapterListe);

        /*
        spinnerListe = (Spinner) findViewById(R.id.select_liste);
        spinnerListe.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapterCategorie = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listeName);
        arrayAdapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListe.setAdapter(arrayAdapterCategorie);
*/

/*
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
         this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.setDrawerListener(toggle);
         toggle.syncState();

         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);
        /* View headerview = navigationView.getHeaderView(0);

         headerview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
        });
        */

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.liste1) {
            Intent intent = new Intent(this, InfosBien.class);
            startActivity(intent);
        }

        if (id == R.id.liste2) {
            Intent intent = new Intent(this, InfosBien.class);
            startActivity(intent);
        }

        if (id == R.id.liste3) {
            Intent intent = new Intent(this, InfosBien.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickAjouterBien(View view){
        TextView textViewNomBien = (TextView) findViewById(R.id.nom_bien);
        TextView textViewDateAchatBien = (TextView) findViewById(R.id.date_achat_bien);
        TextView textViewDescriptionBien = (TextView) findViewById(R.id.description_bien);
        TextView textViewCommentaireBien = (TextView) findViewById(R.id.commentaire_bien);
        TextView textViewPrixBien = (TextView) findViewById(R.id.prix_bien);
        TextView textViewNumeroSerie = (TextView) findViewById(R.id.numero_serie);

        String nomBien = textViewNomBien.getText().toString();
        String dateAchatSaisie = textViewDateAchatBien.getText().toString();
        String commentaireBien = textViewCommentaireBien.getText().toString();
        String descriptionBien = textViewDescriptionBien.getText().toString();
        Float prixBien = Float.valueOf(textViewPrixBien.getText().toString());
        String numeroSerie = textViewNumeroSerie.getText().toString();
        int idCategorieSelectionne = categorieSelectionne.getId_Categorie();

        Date dateSaisie = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateDeSaisie = sdf.format(dateSaisie);

        Bien bien = new Bien(idCategorieSelectionne,
                nomBien,
                dateDeSaisie,
                dateAchatSaisie,
                null,
                commentaireBien,
                prixBien,
                null,
                null,
                null,
                null,
                1,
                descriptionBien,
                numeroSerie
                );

        CategorieDAO categorieDAO = new CategorieDAO(this);

        categorieDAO.open();
        Log.e("MiPa","Salut"+categorieDAO.getNomCategorieByIdBien(1));
        if (categorieDAO.getNomCategorieByIdBien(1).equals("")) {

            Categorie cuisine = new Categorie(0, "Cuisine", "Tous mes objets de la cuisine");
            Categorie salon = new Categorie(0, "Salon", "Tous mes objets du Salon");
            categorieDAO.addCategorie(cuisine);
            categorieDAO.addCategorie(salon);

        }




        Log.e("MiPa",categorieDAO.getNomCategorieByIdBien(1));

        categorieDAO.close();

        BienDAO bienDAO = new BienDAO(this);

        bienDAO.open();

       // bienDAO.addBien(bien);
        Log.e("MiPa",bien.toString());

        bienDAO.close();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
///            Spinner spinnerListe = (Spinner)parent;
        Spinner spinnerCategorie = (Spinner)parent;

        if(spinnerCategorie.getId() == R.id.select_categorie) {
             categorieSelectionne = categorieName.get(position);
            //Toast.makeText(getApplicationContext(), CategorieName[position], Toast.LENGTH_SHORT).show();
        } else {
            nomListeSelectionne = listeName[position];
            //Toast.makeText(getApplicationContext(), ListeName[position], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
