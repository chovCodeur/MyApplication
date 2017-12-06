package com.bien;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.categorie.Categorie;
import com.liste.Liste;
import com.application.MainActivity;
import com.application.inventaire.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Timestamp;
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

    ImageView imagePhotoPrincipale ;
    ImageView imagePhoto1 ;
    ImageView imagePhoto2 ;
    ImageView imagePhoto3 ;

    String pathPhotoPrincipale ;

    int nbPhoto = 0;
    private Context context = this;

    final static int SELECT_PICTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_bien);
        Context context = this;

        if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AjouterBien.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 4);
        }

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

        //Initialise l'imageview on lui met une action
        imagePhotoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);
        imagePhoto1 = (ImageView) findViewById(R.id.photo1);
        imagePhoto2 = (ImageView) findViewById(R.id.photo2);
        imagePhoto3 = (ImageView) findViewById(R.id.photo3);


        Button buttonAjouterPhoto = (Button) findViewById(R.id.ajouterPhoto);

        buttonAjouterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);

            }
        });

    }

    /*
        *Retour du resultat de la galerie
     */
    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        if(resultCode == RESULT_OK && request == SELECT_PICTURE) {
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            String path = getRealPathFromUri(data.getData());

            Log.e("Choix d'image", "uri"+path);
            switch (nbPhoto) {
                case 0 :
                    //bitmapPrincipal = BitmapFactory.decodeFile(path);
                    String format = s.format(new Date());

                    pathPhotoPrincipale = format.toString();
                    if (!savePicture(path, pathPhotoPrincipale)) {
                        pathPhotoPrincipale = null;
                    }

                    nbPhoto++;
                    break;
                case 1 :

                    nbPhoto++;

                    break;
                case 2 :

                    nbPhoto++;

                    break;

                case 3 :

                    nbPhoto++;

                    break;

            }
        }
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


        Log.e("On va"," inserer : "+pathPhotoPrincipale);
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
                idCategorieSelectionne,
                descriptionBien,
                numeroSerie
                );

        BienDAO bienDAO = new BienDAO(this);

        bienDAO.open();

        bienDAO.addBien(bien,1);
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


    private boolean savePicture (String pathFichierOrigine, String nomNouveauFichier) {

        Log.e("MIpa","===============");
        String separator = "/";

        String dirName = "imagesTLS";
        File dir = new File (context.getFilesDir() + separator +dirName);

        if (!dir.exists()) {
            Log.e("MIpa","== CREATION du repertoire");

            dir.mkdir();

        } else {
            Log.e("MIpa","== DIR EJA EXISTANT");

        }

        File fileSrc = new File(pathFichierOrigine);
        Log.e("MIpa","fileSrc"+fileSrc.getAbsolutePath());

        File fileDest = new File(dir.getAbsolutePath()+separator+nomNouveauFichier);

        if (fileDest.exists()){
            fileDest.delete();
        }

        if (fileSrc.exists()) {
            try {
                copy(fileSrc, fileDest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e("MIpa","===============");

        return true;

    }

    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }


}
