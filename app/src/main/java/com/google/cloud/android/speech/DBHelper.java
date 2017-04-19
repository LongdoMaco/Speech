package com.google.cloud.android.speech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by longdo on 18/04/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TranslateDB.db";
    public static final String TRANSLATES_TABLE_NAME = "translates";
    public static final String TRANSLATES_COLUMN_ID = "id";
    public static final String TRANSLATES_COLUMN_LANG_FROM= "lang_from";
    public static final String TRANSLATES_COLUMN_LANG_TO = "lang_to";
    public static final String TRANSLATES_COLUMN_TEXT_FROM = "text_from";
    public static final String TRANSLATES_COLUMN_TEXT_TO= "text_to";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table translates " +
                "(id integer primary key, lang_from text,lang_to text,text_from text, text_to text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists translates");
        onCreate(db);
    }

    public boolean insertTranslate(String lang_from, String lang_to, String text_from, String text_to) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lang_from", lang_from);
        contentValues.put("lang_to", lang_to);
        contentValues.put("text_from", text_from);
        contentValues.put("text_to", text_to);
        db.insert("translates", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from translates where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TRANSLATES_TABLE_NAME);
        return numRows;
    }

    public boolean updateTranslate (Integer id, String lang_from, String lang_to, String text_from, String text_to) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lang_from", lang_from);
        contentValues.put("lang_to", lang_to);
        contentValues.put("text_from", text_from);
        contentValues.put("text_to", text_to);
        db.update("translates", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteTranslate (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("translates",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<TranslateModel> getAllTranslates() {
        ArrayList<TranslateModel> array_list = new ArrayList<TranslateModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from translates", null );
        res.moveToLast();

        while(res.isBeforeFirst() == false){
            TranslateModel item=new TranslateModel(res.getInt(res.getColumnIndex(TRANSLATES_COLUMN_ID)),
                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_LANG_FROM)),
                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_LANG_TO)),
                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_TEXT_FROM)),
                    res.getString(res.getColumnIndex(TRANSLATES_COLUMN_TEXT_TO)));
            Log.d("LONG",item.getText_to());
            array_list.add(item);
            res.moveToPrevious();
        }
        return array_list;

    }
}
