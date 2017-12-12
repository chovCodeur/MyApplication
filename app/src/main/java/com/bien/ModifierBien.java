package com.bien;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.MainActivity;
import com.categorie.Categorie;
import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.application.inventaire.R;
import com.liste.Liste;

import java.io.File;
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
    private ImageView photoPrincipale;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ListeDAO listeDAO;
    private ArrayList<Liste> listes;
    private ArrayList<String> nomListes = new ArrayList<>();;
    private Spinner spinnerCategorie;
    private ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    private Categorie categorieSelectionne;
    private int idCategorieSelectionne;
    private ArrayList<Integer> idPrevListes = new ArrayList<>();
    private ArrayList<Integer> idNouvListes = new ArrayList<>();
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
                idPrevListes = bdao.getAllIdListeByIdBien(bien.getId_bien());

                // récupérer le nom des listes dans lequel le bien existe
                ldao.open();
                for(int i=0;i<idPrevListes.size();i++) {
                    if(idPrevListes.get(i) == 1) {
                        String nom = ldao.getNomListeById(1);
                        nomListes.add(nom);
                    }
                    if(idPrevListes.get(i) == 2) {
                        String nom = ldao.getNomListeById(2);
                        nomListes.add(nom);
                    }
                    if(idPrevListes.get(i) == 3) {
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
                        } else {
                            ctvliste1.setChecked(true);
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
                        } else {
                            ctvliste2.setChecked(true);
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
                        } else {
                            ctvliste3.setChecked(true);
                        }
                    }
                });

                for(i=0;i<idPrevListes.size();i++) {
                    if(idPrevListes.get(i) == 1) {
                        ctvliste1.setChecked(true);
                    }
                    if(idPrevListes.get(i) == 2) {
                        ctvliste2.setChecked(true);
                    }
                    if(idPrevListes.get(i) == 3) {
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

                // Affichage des photos
                if(bien.getPhoto_bien_principal() != null && !bien.getPhoto_bien_principal().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_principal());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);
                        photoPrincipale.setImageBitmap(myBitmap);
                        photoPrincipale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile, photoPrincipale);
                            }
                        });
                    }
                }

                if(bien.getPhoto_bien_miniature1() != null && !bien.getPhoto_bien_miniature1().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_miniature1());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photo1 = (ImageView) findViewById(R.id.photo1);
                        photo1.setImageBitmap(myBitmap);
                        photo1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile, photo1);
                            }
                        });
                    }
                }

                if(bien.getPhoto_bien_miniature2() != null && !bien.getPhoto_bien_miniature2().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_miniature2());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photo2 = (ImageView) findViewById(R.id.photo2);
                        photo2.setImageBitmap(myBitmap);
                        photo2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile, photo2);
                            }
                        });
                    }
                }

                if(bien.getPhoto_bien_miniature3() != null && !bien.getPhoto_bien_miniature3().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_miniature3());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photo3 = (ImageView) findViewById(R.id.photo3);
                        photo3.setImageBitmap(myBitmap);
                        photo3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile, photo3);
                            }
                        });
                    }
                }
            }
        }
    }

    public void modifierBien(View v) {
        if(!nomBien.getText().toString().equals("")) {
            idCategorieSelectionne = categorieSelectionne.getId_Categorie();
            bdao.open();
            bdao.modBien(bien.getId_bien(), nomBien.getText().toString(), dateSaisie, dateAchat.getText().toString(), commentaireBien.getText().toString(), idCategorieSelectionne,
                    descriptionBien.getText().toString(), Float.valueOf(prixBien.getText().toString()), numeroSerie.getText().toString(), null);

            if (ctvliste1.isChecked()) {
                idNouvListes.add(1);
            }
            if (ctvliste2.isChecked()) {
                idNouvListes.add(2);
            }
            if (ctvliste3.isChecked()) {
                idNouvListes.add(3);
            }

            for (int i = 0; i < idPrevListes.size(); i++) {
                if (idPrevListes.get(i) == 1) {
                    bdao.supprimerListeAppartenance(bien.getId_bien(), 1);
                }
                if (idPrevListes.get(i) == 2) {
                    bdao.supprimerListeAppartenance(bien.getId_bien(), 2);
                }
                if (idPrevListes.get(i) == 3) {
                    bdao.supprimerListeAppartenance(bien.getId_bien(), 3);
                }
            }

            for (int i = 0; i < idNouvListes.size(); i++) {
                if (idNouvListes.get(i) == 1) {
                    bdao.addInAppartient(bien.getId_bien(), 1);
                }
                if (idNouvListes.get(i) == 2) {
                    bdao.addInAppartient(bien.getId_bien(), 2);
                }
                if (idNouvListes.get(i) == 3) {
                    bdao.addInAppartient(bien.getId_bien(), 3);
                }
            }

            bdao.close();

            finish();
        } else {
            Toast toast = Toast.makeText(this, "Votre bien doit avoir un nom", Toast.LENGTH_LONG);
            toast.show();
        }
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

    public boolean isACheckboxIsChecked() {
        if(ctvliste1.isChecked() || ctvliste2.isChecked() || ctvliste3.isChecked()) {
            return true;
        }
        return false;
    }

    public void supprimerPhoto(File file, View photo) {
        final View view = (ImageView) photo;
        TextView supprimerImage = new TextView(this);
        supprimerImage.setText("Voulez-vous vraiment supprimer la photo "+file.getName()+" ?");

        LinearLayout layout = new LinearLayout(this);
        layout.addView(supprimerImage);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) supprimerImage.getLayoutParams();
        params.leftMargin = 100;
        params.rightMargin = 100;
        params.topMargin = 50;
        layout.setLayoutParams(params);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Supprimer une photo");
        builder.setView(layout);

        builder.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (view.getTag().toString()) {
                            case "principal" : photoPrincipale.setImageBitmap(null);
                                bien.setPhoto_bien_principal("");
                                break;
                            case "1" : photo1.setImageBitmap(null);
                                bien.setPhoto_bien_miniature1("");
                                break;
                            case "2" : photo2.setImageBitmap(null);
                                bien.setPhoto_bien_miniature2("");
                                break;
                            case "3" : photo3.setImageBitmap(null);
                                bien.setPhoto_bien_miniature3("");
                                break;
                        }
                        bdao.open();
                        //bdao.modBien();
                        bdao.close();

                    }
                });

        builder.setNegativeButton("Non",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }
}