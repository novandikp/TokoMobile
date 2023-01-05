package com.itbrain.aplikasitoko;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Databases_Katagori_Bengkel_ extends SQLiteOpenHelper {

    SQLiteDatabase db;

    private static final String DATABASES_NAME = "dbkategori";
    private static final int VERSION = 1;

    public Databases_Katagori_Bengkel_(Context context) {
        super(context, DATABASES_NAME, null, VERSION);
        db = this.getWritableDatabase();
    }

    boolean runSQL (String sql) {
        try {
            db.execSQL(sql);
            return true;

        }catch (Exception e) {
            return false;
        }
    }

    public void buatTabel() {
     String tblkategori = "CREATE TABLE \"tblkategori\" (\n" +
             "\t\"idkategori\"\tINTEGER,\n" +
             "\t\"kategori\"\tTEXT,\n" +
             "\tPRIMARY KEY(\"idkategori\" AUTOINCREMENT)\n" +
             ");";

        runSQL(tblkategori);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
