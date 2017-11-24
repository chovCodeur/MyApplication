package com.example.michelparis.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AjouterCategorie extends AppCompatActivity {


    Button addCategorie;
    EditText editCategorie;
    EditText editDescription;
    private Menu m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_categorie);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Ajouter une catégorie");
        setSupportActionBar(myToolbar);

        editCategorie = (EditText) findViewById(R.id.editCategorie);
        editDescription = (EditText) findViewById(R.id.editDescription);
        addCategorie = (Button) findViewById(R.id.buttonAddCategorie);
        addCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String categorie = editCategorie.getText().toString();
                String description = editDescription.getText().toString();
                Intent intent = new Intent(AjouterCategorie.this, MainActivity.class);
                    startActivity(intent);

            }
        });


        /*editCategorie.setText(categorie.categorie);
        editDescription.setText(categorie.description);*/

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

}
