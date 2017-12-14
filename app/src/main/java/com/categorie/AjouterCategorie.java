package com.categorie;
//
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.application.MainActivity;
import com.application.inventaire.R;
import com.bien.AjouterBien;
import com.dao.CategorieDAO;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AjouterCategorie extends AppCompatActivity {

    private Menu m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_categorie);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Ajouter une catégorie");
        setSupportActionBar(myToolbar);

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
                Intent intenthome = new Intent(getApplicationContext(), com.application.MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);

                return true;

            case R.id.plus:

                intent = new Intent(this, com.bien.AjouterBien.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickAddCategorie(View view){
        TextView textViewNomCategorie = (TextView) findViewById(R.id.editCategorie);
        TextView textViewDescription = (TextView) findViewById(R.id.editDescription);
        CategorieDAO categorieDAO = new CategorieDAO(this);

        ArrayList<Categorie> categories = new ArrayList<Categorie>();


        String nomCategorie = textViewNomCategorie.getText().toString();

        categorieDAO.open();
        categories = categorieDAO.getAllCategorie();
        categorieDAO.close();

        Boolean erreur = false;
        for (Categorie cate: categories) {
            if(cate.getNom_Categorie().trim().toLowerCase().equals(nomCategorie.trim().toLowerCase())){
                erreur = true;
            }

        }
        String description = textViewDescription.getText().toString();

        if(!textViewNomCategorie.getText().toString().equals("")) {
            if (!erreur) {
                categorieDAO.open();
                Categorie categorie = new Categorie(0, nomCategorie, description);
                categorieDAO.addCategorie(categorie);
                categorieDAO.close();

                Toast.makeText(this, "La catégorie " + nomCategorie + " a bien été ajoutée", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "La catégorie " + nomCategorie + " existe déjà", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Le nom ne peut pas être vide",Toast.LENGTH_SHORT).show();

        }
    }
}
