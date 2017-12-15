package com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.application.inventaire.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;


import java.io.File;

/**
 * Created by Tristan on 25/11/2017.
 */

/**
 * Classe permettant l'affichage d'un fichier format PDF.
 */
public class ReadPDF extends AppCompatActivity implements OnPageChangeListener {

    // Variables de classe
    String pdfName;
    Integer pageNumber = 1;
    PDFView pdfView;

    /**
     * Procédure lancée à la création de l'activité.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pdf);

        pdfView = (PDFView) findViewById(R.id.pdfview);

        // On récupère les informations transmises par les autres activités
        Bundle extras = getIntent().getExtras();

        // Si des informations existent, on stocke le nom du PDF et on l'affiche
        if(extras != null) {
            pdfName = extras.getString("nomPDF");
            display(pdfName, true);
        }
    }

    /**
     * Méthode permettant d'afficher un PDF.
     * @param assetFileName String : contient le nom du PDF à lire.
     * @param jumpToFirstPage Boolean : contient la page à laquelle commencer l'affichage du PDF.
     */
    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;

        File file = new File (assetFileName);
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .load();
    }

    /**
     * Méthode permettant de gérer le numéro de page courant lors de la lecture d'un PDF.
     * @param page int : contient le numéro de la page lue.
     * @param pageCount int.
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    /**
     * Procédure gérant l'action du bouton physique retour du téléphone.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}