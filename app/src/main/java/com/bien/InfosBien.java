package com.bien;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.application.MainActivity;
import com.application.inventaire.R;
import com.utils.ReadPDF;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Tristan on 11/11/2017.
 */

/**
 * Classe servant à afficher les informations que contient l'application pour un bien en particulier.
 */
public class InfosBien extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variables de classe
    private BienDAO bdao;
    private CategorieDAO cdao;
    private ListeDAO ldao;
    private int id = 0;
    private Bien bien;
    private ImageButton photoPrincipale;
    private TextView nomBien;
    private TextView categorieBien;
    private TextView descriptionBien;
    private TextView dateAcquisition;
    private TextView dateSaisie;
    private TextView prix;
    private TextView numeroSerie;
    private TextView commentaire;
    private ImageButton photoMini1;
    private ImageButton photoMini2;
    private ImageButton photoMini3;
    private Spinner spinnerListe;
    private ArrayList<String> listes = new ArrayList<>();
    private ArrayList<Integer> idlistes = new ArrayList<>();
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private Menu m = null;
    private Context context = this;

    /**
     * Procédure lancée à la création de l'activité.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_bien);
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
     * Méthode lancée à chaque arrivée sur l'activité.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // On vide la liste contenant le nom des listes dans lequel le bien se situe
        listes.clear();

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        // On instancie les DAO dont on a besoin dans l'activité
        bdao = new BienDAO(this);
        cdao = new CategorieDAO(this);
        ldao = new ListeDAO(this);

        // On récupère les paramètres provenants d'une autre activité
        Bundle extras = getIntent().getExtras();

        // On effectue les traitements suivants seulement si l'on récupère des paramètres
        if(extras != null) {
            // On stocke l'id du bien dont les informations doivent être affichées
            id = extras.getInt("IDBIEN");

            // Si l'id du bien n'est pas nul
            if(id != 0) {
                // On ouvre le DAO et on récupère le bien par son id
                bdao.open();
                bien = bdao.getBien(id);

                // On récupère les id des listes d'appartenance du bien dans la table Appartient
                idlistes = bdao.getAllIdListeByIdBien(bien.getId_bien());

                // On récupère le nom des listes dans lequel le bien existe grâce aux id récupérés
                // et on les ajoute à notre liste éphémère
                ldao.open();
                for(int i=0;i<idlistes.size();i++) {
                    if(idlistes.get(i) == 1) {
                        String nom = ldao.getNomListeById(1);
                        listes.add(nom);
                    }
                    if(idlistes.get(i) == 2) {
                        String nom = ldao.getNomListeById(2);
                        listes.add(nom);
                    }
                    if(idlistes.get(i) == 3) {
                        String nom = ldao.getNomListeById(3);
                        listes.add(nom);
                    }
                }
                ldao.close();

                bdao.close();

                // On récupère le nom du bien et on l'affiche dans la toolbar customisée
                myToolbar.setTitle(bien.getNom_bien());
                setSupportActionBar(myToolbar);

                // Mise à jour de l'image principale
                photoPrincipale = (ImageButton) findViewById(R.id.photoPrincipaleBien);
                // Si le bien contient une photo principale, on créé une image bitmap que l'on affiche
                if(bien.getPhoto_bien_principal() != null && !bien.getPhoto_bien_principal().equals("")) {
                    File imgFile = new File(bien.getPhoto_bien_principal());
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photoPrincipale.setImageBitmap(myBitmap);
                    }
                // Sinon, on affiche une image par défaut
                } else {
                    photoPrincipale.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                }

                // On met à jour le nom du bien
                nomBien = (TextView) findViewById(R.id.nomBien);
                nomBien.setText(bien.getNom_bien());

                // On met à jour le nom de la catégorie du bien
                categorieBien = (TextView) findViewById(R.id.categorieBien);
                cdao.open();
                categorieBien.setText(cdao.getNomCategorieByIdCategorie(bien.getId_categorie_bien()));
                cdao.close();

                // On met à jour la description du bien
                descriptionBien = (TextView) findViewById(R.id.descriptionBien);
                descriptionBien.setText(bien.getDescription_bien());

                // Affichage de la facture au clic du bouton
                Button facture = (Button) findViewById(R.id.buttonFactureBien);
                facture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bien.getFacture_bien() != null && !bien.getFacture_bien().equals("")) {
                            Intent intent = new Intent(getApplicationContext(), ReadPDF.class);
                            intent.putExtra("nomPDF", bien.getFacture_bien());
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(context, "Pas de facture pour ce bien", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });

                Bitmap myBitmap;
                // Mise à jour des 3 miniatures d'images sur le même principe que l'image principale
                photoMini1 = (ImageButton) findViewById(R.id.Photo1Bien);
                if(bien.getPhoto_bien_miniature1() != null && !bien.getPhoto_bien_miniature1().equals("")) {
                    File imgFile = new File(bien.getPhoto_bien_miniature1());
                    if (imgFile.exists()) {
                        myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photoMini1.setImageBitmap(myBitmap);
                    }
                } else {
                    photoMini1.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                }

                photoMini2 = (ImageButton) findViewById(R.id.Photo2Bien);
                if(bien.getPhoto_bien_miniature2() != null && !bien.getPhoto_bien_miniature2().equals("")) {
                    File imgFile = new File(bien.getPhoto_bien_miniature2());
                    if (imgFile.exists()) {
                        myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photoMini2.setImageBitmap(myBitmap);
                    }
                } else {
                    photoMini2.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                }

                photoMini3 = (ImageButton) findViewById(R.id.Photo3Bien);
                if(bien.getPhoto_bien_miniature3() != null && !bien.getPhoto_bien_miniature3().equals("")) {
                    File imgFile = new File(bien.getPhoto_bien_miniature3());
                    if (imgFile.exists()) {
                        myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        photoMini3.setImageBitmap(myBitmap);
                    }
                } else {
                    photoMini3.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                }

                // Mise à jour du spinner contenant le nom des listes d'appartenance du bien
                spinnerListe = (Spinner) findViewById(R.id.spinnerListesAppartenanceBien);
                spinnerListe.setOnItemSelectedListener(this);
                ArrayAdapter arrayAdapterCategorie = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listes);
                arrayAdapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerListe.setAdapter(arrayAdapterCategorie);


                // Mise à jour date d'acquisition
                dateAcquisition = (TextView) findViewById(R.id.dateAcquisitionBien);
                dateAcquisition.setText("Date d'acquisition : "+bien.getDate_achat_bien());

                // Mise à jour date de saisie
                dateSaisie = (TextView) findViewById(R.id.dateSaisieBien);
                dateSaisie.setText("Date de saisie : "+bien.getDate_saisie_bien());

                // Mise à jour prix du bien
                prix = (TextView) findViewById(R.id.prixBien);
                if (bien.getPrix_bien().equals("") || bien.getPrix_bien() == null) {
                    prix.setText("Prix du bien : ");
                } else {
                    prix.setText("Prix du bien : " + bien.getPrix_bien() + "€");

                }

                // Mise à jour numéro de série du bien
                numeroSerie = (TextView) findViewById(R.id.numeroSerieBien);
                numeroSerie.setText("Numéro de série : "+bien.getNumeroSerie_bien());

                // Mise à jour commentaire du bien
                commentaire = (TextView) findViewById(R.id.commentairesBien);
                commentaire.setText(bien.getCommentaire_bien());

                // Lorsque l'on clique sur l'une des images du bien, si elle existe, un effet de zoom est appliqué
                ImageButton imageButton = (ImageButton) findViewById(R.id.photoPrincipaleBien);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bien.getPhoto_bien_principal() != null && !bien.getPhoto_bien_principal().equals("")) {
                            File file = new File(bien.getPhoto_bien_principal());
                            zoomImageFromThumb(view, BitmapFactory.decodeFile(file.getAbsolutePath()));
                        }
                    }
                });

                ImageButton imageButton1 = (ImageButton) findViewById(R.id.Photo1Bien);
                imageButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bien.getPhoto_bien_miniature1() != null  && !bien.getPhoto_bien_miniature1().equals("")) {
                            File file = new File(bien.getPhoto_bien_miniature1());
                            zoomImageFromThumb(view, BitmapFactory.decodeFile(file.getAbsolutePath()));
                        }
                    }
                });

                ImageButton imageButton2 = (ImageButton) findViewById(R.id.Photo2Bien);
                imageButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bien.getPhoto_bien_miniature2() != null  && !bien.getPhoto_bien_miniature2().equals("")) {
                            File file = new File(bien.getPhoto_bien_miniature2());
                            zoomImageFromThumb(view, BitmapFactory.decodeFile(file.getAbsolutePath()));
                        }
                    }
                });

                ImageButton imageButton3 = (ImageButton) findViewById(R.id.Photo3Bien);
                imageButton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bien.getPhoto_bien_miniature3() != null  && !bien.getPhoto_bien_miniature3().equals("")) {
                            File file = new File(bien.getPhoto_bien_miniature3());
                            zoomImageFromThumb(view, BitmapFactory.decodeFile(file.getAbsolutePath()));
                        }
                    }
                });
            }
        }
    }

    /**
     * Méthode permettant de faire différentes actions suivant le bouton du menu cliqué.
     * @param item du menu
     * @return un booléen indiquant quel item a été sélectionné.
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            // Si l'on clique sur l'icone "Home", on retourne directement à la page d'accueil
            case R.id.home:
                Intent intenthome = new Intent(getApplicationContext(), MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);

                return true;

            // Si l'on clique sur l'icone "+", on accède à l'activité permettant d'ajouter un bien
            case R.id.plus:
                intent = new Intent(this, AjouterBien.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    /**
     * Méthode permettant d'effectuer un zoom sur une image.
     * @param thumbView vue contenant l'image que l'on veut zoomer.
     * @param image sur laquelle on veut zoomer.
     */
    private void zoomImageFromThumb(final View thumbView, Bitmap image) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageView.setImageBitmap(image);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    /**
     * Méthode permettant de lancer l'activité de modification d'un bien.
     * @param v un objet View.
     */
    public void modifierBien(View v) {
        Intent intent = new Intent(this, ModifierBien.class);
        intent.putExtra("IDBIEN",bien.getId_bien());
        intent.putExtra("IDCATEGORIE",bien.getId_categorie_bien());
        startActivity(intent);
    }

    /**
     * Méthode permettant de déclencher la suppression d'un bien.
     * @param v un objet View.
     */
    public void supprimerBien(View v){
        bdao.open();
        bdao.deleteBien(bien);
        bdao.close();

        Intent intenthome = new Intent(this, MainActivity.class);
        intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intenthome);
    }
}