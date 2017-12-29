package com.liste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.application.inventaire.R;
import com.categorie.Categorie;

import java.util.ArrayList;

/**
 * Created by Thib on 14/12/2017.
 */

/**
 * Adapteur pour les catégories d'une liste
 */
public class ListeAdapter extends ArrayAdapter<Categorie> {
    private Context mContext;
    private ArrayList<Categorie> listState;
    private ArrayList<Categorie> listSelected;

    /**
     * Constructeur de la ListeAdapter
     * @param context
     * @param resource
     * @param objects
     * @param listeSelected
     */
    public ListeAdapter(Context context, int resource, ArrayList<Categorie> objects, ArrayList<Categorie> listeSelected) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = objects;
        this.listSelected = listeSelected;
    }

    /**
     * Methode permettant de récupérer la DropDownView
     * @param position
     * @param convertView
     * @param parent
     * @return getCustomView
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * Méthode permettant de récupérer la view
     * @param position
     * @param convertView
     * @param parent
     * @return getConvertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * Classe récupérant automatiquement une vue spécifique au sein de l'adapteur
     * @param position
     * @param convertView
     * @param parent
     * @return convertView
     */
    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        // si la vue est nulle
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.mTextView.setText(listState.get(position).getNom_Categorie());

            // On cherche la position dans la liste
            for (Categorie categorieSelectionee : listSelected) {
                if (categorieSelectionee.getId_Categorie() == getItem(position).getId_Categorie()) {
                    holder.mCheckBox.setChecked(true);
                    listState.get(position).setSelected(true);
                }
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //On vérifie si les checkboxs sont selectionnés ou non
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listState.get(position).setSelected(true);
                } else {
                    listState.get(position).setSelected(false);
                }
            }
        });
        return convertView;
    }

    /**
     * Classe anonyme permettant de personnaliser une ligne
     */
    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}

