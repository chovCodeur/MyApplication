package com.bien;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.application.MainActivity;
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

    private Menu m;
    private int id=0;
    private BienDAO bdao;
    private ListeDAO ldao;
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
    private ArrayList<String> nomListes = new ArrayList<>();;
    private Spinner spinnerCategorie;
    private ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    private Categorie categorieSelectionne;
    private int idCategorieSelectionne;
    private Boolean dansListe1 = false;
    private Boolean dansListe2 = false;
    private Boolean dansListe3 = false;
    private int idListe=0;
    private ArrayList<Integer> idlistes = new ArrayList<>();
    private CheckedTextView ctvliste1=null;
    private CheckedTextView ctvliste2=null;
    private CheckedTextView ctvliste3=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_bien);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        bdao = new BienDAO(this);
        ldao = new ListeDAO(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            id = extras.getInt("IDBIEN");

            if(id != 0) {
                bdao.open();
                bien = bdao.getBien(id);

                // récupérer les id des listes d'appartenance du bien dans la table Appartient
                idlistes = bdao.getAllIdListeByIdBien(bien.getId_bien());

                // récupérer le nom des listes dans lequel le bien existe
                ldao.open();
                for(int i=0;i<idlistes.size();i++) {
                    if(idlistes.get(i) == 1) {
                        String nom = ldao.getNomListeById(1);
                        nomListes.add(nom);
                    }
                    if(idlistes.get(i) == 2) {
                        String nom = ldao.getNomListeById(2);
                        nomListes.add(nom);
                    }
                    if(idlistes.get(i) == 3) {
                        String nom = ldao.getNomListeById(3);
                        nomListes.add(nom);
                    }
                }
                ldao.close();
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

                ctvliste1 = (CheckedTextView) findViewById(R.id.checkListe1);
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


                ctvliste2 = (CheckedTextView) findViewById(R.id.checkListe2);
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

                ctvliste3 = (CheckedTextView) findViewById(R.id.checkListe3);
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

                for(i=0;i<idlistes.size();i++) {
                    if(idlistes.get(i) == 1) {
                        ctvliste1.setChecked(true);
                    }
                    if(idlistes.get(i) == 2) {
                        ctvliste2.setChecked(true);
                    }
                    if(idlistes.get(i) == 3) {
                        ctvliste3.setChecked(true);
                    }
                }

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

        if(dansListe1) {
            idListe = 1;
        }
        if(dansListe2) {
            idListe = 2;
        }
        if(dansListe3) {
            idListe = 3;
        }

        bdao.modifierListeAppartenance(bien.getId_bien(), idListe);

        bdao.close();

        finish();
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
                Intent intenthome = new Intent(getApplicationContext(), MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);

                return true;

            case R.id.plus:
                intent = new Intent(this, AjouterBien.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
        categorieSelectionne = categoriesList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
