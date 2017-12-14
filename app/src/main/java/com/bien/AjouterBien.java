package com.bien;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import com.personne.ModifierPersonne;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AjouterBien extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<Categorie> categoriesList = new ArrayList<Categorie>();
    private Spinner spinnerCategorie;
    private Categorie categorieSelectionne;
    private Boolean dansListe1 = false;
    private Boolean dansListe2 = false;
    private Boolean dansListe3 = false;
    private String regexDate = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$";
    private int retourHome = 1;
    private Boolean perm = false;



    private Menu m;

    private String pathPdf = "";
    private String pathPhotoPrincipale = "";
    private String pathPhoto1 = "";
    private String pathPhoto2 = "";
    private String pathPhoto3 = "";
    private DatePickerDialog datePickerDialog;

    private ArrayList<Integer> listeIdListe = new ArrayList<Integer>();

    int nbPhoto = 0;
    private Context context = this;

    final static int SELECT_IMAGE = 1;
    final static int SELECT_PDF = 2;
    final static int TAKE_IMAGE = 3;

    final static int CHECK_PERM_PICTURE = 4;
    final static int CHECK_PERM_PDF = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_bien);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Ajouter un bien");
        setSupportActionBar(myToolbar);

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

        ArrayAdapter arrayAdapterListe = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listeCategorieName);
        arrayAdapterListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(arrayAdapterListe);

        ListeDAO listeDAO = new ListeDAO(this);

        listeDAO.open();

        ArrayList<Liste> listes = listeDAO.getallListe();

        listeDAO.close();

        final CheckedTextView ctvliste1 = (CheckedTextView) findViewById(R.id.checkListe1);

        ctvliste1.setText(listes.get(0).getLibelle_liste());

        Bundle extras = getIntent().getExtras();

        int fromIdListe = 0;
        if (extras != null) {
            fromIdListe = extras.getInt("ID_CURRENT_LIST");
        }

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


        Button buttonAjouterPhoto = (Button) findViewById(R.id.ajouterPhoto);
        buttonAjouterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbPhoto <= 3) {
                    verifierPermission(CHECK_PERM_PICTURE);
                    if (perm) {
                        recupererPhoto();
                    }
                } else {
                    Toast.makeText(getContext(), "Vous ne pouvez mettre que 4 photos pour un bien.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button buttonAjouterFacture = (Button) findViewById(R.id.ajouterFacture);
        buttonAjouterFacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifierPermission(CHECK_PERM_PDF);

                if (perm) {
                    recupererFacture();
                }
            }
        });

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
                        String day ;
                        if(dayOfMonth <10){
                            day = "0"+dayOfMonth;
                        } else {
                            day = String.valueOf(dayOfMonth);
                        }

                        String monthFormat ;
                        if((month +1) <10){
                            monthFormat = "0"+(month+1);
                        } else {
                            monthFormat = String.valueOf(month+1);
                        }
                        editTextdate.setText(day + "/" +monthFormat+ "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CHECK_PERM_PDF){
            if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                perm = true;
                recupererFacture();
            }
        } else if (requestCode == CHECK_PERM_PICTURE){
            if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                perm = true;
                recupererPhoto();
            }
        }

        if(!perm) {
            Toast.makeText(getContext(), "L'application n'est pas autorisée à accéder aux documents. Verifier les permissions dans les réglages de l'appareil.", Toast.LENGTH_LONG).show();
        }

    }

    public void verifierPermission(int code){
        if (ContextCompat.checkSelfPermission(AjouterBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AjouterBien.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        } else {
            perm = true;
        }
    }

    public void recupererPhoto(){

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_IMAGE);


    }

    /*
        *Retour du resultat de la galerie
     */
    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");

        if (resultCode == RESULT_OK && request == SELECT_PDF) {

            String name = getRealPathFromUriPDF(data.getData());
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            if (path != null && !path.equals("")) {
                String format = s.format(new Date());
                pathPdf = saveFile(path+"/"+name, format.toString(), "pdf");
            }

            TextView tv_pathPdf = (TextView) findViewById(R.id.pathPdf);
            tv_pathPdf.setText("Facture choisie : " + name);

        }

        if (resultCode == RESULT_OK && (request == SELECT_IMAGE || request == TAKE_IMAGE)) {

            String path;
            if(request == SELECT_IMAGE) {
                path = getRealPathFromUri(data.getData());
            } else {
                path = "temp";
            }

            ImageView imagePhotoPrincipale;
            ImageView imagePhoto1;
            ImageView imagePhoto2;
            ImageView imagePhoto3;
            String format = s.format(new Date());
            File imgFile;

            if (path != null && !path.equals("")) {
                switch (nbPhoto) {
                    case 0:

                        imagePhotoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);

                        if (request == SELECT_IMAGE) {
                            pathPhotoPrincipale = saveFile(path, format.toString(), "img");
                            imgFile = new File(pathPhotoPrincipale);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                imagePhotoPrincipale.setImageBitmap(myBitmap);
                            }
                        } else {
                            Bitmap myBitmap= (Bitmap) data.getExtras().get("data");
                            pathPhotoPrincipale = saveImagePrise(myBitmap, format.toString());
                            imagePhotoPrincipale.setImageBitmap(myBitmap);
                        }

                        nbPhoto++;

                    break;
                    case 1:

                        imagePhoto1 = (ImageView) findViewById(R.id.photo1);

                        if (request == SELECT_IMAGE) {
                            pathPhoto1 = saveFile(path, format.toString(), "img");
                            imgFile = new File(pathPhoto1);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                imagePhoto1.setImageBitmap(myBitmap);
                            }
                        } else {
                            Bitmap myBitmap= (Bitmap) data.getExtras().get("data");
                            pathPhoto1 = saveImagePrise(myBitmap, format.toString());
                            imagePhoto1.setImageBitmap(myBitmap);
                        }

                        nbPhoto++;

                        break;
                    case 2:
                        imagePhoto2 = (ImageView) findViewById(R.id.photo2);

                        if (request == SELECT_IMAGE) {
                            pathPhoto2 = saveFile(path, format.toString(), "img");
                            imgFile = new File(pathPhoto2);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                imagePhoto2.setImageBitmap(myBitmap);
                            }
                        } else {
                            Bitmap myBitmap= (Bitmap) data.getExtras().get("data");
                            pathPhoto2 = saveImagePrise(myBitmap, format.toString());
                            imagePhoto2.setImageBitmap(myBitmap);
                        }
                        nbPhoto++;

                        break;

                    case 3:

                        imagePhoto3 = (ImageView) findViewById(R.id.photo3);

                        if (request == SELECT_IMAGE) {
                            pathPhoto3 = saveFile(path, format.toString(), "img");
                            imgFile = new File(pathPhoto3);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                imagePhoto3.setImageBitmap(myBitmap);
                            }
                        } else {
                            Bitmap myBitmap= (Bitmap) data.getExtras().get("data");
                            pathPhoto3 = saveImagePrise(myBitmap, format.toString());
                            imagePhoto3.setImageBitmap(myBitmap);
                        }

                        nbPhoto++;

                        break;
                }
            }
        }

    }


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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                Intent intenthome = new Intent(getApplicationContext(), MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);
                return true;

            case R.id.plus:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickAjouterBien(View view) {
        Boolean erreurSaisie = false;
        TextView textViewNomBien = (TextView) findViewById(R.id.nom_bien);
        TextView textViewDescriptionBien = (TextView) findViewById(R.id.description_bien);
        TextView textViewCommentaireBien = (TextView) findViewById(R.id.commentaire_bien);
        TextView textViewPrixBien = (TextView) findViewById(R.id.prix_bien);
        TextView textViewNumeroSerie = (TextView) findViewById(R.id.numero_serie);

        String nomBien = textViewNomBien.getText().toString();

        if (nomBien == null || nomBien.equals("")) {
            Toast.makeText(this, "Le nom ne peut être vide", Toast.LENGTH_SHORT).show();
            erreurSaisie = true;
        }

        TextView editTextdate = (EditText) findViewById(R.id.date_achat_bien);

        String dateAchatSaisie = editTextdate.getText().toString();

        if (dateAchatSaisie != null && !dateAchatSaisie.equals("")) {
            if (!dateAchatSaisie.matches(regexDate)){
                Toast.makeText(this, "La date doit être au format jj/mm/aaaa", Toast.LENGTH_SHORT).show();
                erreurSaisie = true;
            }

        }

        String commentaireBien = textViewCommentaireBien.getText().toString();
        String descriptionBien = textViewDescriptionBien.getText().toString();

        String prixSaisie =  textViewPrixBien.getText().toString();

        String numeroSerie = textViewNumeroSerie.getText().toString();
        int idCategorieSelectionne = categorieSelectionne.getId_Categorie();

        Date dateSaisie = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateDeSaisie = sdf.format(dateSaisie);

        if (dansListe1) {
            listeIdListe.add(1);

        }
        if (dansListe2) {
            listeIdListe.add(2);
            retourHome = 2;
        }
        if (dansListe3) {
            listeIdListe.add(3);
            retourHome = 3;
        }

        if (listeIdListe.size() == 0 ){
            erreurSaisie = true;
            Toast.makeText(this, "Veuillez selectionner au moins une liste", Toast.LENGTH_SHORT).show();

        }

        if (!erreurSaisie) {

            //descriptionBien = "En attendant CTRL keke desc";
            //commentaireBien = "En attendant CTRL keke com";

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

           /* Intent intenthome = new Intent(getApplicationContext(), MainActivity.class);
            intenthome.putExtra("ID_CURRENT_LIST_FROM_ADD_BIEN", retourHome);
            intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intenthome); */
           finish();

        }




    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorieSelectionne = categoriesList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    private String saveFile(String pathFichierOrigine, String nomNouveauFichier, String type) {

        String separator = "/";
        File dir = null;

        if (type.equals("img")) {
            String dirName = "images";
            dir = new File(context.getFilesDir() + separator + dirName);

            if (!dir.exists()) {
                dir.mkdir();
            }

        } else if (type.equals("pdf")) {
            String dirName = "factures";
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
                if(type.equals("img")) {
                    copy(fileSrc,fileDest);
                    compressImage(fileDest);

                } else {
                    copy(fileSrc,fileDest);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }


        return fileDest.getAbsolutePath();

    }

    public File compressImage(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=80;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);
            outputStream.flush();
            outputStream.close();

            return file;
        } catch (Exception e) {
            return null;
        }
    }
    public static void copy(File src, File dst) throws IOException {

        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
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

    public void recupererFacture() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, SELECT_PDF);

    }

    public Context getContext(){
        return context;
    }

    public void prendrePhoto(View view){
        if (nbPhoto <= 3) {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_IMAGE);
        } else {
            Toast.makeText(getContext(), "Vous ne pouvez mettre que 4 photos pour un bien.", Toast.LENGTH_LONG).show();
        }
    }

    public String saveImagePrise(Bitmap bitmap, String nomNouveauFichier) {
        String separator = "/";

        String dirName = "images";
        File dir = new File(context.getFilesDir() + separator + dirName);

        if (!dir.exists()) {
            dir.mkdir();
        }


        File fileDest = new File(dir.getAbsolutePath() + separator + nomNouveauFichier);

        if (fileDest.exists()) {
            fileDest.delete();
        }

        try {

            FileOutputStream fos = new FileOutputStream(fileDest);
            final BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        compressImage(fileDest);
        return fileDest.getAbsolutePath();
    }

}
