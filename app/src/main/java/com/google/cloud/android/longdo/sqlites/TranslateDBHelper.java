package com.google.cloud.android.longdo.sqlites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.cloud.android.longdo.models.Translate;

import java.util.ArrayList;

/**
 * Created by longdo on 18/04/2017.
 */

public class TranslateDBHelper{

    DatabaseHelper dbHelper;
    public TranslateDBHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public Translate getTranslate(int id){
        Translate translate = null;
        String sql =  "select * from translates where id = " + id;
        try {
            SQLiteDatabase db = dbHelper.openDatabase();
            Log.d("getTranslate", sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                translate = new Translate();
                translate.setId(cursor.getInt(0));
                translate.setLangcode_from(cursor.getString(1));
                translate.setLangcode_to(cursor.getString(2));
                translate.setText_from(cursor.getString(3));
                translate.setText_to(cursor.getString(4));
                translate.setFlag_from(cursor.getString(5));
                translate.setFlag_to(cursor.getString(6));
                translate.setLanguage_from(cursor.getString(7));
                translate.setLanguage_to(cursor.getString(8));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return translate;

    }

    public ArrayList<Translate> getAllTranslates(){
        String qr = "Select * from translates order by id desc";
        SQLiteDatabase db = dbHelper.openDatabase();
        ArrayList<Translate> translateList = new ArrayList<>();
        Translate translate = null;
        Cursor cursor = db.rawQuery(qr, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    translate = new Translate(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                    translateList.add(translate);
                    cursor.moveToNext();
                }
                return translateList;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public boolean insertTranslate(Translate translate) {
        String qr = "insert into translates (LANGCODE_FROM,LANGCODE_TO,TEXT_FROM,TEXT_TO,FLAG_FROM,FLAG_TO,LANGUAGE_FROM,LANGUAGE_TO) values('"+translate.getLangcode_from()+"', '"+translate.getLangcode_to()+"', '"+translate.getText_from()+"', '"+translate.getText_to()+"','"+translate.getFlag_from()+"','"+translate.getFlag_to()+"', '"+translate.getLanguage_from()+"', '"+translate.getLanguage_to()+"')";
        Log.d("sql",qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeInsert();
        return true;
    }


    public boolean deleteTranslate (Integer id) {

        String qr = "delete from translates where id = "+id+" ";
        Log.d("qr", qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeUpdateDelete();
        return true;
    }
    public boolean deleteAllTranslates () {
        String qr = "delete from translates";
        Log.d("qr", qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeUpdateDelete();
        return true;
    }

    public String getSpeechCodeFromId(int id) {

        String textTo;
        String qr= "select language_speech_status from translates,languages where translates.LANGUAGE_TO=languages.language_name and translates.id="+id;
        SQLiteDatabase db = dbHelper.openDatabase();
        Cursor cursor = db.rawQuery(qr, null);
        cursor.moveToFirst();
        textTo=cursor.getString(0);
        Log.d("language_speech_status",id+"  "+String.valueOf(textTo));
        return textTo;
    }

}
