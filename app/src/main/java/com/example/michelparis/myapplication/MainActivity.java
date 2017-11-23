package com.example.michelparis.myapplication;

import android.content.Intent;
import android.database.Cursor;
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
    private BienDAO bdao;
    private PersonneDAO pdao;
    private int idCurrentList=1;

    //int id_bien, String nom_bien, String date_saisie_bien, String date_achat_bien,
    // String commentaire_bien, float prix_bien, int id_categorie_bien, String description_bien, String numeroSerie_bien
    Bien bien1 = new Bien(1,"bien1","19/11/2017","","","",0f,null,null,null,null,1,"ddddd","");
    Bien bien2 = new Bien(2,"bien2","19/11/2017","","","",0f,null,null,null,null,1,"aaaaa","");
    Bien bien3 = new Bien(3,"bien3","19/11/2017","","","",0f,null,null,null,null,1,"vbbbb","");
    Bien bien4 = new Bien(4,"bien4","19/11/2017","","","",0f,null,null,null,null,1,"ezezeez","");
    Bien bien5 = new Bien(5,"bien5","19/11/2017","","","",0f,null,null,null,null,2,"zeezfefz","");
    Bien bien6 = new Bien(6,"bien6","19/11/2017","","","",0f,null,null,null,null,2,"zeezez","");

    //int id_Categorie, String categorie, String description
    Categorie categorie1 = new Categorie(1, "Categorie 1","");
    Categorie categorie2 = new Categorie(2, "Categorie 2","");

    ArrayList<Bien> listeBiens = new ArrayList<Bien>();
    HashMap<Integer, Integer> listCorrespondance = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.toolbarTitle));
        //myToolbar.setTitle("Inventaire personnel");
        setSupportActionBar(myToolbar);

        lv_listeBiens = (ListView) findViewById(R.id.listeBiens);

        pdao = new PersonneDAO(this);
        bdao = new BienDAO(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            idCurrentList = extras.getInt("IDLISTE");
            refreshAdapterView();
        }

        pdao.open();
        pdao.modPersonne(1, "nom", "prenom", "12/04/1995", "adresse", "mail", "0607");
        pdao.close();

        listeBiens.add(bien1);
        listeBiens.add(bien2);
        listeBiens.add(bien3);
        listeBiens.add(bien4);
        listeBiens.add(bien5);
        listeBiens.add(bien6);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*View headerview = navigationView.getHeaderView(0);

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        //refreshAdapterView();

        Intent intent = new Intent(this, InfosBien.class);
        intent.putExtra("IDBIEN", 1);
        startActivity(intent);

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
            idCurrentList = 1;
            refreshAdapterView();
        }

        if (id == R.id.liste2) {
            idCurrentList = 2;
            refreshAdapterView();
        }

        if (id == R.id.liste3) {
            idCurrentList = 3;
            refreshAdapterView();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // A UTILISER A CHAQUE AJOUT DE BIEN OU DE CATEGORIE
    public void refreshAdapterView() {

        // On détruit l'affichage courant
        lv_listeBiens.destroyDrawingCache();
        lv_listeBiens.setVisibility(ListView.INVISIBLE);
        lv_listeBiens.setVisibility(ListView.VISIBLE);
        mAdapter = new BienAdapter(this);
        // ici clear de la liste des biens
        listeBiens.clear();
        listCorrespondance.clear();

        // Ouverture du BienDAO, on retrouve la liste des biens de la liste désignée et on ferme le DAO
        bdao.open();
        // Récupération de la liste en fonction l'id
        listeBiens = bdao.getBiensByListe(idCurrentList);
        bdao.close();

        // On refait la bonne liste de correspondance
        mAdapter.addSectionHeaderItem("Catégorie : "+listeBiens.get(0).getId_categorie_bien());
        int cpt = 0;
        int idCat = 1;
        String item;
        for (int i = 0; i < listeBiens.size(); i++) {

            if (idCat!=listeBiens.get(i).getId_categorie_bien()) {
                cpt++;
                mAdapter.addSectionHeaderItem("Catégorie : "+listeBiens.get(i).getId_categorie_bien());

            }
            listCorrespondance.put(mAdapter.getCount(),Integer.valueOf(cpt));
            item=listeBiens.get(i).getNom_bien()+"#~#"+listeBiens.get(i).getDescription_bien();
            mAdapter.addItem(item);
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

        // On refait un nouvel adapteur et on le set sur la liste
       // mAdapter = new BienAdapter(this);
       // lv_listeBiens.setAdapter(mAdapter);
    }

    public void ajouterCategorie(View v) {
        Log.d("TEST", "Coucou du bouton 1");
    }

    public void exporterListe(View v) {
        Log.d("TEST", "Coucou du bouton 2");
    }

    public void modifierPersonne(View v) {
        Intent intent = new Intent(this, ModifierPersonne.class);
        startActivity(intent);
    }

}
