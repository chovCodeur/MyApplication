package com.bien;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe correspondant à l'activité d'ajout d'un bien
 */

public class AjouterBien extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    private Spinner spinnerCategorie;
    private Categorie categorieSelectionne;
    private Boolean dansListe1 = false;
    private Boolean dansListe2 = false;
    private Boolean dansListe3 = false;
    private String regexDate;
    private Boolean perm = false;

    private ImageView imagePhotoPrincipale;
    private ImageView imagePhoto1;
    private ImageView imagePhoto2;
    private ImageView imagePhoto3;

    private Menu m;

    private String pathPdf = "";
    private String pathPhotoPrincipale = "";
    private String pathPhoto1 = "";
    private String pathPhoto2 = "";
    private String pathPhoto3 = "";
    private DatePickerDialog datePickerDialog;
    private Uri uriImage;


    private ArrayList<Integer> listeIdListe = new ArrayList<Integer>();

    private Context context = this;

    private final static int SELECT_IMAGE = 1;
    private final static int SELECT_PDF = 2;
    private final static int TAKE_IMAGE = 3;

    private final static int CHECK_PERM_PICTURE = 4;
    private final static int CHECK_PERM_PDF = 5;
    private final static int CHECK_TAKE_PICTURE = 6;

    private Utils utils;


    /**
     * Méthode appelée à la creation de l'activité
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("MIPA", "OnCreate#"+pathPhotoPrincipale+"#");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_bien);
        //creation de l'activité

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.toolbar_title_ajouter_bien));
        setSupportActionBar(myToolbar);

        // Création de la classe utilitaire et récupération de la Regexp de contrôle du format des dates
        utils = new Utils(this);
        regexDate = utils.getRegexDate();

        // spinner pour gérer les catégories
        spinnerCategorie = (Spinner) findViewById(R.id.select_categorie);
        spinnerCategorie.setOnItemSelectedListener(this);

        ArrayList<String> listeCategorieName = new ArrayList<String>();
        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
        categoriesList = categorieDAO.getAllCategorie();
        categorieDAO.close();


        int i = 0;
        for (Categorie categorie : categoriesList) {
            listeCategorieName.add(i, categorie.getNom_Categorie());
            i++;
        }

        // on utilise l'adapter pour les spinner
        ArrayAdapter arrayAdapterListe = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listeCategorieName);
        arrayAdapterListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(arrayAdapterListe);

        ListeDAO listeDAO = new ListeDAO(this);
        listeDAO.open();
        ArrayList<Liste> listes = listeDAO.getallListe();
        listeDAO.close();

        // on configure l'affichage des listes qui existent en base
        final CheckedTextView ctvliste1 = (CheckedTextView) findViewById(R.id.checkListe1);
        ctvliste1.setText(listes.get(0).getLibelle_liste());
        Bundle extras = getIntent().getExtras();

        int fromIdListe = 0;
        if (extras != null) {
            fromIdListe = extras.getInt("ID_CURRENT_LIST");
        }

        // si on la connait, on coche par défaut la checkbox qui correspond à la liste d'ou l'on vient
        if (fromIdListe == 1) {
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

        // si on la connait, on coche par défaut la checkbox qui correspond à la liste d'ou l'on vient

        if (fromIdListe == 2) {
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

        // si on la connait, on coche par défaut la checkbox qui correspond à la liste d'ou l'on vient
        if (fromIdListe == 3) {
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


        // pour ajouter une photo depuis la gallerie
        Button buttonAjouterPhoto = (Button) findViewById(R.id.ajouterPhoto);
        buttonAjouterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFirstNullPicture() <= 3) {
                    // si le nombre de photo est < 4 et que les permissions le permettent
                    verifierPermission(CHECK_PERM_PICTURE);
                    if (perm) {
                        recupererPhoto();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.delete_photo_bien, Toast.LENGTH_LONG).show();
                }
            }
        });

        // pour ajouter une facture
        Button buttonAjouterFacture = (Button) findViewById(R.id.ajouterFacture);
        buttonAjouterFacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // on verifie les permissions
                verifierPermission(CHECK_PERM_PDF);

                if (perm) {
                    recupererFacture();
                }
            }
        });


        // pour prendre une photo depuis la camera directment
        ImageView buttonPrendrePhoto = (ImageView) findViewById(R.id.prendrePhoto);
        buttonPrendrePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFirstNullPicture() <= 3) {
                    // si le nombre de photo est < 4 et que les permissions le permettent
                    verifierPermission(CHECK_TAKE_PICTURE);
                    if (perm) {
                        prendrePhoto();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.delete_photo_bien, Toast.LENGTH_LONG).show();
                }
            }
        });

        // pour le datePicker
        final EditText editTextdate = (EditText) findViewById(R.id.date_achat_bien);
        editTextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AjouterBien.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String day;
                        // modification des données pour être conforme au format attendu
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
                            case "US" : editTextdate.setText(monthFormat + "/" + day + "/" + year);
                                break;
                            default : editTextdate.setText(day + "/" + monthFormat + "/" + year);
                                break;
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

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
        // appelé avant l'ouverture d'un PDF
        if (requestCode == CHECK_PERM_PDF) {
            if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                perm = true;
                recupererFacture();
            }
            // appelé avant l'ouverture d'une PHOTO
        } else if (requestCode == CHECK_PERM_PICTURE) {
            if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                perm = true;
                recupererPhoto();
            }
            // appelé avant la prise d'une photo par la caméra
        } else if (requestCode == CHECK_TAKE_PICTURE) {
            if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                perm = true;
                prendrePhoto();
            }
        }

        // si l'utilisateur n'a pas accepté
        if (!perm) {
            Toast.makeText(getContext(), R.string.permission_denied, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Permet d'appeler la méthode de verification des permissions en fonction de l'action utilisateur
     *
     * @param code
     */
    public void verifierPermission(int code) {
        if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AjouterBien.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        } else {
            perm = true;
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
     * Quand on revient sur l'activité d'ajout de bien
     *
     * @param request
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        SimpleDateFormat s = new SimpleDateFormat(utils.getDateStockageFichier());

        // si on vient d'aller chercher un PDF
        if (resultCode == RESULT_OK && request == SELECT_PDF) {

            String name = getRealPathFromUriPDF(data.getData());
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            // verification du path et sauvegarde dans la mémoire interne
            if (path != null && !path.equals("")) {
                String format = s.format(new Date());
                pathPdf = saveFile(path + "/" + name, format.toString(), "pdf");
            }

            TextView tv_pathPdf = (TextView) findViewById(R.id.pathPdf);
            tv_pathPdf.setText(getResources().getString(R.string.pdf_file) +" "+ name);

        }

        // si on vient de selectionner ou de prendre une image
        if (resultCode == RESULT_OK && (request == SELECT_IMAGE || request == TAKE_IMAGE)) {

            String path;
            // on récupère le path
            if (request == SELECT_IMAGE) {
                path = getRealPathFromUri(data.getData());
            } else {
                path = getRealPathFromUri(uriImage);
            }


            String format = s.format(new Date());
            final File imgFile;

            if (path != null && !path.equals("")) {
                switch (getFirstNullPicture()) {
                    // en fonction de la première place disponible
                    case 0:
                        // image principale : grande image
                        imagePhotoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);
                        // on enregistre la photo dans la mémoire interne
                        pathPhotoPrincipale = saveFile(path, format.toString(), "img");

                        break;
                    case 1:
                        imagePhoto1 = (ImageView) findViewById(R.id.photo1);
                        // on enregistre la photo dans la mémoire interne
                        pathPhoto1 = saveFile(path, format.toString(), "img");

                        break;
                    case 2:
                        imagePhoto2 = (ImageView) findViewById(R.id.photo2);
                        // on enregistre la photo dans la mémoire interne
                        pathPhoto2 = saveFile(path, format.toString(), "img");

                        break;

                    case 3:
                        imagePhoto3 = (ImageView) findViewById(R.id.photo3);
                        // on enregistre la photo dans la mémoire interne
                        pathPhoto3 = saveFile(path, format.toString(), "img");
                        break;
                }

                // on affiche les images
                gererAffichageImage();
            }
        }

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
     * Méthode pour le menu supérieur
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                // pour le retour à la page d'accueil
                Intent intenthome = new Intent(getApplicationContext(), MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);
                return true;

            // rien pour le bouton plus car nous sommes déja dans l'activité d'ajout d'un bien
            case R.id.plus:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Lorsque que l'on clique sur le bouton d'ajout d'un bien
     *
     * @param view
     */
    public void onClickAjouterBien(View view) {
        // on récupére les information
        Boolean erreurSaisie = false;
        TextView textViewNomBien = (TextView) findViewById(R.id.nom_bien);
        TextView textViewDescriptionBien = (TextView) findViewById(R.id.description_bien);
        TextView textViewCommentaireBien = (TextView) findViewById(R.id.commentaire_bien);
        TextView textViewPrixBien = (TextView) findViewById(R.id.prix_bien);
        TextView textViewNumeroSerie = (TextView) findViewById(R.id.numero_serie);

        String nomBien = textViewNomBien.getText().toString();

        // controle du nom du bien
        if (nomBien == null || nomBien.equals("")) {
            Toast.makeText(this, R.string.control_item_name, Toast.LENGTH_SHORT).show();
            erreurSaisie = true;
        }

        TextView editTextdate = (EditText) findViewById(R.id.date_achat_bien);

        String dateAchatSaisie = editTextdate.getText().toString();
        // controle de la date d'achat saisie
        if (dateAchatSaisie != null && !dateAchatSaisie.equals("")) {
            if (!dateAchatSaisie.matches(regexDate)) {
                Toast.makeText(this, R.string.date_format_message, Toast.LENGTH_SHORT).show();
                erreurSaisie = true;
            }

        }

        String commentaireBien = textViewCommentaireBien.getText().toString();
        String descriptionBien = textViewDescriptionBien.getText().toString();

        String prixSaisie = textViewPrixBien.getText().toString();

        String numeroSerie = textViewNumeroSerie.getText().toString();
        int idCategorieSelectionne = categorieSelectionne.getId_Categorie();

        Date dateSaisie = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(utils.getDateSimpleDateFormat());
        String dateDeSaisie = sdf.format(dateSaisie);

        // on regarde dans quelle liste le bien est ajouté
        if (dansListe1) {
            listeIdListe.add(1);

        }
        if (dansListe2) {
            listeIdListe.add(2);
        }
        if (dansListe3) {
            listeIdListe.add(3);
        }

        // il faut selectionner au moins une liste
        if (listeIdListe.size() == 0) {
            erreurSaisie = true;
            Toast.makeText(this, R.string.empty_list, Toast.LENGTH_SHORT).show();

        }

        // si pas d'erreur de saisie, on insére le bien
        if (!erreurSaisie) {
            Bien bien = new Bien(0,
                    nomBien,
                    dateDeSaisie,
                    dateAchatSaisie,
                    pathPdf,
                    commentaireBien,
                    prixSaisie,
                    pathPhotoPrincipale,
                    pathPhoto1,
                    pathPhoto2,
                    pathPhoto3,
                    idCategorieSelectionne,
                    descriptionBien,
                    numeroSerie
            );

            BienDAO bienDAO = new BienDAO(this);

            bienDAO.open();
            bienDAO.addBien(bien, listeIdListe);
            bienDAO.close();
            finish();

        }


    }

    /**
     * Pour retrouner la catégorie selectionnée dans le spinner
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

    /**
     * Quand rien n'est selectionné dans spinner
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
            dir = new File(context.getFilesDir() + separator + dirName);

            if (!dir.exists()) {
                dir.mkdir();
            }
            // si c'est un pdf, on crée le repertoire factures

        } else if (type.equals("pdf")) {
            String dirName = getResources().getString(R.string.bills_directory_name);
            dir = new File(context.getFilesDir() + separator + dirName);
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
                byte[] buf = new byte[2048];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.flush();
                out.close();
            }
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
     * Getteur pour retourner le context de l'activité
     *
     * @return context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Pour lancer l'activité de prise de photo
     */
    public void prendrePhoto() {
        verifierPermission(CHECK_TAKE_PICTURE);

        if (perm) {
            // si l'application est autorisé
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, getResources().getString(R.string.new_picture));
            values.put(MediaStore.Images.Media.DESCRIPTION, getResources().getString(R.string.from_camera));
            // on demarrer la nouvelle activité
            uriImage = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
            startActivityForResult(intent, TAKE_IMAGE);
        } else {
            Toast.makeText(getContext(), R.string.number_pictures_max, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Permet de retourner la première place disponible pour une photo (prise de puis la caméra ou la gallerie
     * Retourne 4 s'il n'y a plus de place
     *
     * @return premierePlaceDisponible
     */
    public int getFirstNullPicture() {
        if (pathPhotoPrincipale == null || pathPhotoPrincipale.equals("")) {
            return 0;
        } else if (pathPhoto1 == null || pathPhoto1.equals("")) {
            return 1;
        } else if (pathPhoto2 == null || pathPhoto2.equals("")) {
            return 2;
        } else if (pathPhoto3 == null || pathPhoto3.equals("")) {
            return 3;
        } else {
            return 4;
        }
    }


    /**
     * Pour supprimer une photo de la vue
     *
     * @param fileName
     * @param photo
     */
    public void supprimerPhoto(String fileName, View photo) {
        final View view = (ImageView) photo;

        // on configure l'affichage du dialog
        TextView supprimerImage = new TextView(this);
        supprimerImage.setText(getResources().getString(R.string.delete_photo_approval) +" "+ fileName + " ?");
        LinearLayout layout = new LinearLayout(this);
        layout.addView(supprimerImage);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) supprimerImage.getLayoutParams();
        params.leftMargin = 100;
        params.rightMargin = 100;
        params.topMargin = 50;
        layout.setLayoutParams(params);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResources().getString(R.string.title_delete_photo));
        builder.setView(layout);

        builder.setPositiveButton(R.string.ajouter_bien_dialog_positive_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (view.getTag().toString()) {
                            // en fonction de la photo à supprimer la photo, on met à jours les informations
                            case "principal":
                                imagePhotoPrincipale.setImageBitmap(null);
                                pathPhotoPrincipale = ("");
                                break;
                            case "1":
                                imagePhoto1.setImageBitmap(null);
                                pathPhoto1 = ("");
                                break;
                            case "2":
                                imagePhoto2.setImageBitmap(null);
                                pathPhoto2 = ("");
                                break;
                            case "3":
                                imagePhoto3.setImageBitmap(null);
                                pathPhoto3 = ("");
                                break;
                        }

                    }
                });

        // on ne fait rien si l'utilisateur clique sur Non
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
     * Méthode permettant de gérer l'affichage des 4 images du bien
     */
    public void gererAffichageImage(){
        Bitmap myBitmap;

        // cas de la grande photo
        if (pathPhotoPrincipale != null && !pathPhotoPrincipale.equals("")){
            final File file = new File(pathPhotoPrincipale);
            if (file.exists()) {
                // on mets le bitmap dans la vue
                imagePhotoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);
                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imagePhotoPrincipale.setImageBitmap(myBitmap);

                // pour supprimer
                imagePhotoPrincipale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        supprimerPhoto(file.getName(), imagePhotoPrincipale);
                    }
                });
            }
        }

        if (pathPhoto1 != null && !pathPhoto1.equals("")){
            final File file = new File(pathPhoto1);
            if (file.exists()) {
                // on mets le bitmap dans la vue
                imagePhoto1 = (ImageView) findViewById(R.id.photo1);
                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imagePhoto1.setImageBitmap(myBitmap);

                // pour supprimer
                imagePhoto1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        supprimerPhoto(file.getName(), imagePhoto1);
                    }
                });
            }
        }

        if (pathPhoto2 != null && !pathPhoto2.equals("")){
            final File file = new File(pathPhoto2);
            if (file.exists()) {
                // on mets le bitmap dans la vue
                imagePhoto2 = (ImageView) findViewById(R.id.photo2);
                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imagePhoto2.setImageBitmap(myBitmap);

                // pour supprimer
                imagePhoto2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        supprimerPhoto(file.getName(), imagePhoto2);
                    }
                });
            }
        }

        if (pathPhoto3 != null && !pathPhoto3.equals("")){
            final File file = new File(pathPhoto3);
            if (file.exists()) {
                // on mets le bitmap dans la vue
                imagePhoto3 = (ImageView) findViewById(R.id.photo3);
                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imagePhoto3.setImageBitmap(myBitmap);

                // pour supprimer
                imagePhoto3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        supprimerPhoto(file.getName(), imagePhoto3);
                    }
                });
            }
        }



    }


    /**
     * Méthode appelée après la rotation de l'appareil, utilisée ici pour restaurer les images et la facture
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        // grande image
        if (savedInstanceState.getString("PATH_PHOTO_PRINCIPALE") != null && !savedInstanceState.getString("PATH_PHOTO_PRINCIPALE").equals("")) {
            pathPhotoPrincipale = savedInstanceState.getString("PATH_PHOTO_PRINCIPALE");
        }

        // image 1
        if (savedInstanceState.getString("PATH_PHOTO_1") != null && !savedInstanceState.getString("PATH_PHOTO_1").equals("")) {
            pathPhoto1 = savedInstanceState.getString("PATH_PHOTO_1");
        }

        // image 2
        if (savedInstanceState.getString("PATH_PHOTO_2") != null && !savedInstanceState.getString("PATH_PHOTO_2").equals("")) {
            pathPhoto2 = savedInstanceState.getString("PATH_PHOTO_2");
        }

        // image 3
        if (savedInstanceState.getString("PATH_PHOTO_3") != null && !savedInstanceState.getString("PATH_PHOTO_3").equals("")) {
            pathPhoto3 = savedInstanceState.getString("PATH_PHOTO_3");
        }

        // facture
        if (savedInstanceState.getString("PATH_FACTURE") != null && !savedInstanceState.getString("PATH_FACTURE").equals("")) {
            pathPdf = savedInstanceState.getString("PATH_FACTURE");
        }

        // on affiche les images
        gererAffichageImage();

    }

    /**
     * Méthode appelée avant la rotation de l'affichage, on sauvegarde ici les images et la facture à restaurer après la rotation
     * @param outState
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("PATH_PHOTO_PRINCIPALE", pathPhotoPrincipale);
        outState.putString("PATH_PHOTO_1", pathPhoto1);
        outState.putString("PATH_PHOTO_2", pathPhoto2);
        outState.putString("PATH_PHOTO_3", pathPhoto3);
        outState.putString("PATH_FACTURE", pathPdf);
        super.onSaveInstanceState(outState);
    }
}