package com.itbrain.aplikasitoko.tabungan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.kasir.FQueryKasir;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTabungan extends SQLiteOpenHelper {
    Context ctx;
    SQLiteDatabase db;
    public DatabaseTabungan(Context context) {
        super(context, PrefTabungan.db, null, PrefTabungan.version);
        ctx=context;
        db=this.getWritableDatabase();
        cektbl();
    }
    public Boolean exc(String query){
        try {
            db.execSQL(query);
            return true;
        }catch (Exception e){
            Log.e("ERRORSQL",e.getMessage());
            return false;
        }
    }
    public Cursor sq(String query){
        try {
            Cursor c =  db.rawQuery(query, null) ;
            return c ;
        } catch (Exception e){
            return null ;
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    public Boolean cektbl(){
        try {
            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` (" +
                    "`id`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`namatoko`TEXT," +
                    "`notelp`INTEGER," +
                    "`alamat`TEXT," +
                    "`caption1`TEXT," +
                    "`caption2`TEXT," +
                    "`caption3`TEXT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblanggota` (" +
                    "`idanggota`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`namaanggota`TEXT," +
                    "`alamatanggota`TEXT," +
                    "`notelpanggota`INTEGER" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tbljenissimpanan` (" +
                    "`idjenis`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`jenis`TEXT," +
                    "`bunga`REAL" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblsimpanan` (" +
                    "`idsimpanan`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`idanggota`INTEGER," +
                    "`idjenis`INTEGER," +
                    "`simpanan`TEXT," +
                    "FOREIGN KEY(`idjenis`) REFERENCES `tbljenissimpanan`(`idjenis`) ON UPDATE CASCADE ON DELETE RESTRICT," +
                    "FOREIGN KEY(`idanggota`) REFERENCES `tblanggota`(`idanggota`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tbltransaksi` (" +
                    "`idtransaksi`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`idsimpanan`INTEGER," +
                    "`notransaksi`INTEGER," +
                    "`tgltransaksi`INTEGER," +
                    "`masuk`REAL," +
                    "`keluar`REAL," +
                    "`saldo`REAL," +
                    "`kode`TEXT," +
                    "FOREIGN KEY(`idsimpanan`) REFERENCES `tblsimpanan`(`idsimpanan`) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblkeuangan` (" +
                    "`idtransaksi`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`tgltransaksi`INTEGER," +
                    "`faktur`TEXT," +
                    "`keterangantransaksi`TEXT," +
                    "`masuk`REAL DEFAULT '0'," +
                    "`keluar`REAL DEFAULT '0'," +
                    "`saldo`REAL DEFAULT '0'" +
                    ");");
            exc("CREATE VIEW IF NOT EXISTS qsimpanan AS SELECT " +
                    "tblsimpanan.idsimpanan, " +
                    "tblsimpanan.idanggota, " +
                    "tblanggota.namaanggota, " +
                    "tblsimpanan.idjenis, " +
                    "tbljenissimpanan.jenis, " +
                    "tbljenissimpanan.bunga, " +
                    "tblsimpanan.simpanan " +
                    "FROM tblsimpanan " +
                    "INNER JOIN tblanggota ON tblsimpanan.idanggota = tblanggota.idanggota " +
                    "INNER JOIN tbljenissimpanan ON tblsimpanan.idjenis = tbljenissimpanan.idjenis");

            exc("INSERT INTO tblidentitas (id,namatoko,notelp,alamat,caption1,caption2,caption3) VALUES (1,'KomputerKit.com',83832032077,'Sidoarjo','Harap Simpan','Struk Ini','Sebagai Tanda Bukti')");
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public List<String> getJenis(){
        List<String> labels = new ArrayList<String>();
        labels.clear();
        String q= QueryTabungan.select("tbljenissimpanan");
        Cursor c = db.rawQuery(q,null);
        if (c.getCount()>0){
            labels.add(ModulTabungan.getResString(ctx,R.string.menutransaksisimpanan_sp1));
            while (c.moveToNext()) {
                labels.add(ModulTabungan.getString(c,"jenis")+" - "+"("+ModulTabungan.numFormat(ModulTabungan.getString(c,"bunga"))+"%)");
            }
        }else {
            labels.add(ModulTabungan.getResString(ctx,R.string.kosong));
        }
        return labels;
    }
    public List<String> getIdJenis(){
        List<String> labels = new ArrayList<String>();
        labels.clear();
        String q=QueryTabungan.select("tbljenissimpanan");
        Cursor c = db.rawQuery(q,null);
        if (c.getCount()>0){
            labels.add("0");
            while (c.moveToNext()) {
                labels.add(c.getString(0));
            }
        }else {
            labels.add("0");
        }
        return labels;
    }
}


