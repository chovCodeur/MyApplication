package com.liste;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.application.inventaire.R;
import com.categorie.Categorie;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thib on 14/12/2017.
 */

public class ListeAdapter extends ArrayAdapter<Categorie> {
    private Context mContext;
    private ArrayList<Categorie> listState;
    private ArrayList<Categorie> listSelected;


    public ListeAdapter(Context context, int resource, ArrayList<Categorie> objects, ArrayList<Categorie> listeSelected) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = objects;
        this.listSelected = listeSelected;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.e("aa","nb");
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {

            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.mTextView.setText(listState.get(position).getNom_Categorie());

            for (Categorie categorieSelectionee: listSelected) {
                if(categorieSelectionee.getId_Categorie() == getItem(position).getId_Categorie()){
                    holder.mCheckBox.setChecked(true);
                    listState.get(position).setSelected(true);
                }
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //int getPosition = (Integer) buttonView.getTag();
                if (isChecked) {
                    listState.get(position).setSelected(true);
                } else {
                    listState.get(position).setSelected(false);
                }
            }
        });
        return convertView;
    }


    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}

