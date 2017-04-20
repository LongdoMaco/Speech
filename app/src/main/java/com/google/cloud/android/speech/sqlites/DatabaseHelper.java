package com.google.cloud.android.speech.sqlites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by longdo on 20/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.google.cloud.android.speech/databases/";
    private static String DB_NAME = "TranslateDB.db";
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null, 1);
        mContext = context;
        createDatabase();
    }

    private void createDatabase() {
        if (!checkDatabase()) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private void copyDatabase() {
        try {
            InputStream is = mContext.getAssets().open(DB_NAME);
            String outName = DB_PATH + DB_NAME;
            OutputStream os = new FileOutputStream(outName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length=is.read(buffer))>0){
                os.write(buffer,0,length);
            }
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase openDatabase() {
        mDatabase = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        return mDatabase;
    }

    public void closeDatabase(){
        if(mDatabase != null){
            mDatabase.close();
        }
    }

    private boolean checkDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        return file.exists();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
