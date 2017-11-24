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
import android.view.Menu;
import android.view.MenuInflater;
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
    private Menu m;


    ArrayList<Bien> listeBiens = new ArrayList<Bien>();
    HashMap<Integer, Integer> listCorrespondance = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(myToolbar);

        lv_listeBiens = (ListView) findViewById(R.id.listeBiens);

        pdao = new PersonneDAO(this);
        ldao = new ListeDAO(this);
        bdao = new BienDAO(this);
        cdao = new CategorieDAO(this);


        bdao.open();
        if(bdao.compterBienEnBase()<=0){
            remplirBeDeForTest();
        }

        bdao.close();

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
    protected void onResume() {
        super.onResume();
        // on rafraichi simplement l'affichage
        refreshAdapterView();
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

                return true;

            case R.id.plus:

                intent = new Intent(this, AjouterBien.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

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
        cdao.open();
        mAdapter.addSectionHeaderItem("Catégorie : "+cdao.getNomCategorieByIdCategorie(listeBiens.get(0).getId_categorie_bien()));
        cdao.close();
        int cpt = 0;
        int idCat = 1;
        String item;
        for (int i = 0; i < listeBiens.size(); i++) {

            if (idCat!=listeBiens.get(i).getId_categorie_bien()) {
                cpt++;
                cdao.open();
                mAdapter.addSectionHeaderItem("Catégorie : "+cdao.getNomCategorieByIdCategorie(listeBiens.get(i).getId_categorie_bien()));
                cdao.close();

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
                Log.e("id-debugkt",String.valueOf(listeBiens.get((position)-listCorrespondance.get((position))-1)));

                //i.putExtra("IDBIEN", (position)-listCorrespondance.get((position)));
                i.putExtra("IDBIEN", listeBiens.get((position)-listCorrespondance.get((position))-1).getId_bien());
                startActivity(i);
            }
        });
    }

    public void ajouterCategorie(View v) {
        Intent intent = new Intent(this, AjouterCategorie.class);
        startActivity(intent);
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
        bdao.addBien(bien1, 1);
        bdao.addBien(bien2, 1);
        bdao.addBien(bien3, 1);
        bdao.addBien(bien4, 1);
        bdao.addBien(bien5, 1);
        bdao.addBien(bien6, 1);

        Bien bienTemp = bdao.getBien(1);
        bdao.addBien(bienTemp, 2);

        bienTemp = bdao.getBien(2);
        bdao.addBien(bienTemp, 2);

        bienTemp = bdao.getBien(3);
        bdao.addBien(bienTemp, 3);

        bienTemp = bdao.getBien(4);
        bdao.addBien(bienTemp, 3);

        bdao.close();
    }
}