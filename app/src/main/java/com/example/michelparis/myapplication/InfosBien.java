package com.example.michelparis.myapplication;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Rodnor on 11/11/2017.
 */

public class InfosBien extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   // private BienManager bm;
   // private CategorieManager cm;
    private int id = 0;
    private Bien bien;
    private TextView nomBien;
    private TextView categorieBien;
    private TextView descriptionBien;
    private Button facture;
    private TextView dateAcquisition;
    private TextView dateSaisie;
    private TextView prix;
    private TextView numeroSerie;
    private TextView commentaire;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_bien);

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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
        });*/

        bien = new Bien(1, "Lunettes de soleil", "12-11-2017", "23-02-2012", "Ce sont des lunettes de soleil Rayban de type Aviator", 120.99f, 1);

     /*   Bundle extras = getIntent().getExtras();

        if(extras != null) {
            id = (extras.getInt("id"));
        }

        if(id != 0) {
            bm.open();
            bien = bm.getBienById(id);
            bm.close();*/

            // Mise à jour de l'image principale

            // On met à jour le nom du bien
            nomBien = (TextView) findViewById(R.id.nomBien);
            nomBien.setText(bien.getNom_bien());

            // On met à jour le nom de la catégorie du bien
            categorieBien = (TextView) findViewById(R.id.categorieBien);
           // cm.open();
            categorieBien.setText(String.valueOf(bien.getId_categorie_bien()));
            //categorieBien.setText(cm.getNomCategorieById(bien.getId_categorie_bien()));
            //cm.close();

            // On met à jour la description du bien
            //descriptionBien = (TextView) findViewById(R.id.descriptionBien);
           // descriptionBien.setText(bien.getDescription());

            // Affichage de la facture au clic du bouton
            facture = (Button) findViewById(R.id.buttonFactureBien);
            facture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            // Mise à jour des 3 miniatures d'images

            // Mise à jour date d'acquisition
            dateAcquisition = (TextView) findViewById(R.id.dateAcquisitionBien);
            dateAcquisition.setText("Date d'acquisition : "+bien.getDate_achat_bien());

            // Mise à jour date de saisie
            dateSaisie = (TextView) findViewById(R.id.dateSaisieBien);
            dateSaisie.setText("Date de saisie : "+bien.getDate_saisie_bien());

            // Mise à jour prix du bien
            prix = (TextView) findViewById(R.id.prixBien);
            prix.setText("Prix du bien : "+bien.getPrix_bien());

            // Mise à jour numéro de série du bien
           // numeroSerie = (TextView) findViewById(R.id.numeroSerieBien);
            //numeroSerie.setText("Numéro de série : "+bien.getNumeroDeSerie());

            // Mise à jour commentaire du bien
            commentaire = (TextView) findViewById(R.id.commentairesBien);
            commentaire.setText(bien.getCommentaire_bien());
       // }
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
}
