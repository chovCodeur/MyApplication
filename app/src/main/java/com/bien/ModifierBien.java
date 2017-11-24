package com.bien;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.categorie.Categorie;
import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.application.inventaire.R;
import com.liste.Liste;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ModifierBien extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int id=0;
    private BienDAO bdao;
    private Bien bien;
    private EditText nomBien;
    private EditText dateAchat;
    private EditText descriptionBien ;
    private EditText commentaireBien;
    private EditText numeroSerie;
    private EditText prixBien;
    private String dateSaisie;
    private ListeDAO listeDAO;
    private ArrayList<Liste> listes;
    private Spinner spinnerCategorie;
    private ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    private Categorie categorieSelectionne;
    private int idCategorieSelectionne;
    private Boolean dansListe1 = false;
    private Boolean dansListe2 = false;
    private Boolean dansListe3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_bien);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        bdao = new BienDAO(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            id = extras.getInt("IDBIEN");

            if(id != 0) {
                bdao.open();
                bien = bdao.getBien(id);

                // récupérer les id des listes d'appartenance du bien dans la table Appartient

                // compter le nombre d'id

                // récupérer le nom des listes dans lequel le bien existe
                /*ldao.open();
                for(int i=0;i<nombreID;i++) {
                    String nom = ldao.getNomListeById(i);
                    if(!nom.equals("")){
                        listes.add(nom);
                    }
                }
                ldao.close();*/

                bdao.close();

                myToolbar.setTitle(bien.getNom_bien());
                setSupportActionBar(myToolbar);

                listeDAO = new ListeDAO(this);
                listeDAO.open();
                listes  = listeDAO.getallListe();
                listeDAO.close();

                spinnerCategorie =(Spinner) findViewById(R.id.select_categorie);
                spinnerCategorie.setOnItemSelectedListener(this);

                ArrayList<String> listeCategorieName = new ArrayList<String>();
                CategorieDAO categorieDAO = new CategorieDAO(this);
                categorieDAO.open();
                categoriesList = categorieDAO.getAllCategorie();
                categorieDAO.close();


                int i =0;
                for (Categorie categorie: categoriesList) {
                    listeCategorieName.add(i,categorie.getNom_Categorie());
                    i++;
                }

                ArrayAdapter arrayAdapterListe = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listeCategorieName);
                arrayAdapterListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategorie.setAdapter(arrayAdapterListe);

                final CheckedTextView ctvliste1 = (CheckedTextView) findViewById(R.id.checkListe1);
                ctvliste1.setText(listes.get(0).getLibelle_liste());
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


                final CheckedTextView ctvliste2 = (CheckedTextView) findViewById(R.id.checkListe2);
                ctvliste2.setText(listes.get(1).getLibelle_liste());
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

                final CheckedTextView ctvliste3 = (CheckedTextView) findViewById(R.id.checkListe3);
                ctvliste3.setText(listes.get(2).getLibelle_liste());
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
                    }
                });

                nomBien = (EditText) findViewById(R.id.nom_bien);
                nomBien.setText(bien.getNom_bien());

                dateAchat = (EditText) findViewById(R.id.date_achat_bien);
                dateAchat.setText(bien.getDate_achat_bien());

                descriptionBien = (EditText) findViewById(R.id.description_bien);
                descriptionBien.setText(bien.getDescription_bien());

                commentaireBien = (EditText) findViewById(R.id.commentaire_bien);
                commentaireBien.setText(bien.getCommentaire_bien());

                numeroSerie = (EditText) findViewById(R.id.numero_serie);
                numeroSerie.setText(bien.getNumeroSerie_bien());

                prixBien = (EditText) findViewById(R.id.prix_bien);
                prixBien.setText(String.valueOf(bien.getPrix_bien()));

                Date actuelle = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dateSaisie = dateFormat.format(actuelle);
            }
        }
    }

    public void modifierBien(View v) {
        idCategorieSelectionne = categorieSelectionne.getId_Categorie();
        bdao.close();
        bdao.modBien(bien.getId_bien(), nomBien.getText().toString(), dateSaisie , dateAchat.getText().toString(), commentaireBien.getText().toString(), idCategorieSelectionne,
        descriptionBien.getText().toString(), Float.valueOf(prixBien.getText().toString()), numeroSerie.getText().toString());
        bdao.close();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
        categorieSelectionne = categoriesList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
