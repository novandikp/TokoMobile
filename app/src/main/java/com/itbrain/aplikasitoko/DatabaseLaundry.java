package com.itbrain.aplikasitoko;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseLaundry extends SQLiteOpenHelper {
    public static final String NAMA_DATABASE="db_laundry";
    private static final Integer VERSI_DATABASE=1;
    Context ctx;
    SQLiteDatabase db;
    private static final String TAG = "Database";

    public DatabaseLaundry(Context context) {
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
//            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean cektbl(){
        try{
            exc("PRAGMA foreign_keys=ON");
            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` (" +
                    "`ididentitas`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
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
            exc("CREATE TABLE IF NOT EXISTS `tbljasa` (" +
                    "`idjasa`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`idkategori`INTEGER," +
                    "`jasa`TEXT," +
                    "`biaya`REAL," +
                    "`satuan`TEXT," +
                    "FOREIGN KEY(`idkategori`) REFERENCES `tblkategori`(`idkategori`) on update cascade on delete restrict" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tbllaundry` (" +
                    "`idlaundry`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`idpelanggan`INTEGER," +
                    "`idpegawai`INTEGER," +
                    "`faktur`TEXT," +
                    "`tgllaundry`INTEGER," +
                    "`tglselesai`INTEGER," +
                    "`total`REAL," +
                    "`bayar`REAL," +
                    "`kembali`REAL," +
                    "`tglbayar`INTEGER," +
                    "`statuslaundry`TEXT DEFAULT 'Proses'," +
                    "`statusbayar`TEXT DEFAULT 'Belum Bayar'," +
                    "FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) on update cascade on delete restrict," +
                    "FOREIGN KEY(`idpegawai`) REFERENCES `tblpegawai`(`idpegawai`) on update cascade on delete restrict" +
//                    "FOREIGN KEY (`faktur`) REFERENCES `tbllaundrydetail`(`faktur`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tbllaundrydetail` (" +
                    "`idlaundrydetail`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`idjasa`INTEGER," +
                    "`idlaundry`INTEGER," +
                    "`jumlahlaundry`INTEGER," +
                    "`biayalaundry`REAL," +
                    "`keterangan`TEXT," +
                    "FOREIGN KEY(`idlaundry`) REFERENCES `tbllaundry`(`idlaundry`) on update cascade on delete restrict," +
                    "FOREIGN KEY(`idjasa`) REFERENCES `tbljasa`(`idjasa`) ON UPDATE cascade on delete restrict" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblpegawai` (" +
                    "`idpegawai`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`pegawai`TEXT," +
                    "`alamatpegawai`TEXT," +
                    "`notelppegawai`TEXT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` (" +
                    "`idpelanggan`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`pelanggan`TEXT," +
                    "`alamat`TEXT," +
                    "`notelp`TEXT," +
                    "`hutang`REAL DEFAULT '0'" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblbayarhutang` (" +
                    "`idbayarhutang`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`idpelanggan`INTEGER," +
                    "`tglbayar`INTEGER," +
                    "`hutang`REAL," +
                    "`bayar`REAL," +
                    "`bayarhutang`REAL," +
                    "`kembali`REAL," +
                    "`keteranganhutang`TEXT," +
                    "FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");

            exc("CREATE VIEW IF NOT EXISTS qjasa AS SELECT " +
                    "tbljasa.idjasa, " +
                    "tbljasa.idkategori, " +
                    "tblkategori.kategori, " +
                    "tbljasa.jasa, " +
                    "tbljasa.biaya, " +
                    "tbljasa.satuan " +
                    "FROM tbljasa " +
                    "INNER JOIN tblkategori ON tbljasa.idkategori=tblkategori.idkategori");
            exc("CREATE VIEW IF NOT EXISTS qlaundry AS SELECT " +
                    "tbllaundry.idpelanggan, " +
                    "tblpelanggan.pelanggan, " +
                    "tbllaundry.idpegawai, " +
                    "tblpegawai.pegawai, " +
                    "tbllaundry.faktur, " +
                    "tbllaundry.tgllaundry, " +
                    "tbllaundry.tglselesai, " +
                    "tbllaundry.total, " +
                    "tbllaundry.bayar, " +
                    "tbllaundry.kembali, " +
                    "tbllaundry.statuslaundry, " +
                    "tbllaundry.statusbayar, " +
                    "tbllaundrydetail.idlaundrydetail, " +
                    "tbllaundrydetail.idlaundry, " +
                    "tbljasa.idkategori, " +
                    "tblkategori.kategori, " +
                    "tbllaundrydetail.idjasa, " +
                    "tbljasa.jasa, " +
                    "tbljasa.biaya, " +
                    "tbljasa.satuan, " +
                    "tbllaundrydetail.jumlahlaundry, " +
                    "tbllaundrydetail.biayalaundry, " +
                    "tbllaundrydetail.keterangan " +
                    "FROM tbllaundrydetail " +
                    "INNER JOIN tbllaundry ON tbllaundrydetail.idlaundry = tbllaundry.idlaundry " +
                    "INNER JOIN tblkategori ON tbljasa.idkategori = tblkategori.idkategori " +
                    "INNER JOIN tbljasa ON tbllaundrydetail.idjasa=tbljasa.idjasa " +
                    "INNER JOIN tblpegawai ON tbllaundry.idpegawai=tblpegawai.idpegawai " +
                    "INNER JOIN tblpelanggan ON tbllaundry.idpelanggan=tblpelanggan.idpelanggan");
            exc("CREATE VIEW IF NOT EXISTS qcart AS SELECT " +
                    "tbllaundrydetail.idlaundrydetail, " +
                    "tbllaundrydetail.idjasa, " +
                    "tbllaundrydetail.idlaundry, " +
                    "tbllaundry.faktur, " +
                    "tbllaundrydetail.jumlahlaundry, " +
                    "tbllaundrydetail.biayalaundry, " +
                    "tbllaundrydetail.keterangan, " +
                    "tbljasa.idkategori, " +
                    "tblkategori.kategori, " +
                    "tbljasa.jasa, tbljasa.biaya, " +
                    "tbljasa.satuan " +
                    "FROM tbllaundrydetail " +
                    "INNER JOIN tbllaundry ON tbllaundrydetail.idlaundry = tbllaundry.idlaundry " +
                    "INNER JOIN tbljasa ON tbllaundrydetail.idjasa = tbljasa.idjasa " +
                    "INNER JOIN tblkategori ON tbljasa.idkategori = tblkategori.idkategori");
            exc("CREATE VIEW IF NOT EXISTS qproses AS SELECT " +
                    "tbllaundry.idlaundry, " +
                    "tbllaundry.idpelanggan, " +
                    "tbllaundry.faktur, " +
                    "tbllaundry.tgllaundry, " +
                    "tbllaundry.tglselesai, " +
                    "tbllaundry.total, " +
                    "tbllaundry.statuslaundry, " +
                    "tbllaundry.statusbayar, " +
                    "tblpelanggan.pelanggan, " +
                    "tblpelanggan.alamat, " +
                    "tblpelanggan.notelp " +
                    "FROM tbllaundry " +
                    "INNER JOIN tblpelanggan ON tbllaundry.idpelanggan = tblpelanggan.idpelanggan");
            exc("CREATE VIEW IF NOT EXISTS qbayarhutang AS SELECT " +
                    "tblbayarhutang.idbayarhutang, " +
                    "tblbayarhutang.idpelanggan, " +
                    "tblpelanggan.pelanggan, " +
                    "tblbayarhutang.tglbayar, " +
                    "tblbayarhutang.hutang, " +
                    "tblbayarhutang.bayar, " +
                    "tblbayarhutang.kembali " +
                    "FROM tblbayarhutang " +
                    "INNER JOIN tblpelanggan ON tblbayarhutang.idpelanggan = tblpelanggan.idpelanggan");
            exc("CREATE VIEW IF NOT EXISTS qbayar AS SELECT " +
                    "tbllaundry.idlaundry, " +
                    "tbllaundry.idpelanggan, " +
                    "tblpelanggan.pelanggan, " +
                    "tbllaundry.total, " +
                    "tbllaundry.bayar, " +
                    "tbllaundry.kembali, " +
                    "tbllaundry.tglbayar, " +
                    "tbllaundry.statusbayar, " +
                    "tbllaundry.statuslaundry " +
                    "FROM tbllaundry " +
                    "INNER JOIN tblpelanggan ON tbllaundry.idpelanggan=tblpelanggan.idpelanggan");

            exc("CREATE TRIGGER IF NOT EXISTS kurang_jasa " +
                    "AFTER DELETE ON tblkategori " +
                    "FOR EACH ROW BEGIN DELETE FROM tbljasa " +
                    "WHERE tbljasa.idkategori = OLD.idkategori; " +
                    "END");
            exc("CREATE TRIGGER IF NOT EXISTS kurang_detail " +
                    "AFTER DELETE ON tbllaundry " +
                    "FOR EACH ROW BEGIN DELETE FROM tbllaundrydetail " +
                    "WHERE tbllaundrydetail.idlaundry = OLD.idlaundry; " +
                    "END");
            exc("CREATE TRIGGER IF NOT EXISTS bayar_hutang " +
                    "AFTER INSERT ON tblbayarhutang " +
                    "FOR EACH ROW BEGIN UPDATE tblpelanggan " +
                    "SET hutang=hutang-NEW.bayarhutang " +
                    "WHERE tblpelanggan.idpelanggan=NEW.idpelanggan; " +
                    "END");
//            exc("INSERT INTO tblidentitas VALUES (1,'KomputerKit.com','Sidoarjo','0838 320 320 77','Terima Kasih','Sudah Berbelanja','Di Toko Kami')") ;
                        exc("INSERT INTO tblidentitas VALUES (1,'KomputerKit.com','Sidoarjo')") ;
            exc("INSERT INTO tblpegawai VALUES (0,'Pemilik',' ',' ')");
            exc("INSERT INTO tblpelanggan VALUES (0,'Umum',' ',' ',0)");
            exc("DELETE FROM tblkategori WHERE idkategori=0");
            return true;
        }catch (Exception e){
            Log.e(TAG,"err : "+e.getMessage());
            return false;
        }
    }

    public Boolean insertToPegawai (String namaPegawai,String alamatPegawai,String notelpPegawai){
        ContentValues cv = new ContentValues();
        cv.put("pegawai",namaPegawai);
        cv.put("alamatpegawai",alamatPegawai);
        cv.put("notelppegawai",notelpPegawai);
        long result = db.insert("tblpegawai",null,cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Boolean updatePegawai(Integer idPegawai, String pegawai, String alamatpegawai, String notelppegawai){
        ContentValues cv = new ContentValues();
        cv.put("pegawai",pegawai);
        cv.put("alamatpegawai",alamatpegawai);
        cv.put("notelppegawai",notelppegawai);
        long result = db.update("tblpegawai",cv,"idpegawai=?",new String[]{String.valueOf(idPegawai)});

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }
    public Boolean deletePegawai(Integer idpegawai){
        try{
            db.delete("tblpegawai","idpegawai= ?",new String[]{String.valueOf(idpegawai)});
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Boolean insertToPelanggan (String namaPelanggan,String alamatPelanggan,String noTelpPelanggan){
        ContentValues cv = new ContentValues();
        cv.put("pelanggan",namaPelanggan);
        cv.put("alamat",alamatPelanggan);
        cv.put("notelp",noTelpPelanggan);
        long result = db.insert("tblpelanggan",null,cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Boolean updatePelanggan(Integer idPelanggan, String pelanggan, String alamatpelanggan, String notelppelanggan){
        ContentValues cv = new ContentValues();
        cv.put("pelanggan",pelanggan);
        cv.put("alamat",alamatpelanggan);
        cv.put("notelp",notelppelanggan);
        long result = db.update("tblpelanggan",cv,"idpelanggan=?",new String[]{String.valueOf(idPelanggan)});

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }
    public Boolean deletePelanggan(Integer idPelanggan){
        try{
            db.delete("tblpelanggan","idpelanggan= ?",new String[]{String.valueOf(idPelanggan)});
            return true;
        }catch (Exception e){
            return  false;
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
        String q=Query.select("tblkategori");
        Cursor c = db.rawQuery(q,null);
        if (Modul.getCount(c)>0){
            labels.add("Semua Kategori");
            while (c.moveToNext()) {
                labels.add(Modul.getStringfromCol(c,1));
            }
        }else {
            labels.add("Data Kosong");
        }
        return labels;
    }
    public List<String> getIdKategori(){
        List<String> labels = new ArrayList<String>();
        String q=Query.select("tblkategori");
        Cursor c = db.rawQuery(q,null);
        if (Modul.getCount(c)>0){
            labels.add("0");
            while (c.moveToNext()) {
                labels.add(Modul.getStringfromCol(c,0));
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
        try{
            db.delete("tblkategori","idkategori= ?",new String[]{String.valueOf(idKategori)});
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public Boolean insertToJasa(Integer idKat,String jasa,String biaya,String satuan){
        ContentValues cv = new ContentValues();
        cv.put("idkategori",idKat);
        cv.put("jasa",jasa);
        cv.put("biaya",biaya);
        cv.put("satuan",satuan);
        long result = db.insert("tbljasa",null,cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Boolean updateJasa(Integer idJasa,Integer idKat,String jasa,String biaya,String satuan){
        ContentValues cv = new ContentValues();
        cv.put("idkategori",idKat);
        cv.put("jasa",jasa);
        cv.put("biaya",biaya);
        cv.put("satuan",satuan);
        long result = db.update("tbljasa",cv,"idjasa=?",new String[]{String.valueOf(idJasa)});
        if (result==-1){
            return false;
        }else {
            return true;
        }

    }
    public Boolean deleteJasa(Integer idJasa){
        try{
            db.delete("tbljasa","idjasa= ?",new String[]{String.valueOf(idJasa)});
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
