package com.utils;

import android.content.Context;

/**
 * Created by rodnor on 01/12/2017.
 */

public class Utils {
    private String regexDate;
    private String dateSimpleDateFormat;
    private String dateStockageFichier;
    private String locale;

    public Utils(Context context) {
        locale = context.getResources().getConfiguration().locale.getCountry();

        switch (locale) {
            case "US" : regexDate = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
                dateSimpleDateFormat = "MM/dd/yyyy";
                dateStockageFichier = "MMddyyyyhhmmss";
                break;
            default:
                regexDate = "^(0[0-9]||1[0-2]).([0-2][0-9]||3[0-1]).([0-9][0-9])?[0-9][0-9]$";
                dateSimpleDateFormat = "dd/MM/yyyy";
                dateStockageFichier = "ddMMyyyyhhmmss";
                break;
        }
    }

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
