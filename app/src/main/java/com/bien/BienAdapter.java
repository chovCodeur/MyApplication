package com.bien;

/**
 * Created by Kevin on 19/11/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.inventaire.R;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Classe permettant de créer un adapter pour la liste des bien
 */
public class BienAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private Context context;

    private LayoutInflater mInflater;

    /**
     * Constructeur
     * @param context
     */
    public BienAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    /**
     * Pour ajouter un bien (item)
     * @param item
     */
    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    /**
     * Pour ajouter un séparateur (une catégorie)
     * @param item
     */
    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    /**
     * Pour connaitre le type de ligne (separateur ou item)
     * @return 2
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * Pour savoir le type de ligne (separateur ou item)
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    /**
     * Pour connaitre la taille de la liste de l'adapteur
     * @return taille
     */
    @Override
    public int getCount() {
        return mData.size();
    }

    /**
     * Pour connaitre
     * @param position
     * @return
     */
    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    /**
     *
     * @param position
     * @return la position dans la liste
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Méthode appelée automatiquement par l'adapteur
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        //pour connaitre la position dans la liste
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            // premier passage
            holder = new ViewHolder();
            switch (rowType) {
                // cas d'un item (bien)
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_bien, null);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageBien);
                    holder.textView = (TextView) convertView.findViewById(R.id.nomBien);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.descriptionBien);
                    break;

                // cas d'un separateur (categorie)
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.separator_bien, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            // on conserve l'ancien holder
            holder = (ViewHolder) convertView.getTag();
        }

        // on rempli l'affichage de l'item en question
        remplirAffichage(mData.get(position), holder);
        return convertView;
    }

    /**
     * Classe anonyme permettant de personnaliser une ligne (holder)
     */
    public static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public TextView textView2;
    }

    /**
     * Classe permettant de remplir la ligne en fonction des données
     * @param data
     * @param holder
     */
    public void remplirAffichage(String data, ViewHolder holder) {

        // separateur unique
        String[] str = data.split("#~#");

        // C'est une categorie, on ne mets qu'un seul string
        if (str[0].equals("CATEGORIE_CATEGORIE") && str.length > 1) {
            holder.textView.setText(str[1]);
        } else {
            // c'est un bien
            // on rempli le nom
            holder.textView.setText(str[0]);
            // s'il y a une description, on l'affiche
            if (str.length > 1 && str[1] != null && !str[1].equals("")) {
                holder.textView2.setText(str[1]);
            } else {
                holder.textView2.setText("  ");

            }

            // s'il y a un path pour l'image principale, on l'affiche
            if (str.length > 2 && str[2] != null && !str[2].equals("")) {
                File imgFile = new File(str[2]);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    holder.imageView.setImageBitmap(myBitmap);
                }
            } else {
                holder.imageView.setImageResource(R.drawable.no_image);
            }

        }
    }
}