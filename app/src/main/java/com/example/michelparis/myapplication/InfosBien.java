package com.example.michelparis.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Rodnor on 11/11/2017.
 */

public class InfosBien extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_bien);

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
}
