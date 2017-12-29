package com.application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.application.inventaire.R;
import com.bien.AjouterBien;
import com.bien.Bien;
import com.bien.BienAdapter;
import com.bien.InfosBien;
import com.categorie.AjouterCategorie;
import com.categorie.Categorie;
import com.categorie.ModifierCategorie;
import com.dao.BienDAO;
import com.dao.CategorieDAO;
import com.dao.ListeDAO;
import com.dao.PersonneDAO;
import com.liste.ExportListe;
import com.liste.Liste;
import com.personne.ModifierPersonne;
import com.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Classe principale appelée au lancement de l'application
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private BienAdapter mAdapter;
    private ListView lv_listeBiens;
    private BienDAO bdao;
    private ListeDAO ldao;
    private PersonneDAO pdao;
    private CategorieDAO cdao;
    private int idCurrentList = 1;
    private Menu m;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private LinearLayout layout;
    private Context context = this;
    private NavigationView navigationView;
    private Toolbar myToolbar;
    private boolean nomIdentique = false;


    ArrayList<Bien> listeBiens = new ArrayList<Bien>();
    HashMap<Integer, Integer> listCorrespondance = new HashMap<>();
    HashMap<Integer, Integer> listeHeader = new HashMap<>();


    /**
     * Procédure lancée à la création de l'activité.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // initialisation des DAO
        pdao = new PersonneDAO(this);
        ldao = new ListeDAO(this);
        bdao = new BienDAO(this);
        cdao = new CategorieDAO(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // pour remplir la BD lors du premier lancement
        pdao.open();
        if (pdao.getPersonne(1) == null){
            initialiserBD();
        }
        pdao.close();

        // initialisation des vues
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        ldao.open();
        myToolbar.setTitle(ldao.getNomListeById(idCurrentList));
        ldao.close();
        setSupportActionBar(myToolbar);

        lv_listeBiens = (ListView) findViewById(R.id.listeBiens);



        // initialisation du drawer sur le coté
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ldao.open();
        navigationView.getMenu().findItem(R.id.liste1).setTitle(ldao.getNomListeById(1));
        navigationView.getMenu().findItem(R.id.liste2).setTitle(ldao.getNomListeById(2));
        navigationView.getMenu().findItem(R.id.liste3).setTitle(ldao.getNomListeById(3));
        ldao.close();

    }

    /**
     * Méthode appelée après la rotation de l'appareil, utilisée ici pour restaurer la liste courante
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getString("ID_CURRENT_LISTE") != null && !savedInstanceState.getString("ID_CURRENT_LISTE").equals("")) {
            idCurrentList = Integer.valueOf(savedInstanceState.getString("ID_CURRENT_LISTE"));
        } else {
            idCurrentList = 1;
        }
    }

    /**
     * Méthode appelée avant la rotation de l'appareil, utilisée ici pour sauvegarder la liste courante
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ID_CURRENT_LISTE", String.valueOf(idCurrentList));

        super.onSaveInstanceState(outState);
    }

    /**
     * Méthode appelée lorsque l'on revient sur l'activité
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapterView();

    }

    /**
     * Méthode appelée lorsque le bouton retour physique de l'appareil est utilisé
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Méthode pour la gestion des menus
     * @param item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
    }

    /**
     * Méthode pour la création des menus
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        m = menu;

        return true;
    }

    /**
     * Méthode appelée lors de la selection d'un item dans le menu
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:

                return true;

            case R.id.plus:

                intent = new Intent(this, AjouterBien.class);
                intent.putExtra("ID_CURRENT_LIST", idCurrentList);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode pour la gestion du drawer
     * @param menu
     * @return true
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // drawer
        ViewGroup navigationMenuView = (ViewGroup) navigationView.getChildAt(0);
        ViewGroup navigationMenuItemView1 = (ViewGroup) navigationMenuView.getChildAt(1);
        ViewGroup navigationMenuItemView2 = (ViewGroup) navigationMenuView.getChildAt(2);
        ViewGroup navigationMenuItemView3 = (ViewGroup) navigationMenuView.getChildAt(3);
        View appCompatCheckedTextView1 = navigationMenuItemView1.getChildAt(0);
        View appCompatCheckedTextView2 = navigationMenuItemView2.getChildAt(0);
        View appCompatCheckedTextView3 = navigationMenuItemView3.getChildAt(0);


        // pour changer le nom de la liste 1
        appCompatCheckedTextView1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                idCurrentList = 1;

                changerNomListe();

                return true;
            }
        });

        // pour passer sur la liste 1
        appCompatCheckedTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCurrentList = 1;
                refreshAdapterView();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        // pour changer le nom de la liste 2
        appCompatCheckedTextView2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                idCurrentList = 2;

                changerNomListe();

                return true;
            }
        });

        // pour passer sur la liste 2
        appCompatCheckedTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCurrentList = 2;
                refreshAdapterView();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        // pour changer le nom de la liste 2
        appCompatCheckedTextView3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                idCurrentList = 3;

                changerNomListe();

                return true;
            }
        });
        // pour passer sur la liste 3
        appCompatCheckedTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCurrentList = 3;
                refreshAdapterView();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }


    /**
     * Méthode pour rafraichir l'adapter des biens (liste des biens)
     */
    public void refreshAdapterView() {
        // nom de la liste en cours
        ldao.open();
        myToolbar.setTitle(ldao.getNomListeById(idCurrentList));
        ldao.close();

        mAdapter = new BienAdapter(this);
        // ici clear de la liste des biens
        lv_listeBiens.destroyDrawingCache();
        lv_listeBiens.setAdapter(null);
        lv_listeBiens.setVisibility(ListView.INVISIBLE);
        lv_listeBiens.setVisibility(ListView.VISIBLE);

        listeBiens.clear();
        listCorrespondance.clear();
        listeHeader.clear();

        bdao.open();
        // Récupération de la liste en fonction l'id
        listeBiens = bdao.getBiensByListe(idCurrentList);
        bdao.close();

        // si la liste n'est pas vide
        if (!listeBiens.isEmpty()) {
            cdao.open();

            // on passe les infos pour le separateur (categorie)
            // cas particulier pour la première categorie
            mAdapter.addSectionHeaderItem("CATEGORIE_CATEGORIE#~#"+ getResources().getString(R.string.category) +" "+ cdao.getNomCategorieByIdCategorie(listeBiens.get(0).getId_categorie_bien()));
            listeHeader.put(0, listeBiens.get(0).getId_categorie_bien());
            int cpt = 0;
            int idCatEnTete = listeBiens.get(0).getId_categorie_bien();
            String item ;

            cdao.close();

            // on parcours la liste des biens
            for (int i = 0; i < listeBiens.size(); i++) {

                if (idCatEnTete != listeBiens.get(i).getId_categorie_bien()) {
                    cpt++;
                    cdao.open();
                    // on passe les infos pour le separateur (categorie)
                    mAdapter.addSectionHeaderItem("CATEGORIE_CATEGORIE#~#"+ getResources().getString(R.string.category) +" "+ cdao.getNomCategorieByIdCategorie(listeBiens.get(i).getId_categorie_bien()));
                    listeHeader.put(i + cpt, listeBiens.get(i).getId_categorie_bien());
                    cdao.close();

                }
                // on passe les infos pour l'item (bien)
                listCorrespondance.put(mAdapter.getCount(), Integer.valueOf(cpt));
                item = listeBiens.get(i).getNom_bien() + "#~#" + listeBiens.get(i).getDescription_bien() + "#~#" + listeBiens.get(i).getPhoto_bien_principal();
                mAdapter.addItem(item);
                idCatEnTete = listeBiens.get(i).getId_categorie_bien();
            }

            // on attribue l'adapter à la listeView
            lv_listeBiens.setAdapter(mAdapter);
            lv_listeBiens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listeHeader.containsKey(position)) {
                        // pour modifier une categorie
                        Intent i = new Intent(getApplicationContext(), ModifierCategorie.class);
                        i.putExtra("IDCATEGORIE", listeHeader.get(position));
                        startActivity(i);
                    } else {
                        // pour voir un bien (infoBien)
                        Intent i = new Intent(getApplicationContext(), InfosBien.class);
                        i.putExtra("IDBIEN", listeBiens.get((position) - listCorrespondance.get((position)) - 1).getId_bien());
                        startActivity(i);
                    }
                }
            });
        }
    }

    /**
     * Pour ajouter une categorie lorsque l'on clique sur le bouton dans le Drawer
     * @param v
     */
    public void ajouterCategorie(View v) {
        Intent intent = new Intent(this, AjouterCategorie.class);
        startActivity(intent);
    }

    /**
     * Pour exportet des données au foramt CSV lorsque l'on clique sur le bouton dans le Drawer
     * @param v
     */
    public void exporterListe(View v) {
        Intent intent = new Intent(this, ExportListe.class);
        startActivity(intent);

    }

    /**
     * Pour modifier les infos de la personne (utile pour le CSV)
     * @param v
     */
    public void modifierPersonne(View v) {
        Intent intent = new Intent(this, ModifierPersonne.class);
        startActivity(intent);
    }

    /**
     * Pour remplir la BD lors du premier lancement
     */
    public void initialiserBD() {

        // on crée une personne avec tous les champs à vide, cela nous permettra de tester l'existence de la BD pour les futurs lancements.
        pdao.open();
        pdao.creerPersonnePremierLancement();
        pdao.close();

        // création des 3 listes
        Liste liste1 = new Liste(1, "Maison", "La liste de ma maison");
        Liste liste2 = new Liste(2, "Garage", "La liste de mon garage");
        Liste liste3 = new Liste(3, "Magasin", "La liste de mon magasin");

        // ajout des listes
        ldao.open();
        ldao.ajouterListe(liste1);
        ldao.ajouterListe(liste2);
        ldao.ajouterListe(liste3);
        ldao.close();

        // on ajoute 3 catégories
        CategorieDAO categorieDAO = new CategorieDAO(this);
        categorieDAO.open();
        Categorie cuisine = new Categorie(0, "Cuisine", "Tous les objets de la cuisine");
        Categorie salon = new Categorie(0, "Salon", "Tous les objets du Salon");
        Categorie chambre = new Categorie(0, "Chambre", "Tous les objets de la chambre");
        categorieDAO.addCategorie(cuisine);
        categorieDAO.addCategorie(salon);
        categorieDAO.addCategorie(chambre);
        categorieDAO.close();

        Date date = new Date();

        // Création de la classe utilitaire pour les dates
        Utils utils = new Utils(this);
        String dateSimpleDateFormat = utils.getDateSimpleDateFormat();
        SimpleDateFormat sdf = new SimpleDateFormat(dateSimpleDateFormat);
        String dateDuJour = sdf.format(date);

        // texte explicatif
        String explication = "Cet objet est crée au demarrage de l'application, vous pouvez le supprimer en cliquant dessus.";

        // on ajoute 6 bien pour exemple
        Bien bien1 = new Bien(1, "Lunette", dateDuJour, dateDuJour, "", "Légèrement rayées sur le coté", "251", "", "", "", "", 2, explication, "");
        Bien bien2 = new Bien(2, "Frigo", dateDuJour, dateDuJour, "", "", "3599", "", "", "", "", 1, explication, "45DG425845DA");
        Bien bien3 = new Bien(3, "Ordinateur", dateDuJour, dateDuJour, "", "Manque une touche", "1099", "", "", "", "", 3, explication, "515D-TGH2336");
        Bien bien4 = new Bien(4, "Vaisselle", dateDuJour, dateDuJour, "", "Vaisselle de Mémé", "6902", "", "", "", "", 1, explication, "");
        Bien bien5 = new Bien(5, "TV", dateDuJour, dateDuJour, "", "", "350", "", "", "", "", 2, explication, "");
        Bien bien6 = new Bien(6, "Home cinéma", dateDuJour, dateDuJour, "", "Marque Pioneer - Une enceinte grésille un peu", "400", "", "", "", "", 2, explication, "");


        // dans la liste 1
        ArrayList<Integer> listeIdListe1 = new ArrayList<Integer>();
        listeIdListe1.add(1);


        bdao.open();

        bdao.addBien(bien1, listeIdListe1);
        bdao.addBien(bien2, listeIdListe1);
        bdao.addBien(bien3, listeIdListe1);
        bdao.addBien(bien4, listeIdListe1);
        bdao.addBien(bien5, listeIdListe1);
        bdao.addBien(bien6, listeIdListe1);

        bdao.close();
    }

    /**
     * Méthode pour changer le nom d'une liste pré-enregistrée
     */
    public void changerNomListe() {
        final EditText libelle = new EditText(context);
        ldao.open();
        libelle.setText(ldao.getNomListeById(idCurrentList));
        ldao.close();

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(libelle);

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(R.string.title_change_list_name);
        builder.setView(layout);
        // Si on clique sur "OK"
        builder.setPositiveButton(R.string.dialog_positive_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        libelle.setText(libelle.getText().toString().trim());
                        // controle du nom trop long ou déjà existant
                        if (!libelle.getText().toString().equals("")) {
                            if (libelle.getText().length() <= 10) {
                                for (int i = 0; i < 3; i++) {
                                    ldao.open();
                                    if (libelle.getText().toString().toLowerCase().equals(ldao.getNomListeById(i + 1).toLowerCase())) {
                                        nomIdentique = true;
                                        ldao.close();
                                        break;
                                    }
                                }
                                // si le nom est ok
                                if (!nomIdentique) {
                                    ldao.open();
                                    ldao.modifierListe(idCurrentList, libelle.getText().toString(), "");
                                    switch (idCurrentList) {
                                        case 1:
                                            navigationView.getMenu().findItem(R.id.liste1).setTitle(libelle.getText().toString());
                                            break;
                                        case 2:
                                            navigationView.getMenu().findItem(R.id.liste2).setTitle(libelle.getText().toString());
                                            break;
                                        case 3:
                                            navigationView.getMenu().findItem(R.id.liste3).setTitle(libelle.getText().toString());
                                            break;
                                    }
                                    refreshAdapterView();
                                    ldao.close();
                                    // sinon, le nom existe déjà
                                } else {
                                    nomIdentique = false;
                                    Toast toast = Toast.makeText(context, R.string.list_name_already_exists, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                // le nom est trop long
                            } else {
                                Toast toast = Toast.makeText(context, R.string.list_name_too_long, Toast.LENGTH_LONG);
                                toast.show();
                            }
                            // le nom est vide
                        } else {
                            Toast toast = Toast.makeText(context, R.string.list_name_empty, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
        // annuler
        builder.setNegativeButton(R.string.dialog_negative_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog = builder.create();
        dialog.show();
    }

}