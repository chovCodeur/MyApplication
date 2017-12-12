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
import android.widget.Toast;

import com.bien.AjouterBien;
import com.application.MainActivity;
import com.application.inventaire.R;
import com.dao.PersonneDAO;

import java.util.Calendar;

public class ModifierPersonne extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Menu m;
    private TextView textViewDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_personne);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Informations du compte");
        setSupportActionBar(myToolbar);

        textViewDate =(TextView) findViewById(R.id.editTextDate);
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(ModifierPersonne.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewDate.setText(dayOfMonth + "/" +(month + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
        TextView textViewAddress =(TextView) findViewById(R.id.editAdress);
        TextView textViewEmail = (TextView) findViewById(R.id.editEmail);
        TextView textViewPhoneNumber = (TextView) findViewById(R.id.editPhon_Number);

        String nomPersonne = textViewNomPersonne.getText().toString();
        String prenomPersonne = textViewPrenomPersonne.getText().toString();
        String date = textViewDate.getText().toString();
        String address = textViewAddress.getText().toString();
        String email = textViewEmail.getText().toString();
        String phoneNumber = textViewPhoneNumber.getText().toString();


        Personne personne = new Personne(1, nomPersonne, prenomPersonne, date, address, email, phoneNumber);

        if(!nomPersonne.equals("") && !prenomPersonne.equals("") && !date.equals("") && !address.equals("") && email.equals("") && phoneNumber.equals("")) {
            PersonneDAO personneDAO = new PersonneDAO(this);
            personneDAO.open();
            personneDAO.modPersonne(1, nomPersonne, prenomPersonne, address, email, phoneNumber, date);
            personneDAO.close();
            finish();
        } else {
            Toast toast = Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
