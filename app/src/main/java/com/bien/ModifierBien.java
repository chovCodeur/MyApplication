package com.bien;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.MainActivity;
import com.application.inventaire.R;
import com.categorie.Categorie;
import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.liste.Liste;
import com.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tristan on 11/11/2017.
 */

/**
 * Classe servant à modifier les informations que contient l'application pour un bien en particulier.
 */
public class ModifierBien extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variables de classe
    private Menu m;
    private int id = 0;
    private BienDAO bdao;
    private ListeDAO ldao;
    private CategorieDAO categorieDAO;
    private ListeDAO listeDAO;
    private Bien bien;
    private EditText nomBien;
    private EditText dateAchat;
    private EditText descriptionBien;
    private EditText commentaireBien;
    private EditText numeroSerie;
    private EditText prixBien;
    private String dateSaisie;
    private ImageView photoPrincipale;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ArrayList<Liste> listes;
    private ArrayList<String> nomListes = new ArrayList<>();
    ;
    private Spinner spinnerCategorie;
    private ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    private Categorie categorieSelectionne;
    private int idCategorieSelectionne;
    private ArrayList<Integer> idPrevListes = new ArrayList<>();
    private ArrayList<Integer> idNouvListes = new ArrayList<>();
    private ArrayList<String> listeCategorieName = new ArrayList<String>();
    private CheckedTextView ctvliste1 = null;
    private CheckedTextView ctvliste2 = null;
    private CheckedTextView ctvliste3 = null;
    private String regexDate;
    private DatePickerDialog datePickerDialog;
    private Boolean perm = false;
    private final static int SELECT_IMAGE = 1;
    private final static int SELECT_PDF = 2;
    private final static int TAKE_IMAGE = 3;
    private final static int CHECK_PERM_PICTURE = 4;
    private final static int CHECK_PERM_PDF = 5;
    private final static int CHECK_TAKE_PHOTO = 6;

    private Uri uriImagePrise;

    private Utils utils;


    /**
     * Procédure lancée à la création de l'activité.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_bien);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        // Création de la classe utilitaire et récupération de la Regexp de contrôle du format des dates
        utils = new Utils(this);
        regexDate = utils.getRegexDate();

        // Instanciation des DAO nécessaires
        bdao = new BienDAO(this);
        ldao = new ListeDAO(this);
        listeDAO = new ListeDAO(this);
        categorieDAO = new CategorieDAO(this);

        // Récupération des paramètres en provenance des autres activités
        Bundle extras = getIntent().getExtras();

        // S'il y a des paramètres, on stocke l'id du bien et de sa catégorie courante
        if (extras != null) {
            id = extras.getInt("IDBIEN");
            idCategorieSelectionne = extras.getInt("IDCATEGORIE");

            // Si l'id du bien n'est pas nul
            if (id != 0) {
                // On ouvre le DAO et on récupère le bien bien son id
                bdao.open();
                bien = bdao.getBien(id);

                // On récupère les id des listes d'appartenance du bien dans la table Appartient
                idPrevListes = bdao.getAllIdListeByIdBien(bien.getId_bien());

                // On récupère le nom des listes dans lequel le bien existe grâce aux id récupérés
                // et on les ajoute à notre liste éphémère
                ldao.open();
                for (int i = 0; i < idPrevListes.size(); i++) {
                    if (idPrevListes.get(i) == 1) {
                        String nom = ldao.getNomListeById(1);
                        nomListes.add(nom);
                    }
                    if (idPrevListes.get(i) == 2) {
                        String nom = ldao.getNomListeById(2);
                        nomListes.add(nom);
                    }
                    if (idPrevListes.get(i) == 3) {
                        String nom = ldao.getNomListeById(3);
                        nomListes.add(nom);
                    }
                }
                ldao.close();
                bdao.close();

                // On ajoute le nom du bien en tant que titre de notre toolbar
                myToolbar.setTitle(bien.getNom_bien());
                setSupportActionBar(myToolbar);

                // On récupère toutes les listes de l'application
                listeDAO.open();
                listes = listeDAO.getallListe();
                listeDAO.close();

                spinnerCategorie = (Spinner) findViewById(R.id.select_categorie);
                spinnerCategorie.setOnItemSelectedListener(this);

                // On récupère toutes les catégories de l'application
                categorieDAO.open();
                categoriesList = categorieDAO.getAllCategorie();
                categorieDAO.close();

                // On récupère et on stocke le nom des catégories dans une liste
                int i = 0;
                for (Categorie categorie : categoriesList) {
                    listeCategorieName.add(i, categorie.getNom_Categorie());
                    i++;
                }

                // On utilise la liste fraîchement créée pour la mettre dans le spinner des catégories
                ArrayAdapter arrayAdapterListe = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listeCategorieName);
                arrayAdapterListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategorie.setAdapter(arrayAdapterListe);

                // On sélectionne la catégorie dans laquelle le bien est avant d'éventuellement la modifier
                categorieDAO.open();
                String nomCategorie = categorieDAO.getNomCategorieByIdCategorie(idCategorieSelectionne);

                // On boucle dans les éléments du spinner
                for (i = 0; i < spinnerCategorie.getCount(); i++) {
                    // Lorsque l'on trouve la bonne catégorie dans le spinner, on la sélectionne
                    if (spinnerCategorie.getItemAtPosition(i).toString().equals(nomCategorie)) {
                        spinnerCategorie.setSelection(i);
                        categorieDAO.close();
                    }
                }

                // On récupère la première checkbox, on lui donne le nom de la première liste et on lui ajoute un listener pour cocher/décocher
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

                // On récupère la deuxième checkbox, on lui donne le nom de la deuxième liste et on lui ajoute un listener pour cocher/décocher
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

                // On récupère la troisième checkbox, on lui donne le nom de la troisième liste et on lui ajoute un listener pour cocher/décocher
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

                // On coche les listes dans lesquelles le bien figure déjà
                for (i = 0; i < idPrevListes.size(); i++) {
                    if (idPrevListes.get(i) == 1) {
                        ctvliste1.setChecked(true);
                    }
                    if (idPrevListes.get(i) == 2) {
                        ctvliste2.setChecked(true);
                    }
                    if (idPrevListes.get(i) == 3) {
                        ctvliste3.setChecked(true);
                    }
                }

                // On met à jour le nom du bien
                nomBien = (EditText) findViewById(R.id.nom_bien);
                nomBien.setText(bien.getNom_bien());

                // On met à jour la date d'achat du bien
                dateAchat = (EditText) findViewById(R.id.date_achat_bien);
                dateAchat.setText(bien.getDate_achat_bien());

                // Ajout d'un listener sur la date d'achat pour la modifier grâce à un DatePicker
                dateAchat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(ModifierBien.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String day;
                                if (dayOfMonth < 10) {
                                    day = "0" + dayOfMonth;
                                } else {
                                    day = String.valueOf(dayOfMonth);
                                }

                                String monthFormat;
                                if ((month + 1) < 10) {
                                    monthFormat = "0" + (month + 1);
                                } else {
                                    monthFormat = String.valueOf(month + 1);
                                }
                                switch (utils.getLocale()) {
                                    case "US" : dateAchat.setText(monthFormat + "/" + day + "/" + year);
                                        break;
                                    default : dateAchat.setText(day + "/" + monthFormat + "/" + year);
                                        break;
                                }
                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                // On met à jour la description du bien
                descriptionBien = (EditText) findViewById(R.id.description_bien);
                descriptionBien.setText(bien.getDescription_bien());

                // On met à jour le commentaire du bien
                commentaireBien = (EditText) findViewById(R.id.commentaire_bien);
                commentaireBien.setText(bien.getCommentaire_bien());

                // On met à jour le numéro de série du bien
                numeroSerie = (EditText) findViewById(R.id.numero_serie);
                numeroSerie.setText(bien.getNumeroSerie_bien());

                // On met à jour le prix du bien
                prixBien = (EditText) findViewById(R.id.prix_bien);
                prixBien.setText(String.valueOf(bien.getPrix_bien()));

                // On récupère la date actuelle et on l'affecte à la date de saisie
                Date actuelle = new Date();
                //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat dateFormat = new SimpleDateFormat(utils.getDateSimpleDateFormat());
                dateSaisie = dateFormat.format(actuelle);
                Log.d("FORMAT", dateSaisie);

                // Affichage des photos lorsqu'elles existent pour le bien
                if (bien.getPhoto_bien_principal() != null && !bien.getPhoto_bien_principal().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_principal());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);
                        photoPrincipale.setImageBitmap(myBitmap);
                        // On ajoute un listener pour supprimer la photo lorsque l'utilisateur clique dessus
                        photoPrincipale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), photoPrincipale);
                            }
                        });
                    }
                }

                if (bien.getPhoto_bien_miniature1() != null && !bien.getPhoto_bien_miniature1().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_miniature1());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photo1 = (ImageView) findViewById(R.id.photo1);
                        photo1.setImageBitmap(myBitmap);
                        // On ajoute un listener pour supprimer la photo lorsque l'utilisateur clique dessus
                        photo1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), photo1);
                            }
                        });
                    }
                }

                if (bien.getPhoto_bien_miniature2() != null && !bien.getPhoto_bien_miniature2().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_miniature2());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photo2 = (ImageView) findViewById(R.id.photo2);
                        photo2.setImageBitmap(myBitmap);
                        // On ajoute un listener pour supprimer la photo lorsque l'utilisateur clique dessus
                        photo2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), photo2);
                            }
                        });
                    }
                }

                if (bien.getPhoto_bien_miniature3() != null && !bien.getPhoto_bien_miniature3().equals("")) {
                    final File imgFile = new File(bien.getPhoto_bien_miniature3());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photo3 = (ImageView) findViewById(R.id.photo3);
                        photo3.setImageBitmap(myBitmap);
                        // On ajoute un listener pour supprimer la photo lorsque l'utilisateur clique dessus
                        photo3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), photo3);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * Méthode permettant d'effectuer la modification du bien dans la base de données.
     *
     * @param v une View
     */
    public void modifierBien(View v) {
        Boolean erreurSaisieDate = false;
        String dateAchatSaisie = dateAchat.getText().toString();

        // Remonter une erreur si la date saisie est incorrecte
        if (dateAchatSaisie != null && !dateAchatSaisie.equals("")) {
            if (!dateAchatSaisie.matches(regexDate)) {
                Toast.makeText(this, R.string.date_format_message, Toast.LENGTH_SHORT).show();
                erreurSaisieDate = true;
            }

        }

        // On procède à la modification seulement si le nom du bien n'est pas vide, que la date saisie est valide et que le bien est dans au moins une liste
        if (!nomBien.getText().toString().trim().equals("") && isACheckboxIsChecked() && !erreurSaisieDate) {
            idCategorieSelectionne = categorieSelectionne.getId_Categorie();
            bdao.open();

            // On créé un nouveau bien avec les champs renseignés par l'utilisateur puis on modifie le bien dans la base
            Bien updateBien = new Bien(bien.getId_bien(), nomBien.getText().toString(), dateSaisie, dateAchatSaisie, bien.getFacture_bien(),
                    commentaireBien.getText().toString(), prixBien.getText().toString(), bien.getPhoto_bien_principal(), bien.getPhoto_bien_miniature1(),
                    bien.getPhoto_bien_miniature2(), bien.getPhoto_bien_miniature3(), idCategorieSelectionne, descriptionBien.getText().toString(), numeroSerie.getText().toString());
            bdao.modBien(updateBien);

            // On récupère les nouvelles listes d'appartenance
            if (ctvliste1.isChecked()) {
                idNouvListes.add(1);
            }
            if (ctvliste2.isChecked()) {
                idNouvListes.add(2);
            }
            if (ctvliste3.isChecked()) {
                idNouvListes.add(3);
            }

            // On supprime toutes les listes d'appartenance dans la table Appartient
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

            // On recréé les entrées de la table Appartient en fonction des listes choisies
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
            Toast toast = Toast.makeText(this, R.string.control_item_name, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Procédure gérant l'action du bouton physique retour du téléphone.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Méthode permettant d'assigner le menu et ses options à l'activité.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        m = menu;

        return true;
    }

    /**
     * Méthode permettant de faire différentes actions suivant le bouton du menu cliqué.
     *
     * @param item du menu
     * @return un booléen indiquant quel item a été sélectionné.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
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

    /**
     * Méthode permettant de récupérer l'id de la catégorie sélectionnée dans le spinner.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorieSelectionne = categoriesList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    /**
     * Fonction permettant de savoir si le bien appartient au moins à une liste ou non.
     *
     * @return un booléen contenant true si le bien appartient à au moins une liste, false sinon.
     */
    public boolean isACheckboxIsChecked() {
        if (ctvliste1.isChecked() || ctvliste2.isChecked() || ctvliste3.isChecked()) {
            return true;
        }
        return false;
    }

    /**
     * Méthode permettant de supprimer une photo pour le bien.
     *
     * @param fileName String contenant le chemin d'accès de la photo sur le téléphone.
     * @param photo    View permettant de savoir quelle photo il faut supprimer.
     */
    public void supprimerPhoto(String fileName, View photo) {
        final ImageView view = (ImageView) photo;

        TextView supprimerImage = new TextView(this);
        supprimerImage.setText(getResources().getString(R.string.delete_photo_approval) + fileName + " ?");

        LinearLayout layout = new LinearLayout(this);
        layout.addView(supprimerImage);

        // On règle les marges du layout
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) supprimerImage.getLayoutParams();
        params.leftMargin = 100;
        params.rightMargin = 100;
        params.topMargin = 50;
        layout.setLayoutParams(params);

        // On construit un AlertDialog permettant de récupérer le choix de l'utilisateur
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.title_delete_photo));
        builder.setView(layout);

        // S'il clique sur le bouton "Oui"
        builder.setPositiveButton(R.string.ajouter_bien_dialog_positive_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Suivant le tag de la vue sur laquelle il a cliqué, on supprime la photo correspondante
                        switch (view.getTag().toString()) {
                            case "principal":
                                photoPrincipale.setImageBitmap(null);
                                bien.setPhoto_bien_principal("");
                                break;
                            case "1":
                                photo1.setImageBitmap(null);
                                bien.setPhoto_bien_miniature1("");
                                break;
                            case "2":
                                photo2.setImageBitmap(null);
                                bien.setPhoto_bien_miniature2("");
                                break;
                            case "3":
                                photo3.setImageBitmap(null);
                                bien.setPhoto_bien_miniature3("");
                                break;
                        }

                    }
                });

        // S'il clique sur non, on ne fait rien
        builder.setNegativeButton(R.string.ajouter_bien_dialog_negative_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Méthoder permettant de modifier la facture du bien
     *
     * @param v une View
     */
    public void modifierFacture(View v) {
        // On vérifie si l'application peut accéder à la mémoire du téléphone
        verifierPermission(CHECK_PERM_PDF);
        // Si oui, on invoque la méthode permettant de récupérer une facture
        if (perm) {
            recupererFacture();
        }
    }

    /**
     * Permet d'appeler la méthode de verification des permissions en fonction de l'action utilisateur
     *
     * @param code
     */
    public void verifierPermission(int code) {
        if (ContextCompat.checkSelfPermission(ModifierBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ModifierBien.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        } else {
            perm = true;
        }
    }

    /**
     * Pour lancer l'activité de récuperation de facture
     */
    public void recupererFacture() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, SELECT_PDF);

    }

    /**
     * Quand on revient sur l'activité d'ajout de bien
     *
     * @param request
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        //SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        SimpleDateFormat s = new SimpleDateFormat(utils.getDateStockageFichier());

        // si on vient d'aller chercher un PDF
        if (resultCode == RESULT_OK && request == SELECT_PDF) {

            String name = getRealPathFromUriPDF(data.getData());
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            if (path != null && !path.equals("")) {
                String format = s.format(new Date());
                String pathPdf = saveFile(path + "/" + name, format.toString(), "pdf");
                bien.setFacture_bien(pathPdf);
            }

            TextView tv_pathPdf = (TextView) findViewById(R.id.pathPdf);
            tv_pathPdf.setText(getResources().getString(R.string.pdf_file) + name);

        }

        int numPhoto = getFirstNullPicture();
        // si on vient de selectionner ou de prendre une photo

        if (resultCode == RESULT_OK && (request == SELECT_IMAGE || request == TAKE_IMAGE)) {
            String path;

            // on récupère le path
            if (request == SELECT_IMAGE) {
                path = getRealPathFromUri(data.getData());
            } else {
                path = getRealPathFromUri(uriImagePrise);
            }

            final ImageView imagePhotoPrincipale;
            final ImageView imagePhoto1;
            final ImageView imagePhoto2;
            final ImageView imagePhoto3;
            Bitmap myBitmap;
            String format = s.format(new Date());

            final File imgFile;


            if (path != null && !path.equals("")) {
                switch (numPhoto) {
                    case 0:
                        // en fonction de la première place disponible
                        imagePhotoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);
                        final String pathPhotoPrincipale;

                        // on enregistre la photo dans la mémoire interne
                        pathPhotoPrincipale = saveFile(path, format.toString(), "img");
                        imgFile = new File(pathPhotoPrincipale);
                        if (imgFile.exists()) {
                            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imagePhotoPrincipale.setImageBitmap(myBitmap);
                        }

                        // pour supprimer la photo
                        imagePhotoPrincipale.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), imagePhotoPrincipale);
                            }
                        });

                        bien.setPhoto_bien_principal(pathPhotoPrincipale);

                        break;


                    case 1:

                        imagePhoto1 = (ImageView) findViewById(R.id.photo1);
                        final String pathPhoto1;
                        // on enregistre la photo dans la mémoire interne
                        pathPhoto1 = saveFile(path, format.toString(), "img");
                        imgFile = new File(pathPhoto1);
                        if (imgFile.exists()) {
                            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imagePhoto1.setImageBitmap(myBitmap);
                        }

                        // pour supprimer la photo
                        imagePhoto1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), imagePhoto1);
                            }
                        });

                        bien.setPhoto_bien_miniature1(pathPhoto1);

                        break;
                    case 2:
                        imagePhoto2 = (ImageView) findViewById(R.id.photo2);
                        final String pathPhoto2;

                        // on enregistre la photo dans la mémoire interne
                        pathPhoto2 = saveFile(path, format.toString(), "img");
                        imgFile = new File(pathPhoto2);
                        if (imgFile.exists()) {
                            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imagePhoto2.setImageBitmap(myBitmap);
                        }

                        // pour supprimer la photo
                        imagePhoto2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), imagePhoto2);
                            }
                        });

                        bien.setPhoto_bien_miniature2(pathPhoto2);
                        break;

                    case 3:

                        imagePhoto3 = (ImageView) findViewById(R.id.photo3);
                        final String pathPhoto3;

                        // on enregistre la photo dans la mémoire interne
                        pathPhoto3 = saveFile(path, format.toString(), "img");
                        imgFile = new File(pathPhoto3);
                        if (imgFile.exists()) {
                            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imagePhoto3.setImageBitmap(myBitmap);
                        }

                        // pour supprimer la photo
                        imagePhoto3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                supprimerPhoto(imgFile.getName(), imagePhoto3);
                            }
                        });

                        bien.setPhoto_bien_miniature3(pathPhoto3);

                        break;
                }
            }
        }
    }


    /**
     * Permet de demarrer l'activité pour la récupération de photo depuis la gallerie
     */
    public void recupererPhoto() {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_IMAGE);

    }


    /**
     * Pour récuperer le path d'origine d'une photo
     *
     * @param contentUri
     * @return path
     */
    public String getRealPathFromUri(Uri contentUri) {
        String result = "";

        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);

        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (idx != -1) {
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    /**
     * Pour récuperer le path d'origine d'un PDF
     *
     * @param contentUri
     * @return path
     */
    public String getRealPathFromUriPDF(Uri contentUri) {
        String result = "";

        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);

        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (idx != -1) {
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    /**
     * Pour sauvegarder un fichier dans la mémoire interne de l'appareil
     *
     * @param pathFichierOrigine
     * @param nomNouveauFichier
     * @param type
     * @return path
     */
    private String saveFile(String pathFichierOrigine, String nomNouveauFichier, String type) {

        String separator = "/";
        File dir = null;

        // si c'est une image, on crée le repertoire image
        if (type.equals("img")) {
            String dirName = "images";
            dir = new File(this.getFilesDir() + separator + dirName);

            if (!dir.exists()) {
                dir.mkdir();
            }

            // si c'est un pdf, on crée le repertoire factures
        } else if (type.equals("pdf")) {
            String dirName = getResources().getString(R.string.bills_directory_name);
            dir = new File(this.getFilesDir() + separator + dirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
        }

        File fileSrc = new File(pathFichierOrigine);
        File fileDest = new File(dir.getAbsolutePath() + separator + nomNouveauFichier);

        if (fileDest.exists()) {
            fileDest.delete();
        }

        if (fileSrc.exists()) {
            try {
                if (type.equals("img")) {
                    // c'est une image, on copie et on compresse le fichier destination
                    copy(fileSrc, fileDest);
                    compressImage(fileDest);
                } else {
                    // sinon, c'est une facture, on copie vers le fichier destination
                    copy(fileSrc, fileDest);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return fileDest.getAbsolutePath();

    }

    /**
     * Pour copier une fichier source vers un fichier de destination
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {

        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // On transfert le byte par le buffer
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    /**
     * Méthode appelée après la verification des permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CHECK_PERM_PDF) {
            // appelé avant l'ouverture d'un PDF
            if (ContextCompat.checkSelfPermission(ModifierBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                perm = true;
                recupererPhoto();
            }
            // appelé avant l'ouverture d'une PHOTO
        } else if (requestCode == CHECK_PERM_PICTURE) {
            if (ContextCompat.checkSelfPermission(ModifierBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                perm = true;
                recupererFacture();
            }
            // appelé avant la prise d'une photo par la caméra
        } else if (requestCode == CHECK_TAKE_PHOTO) {
            if (ContextCompat.checkSelfPermission(ModifierBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                perm = true;
                prendrePhoto(null);
            }
        }

        // si l'utilisateur n'a pas accepté
        if (!perm) {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Méthode appelée pour la modification des photos depuis la gallerie
     *
     * @param v la vue
     */
    public void modifierPhoto(View v) {
        verifierPermission(CHECK_PERM_PDF);
        int numPhoto = getFirstNullPicture();
        if (perm) {
            // si le nombre de photo est < 4 et que les permissions le permettent
            if (numPhoto != 4) {
                recupererPhoto();
            } else {
                Toast.makeText(this, R.string.delete_photo_bien, Toast.LENGTH_LONG).show();

            }
        }
    }

    /**
     * Permet de retourner la première place disponible pour une photo (prise de puis la caméra ou la gallerie
     * Retourne 4 s'il n'y a plus de place
     *
     * @return premierePlaceDisponible
     */
    public int getFirstNullPicture() {
        if (bien.getPhoto_bien_principal() == null || bien.getPhoto_bien_principal().equals("")) {
            return 0;
        } else if (bien.getPhoto_bien_miniature1() == null || bien.getPhoto_bien_miniature1().equals("")) {
            return 1;
        } else if (bien.getPhoto_bien_miniature2() == null || bien.getPhoto_bien_miniature2().equals("")) {
            return 2;
        } else if (bien.getPhoto_bien_miniature3() == null || bien.getPhoto_bien_miniature3().equals("")) {
            return 3;
        } else {
            return 4;
        }
    }


    /**
     * Pour compresser une image
     *
     * @param file
     * @return file
     */
    public File compressImage(File file) {
        try {

            // Pour parametrer la compression
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;

            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // Compression du flux
            final int REQUIRED_SIZE = 80;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // nouvelle compression
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // Compression du flux
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Pour lancer l'activité de prise de photo
     */
    public void prendrePhoto(View view) {
        verifierPermission(CHECK_TAKE_PHOTO);
        if (perm) {
            // si il reste de la place pour une photo
            if (getFirstNullPicture() < 4) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, getResources().getString(R.string.new_picture));
                values.put(MediaStore.Images.Media.DESCRIPTION, getResources().getString(R.string.from_camera));
                // on demarrer la nouvelle activité
                uriImagePrise = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePrise);
                startActivityForResult(intent, TAKE_IMAGE);
            } else {
                Toast.makeText(this, R.string.number_pictures_max, Toast.LENGTH_LONG).show();
            }
        }
    }
}