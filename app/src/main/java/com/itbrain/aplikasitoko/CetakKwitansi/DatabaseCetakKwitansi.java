package com.itbrain.aplikasitoko.CetakKwitansi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseCetakKwitansi extends SQLiteOpenHelper {
    public static final String nama_database = "db_receipt";
    public static final int versi_database = 1;
    SQLiteDatabase db;
    Context a;

    public DatabaseCetakKwitansi(Context context){
        super(context, nama_database, null, versi_database);
        db = this.getWritableDatabase();
        a = context;
        cektbl();
    }

    public Boolean cektbl(){
        try {
            exc("PRAGMA foreign_keys = ON;");
            //create tabel jasa
            exc("CREATE TABLE IF NOT EXISTS `tbljasa` (\n" +
                    "\t`idjasa`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`jasa`\tTEXT\n" +
                    ");");

            //create tabel pelanggan
            exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` (\n" +
                    "\t`idpelanggan`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`pelanggan`\tTEXT,\n" +
                    "\t`alamat`\tTEXT,\n" +
                    "\t`notelp`\tTEXT\n" +
                    ");");

            //create tabel transaksi
            exc("CREATE TABLE IF NOT EXISTS `tbltransaksi` (\n" +
                    "\t`idtransaksi`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`idpelanggan`\tINTEGER,\n" +
                    "\t`faktur`\tTEXT,\n" +
                    "\t`tgltransaksi`\tINTEGER,\n" +
                    "\t`penerima`\tTEXT,\n" +
                    "\t`total`\tREAL,\n" +
                    "\t`terbilang`\tTEXT,\n" +
                    "\t`status`\tTEXT\n DEFAULT 0,\n" +
                    "\tFOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) on update cascade on delete restrict\n" +
                    ");");

            //create tabel transaksi detail
            exc("CREATE TABLE IF NOT EXISTS `tbltransaksidetail` (\n" +
                    "\t`idtransaksidetail`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`idtransaksi`\tINTEGER,\n" +
                    "\t`idjasa`\tINTEGER,\n" +
                    "\t`jasatransaksi`\tTEXT,\n" +
                    "\t`jumlah`\tINTEGER,\n" +
                    "\t`harga`\tREAL,\n" +
                    "\t`keterangan`\tTEXT,\n" +
                    "\tFOREIGN KEY(`idtransaksi`) REFERENCES `tbltransaksi`(`idtransaksi`) ON UPDATE CASCADE ON DELETE RESTRICT,\n" +
                    "\tFOREIGN KEY(`idjasa`) REFERENCES `tbljasa`(`idjasa`)\n" +
                    ");");

            //create tabel identitas
            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` (\n" +
                    "\t`ididentitas` \tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`nama` \tTEXT,\n" +
                    "\t`alamatid` \tTEXT,\n" +
                    "\t`telp` \tTEXT,\n" +
                    "\t`cap1` \tTEXT,\n" +
                    "\t`cap2` \tTEXT,\n" +
                    "\t`cap3` \tTEXT\n" +
                    ");");

            //create view
            exc("CREATE VIEW vtransaksi AS SELECT " +
//                    "tbltransaksi.idtransaksi, " +
                    "tbltransaksi.idpelanggan, " +
                    "tbltransaksidetail.idtransaksidetail, " +
                    "tbltransaksidetail.idtransaksi, " +
                    "tbltransaksi.faktur, " +
                    "tbltransaksi.tgltransaksi, " +
                    "tbltransaksi.penerima, " +
                    "tbltransaksi.total, " +
                    "tbltransaksi.status, " +
                    "tbltransaksidetail.jasatransaksi, " +
                    "tbltransaksidetail.jumlah, " +
                    "tbltransaksidetail.harga, " +
                    "tbltransaksidetail.keterangan, " +
                    "tbltransaksi.terbilang " +
                    "FROM tbltransaksidetail " +
                    "INNER JOIN tbltransaksi ON tbltransaksidetail.idtransaksi = tbltransaksi.idtransaksi");

            //insert Identitas
            exc("INSERT INTO tblidentitas VALUES (1, 'KomputerKit.com','Sidoarjo','Sidoarjo','Dewo','0838 320 320 77','Di Toko Kami')");

            //create trigger tambah_total
            exc("CREATE TRIGGER tambah_total AFTER INSERT ON tbltransaksidetail FOR EACH ROW BEGIN UPDATE tbltransaksi SET total= total + (NEW.harga * NEW.jumlah) WHERE idtransaksi = NEW.idtransaksi; END");

            //create trigger kurang_total
            exc("CREATE TRIGGER kurang_total AFTER DELETE ON tbltransaksidetail FOR EACH ROW BEGIN UPDATE tbltransaksi SET total=total - (OLD.harga * OLD.jumlah) WHERE idtransaksi = OLD.idtransaksi; END");

            //insert Pelanggan
            exc("INSERT INTO tblpelanggan VALUES (0, 'kosong','','')");

            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +nama_database);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }

    public boolean exc(String query){
        try {
            db.execSQL(query);
            return true ;
        } catch (Exception e){
//            Toast.makeText(a, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false ;
        }
    }

    public Cursor sq(String query){
        try {
            Cursor cursor = db.rawQuery(query, null);
            return cursor;
        } catch (Exception e){
            return null ;
        }
    }

    //jasa

    public List<String> getIdJasa(){
        List<String> labels = new ArrayList<String>();
        String q= "SELECT * FROM tbljasa";
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(0));
            }while (c.moveToNext());
        }
        return labels;
    }

    public boolean insertJasa(String jasa){
        ContentValues cv= new ContentValues();
        cv.put("jasa", jasa );
        long result= db.insert("tbljasa", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public List<String> getJasa(){
        List<String> labels = new ArrayList<String>();
        String q= "SELECT * FROM tbljasa";
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(1));
            }while (c.moveToNext());
        }
        return labels;
    }

    public Boolean deleteJasa(Integer idjasa){
        try{
            db.delete("tbljasa","idjasa= ?",new String[]{String.valueOf(idjasa)});
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean updateJasa(Integer idjasa, String jasa){
        ContentValues cv = new ContentValues();
        cv.put("jasa", jasa );
        long result = db.update("tbljasa", cv, "idjasa=?", new String[]{String.valueOf(idjasa)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //pelanggan

    public List<String> getIdPelanggan(){
        List<String> labels = new ArrayList<String>();
        String q= "SELECT * FROM tblpelanggan";
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(0));
            }while (c.moveToNext());
        }
        return labels;
    }

    public boolean insertPelanggan(String pelanggan, String alamat, String notelp){
        ContentValues cv= new ContentValues();
        cv.put("pelanggan", pelanggan );
        cv.put("alamat", alamat );
        cv.put("notelp",notelp );
        long result= db.insert("tblpelanggan", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }


    public List<String> getPelanggan(){
        List<String> labels = new ArrayList<String>();
        String q= "SELECT * FROM tblpelanggan";
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(1));
            }while (c.moveToNext());
        }
        return labels;
    }

    public Boolean deletePelanggan(Integer idpelanggan){
        try{
            db.delete("tblpelanggan","idpelanggan= ?",new String[]{String.valueOf(idpelanggan)});
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean updatePelanggan(Integer idpelanggan, String pelanggan, String alamat, String notelp){
        ContentValues cv = new ContentValues();
        cv.put("pelanggan", pelanggan);
        cv.put("alamat", alamat );
        cv.put("notelp", notelp );
        long result = db.update("tblpelanggan", cv, "idpelanggan=?", new String[]{String.valueOf(idpelanggan)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }
}
