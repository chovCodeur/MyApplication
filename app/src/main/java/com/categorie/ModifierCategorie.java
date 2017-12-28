package com.categorie;

/**
 * Classe permettant la modification des informations que contient une catégorie.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.MainActivity;
import com.application.inventaire.R;
import com.bien.AjouterBien;
import com.dao.CategorieDAO;

import java.util.ArrayList;

public class ModifierCategorie extends AppCompatActivity {

    //variables des classes
    private Button modifCategorie;
    private EditText editCategorie;
    private EditText editDescription;
    private int idcat;
    private CategorieDAO cdao;
    private Menu m;
    private String nomcat;
    private String nomdescrip;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_categorie);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.toolbar_title_modifier_categorie));
        setSupportActionBar(myToolbar);
// intenciation de la DAO
        cdao = new CategorieDAO(this);

        editCategorie = (EditText) findViewById(R.id.editCategorie);
        editDescription = (EditText) findViewById(R.id.editDescription);
        modifCategorie = (Button) findViewById(R.id.buttonModifierCategorie);

        // Récupération des paramètres en provenance des autres activités
        Bundle extras = getIntent().getExtras();
//on stocke l'id de la catégorie courante
        if (extras != null) {
            idcat = extras.getInt("IDCATEGORIE");
            Log.d("IDCATEGORIE", String.valueOf(idcat));
            //Si l'id de la catégorie n'est pas null
            if (idcat != 0) {
                //On ouvre le DAO et on récupère son id
                cdao.open();
                //categorie = cdao.getCategorieById(idcat);
                // On récupère les id de la catégorie du bien
                nomcat = cdao.getNomCategorieByIdCategorie(idcat);
                nomdescrip = cdao.getDescriptionCategorieByIdCategorie(idcat);

                Log.d("NOMCATEGORIE", cdao.getNomCategorieByIdCategorie(idcat));
                cdao.close();
            }
        }


        editCategorie.setText(nomcat);
        editDescription.setText(nomdescrip);
//Puis on passe à la modification proprement dit et l'on vérifie avec les valeurs existantes dans la table

        modifCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomCategorie = editCategorie.getText().toString();
                String description = editDescription.getText().toString();
                //Intent intent = new Intent(ModifierCategorie.this, MainActivity.class);
                //startActivity(intent);

                CategorieDAO categorieDAO = new CategorieDAO(context);
                ArrayList<Categorie> categories = new ArrayList<Categorie>();

                categorieDAO.open();
                categories = categorieDAO.getAllCategorie();
                categorieDAO.close();

                Boolean erreur = false;
                for (Categorie cate : categories) {
                    if (cate.getNom_Categorie().trim().toLowerCase().equals(nomCategorie.trim().toLowerCase())) {
                        erreur = true;
                    }

                }

                if (!nomCategorie.equals("")) {
                    if (!erreur) {
                        categorieDAO.open();
                        categorieDAO.modCategorie(idcat, nomCategorie, description);
                        categorieDAO.close();

                        Toast.makeText(context, getResources().getString(R.string.message_categorie_added_1_2) +" "+nomCategorie +" "+ getResources().getString(R.string.modified_categorie), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.message_categorie_added_1_2) +" "+ nomCategorie +" "+ getResources().getString(R.string.categorie_exists), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, R.string.control_item_name, Toast.LENGTH_SHORT).show();

                }
            }
        });
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

                intent = new Intent(this, AjouterBien.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}