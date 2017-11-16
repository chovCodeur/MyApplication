package com.example.michelparis.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

public class AjouterBien extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private DrawerLayout drawer;
    String[] CategorieName={"BOI","SBI","HDFC","PNB","OBC"};
    String[] ListeName={"L1","L2","L3"};
    Spinner spinnerCategorie;
    Spinner spinnerListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_bien);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        //spinnerListe.
        myToolbar.setTitle("NomAppli");
        setSupportActionBar(myToolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.toolbarTitle));
        spinnerCategorie =(Spinner) findViewById(R.id.select_categorie);
        spinnerCategorie.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapterListe = new ArrayAdapter(this,android.R.layout.simple_spinner_item, CategorieName);
        arrayAdapterListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(arrayAdapterListe);


        spinnerListe = (Spinner) findViewById(R.id.select_liste);
        spinnerListe.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapterCategorie = new ArrayAdapter(this,android.R.layout.simple_spinner_item, ListeName);
        arrayAdapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListe.setAdapter(arrayAdapterCategorie);


        /** drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
         this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
         drawer.setDrawerListener(toggle);
         toggle.syncState();

         NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);
         View headerview = navigationView.getHeaderView(0);

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

       /* if (id == R.id.youtube) {
            Intent youtube = new Intent(Intent.ACTION_VIEW);
            youtube.setData(Uri.parse("https://www.youtube.com/user/bibi300"));
            startActivity(youtube);
        }

        if (id == R.id.twitch) {
            Intent twitch = new Intent(Intent.ACTION_VIEW);
            twitch.setData(Uri.parse("https://go.twitch.tv/bibi300"));
            startActivity(twitch);
        }

        if (id == R.id.dailymotion) {
            Intent dailymotion = new Intent(Intent.ACTION_VIEW);
            dailymotion.setData(Uri.parse("https://www.dailymotion.com/bibi300"));
            startActivity(dailymotion);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickAjouterBien(View view){
        TextView textViewNomBien = (TextView) findViewById(R.id.nom_bien);
        TextView textViewDateAchatBien = (TextView) findViewById(R.id.date_achat_bien);
        TextView textViewDescriptionBien = (TextView) findViewById(R.id.description_bien);
        TextView textViewPrixBien = (TextView) findViewById(R.id.prix_bien);


        Log.e("MiPA","ici"+textViewNomBien.getText());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
///            Spinner spinnerListe = (Spinner)parent;
        Spinner spinnerCategorie = (Spinner)parent;

        if(spinnerCategorie.getId() == R.id.select_categorie) {
            Toast.makeText(getApplicationContext(), CategorieName[position], Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), ListeName[position], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
