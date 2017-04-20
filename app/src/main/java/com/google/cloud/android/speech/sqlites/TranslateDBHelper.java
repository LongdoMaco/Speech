package com.google.cloud.android.speech.sqlites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.cloud.android.speech.models.Translate;

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
                translate.setLang_from(cursor.getString(1));
                translate.setLang_to(cursor.getString(2));
                translate.setText_from(cursor.getString(3));
                translate.setLang_to(cursor.getString(4));
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
                    translate = new Translate(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4));
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
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("lang_from", lang_from);
//        contentValues.put("lang_to", lang_to);
//        contentValues.put("text_from", text_from);
//        contentValues.put("text_to", text_to);
//        db.insert("translates", null, contentValues);
//        return true;
        String qr = "insert into translates (LANG_FROM,LANG_TO,TEXT_FROM,TEXT_TO) values('"+translate.getLang_from()+"', '"+translate.getLang_to()+"', '"+translate.getText_from()+"', '"+translate.getText_to()+"')";
        Log.d("sql",qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeInsert();
        return true;
    }
//
//    public Cursor getData(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from translates where id="+id+"", null );
//        return res;
//    }
//
//    public int numberOfRows(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, TRANSLATES_TABLE_NAME);
//        return numRows;
//    }

//    public boolean updateTranslate (Integer id, String lang_from, String lang_to, String text_from, String text_to) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("lang_from", lang_from);
//        contentValues.put("lang_to", lang_to);
//        contentValues.put("text_from", text_from);
//        contentValues.put("text_to", text_to);
//        db.update("translates", contentValues, "id = ? ", new String[] { Inteer.toString(id) } );
//        return true;
//    }

    public boolean deleteTranslate (Integer id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("translates",
//                "id = ? ",
//                new String[] { Integer.toString(id) });
        String qr = "delete from translates where id = "+id+" ";
        Log.d("qr", qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeUpdateDelete();
        return true;
    }
    public boolean deleteAllTranslates () {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("translates",
//                "id = ? ",
//                new String[] { Integer.toString(id) });
        String qr = "delete from translates";
        Log.d("qr", qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeUpdateDelete();
        return true;
    }

//    public ArrayList<Translate> getAllTranslates() {
//        ArrayList<Translate> array_list = new ArrayList<Translate>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from translates", null );
//        res.moveToLast();
//
//        while(res.isBeforeFirst() == false){
//            Translate item=new Translate(res.getInt(res.getColumnIndex(TRANSLATES_COLUMN_ID)),
//                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_LANG_FROM)),
//                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_LANG_TO)),
//                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_TEXT_FROM)),
//                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_TEXT_TO)));
//            array_list.add(item);
//            res.moveToPrevious();
//        }
//        return array_list;
//
//    }
    public String getTextToFromId(int id) {
        String textTo;
        //hp = new HashMap();
        String qr= "select * from translates where id="+id;
        SQLiteDatabase db = dbHelper.openDatabase();
        Cursor cursor = db.rawQuery(qr, null);
        cursor.moveToFirst();
        textTo=cursor.getString(4);
        Log.d("TEXT",String.valueOf(textTo));
        return textTo;
    }
}
