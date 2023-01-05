package com.itbrain.aplikasitoko;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database_Tambah_Jasa_Bengkel_ extends SQLiteOpenHelper {

    SQLiteDatabase db;

    private static final String DATABASES_NAME = "dbjasa";
    private static final int VERSION = 1;

    public Database_Tambah_Jasa_Bengkel_(Context context) {
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
        String tbljasa = "CREATE TABLE \"tbljasa\" (\n" +
                "\t\"idjasa\"\tINTEGER,\n" +
                "\t\"nama_jasa\"\tTEXT,\n" +
                "\t\"harga_jasa\"\tREAL,\n" +
                "\t\"pendapatan\"\tREAL,\n" +
                "\tPRIMARY KEY(\"idjasa\" AUTOINCREMENT)\n" +
                ");";

        runSQL(tbljasa);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
