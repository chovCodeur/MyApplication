package com.example.michelparis.myapplication;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class ModifierPersonne extends AppCompatActivity {

    Button ajoutInfos;
    EditText editNom;
    EditText editPrenom;
    EditText editMail;
    EditText editPhoneNumber;
    EditText editAddress;
    EditText editTextdate;
    DatePickerDialog datePickerDialog;
   // DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_personne);

        editNom = (EditText)findViewById(R.id.editNom);
        editPrenom = (EditText)findViewById(R.id.editPrenom);
        editAddress = (EditText)findViewById(R.id.editAdress);
        editPhoneNumber = (EditText)findViewById(R.id.editPhon_Number);
        editMail = (EditText)findViewById(R.id.editEmail);
        editTextdate = (EditText)findViewById(R.id.editTextDate);


       /* editNom.setText(personne.nom);
        editPrenom.setText(personne.prenom);
        editAddress.setText(personne.address);
        editMail.setText(personne.mail);
        editPhoneNumber.setText(personne.phoneNumber);*/

        editTextdate = (EditText)findViewById(R.id.editTextDate);
        editTextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(ModifierPersonne.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextdate.setText(dayOfMonth + "/" +(month + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        ajoutInfos = (Button) findViewById(R.id.buttonAjoutInfos);
        ajoutInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nom = editNom.getText().toString();
                String prenom = editPrenom.getText().toString();
                String address = editAddress.getText().toString();
                String mail = editMail.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();
                String date = editTextdate.getText().toString();
            }
        });

    }
}
