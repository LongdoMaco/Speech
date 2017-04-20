package com.google.cloud.android.speech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.cloud.android.speech.R;
import com.google.cloud.android.speech.models.Language;

import java.util.ArrayList;


/**
 * Created by longdo on 31/03/2017.
 */

public class ListLanguageAdapter extends ArrayAdapter<Language> {

    private Context context;
    private ArrayList<Language> languages;
    LayoutInflater inflter;

    public ListLanguageAdapter(Context context, int textViewResourceId,
                               ArrayList<Language> languages) {
        super(context, textViewResourceId, languages);
        this.context = context;
        this.languages = languages;
        this.inflter=(LayoutInflater.from(context));
    }

    public int getCount(){
        return languages.size();
    }

    public Language getItem(int position){
        return languages.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_list_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(languages.get(position).getIcon());
        names.setText(languages.get(position).getLanguageName());
        return  view;
    }

    @Override
    public View getDropDownView(int position, View view,
                                ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_list_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(languages.get(position).getIcon());
        names.setText(languages.get(position).getLanguageName());
        return  view;
    }
}
