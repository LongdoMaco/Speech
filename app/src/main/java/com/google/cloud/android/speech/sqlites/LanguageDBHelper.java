package com.google.cloud.android.speech.sqlites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.cloud.android.speech.models.Language;

import java.util.ArrayList;

/**
 * Created by longdo on 20/04/2017.
 */

public class LanguageDBHelper {

    DatabaseHelper dbHelper;
    private Context context;

    public LanguageDBHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public Language getLanguage(int id){
        Language language = null;
        String sql =  "select * from languages where id = " + id;
        try {
            SQLiteDatabase db = dbHelper.openDatabase();
            Log.d("getLanguage", sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                language = new Language();
                language.setId(cursor.getInt(0));
                language.setLanguageName(cursor.getString(1));
                language.setLanguageCode(cursor.getString(2));
                language.setIcon(cursor.getInt(3));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return language;

    }

    public ArrayList<Language> getLanguageList(){
        String qr = "Select * from languages";
        SQLiteDatabase db = dbHelper.openDatabase();
        ArrayList<Language> languageList = new ArrayList<>();
        Language language = null;
        Cursor cursor = db.rawQuery(qr, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    language = new Language(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                    languageList.add(language);
                    cursor.moveToNext();
                }
                return languageList;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
