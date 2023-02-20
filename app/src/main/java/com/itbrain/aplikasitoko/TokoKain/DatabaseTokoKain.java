package com.itbrain.aplikasitoko.TokoKain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTokoKain extends SQLiteOpenHelper {
    public static final String NAMA_DATABASE = "dbtokokain";
    private static final int VERSI_DATABASE = 1;
    Context ctx;
    SQLiteDatabase db;
    private static final String TAG = "Database";


    public DatabaseTokoKain(Context context) {
        super(context, NAMA_DATABASE, null, VERSI_DATABASE);
        ctx=context;
        db=this.getWritableDatabase();
        cektbl();
    }

    public Boolean exc(String query){
        try {
            db.execSQL(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public Cursor sq(String query){
        try {
            Cursor c =  db.rawQuery(query, null) ;
            return c ;
        } catch (Exception e){
            Log.e("sqlerror", "sq: "+e.getMessage() );
            return null ;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Boolean cektbl(){
        try{
            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` (" +
                    "`id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`namatoko`TEXT," +
                    "`alamattoko`TEXT," +
                    "`notelptoko`TEXT," +
                    "`caption_1`TEXT," +
                    "`caption_2`TEXT," +
                    "`caption_3`TEXT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblkategori` (" +
                    "`idkategori`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`kategori`TEXT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblkain` (" +
                    "`idkain`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`idkategori`INTEGER," +
                    "`kain`TEXT," +
                    "`biaya`REAL," +
                    "FOREIGN KEY(`idkategori`) REFERENCES `tblkategori`(`idkategori`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` (" +
                    "`idpelanggan`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`namapelanggan`TEXT," +
                    "`alamatpelanggan`TEXT," +
                    "`telppelanggan`TEXT" +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblorder` (" +
                    "`idorder`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`idpelanggan`INTEGER," +
                    "`faktur`TEXT," +
                    "`tglorder`INTEGER," +
                    "`total`REAL, " +
                    "`bayar`REAL," +
                    "`kembali`REAL," +
                    "`tglbayar`INTEGER," +
                    "FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) ON UPDATE CASCADE ON DELETE RESTRICT" +
//                    "FOREIGN KEY (`faktur`) REFERENCES `tbllaundrydetail`(`faktur`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblorderdetail` (" +
                    "`idorderdetail`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`idorder`INTEGER," +
                    "`idkain`INTEGER," +
                    "`panjang`REAL," +
                    "`lebar`REAL," +
                    "`jumlah`REAL, " +
                    "`hargaakhir`REAL," +
                    "`keterangan`TEXT," +
                    "FOREIGN KEY(`idorder`) REFERENCES `tblorder`(`idorder`) on update cascade on delete restrict" +
                    ");");

            exc("CREATE VIEW IF NOT EXISTS qkain AS SELECT " +
                    "tblkain.idkain, " +
                    "tblkain.idkategori, " +
                    "tblkategori.kategori, " +
                    "tblkain.kain, " +
                    "tblkain.biaya " +
                    "FROM tblkain " +
                    "INNER JOIN tblkategori ON tblkain.idkategori=tblkategori.idkategori");

            exc("CREATE VIEW IF NOT EXISTS qorder AS SELECT " +
                    "tblorder.idpelanggan, " +
                    "tblpelanggan.namapelanggan, " +
                    "tblorder.faktur, " +
                    "tblorder.tglorder, " +
                    "tblorder.total, " +
                    "tblorder.bayar, " +
                    "tblorder.kembali, " +
                    "tblorderdetail.idorderdetail, " +
                    "tblorderdetail.idorder, " +
                    "tblkain.idkategori, " +
                    "tblkategori.kategori, " +
                    "tblorderdetail.idkain, " +
                    "tblkain.kain, " +
                    "tblkain.biaya, " +
                    "tblorderdetail.panjang, " +
                    "tblorderdetail.lebar, " +
                    "tblorderdetail.jumlah, " +
                    "tblorderdetail.hargaakhir, " +
                    "tblorderdetail.keterangan " +
                    "FROM tblorderdetail " +
                    "INNER JOIN tblorder ON tblorderdetail.idorder = tblorder.idorder " +
                    "INNER JOIN tblkategori ON tblkain.idkategori = tblkategori.idkategori " +
                    "INNER JOIN tblkain ON tblorderdetail.idkain=tblkain.idkain " +
                    "INNER JOIN tblpelanggan ON tblorder.idpelanggan=tblpelanggan.idpelanggan");


            exc("CREATE VIEW IF NOT EXISTS qorderdetail AS SELECT " +
                    "tblorderdetail.idorderdetail, " +
                    "tblorderdetail.idkain, " +
                    "tblorderdetail.idorder, " +
                    "tblorder.faktur, " +
                    "tblorderdetail.panjang, " +
                    "tblorderdetail.lebar, " +
                    "tblorderdetail.jumlah, " +
                    "tblorderdetail.hargaakhir, " +
                    "tblorderdetail.keterangan, " +
                    "tblkain.idkategori, " +
                    "tblkategori.kategori, " +
                    "tblkain.kain, " +
                    "tblkain.biaya " +
                    "FROM tblorderdetail " +
                    "INNER JOIN tblorder ON tblorderdetail.idorder = tblorder.idorder " +
                    "INNER JOIN tblkain ON tblorderdetail.idkain = tblkain.idkain " +
                    "INNER JOIN tblkategori ON tblkain.idkategori = tblkategori.idkategori");
            exc("CREATE VIEW IF NOT EXISTS qbayar AS SELECT " +
                    "tblorder.idorder, " +
                    "tblorder.idpelanggan, " +
                    "tblpelanggan.namapelanggan, " +
                    "tblorder.total, " +
                    "tblorder.bayar, " +
                    "tblorder.kembali, " +
                    "tblorder.tglbayar " +
                    "FROM tblorder " +
                    " INNER JOIN tblpelanggan ON tblorder.idpelanggan=tblpelanggan.idpelanggan");




            exc("INSERT INTO tblidentitas VALUES (1,'KomputerKit.com','Sidoarjo','0838 320 320 77','Terima Kasih','Sudah Berbelanja','Di Toko Kami')") ;
            exc("DELETE FROM tblkategori WHERE idkategori=0");
            exc("INSERT INTO tblpelanggan VALUES (0,'Umum',' ',' ')");
            return true;
        }catch (Exception e){
            Log.e(TAG,"err : "+e.getMessage());
            return false;
        }
    }


    public Boolean insertToKategori(String kategori){
        ContentValues cv = new ContentValues();
        cv.put("kategori",kategori);
        long result = db.insert("tblkategori",null,cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }


    public List<String> getKategori(){
        List<String> labels = new ArrayList<String>();
        String q=FQueryTokoKain.select("tblkategori");
        Cursor c = db.rawQuery(q,null);
        if (c.getCount()>0){
            labels.add("Semua Kategori");
            while (c.moveToNext()) {
                labels.add(c.getString(1));
            }
        }else {
            labels.add("Data Kosong");
        }
        return labels;
    }
    public List<String> getIdKategori(){
        List<String> labels = new ArrayList<String>();
        String q=FQueryTokoKain.select("tblkategori");
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
    public Boolean updateKategori(Integer idKategori, String kategori){
        ContentValues cv = new ContentValues();
        cv.put("kategori",kategori);
        long result = db.update("tblkategori",cv,"idkategori=?",new String[]{String.valueOf(idKategori)});

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }
    public Boolean deleteKategori(Integer idKategori){
        if (db.delete("tblkategori","idkategori= ?",new String[]{String.valueOf(idKategori)})==-1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean deletePelanggan(Integer idPelanggan){
        if (db.delete("tblpelanggan","idpelanggan= ?",new String[]{String.valueOf(idPelanggan)})==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean insertToPelanggan (String namaPelanggan,String alamatPelanggan,String noTelpPelanggan){
        ContentValues cv = new ContentValues();
        cv.put("namapelanggan",namaPelanggan);
        cv.put("alamatpelanggan",alamatPelanggan);
        cv.put("telppelanggan",noTelpPelanggan);
        long result = db.insert("tblpelanggan",null,cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Boolean updatePelanggan(Integer idPelanggan, String pelanggan, String alamatpelanggan, String notelppelanggan){
        ContentValues cv = new ContentValues();
        cv.put("namapelanggan",pelanggan);
        cv.put("alamatpelanggan",alamatpelanggan);
        cv.put("telppelanggan",notelppelanggan);
        long result = db.update("tblpelanggan",cv,"idpelanggan=?",new String[]{String.valueOf(idPelanggan)});

        if (result == -1){
            return false;
        }else {
            return true;
        }



    }
    public Boolean insertToKain(Integer idKat,String kain,String biaya){
        ContentValues cv = new ContentValues();
        cv.put("idkategori",idKat);
        cv.put("kain",kain);
        cv.put("biaya",biaya);
        long result = db.insert("tblkain",null,cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Boolean updateKain(Integer idKain,Integer idKat,String kain,String biaya){
        ContentValues cv = new ContentValues();
        cv.put("idkategori",idKat);
        cv.put("kain",kain);
        cv.put("biaya",biaya);
        long result = db.update("tblkain",cv,"idkain=?",new String[]{String.valueOf(idKain)});
        if (result==-1){
            return false;
        }else {
            return true;
        }

    }
    public Boolean deleteKain(Integer idKain){
        if (db.delete("tblkain","idkain= ?",new String[]{String.valueOf(idKain)})==-1){
            return false;
        }else{
            return true;
        }
    }

}
