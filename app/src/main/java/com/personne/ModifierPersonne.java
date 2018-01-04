package com.personne;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.application.MainActivity;
import com.application.inventaire.R;
import com.bien.AjouterBien;
import com.dao.PersonneDAO;
import com.utils.Utils;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Classe permettant de modifier une personne
 */
public class ModifierPersonne extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Menu m;
    private TextView textViewDate;
    private String regexDate;
    private Boolean dejaEnBase = false;
    private Utils utils;


    /**
     * Méthode appelée à la creation de l'activité
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_personne);

        // nom de l'activité
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.toolbar_title_modifier_personne));
        setSupportActionBar(myToolbar);

        // Création de l'utilitaire et récupération de la regexp pour les dates
        utils = new Utils(this);
        regexDate = utils.getRegexDate();

        PersonneDAO personneDAO = new PersonneDAO(this);

        // on récupére la personne
        personneDAO.open();
        Personne per = personneDAO.getPersonne(1);
        personneDAO.close();

        // si elle est en base, on change le booléen
        if (per != null) {
            dejaEnBase = true;
        }


        // récupére les objets de la vue pour y mettre les infos
        TextView textViewNomPersonne = (TextView) findViewById(R.id.editNom);
        TextView textViewPrenomPersonne = (TextView) findViewById(R.id.editPrenom);
        TextView textViewAddress = (TextView) findViewById(R.id.editAdress);
        TextView textViewEmail = (TextView) findViewById(R.id.editEmail);
        TextView textViewPhoneNumber = (TextView) findViewById(R.id.editPhon_Number);
        textViewDate = (TextView) findViewById(R.id.editTextDate);
        TextView textViewNumberContrat = (TextView) findViewById(R.id.editNumero_contrat);

        textViewDate.setText(per.getDate());
        textViewNomPersonne.setText(per.getNom());
        textViewPrenomPersonne.setText(per.getPrenom());
        textViewAddress.setText(per.getAddress());
        textViewEmail.setText(per.getMail());
        textViewPhoneNumber.setText(per.getPhoneNumber());
        textViewNumberContrat.setText(per.getNumero_contrat());

        // éditer la date avec une datepicker et le choix du format à stocker dans la table
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
                        String day;
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = String.valueOf(dayOfMonth);
                        }

                        String monthFormat;
                        if ((month + 1) < 10) {
                            monthFormat = "0" + (month + 1);
                        } else {
                            monthFormat = String.valueOf(month + 1);
                        }
                        switch (utils.getLocale()) {
                            case "US" : textViewDate.setText(monthFormat + "/" + day + "/" + year);
                                break;
                            default : textViewDate.setText(day + "/" + monthFormat + "/" + year);
                                break;
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    /**
     * Procédure gérant l'action du bouton physique retour du téléphone.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Méthode permettant d'assigner le menu et ses options à l'activité.
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        m = menu;

        return true;
    }

    /**
     * Méthode pour le menu supérieur
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                // retour accueil
                Intent intenthome = new Intent(getApplicationContext(), MainActivity.class);
                intenthome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenthome);

                return true;

            case R.id.plus:
                // ajouter un bien
                intent = new Intent(this, AjouterBien.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode pour modifier les information
     * @param view
     */
    public void onClickAjoutInfos(View view) {

        TextView textViewNomPersonne = (TextView) findViewById(R.id.editNom);
        TextView textViewPrenomPersonne = (TextView) findViewById(R.id.editPrenom);
        TextView textViewAddress = (TextView) findViewById(R.id.editAdress);
        TextView textViewEmail = (TextView) findViewById(R.id.editEmail);
        TextView textViewPhoneNumber = (TextView) findViewById(R.id.editPhon_Number);
        TextView textViewNumeroContrat = (TextView) findViewById(R.id.editNumero_contrat);

        String nomPersonne = textViewNomPersonne.getText().toString();
        String prenomPersonne = textViewPrenomPersonne.getText().toString();
        String date = textViewDate.getText().toString();
        String address = textViewAddress.getText().toString();
        String email = textViewEmail.getText().toString();
        String phoneNumber = textViewPhoneNumber.getText().toString();
        String numero_Contrat = textViewNumeroContrat.getText().toString();

        Boolean erreurSaisieDate = false;
        // on controle la date
        if (date != null && !date.equals("")) {
            if (!date.matches(regexDate)) {
                Toast.makeText(this, R.string.date_format_message, Toast.LENGTH_SHORT).show();
                erreurSaisieDate = true;
            }

        }

        // les champs ne peuvent pas être vides et doivent corrsondre aux regex (mail et telephone)
        if (!erreurSaisieDate && !nomPersonne.equals("") && !prenomPersonne.equals("") && !date.equals("") && !address.equals("") && validEmail(email) && validPhone(phoneNumber)) {
            PersonneDAO personneDAO = new PersonneDAO(this);
            personneDAO.open();

            // on modifie la personne ou on la crée (théoriquement, toujours une modification)
            if (dejaEnBase) {
                personneDAO.modPersonne(1, nomPersonne, prenomPersonne, date, address, email, phoneNumber, numero_Contrat);
            } else {
                personneDAO.insertPersonne(nomPersonne, prenomPersonne, date, address, email, phoneNumber, numero_Contrat);
            }
            personneDAO.close();
            finish();
        } else {
            // tous les champs sont obligatoires
            Toast toast = Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Permet de verifier le patern de l'adresse mail
     * @param email
     * @return true si ok false sinon
     */
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    /**
     * Permet de verifier le patern du numéro de téléphone
     * @param phone
     * @return true si ok false sinon
     */
    private boolean validPhone(String phone) {
        Pattern pattern = Pattern.compile("(0|\\+33|0033)[1-9][0-9]{8}");
        return pattern.matcher(phone).matches();
    }
}
//
