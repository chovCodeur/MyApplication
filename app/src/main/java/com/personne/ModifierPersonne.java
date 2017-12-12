package com.personne;
//
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bien.AjouterBien;
import com.application.MainActivity;
import com.application.inventaire.R;
import com.dao.PersonneDAO;

import java.util.Calendar;

public class ModifierPersonne extends AppCompatActivity {

    private Button ajoutInfos;
    private EditText editNom;
    private EditText editPrenom;
    private EditText editMail;
    private EditText editPhoneNumber;
    private EditText editAddress;
    private EditText editTextdate;
    private DatePickerDialog datePickerDialog;
   // DatePicker datePicker;
    private Menu m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_personne);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Informations du compte");
        setSupportActionBar(myToolbar);

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
                        editTextdate.setText(dayOfMonth + "-" +(month + 1) + "-" + year);
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

                Intent intent = new Intent(ModifierPersonne.this, MainActivity.class);
                startActivity(intent);
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

    public void onClickAjoutInfos (View view){

        TextView textViewNomPersonne = (TextView) findViewById(R.id.editNom);
        TextView textViewPrenomPersonne = (TextView) findViewById(R.id.editPrenom);
        TextView textViewDate =(TextView) findViewById(R.id.editTextDate);
        TextView textViewAddress =(TextView) findViewById(R.id.editAdress);
        TextView textViewEmail = (TextView) findViewById(R.id.editEmail);
        TextView textViewPhoneNumber = (TextView) findViewById(R.id.editPhon_Number);



        String nomPersonne = textViewNomPersonne.getText().toString();
        String prenomPersonne = textViewPrenomPersonne.getText().toString();
        String date = textViewDate.getText().toString().replace("-","/");
        String address = textViewAddress.getText().toString();
        String email = textViewEmail.getText().toString();
        String phoneNumber = textViewPhoneNumber.getText().toString();


        Personne personne = new Personne(1, nomPersonne, prenomPersonne, date, address, email, phoneNumber);

        PersonneDAO personneDAO = new PersonneDAO(this);
        personneDAO.open();
        //personneDAO.modPersonne(personne);
        personneDAO.modPersonne(1, nomPersonne, prenomPersonne, address, email, phoneNumber, date);
        personneDAO.close();
    }
}
