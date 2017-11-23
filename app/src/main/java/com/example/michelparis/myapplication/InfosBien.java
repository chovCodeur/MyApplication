package com.example.michelparis.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.ArrayList;

/**
 * Created by Rodnor on 11/11/2017.
 */

public class InfosBien extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private BienDAO bdao;
    private CategorieDAO cdao;
    private int id = 0;
    private Bien bien;
    private ImageButton photoPrincipale;
    private TextView nomBien;
    private TextView categorieBien;
    private TextView descriptionBien;
    private Button facture;
    private TextView dateAcquisition;
    private TextView dateSaisie;
    private TextView prix;
    private TextView numeroSerie;
    private TextView commentaire;
    //private DrawerLayout drawer;
    private ImageButton photoMini1;
    private ImageButton photoMini2;
    private ImageButton photoMini3;
    private Spinner spinnerListe;
    private ArrayList<String> listes = new ArrayList<>();
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_bien);

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.toolbarTitle));
        setSupportActionBar(myToolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        bdao = new BienDAO(this);
        cdao = new CategorieDAO(this);

        //bien = new Bien(1, "Lunettes de soleil", "12-11-2017", "23-02-2012", "test.pdf", "Ces lunettes ont le verre droit rayé", 120.99f, null,null,null,null, 1,"Ce sont des lunettes de soleil Rayban de type Aviator","123456789");

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            id = extras.getInt("IDBIEN");
        }

        if(id != 0) {
            bdao.open();
            bien = bdao.getBien(id);
            bdao.close();
        }

        // Mise à jour de l'image principale
        photoPrincipale = (ImageButton) findViewById(R.id.photoPrincipaleBien);
        photoPrincipale.setImageBitmap(bien.getPhoto_bien_principal());

        // On met à jour le nom du bien
        nomBien = (TextView) findViewById(R.id.nomBien);
        nomBien.setText(bien.getNom_bien());

        // On met à jour le nom de la catégorie du bien
        categorieBien = (TextView) findViewById(R.id.categorieBien);
        cdao.open();
        categorieBien.setText(cdao.getNomCategorieByIdBien(bien.getId_categorie_bien()));
        cdao.close();

        // On met à jour la description du bien
        descriptionBien = (TextView) findViewById(R.id.descriptionBien);
        descriptionBien.setText(bien.getDescription_bien());

        // Affichage de la facture au clic du bouton
        facture = (Button) findViewById(R.id.buttonFactureBien);
        facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Mise à jour des 3 miniatures d'images
        photoMini1 = (ImageButton) findViewById(R.id.Photo1Bien);
        photoMini1.setImageBitmap(bien.getPhoto_bien_miniature1());

        photoMini2 = (ImageButton) findViewById(R.id.Photo2Bien);
        photoMini2.setImageBitmap(bien.getPhoto_bien_miniature2());

        photoMini3 = (ImageButton) findViewById(R.id.Photo3Bien);
        photoMini3.setImageBitmap(bien.getPhoto_bien_miniature3());

        // Mise à jour des listes dans lequel l'objet apparaît
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
        prix.setText("Prix du bien : "+bien.getPrix_bien()+"€");

        // Mise à jour numéro de série du bien
        numeroSerie = (TextView) findViewById(R.id.numeroSerieBien);
        numeroSerie.setText("Numéro de série : "+bien.getNumeroSerie_bien());

        // Mise à jour commentaire du bien
        commentaire = (TextView) findViewById(R.id.commentairesBien);
        commentaire.setText(bien.getCommentaire_bien());
        // }

        Button btn = (Button) findViewById(R.id.buttonFactureBien);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadPDF.class);
                intent.putExtra("nomPDF", bien.getFacture_bien());
                //intent.putExtra("nomPDF", "test.pdf");
                startActivity(intent);
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.photoPrincipaleBien);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(view, R.drawable.no_image);
            }
        });

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.Photo1Bien);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(view, R.drawable.no_image);
            }
        });

        ImageButton imageButton2 = (ImageButton) findViewById(R.id.Photo2Bien);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(view, R.drawable.no_image);
            }
        });

        ImageButton imageButton3 = (ImageButton) findViewById(R.id.Photo3Bien);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(view, R.drawable.no_image);
            }
        });
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.liste1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("IDLISTE", 1);
            startActivity(intent);
        }

        if (id == R.id.liste2) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("IDLISTE", 2);
            startActivity(intent);
        }

        if (id == R.id.liste3) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("IDLISTE", 3);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

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
}