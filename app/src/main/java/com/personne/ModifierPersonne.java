package com.personne;
//
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class ModifierPersonne extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Menu m;
    private TextView textViewDate;
    private String regexDate = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
    private Boolean dejaEnBase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_personne);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Informations du compte");
        setSupportActionBar(myToolbar);

        PersonneDAO personneDAO = new PersonneDAO(this);
        personneDAO.open();
        Personne per = personneDAO.getPersonne(1);
        personneDAO.close();

        if (per!=null){
            dejaEnBase = true;
        }

        Log.e("per",per.toString());


        TextView textViewNomPersonne = (TextView) findViewById(R.id.editNom);
        TextView textViewPrenomPersonne = (TextView) findViewById(R.id.editPrenom);
        TextView textViewAddress =(TextView) findViewById(R.id.editAdress);
        TextView textViewEmail = (TextView) findViewById(R.id.editEmail);
        TextView textViewPhoneNumber = (TextView) findViewById(R.id.editPhon_Number);
        textViewDate = (TextView) findViewById(R.id.editTextDate);

        textViewDate.setText(per.getDate());
        textViewNomPersonne.setText(per.getNom());
        textViewPrenomPersonne.setText(per.getPrenom());
        textViewAddress.setText(per.getAddress());
        textViewEmail.setText(per.getMail());
        Log.e("aa","phone"+per.getPhoneNumber());
        textViewPhoneNumber.setText(per.getPhoneNumber());

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
                        String day ;
                        if(dayOfMonth <10){
                            day = "0"+dayOfMonth;
                        } else {
                            day = String.valueOf(dayOfMonth);
                        }

                        String monthFormat ;
                        if((month +1) <10){
                            monthFormat = "0"+month;
                        } else {
                            monthFormat = String.valueOf(month);
                        }
                        textViewDate.setText(day + "/" +monthFormat+ "/" + year);
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

        Boolean erreurSaisieDate = false;
        if (date != null && !date.equals("")) {
            if (!date.matches(regexDate)){
                Toast.makeText(this, "La date doit être au format jj/mm/aaaa", Toast.LENGTH_SHORT).show();
                 erreurSaisieDate = true;
            }

        }

        if(!erreurSaisieDate && !nomPersonne.equals("") && !prenomPersonne.equals("") && !date.equals("") && !address.equals("") && validEmail(email) && validPhone(phoneNumber)) {
            PersonneDAO personneDAO = new PersonneDAO(this);
            personneDAO.open();
            if (dejaEnBase) {
                personneDAO.modPersonne(1, nomPersonne, prenomPersonne, date, address, email, phoneNumber);
            } else {
                personneDAO.insertPersonne(nomPersonne, prenomPersonne, date, address, email, phoneNumber);
            }
            personneDAO.close();
            finish();
        } else {
            Toast toast = Toast.makeText(this, "Tous les champs doivent être remplis correctement", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean validPhone(String phone) {
        Pattern pattern = Pattern.compile("(0|\\+33|0033)[1-9][0-9]{8}");
        return pattern.matcher(phone).matches();
    }
}
