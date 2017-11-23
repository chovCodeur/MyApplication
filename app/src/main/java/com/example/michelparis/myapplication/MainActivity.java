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
    private ListeDAO ldao;
    private PersonneDAO pdao;
    private CategorieDAO cdao;
    private int idCurrentList=1;



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
        ldao = new ListeDAO(this);
        bdao = new BienDAO(this);
        cdao = new CategorieDAO(this);
        cdao.open();
        if (cdao.getNomCategorieByIdBien(1).equals("")) {

            remplirBeDeForTest();
        }
        cdao.close();
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            idCurrentList = extras.getInt("IDLISTE");
            refreshAdapterView();
        }


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

        Intent intent = new Intent(this, AjouterBien.class);
       // startActivity(intent);

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

    public void remplirBeDeForTest () {



        pdao.open();
        pdao.modPersonne(1, "Jacky", "Philippe", "12/04/1995", "10 rue de la verge", "jk.phil@hotmail.com", "0607548796");
        pdao.close();

        Liste liste1 = new Liste(1,"Maison","La liste de ma maison");
        Liste liste2 = new Liste(2,"Garage","La liste de mon garage");
        Liste liste3 = new Liste(3,"Magasin","La liste de mon magasin");

        ldao.open();
        ldao.ajouterListe(liste1);
        ldao.ajouterListe(liste2);
        ldao.ajouterListe(liste3);
        ldao.close();

        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
            Categorie cuisine = new Categorie(0, "Cuisine", "Tous mes objets de la cuisine");
            Categorie salon = new Categorie(0, "Salon", "Tous mes objets du Salon");
            Categorie chambre = new Categorie(0, "Chambre", "Tous mes objets de la chambre");
            categorieDAO.addCategorie(cuisine);
            categorieDAO.addCategorie(salon);
            categorieDAO.addCategorie(chambre);
        categorieDAO.close();

        //int id_bien, String nom_bien, String date_saisie_bien, String date_achat_bien, String facture_bien,
        //String commentaire_bien, float prix_bien, Bitmap photo_bien_principal, Bitmap photo_bien_miniature1,
         //       Bitmap photo_bien_miniature2, Bitmap photo_bien_miniature3, int id_categorie_bien, String description_bien, String numeroSerie_bien

        Bien bien1 = new Bien(1,"Lunette","19/11/2017","21/11/2017","","Légèrement rayées sur le coté",251.6f,null,null,null,null,3,"Lunette de marque Rayban","");
        Bien bien2 = new Bien(2,"Frigo connecté SAMSUNG","19/11/2017","23/11/2017","","",3599.99f,null,null,null,null,1,"Samsung Family Hub","45DG425845DA");
        Bien bien3 = new Bien(3,"Ordinateur portable","19/11/2017","01/12/2017","","Manque une touche",1099.99f,null,null,null,null,2,"PC Portable Gamer de marque MSI","515D-TGH2336");
        Bien bien4 = new Bien(4,"Vaisselle en porcelaine","20/11/2017","03/06/2017","","Vaisselle de Mémé",6902.30f,null,null,null,null,1,"En porcelaine chinoise datée de 1640","");
        Bien bien5 = new Bien(5,"Robot patissier","21/11/2017","19/05/2016","","",350f,null,null,null,null,1,"Marque Kenwood","");
        Bien bien6 = new Bien(6,"Home Cinema","21/11/2017","19/01/2017","","Une enceinte grésille un peu",400f,null,null,null,null,2,"Marque Pioneer","");

        bdao.open();
        bdao.addBien(bien1);
        bdao.addBien(bien2);
        bdao.addBien(bien3);
        bdao.addBien(bien4);
        bdao.addBien(bien5);
        bdao.addBien(bien6);
        bdao.close();
    }

}
