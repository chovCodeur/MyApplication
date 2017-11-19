package com.example.michelparis.myapplication;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 16/11/2017.
 */

public class BienAdapter extends ArrayAdapter<Bien>{


    /**
     * Constructeur de la classe ArticleAdapter
     * @param context Context le contexte
     * @param biens ArrayList<Bien> : la liste d'articles
     */
    public BienAdapter(Context context, ArrayList<Bien> biens) {
        super(context, 0, biens);
    }

    /**
     * Méthode permettant de positionner les articles dans la vue
     * @param position int : la position de l'article
     * @param convertView View
     * @param parent ViewGroup
     * @return
     */
    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bien bien = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cellule_article, parent, false);
        }
        TextView articleNom = (TextView) convertView.findViewById(R.id.nomArticle);
        TextView articleNbr = (TextView) convertView.findViewById(R.id.nbArticle);
        articleNom.setText(article.getNomArticle());
        articleNbr.setText("Quantité : "+String.valueOf(article.getNbArticle()));
        return convertView;
    }*/
}
