package com.example.michelparis.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class
MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private BienAdapter mAdapter;
    private ListView lv_listeBiens;

    //int id_bien, String nom_bien, String date_saisie_bien, String date_achat_bien,
    // String commentaire_bien, float prix_bien, int id_categorie_bien, String description_bien, String numeroSerie_bien
    Bien bien1 = new Bien(1,"bien1","19/11/2017","","",0f,1,"","");
    Bien bien2 = new Bien(2,"bien2","19/11/2017","","",0f,1,"","");
    Bien bien3 = new Bien(3,"bien3","19/11/2017","","",0f,1,"","");
    Bien bien4 = new Bien(4,"bien4","19/11/2017","","",0f,1,"","");
    Bien bien5 = new Bien(5,"bien5","19/11/2017","","",0f,2,"","");
    Bien bien6 = new Bien(6,"bien6","19/11/2017","","",0f,2,"","");

    //int id_Categorie, String categorie, String description
    Categorie categorie1 = new Categorie(1, "Categorie 1","");
    Categorie categorie2 = new Categorie(2, "Categorie 2","");

    ArrayList<Bien> listeBiens = new ArrayList<Bien>();
    HashMap<Integer, Integer> listCorrespondance = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv_listeBiens = (ListView)findViewById(R.id.listeBiens);

        listeBiens.add(bien1);
        listeBiens.add(bien2);
        listeBiens.add(bien3);
        listeBiens.add(bien4);
        listeBiens.add(bien5);
        listeBiens.add(bien6);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.toolbarTitle));

        myToolbar.setTitle("typuo");
        setSupportActionBar(myToolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        mAdapter = new BienAdapter(this);
        mAdapter.addSectionHeaderItem("Catégorie : "+listeBiens.get(0).getId_categorie_bien());
        int cpt = 0;
        int idCat = 1;
        for (int i = 0; i < listeBiens.size(); i++) {

            if (idCat!=listeBiens.get(i).getId_categorie_bien()) {
                cpt++;
                mAdapter.addSectionHeaderItem("Catégorie : "+listeBiens.get(i).getId_categorie_bien());

            }
            listCorrespondance.put(mAdapter.getCount(),Integer.valueOf(cpt));
            mAdapter.addItem(listeBiens.get(i).getNom_bien());
            idCat=listeBiens.get(i).getId_categorie_bien();
        }
        //listCorrespondance.remove(1);
        Log.d("liste",listCorrespondance.toString());
        lv_listeBiens.setAdapter(mAdapter);
        lv_listeBiens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), InfosBien.class);
                Log.d("id-debugkt",String.valueOf(listCorrespondance.get(position)));
                i.putExtra("IDBIEN", (position)-listCorrespondance.get((position)));
                startActivity(i);
            }
        });

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

    public void testAfficherBien(View view){
        Intent testAfficherBien = new Intent(this, InfosBien.class);
        startActivity(testAfficherBien);
    }

}
