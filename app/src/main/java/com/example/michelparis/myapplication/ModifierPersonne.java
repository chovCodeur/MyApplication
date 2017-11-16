package com.example.michelparis.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class ModifierPersonne extends AppCompatActivity {

    Button ajoutInfos;
    EditText editNom;
    EditText editPrenom;
    EditText editMail;
    EditText editPhoneNumber;
    EditText editAddress;
    DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_personne);

        editNom = (EditText)findViewById(R.id.editNom);
        editPrenom = (EditText)findViewById(R.id.editPrenom);
        editAddress = (EditText)findViewById(R.id.editAdress);
        editPhoneNumber = (EditText)findViewById(R.id.editPhon_Number);
        editMail = (EditText)findViewById(R.id.editEmail);
        datePicker = (DatePicker) findViewById(R.id.datePicker);


        editNom.setText(personne.nom);
        editPrenom.setText(personne.prenom);
        editAddress.setText(personne.address);
        editMail.setText(personne.mail);
        editPhoneNumber.setText(personne.phoneNumber);
        datePicker.setOnDateChangedListener(personne.datePicker);





        ajoutInfos = (Button) findViewById(R.id.buttonAjoutInfos);
        ajoutInfos.setOnClickListener(this);
    }
}
