package com.google.cloud.android.speech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by longdo on 31/03/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<Language> {

    private Context context;
    private Language[] languages;
    LayoutInflater inflter;

    public SpinnerAdapter(Context context, int textViewResourceId,
                            Language[] languages) {
        super(context, textViewResourceId, languages);
        this.context = context;
        this.languages = languages;
        this.inflter=(LayoutInflater.from(context));
    }

    public int getCount(){
        return languages.length;
    }

    public Language getItem(int position){
        return languages[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(languages[position].getFlag());
        names.setText(languages[position].getCountryName());
        return  view;
    }

    @Override
    public View getDropDownView(int position, View view,
                                ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(languages[position].getFlag());
        names.setText(languages[position].getCountryName());
        return  view;
    }
}
