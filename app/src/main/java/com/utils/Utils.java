package com.utils;

import android.content.Context;

/**
 * Created by rodnor on 01/12/2017.
 */

/**
 * Classe permettant de gérer le changement de langage au sein de l'application.
 */
public class Utils {

    // Variables de classe
    private String regexDate;
    private String dateSimpleDateFormat;
    private String dateStockageFichier;
    private String locale;

    /**
     * Constructeur de classe.
     * @param context Context de l'application.
     */
    public Utils(Context context) {
        // On récupère la locale du système qui permet de savoir en quelle langue le système s'exécute actuellement
        locale = context.getResources().getConfiguration().locale.getCountry();

        // Suivant la langue d'exécution courante
        switch (locale) {
            // Si c'est en anglais américain, on applique la regexp américaine pour le test de la date ainsi que les formats de date américains avec mois/jour/année
            case "US" : regexDate = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
                dateSimpleDateFormat = "MM/dd/yyyy";
                dateStockageFichier = "MMddyyyyhhmmss";
                break;
            // Si la langue est autre que l'anglais américain, on applique la regexp et les formats de date de style jour/mois/année
            default:
                regexDate = "^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$";
                dateSimpleDateFormat = "dd/MM/yyyy";
                dateStockageFichier = "ddMMyyyyhhmmss";
                break;
        }
    }

    // Getters de classe

    public String getRegexDate() {
        return this.regexDate;
    }

    public String getDateSimpleDateFormat() {
        return dateSimpleDateFormat;
    }

    public String getDateStockageFichier() {
        return dateStockageFichier;
    }

    public String getLocale() {
        return locale;
    }
}
