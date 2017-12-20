package com.categorie;
//

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


    private Button modifCategorie;
    private EditText editCategorie;
    private EditText editDescription;
    private int idcat;
    private CategorieDAO cdao;
    private Categorie categorie;
    private Menu m;
    private String nomcat;
    private String nomdescrip;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_categorie);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Modifier une catégorie");
        setSupportActionBar(myToolbar);

        cdao = new CategorieDAO(this);

        editCategorie = (EditText) findViewById(R.id.editCategorie);
        editDescription = (EditText) findViewById(R.id.editDescription);
        modifCategorie = (Button) findViewById(R.id.buttonModifierCategorie);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            idcat = extras.getInt("IDCATEGORIE");
            Log.d("IDCATEGORIE", String.valueOf(idcat));
            if (idcat != 0) {
                cdao.open();
                //categorie = cdao.getCategorieById(idcat);
                nomcat = cdao.getNomCategorieByIdCategorie(idcat);
                nomdescrip = cdao.getDescriptionCategorieByIdCategorie(idcat);

                Log.d("NOMCATEGORIE", cdao.getNomCategorieByIdCategorie(idcat));
                cdao.close();
            }
        }
        editCategorie.setText(nomcat);
        editDescription.setText(nomdescrip);

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

                        Toast.makeText(context, "La catégorie " + nomCategorie + " a bien été modifiée", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, "La catégorie " + nomCategorie + " existe déjà", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Le nom ne peut pas être vide", Toast.LENGTH_SHORT).show();

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
