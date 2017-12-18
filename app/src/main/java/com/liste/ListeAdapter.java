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

import java.util.ArrayList;
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
            this.listState = (ArrayList<Categorie>) objects;
            this.listSelected=listeSelected;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(final int position, View convertView,
                                  ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(mContext);
                convertView = layoutInflator.inflate(R.layout.spinner_item, null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView
                        .findViewById(R.id.text);
                holder.mCheckBox = (CheckBox) convertView
                        .findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextView.setText(listState.get(position).getNom_Categorie());

            // To check weather checked event fire from getview() or user input
//            isFromView = true;
//            holder.mCheckBox.setChecked(listState.get(position).isSelected());
//            isFromView = false;

//            if ((position == -1)) {
//                holder.mCheckBox.setVisibility(View.INVISIBLE);
//            } else {
                holder.mCheckBox.setVisibility(View.VISIBLE);
//            }
            int i=0;
            boolean trouve=false;
            Log.d("passeparla", String.valueOf(listSelected.size()));
            while(i<listSelected.size() && !trouve){
                Log.d("leCheck", String.valueOf(getItem(position).getId_Categorie())+" / "+String.valueOf(listSelected.get(i).getId_Categorie()));
                holder.mCheckBox.setChecked(false);
                if(getItem(position).getId_Categorie()==listSelected.get(i).getId_Categorie()){
                    Log.d("AAAAAH", String.valueOf(getItem(position).getNom_Categorie()));
                    holder.mCheckBox.setChecked(true);
                    listState.get(position).setSelected(true);
                    trouve=true;
                }
                i++;
            }

            //holder.mCheckBox.setTag(position);
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //int getPosition = (Integer) buttonView.getTag();
                    if(isChecked){
                        listState.get(position).setSelected(true);
                        Log.d("Testlaliste", listState.get(position).toString());
                    }
                    else {
                        listState.get(position).setSelected(false);
                        Log.d("TESTlaliste2", listState.get(position).toString());
                    }
                    Log.d("lalistefinal", listSelected.toString());
                }
            });
            return convertView;
        }

        public ArrayList<Categorie> getListSelected(){
            return listSelected;
        }

        private class ViewHolder {
            private TextView mTextView;
            private CheckBox mCheckBox;
        }
    }

