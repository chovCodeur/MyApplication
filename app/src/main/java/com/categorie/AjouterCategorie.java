package com.categorie;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.application.inventaire.R;
import com.dao.CategorieDAO;

import java.util.ArrayList;

/**
 * Classe correspondant à l'activité d'ajout d'une catégorie
 */


public class AjouterCategorie extends AppCompatActivity {

    private Menu m;

    /**
     * Méthode appelée à la creation de l'activité
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_categorie);

        // titre de l'activité
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.toolbar_title_ajouter_categorie));
        setSupportActionBar(myToolbar);

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
     * Méthode pour le menu supérieur
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                // retour à l'accueil
                Intent intenthome = new Intent(getApplicationContext(), com.application.MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);
                return true;

                // ajout d'un bien
            case R.id.plus:
                intent = new Intent(this, com.bien.AjouterBien.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Pour l'ajout d'une categorie
     * @param view
     */
    public void onClickAddCategorie(View view) {
        TextView textViewNomCategorie = (TextView) findViewById(R.id.editCategorie);
        TextView textViewDescription = (TextView) findViewById(R.id.editDescription);
        CategorieDAO categorieDAO = new CategorieDAO(this);

        String nomCategorie = textViewNomCategorie.getText().toString();

        // on récupére les catégories existantes
        categorieDAO.open();
        ArrayList<Categorie> categories = categorieDAO.getAllCategorie();
        categorieDAO.close();

        Boolean erreur = false;
        // on regarde si la catégorie n'existe pas encore
        for (Categorie cate : categories) {
            if (cate.getNom_Categorie().trim().toLowerCase().equals(nomCategorie.trim().toLowerCase())) {
                erreur = true;
            }
        }
        String description = textViewDescription.getText().toString();

        //si le nom est vide
        if (!textViewNomCategorie.getText().toString().equals("")) {
            // si la catégorie n'existe pas
            if (!erreur) {

                categorieDAO.open();
                Categorie categorie = new Categorie(0, nomCategorie, description);
                categorieDAO.addCategorie(categorie);
                categorieDAO.close();
                Toast.makeText(this, getResources().getString(R.string.message_categorie_added_1_2) + nomCategorie + getResources().getString(R.string.message_categorie_added_2_2), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Si la catégorie existe déjà
                Toast.makeText(this, getResources().getString(R.string.message_categorie_added_1_2) + nomCategorie + getResources().getString(R.string.categorie_exists), Toast.LENGTH_SHORT).show();
            }
        } else {
            //si le nom de la catégorie est vide
            Toast.makeText(this, R.string.control_item_name, Toast.LENGTH_SHORT).show();
        }
    }
}