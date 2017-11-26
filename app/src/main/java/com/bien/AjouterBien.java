package com.bien;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.categorie.Categorie;
import com.liste.Liste;
import com.application.MainActivity;
import com.application.inventaire.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AjouterBien extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    Spinner spinnerCategorie;
    Categorie categorieSelectionne;
    Boolean dansListe1 = false;
    Boolean dansListe2 = false;
    Boolean dansListe3 = false;
    private Menu m;

    final static int SELECT_PICTURE = 1;
    ImageView imageVue;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_bien);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Ajouter un bien");
        setSupportActionBar(myToolbar);

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

        ListeDAO listeDAO = new ListeDAO(this);

        listeDAO.open();

        ArrayList<Liste> listes  = listeDAO.getallListe();

        listeDAO.close();

        final CheckedTextView ctvliste1 = (CheckedTextView) findViewById(R.id.checkListe1);

        ctvliste1.setText(listes.get(0).getLibelle_liste());

        Bundle extras = getIntent().getExtras();

        int fromIdListe = 0;
        if(extras != null) {
            fromIdListe = extras.getInt("ID_CURRENT_LIST");
        }

        if (fromIdListe == 1){
            ctvliste1.setChecked(true);
            dansListe1 = true;

        }

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

        if (fromIdListe == 2){
            ctvliste2.setChecked(true);
            dansListe2 = true;
        }

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

        if (fromIdListe == 3){
            ctvliste3.setChecked(true);
            dansListe3 = true;
        }

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

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_youtube);

        //Initialise l'imageview on lui met une action
        imageVue = (ImageView) findViewById(R.id.photoPrincipale);
        imageVue.setImageBitmap(bitmap);

        Button buttonAjouterPhotoPrincipale = (Button) findViewById(R.id.ajouterPhotoPrincipale);

        buttonAjouterPhotoPrincipale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btGalleryClick(v);
            }
        });

    }

    /*
        *Méthode pour ouvrir une galerie d'image
     */
    public void btGalleryClick(View v){

        //creation et ouverture de la boite de dialogue
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selectionnez une image"), SELECT_PICTURE);
    }

    /*
        *Retour du resultat de la galerie
     */
    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch (request) {
                case SELECT_PICTURE :
                    String path = getRealPathFromUri(data.getData());
                    Log.e("Choix d'image", "uri"+path);

                    //Transforme l'image en jpg
                    bitmap = BitmapFactory.decodeFile(path);

                    imageVue.setImageBitmap(bitmap);

                    break;

            }
        }
    }

    /*
    *Methode pour récuperer l'Uri de l'image
     */
    private String getRealPathFromUri(Uri contentUri) {
        String result;

        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);

        if(cursor == null){
            result = contentUri.getPath();
        }else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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

                //intent = new Intent(this, AjouterBien.class);
                //startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
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

        Bien bien = new Bien(0,
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

        BienDAO bienDAO = new BienDAO(this);

        bienDAO.open();

        //bienDAO.addBien(bien);
        Log.e("MiPa",bien.toString());

        bienDAO.close();

        // TODO Faire ajout dans la liste d'appartenance
    }

    public void oncClickAjouterPhotoPrincipale (View view){


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
        categorieSelectionne = categoriesList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
