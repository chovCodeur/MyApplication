package com.bien;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.categorie.Categorie;
import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.application.inventaire.R;
import com.liste.Liste;
import com.personne.ModifierPersonne;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_bien);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        bdao = new BienDAO(this);
        ldao = new ListeDAO(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {

            dateAchat =(EditText) findViewById(R.id.date_achat_bien);
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
                            dateAchat.setText(dayOfMonth + "/" +(month + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });

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
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
            Bien updateBien = new Bien(bien.getId_bien(), nomBien.getText().toString(), dateSaisie, bien.getDate_achat_bien(), bien.getFacture_bien(),
                    commentaireBien.getText().toString(), prixBien.getText().toString(), bien.getPhoto_bien_principal(), bien.getPhoto_bien_miniature1(),
                    bien.getPhoto_bien_miniature2(), bien.getPhoto_bien_miniature3(), idCategorieSelectionne, descriptionBien.getText().toString(), numeroSerie.getText().toString());
            bdao.modBien(updateBien);

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

    public void modifierFacture(View v){
        verifierPermission(CHECK_PERM_PDF);
        if (perm) {
            recupererFacture();
        }
    }

    private Boolean perm = false;
    final static int SELECT_IMAGE = 1;
    final static int SELECT_PDF = 2;
    final static int CHECK_PERM_PICTURE = 4;
    final static int CHECK_PERM_PDF = 5;

    public void verifierPermission(int code){
        if (ContextCompat.checkSelfPermission(ModifierBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ModifierBien.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        } else {
            perm = true;
        }
    }

    public void recupererFacture() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, SELECT_PDF);

    }

    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");

        if (resultCode == RESULT_OK && request == SELECT_PDF) {

            String name = getRealPathFromUriPDF(data.getData());
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            if (path != null && !path.equals("")) {
                String format = s.format(new Date());
                String pathPdf = saveFile(path+"/"+name, format.toString(), "pdf");
                bien.setFacture_bien(pathPdf);
            }

            TextView tv_pathPdf = (TextView) findViewById(R.id.pathPdf);
            tv_pathPdf.setText("Facture choisie : " + name);

        }

        int numPhoto = getFirstNullPicture();

        if (resultCode == RESULT_OK && request == SELECT_IMAGE) {
            String path = getRealPathFromUri(data.getData());

            final ImageView imagePhotoPrincipale;
            final ImageView imagePhoto1;
            final ImageView imagePhoto2;
            final ImageView imagePhoto3;


            if (path != null && !path.equals("")) {
                switch (numPhoto) {
                    case 0:
                        //bitmapPrincipal = BitmapFactory.decodeFile(path);
                        String format = s.format(new Date());
                        String pathPhotoPrincipale = saveFile(path, format.toString(), "img");

                        final File imgFile = new File(pathPhotoPrincipale);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imagePhotoPrincipale = (ImageView) findViewById(R.id.photoPrincipale);
                            imagePhotoPrincipale.setImageBitmap(myBitmap);
                            bien.setPhoto_bien_principal(pathPhotoPrincipale);
                            imagePhotoPrincipale.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    supprimerPhoto(imgFile, imagePhotoPrincipale);
                                }
                            });


                        }

                        break;
                    case 1:

                        format = s.format(new Date());
                        String pathPhoto1 = saveFile(path, format.toString(), "img");

                        final File imgFileMiniature1 = new File(pathPhoto1);
                        if (imgFileMiniature1.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFileMiniature1.getAbsolutePath());
                            imagePhoto1 = (ImageView) findViewById(R.id.photo1);
                            imagePhoto1.setImageBitmap(myBitmap);
                            bien.setPhoto_bien_miniature1(pathPhoto1);
                            imagePhoto1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    supprimerPhoto(imgFileMiniature1, imagePhoto1);
                                }
                            });

                        }

                        break;
                    case 2:
                        format = s.format(new Date());
                        String pathPhoto2 = saveFile(path, format.toString(), "img");

                        final File imgFileMiniature2 = new File(pathPhoto2);
                        if (imgFileMiniature2.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFileMiniature2.getAbsolutePath());
                            imagePhoto2 = (ImageView) findViewById(R.id.photo2);
                            imagePhoto2.setImageBitmap(myBitmap);
                            bien.setPhoto_bien_miniature2(pathPhoto2);
                            imagePhoto2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    supprimerPhoto(imgFileMiniature2, imagePhoto2);
                                }
                            });
                        }

                        break;

                    case 3:

                        format = s.format(new Date());
                        String pathPhoto3 = saveFile(path, format.toString(), "img");

                        final File imgFileMiniature3 = new File(pathPhoto3);
                        if (imgFileMiniature3.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFileMiniature3.getAbsolutePath());
                            imagePhoto3 = (ImageView) findViewById(R.id.photo3);
                            imagePhoto3.setImageBitmap(myBitmap);
                            bien.setPhoto_bien_miniature3(pathPhoto3);
                            imagePhoto3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    supprimerPhoto(imgFileMiniature3, imagePhoto3);
                                }
                            });
                        }

                        break;
                }
            }
        }
    }



    public void recupererPhoto(){

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_IMAGE);

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

    private String saveFile(String pathFichierOrigine, String nomNouveauFichier, String type) {

        String separator = "/";
        File dir = null;

        if (type.equals("img")) {
            String dirName = "images";
            dir = new File(this.getFilesDir() + separator + dirName);

            if (!dir.exists()) {
                dir.mkdir();
            }

        } else if (type.equals("pdf")) {
            String dirName = "factures";
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

   /* public static void copyAndCompress(File src, File dst) throws IOException {

        Bitmap bmp = BitmapFactory.decodeFile(src.getAbsolutePath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 0, bos);

        try (InputStream in = new ByteArrayInputStream(bos.toByteArray())) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    } */

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CHECK_PERM_PDF){
            if (ContextCompat.checkSelfPermission(ModifierBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                perm = true;
                recupererFacture();
            }
        } else if (requestCode == CHECK_PERM_PICTURE){
            if (ContextCompat.checkSelfPermission(ModifierBien.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                perm = true;
                recupererPhoto();
            }
        }

        if(!perm) {
            Toast.makeText(this, "L'application n'est pas autorisée à accéder aux documents. Verifier les permissions dans les réglages de l'appareil.", Toast.LENGTH_LONG).show();
        }

    }

    public void modifierPhoto(View v){
        verifierPermission(CHECK_PERM_PDF);
        int numPhoto = getFirstNullPicture();
        if (perm) {
            if (numPhoto != 4) {
                recupererPhoto();
            } else {
                Toast.makeText(this, "Vous devez déjà supprimer une photo ! ", Toast.LENGTH_LONG).show();

            }
        }
    }


    public int getFirstNullPicture(){
        if (bien.getPhoto_bien_principal()== null || bien.getPhoto_bien_principal().equals("")){
            return 0;
        } else if (bien.getPhoto_bien_miniature1()== null || bien.getPhoto_bien_miniature1().equals("")){
            return  1;
        } else if (bien.getPhoto_bien_miniature2()== null || bien.getPhoto_bien_miniature2().equals("")){
            return  2;
        } else if (bien.getPhoto_bien_miniature3()== null || bien.getPhoto_bien_miniature3().equals("")) {
            return 3;
        } else {
            return 4;
        }
    }
    /*

    public int getFirstNotNullPicture(){
        if (bien.getPhoto_bien_principal()!= null && !bien.getPhoto_bien_principal().equals("")){
            return 0;
        } else if (bien.getPhoto_bien_miniature1()!= null && !bien.getPhoto_bien_miniature1().equals("")){
            return  1;
        } else if (bien.getPhoto_bien_miniature2()!= null && !bien.getPhoto_bien_miniature2().equals("")){
            return  2;
        } else if (bien.getPhoto_bien_miniature3()!= null && ! bien.getPhoto_bien_miniature3().equals("")) {
            return 3;
        } else {
            return 4;
        }
    }

    public void rangerPhoto(){
        int premierVide = getFirstNullPicture();
        int premiereNonVide = getFirstNotNullPicture();

        if (premierVide != 4 && premiereNonVide != 4){
            switch (premierVide){
                case 0:
                    echangerPhotoEnbase(0, premiereNonVide);
                    break;

                case 1:
                    echangerPhotoEnbase(1, premiereNonVide);

                    break;

                case 2:
                    break;

                case 3:
                    break;

            }
        }
    }

    public void echangerPhotoEnbase(int vide, int aDeplacer){

    }
    */


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
            final int REQUIRED_SIZE=75;

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
}
