package com.bien;

/**
 * Created by Kevin on 19/11/2017.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.inventaire.R;
import com.dao.BienDAO;

public class BienAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private Context context;

    private LayoutInflater mInflater;

    public BienAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            String[] str = mData.get(position).split("#~#");
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:

                    convertView = mInflater.inflate(R.layout.item_bien, null);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageBien);
                    holder.textView = (TextView) convertView.findViewById(R.id.nomBien);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.descriptionBien);

                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.separator_bien, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    //holder.textView.setText(str[0]);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        remplirAffichage(mData.get(position), holder);


        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public TextView textView2;
    }

    public void remplirAffichage(String data, ViewHolder holder){

        String[] str = data.split("#~#");

        if (str[0].equals("CATEGORIE_CATEGORIE") && str.length > 1) {
            holder.textView.setText(str[1]);
        } else {
            holder.textView.setText(str[0]);
            if (str.length > 1 && str[1] != null && !str[1].equals("")){
                holder.textView2.setText(str[1]);
            } else {
                holder.textView2.setText("  ");

            }

          if (str.length > 2 && str[2] != null && !str[2].equals("")){
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