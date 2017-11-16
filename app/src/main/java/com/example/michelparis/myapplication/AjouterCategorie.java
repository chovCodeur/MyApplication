package com.example.michelparis.myapplication;
//
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AjouterCategorie extends AppCompatActivity {


    Button addCategorie;
    EditText editCategorie;
    EditText editDescriptif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_categorie);


        editCategorie = (EditText) findViewById(R.id.editCategorie);
        editDescriptif = (EditText) findViewById(R.id.editDescriptif);

        addCategorie = (Button) findViewById(R.id.buttonAddCategorie);
        addCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String categorie = editCategorie.getText().toString();
                String descriptif = editDescriptif.getText().toString();
            }
        });


        /*editCategorie.setText(categorie.categorie);
        editDescriptif.setText(categorie.descriptif);*/

    }

}
