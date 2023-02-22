package com.itbrain.aplikasitoko.Salon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSalon extends SQLiteOpenHelper {
    public static final String nama_database="db_salon";
    public static final int versi_database=1;
    SQLiteDatabase db;
    Context a;

    public DatabaseSalon(Context context){
        super(context, nama_database, null, versi_database);
        db = this.getWritableDatabase();
        a = context;
        cektbl();
    }

    public Boolean cektbl(){
        try {
            //create table janji
            exc("CREATE TABLE IF NOT EXISTS `tbljanji` (\n" +
                    "\t`idjanji`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`idpelanggan`\tINTEGER,\n" +
                    "\t`tgljanji`\tTEXT,\n" +
                    "\t`jamjanji`\tTEXT,\n" +
                    "\t`status`\tTEXT,\n" +
                    "\tFOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) on update cascade on delete restrict\n" +
                    ");");

            //create table jasa
            exc("CREATE TABLE IF NOT EXISTS `tbljasa` (\n" +
                    "\t`idjasa`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`jasa`\tTEXT,\n" +
                    "\t`harga`\tREAL\n" +
                    ");");

            //create table order
            exc("CREATE TABLE IF NOT EXISTS `tblorder` (\n" +
                    "\t`idorder`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`idpelanggan`\tINTEGER,\n" +
                    "\t`faktur`\tTEXT,\n" +
                    "\t`tglorder`\tTEXT,\n" +
                    "\t`total`\tREAL,\n" +
                    "\t`bayar`\tREAL DEFAULT 0,\n" +
                    "\t`kembali`\tREAL DEFAULT 0,\n" +
                    "\t`tipe`\tTEXT,\n" +
                    "\tFOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) on update cascade on delete restrict\n" +
                    ");");

            //create table order detail
            exc("CREATE TABLE IF NOT EXISTS `tblorderdetail` (\n" +
                    "\t`idorderdetail`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`idorder`\tINTEGER,\n" +
                    "\t`idjasa`\tINTEGER,\n" +
                    "\t`hargajual` \tREAL,\n" +
                    "\t`jumlah`\tREAL,\n" +
                    "\tFOREIGN KEY(`idorder`) REFERENCES `tblorder`(`idorder`) ON UPDATE CASCADE ON DELETE RESTRICT,\n" +
                    "\tFOREIGN KEY(`idjasa`) REFERENCES `tbljasa`(`idjasa`)\n" +
                    ");");

            //create table pelanggan
            exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` (\n" +
                    "\t`idpelanggan`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`pelanggan`\tTEXT,\n" +
                    "\t`alamatpel`\tTEXT,\n" +
                    "\t`telppel`\tTEXT\n" +
                    ");");

            //create table identitas
            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` (\n" +
                    "\t`ididentitas` \tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`nama` \tTEXT,\n" +
                    "\t`alamat` \tTEXT,\n" +
                    "\t`telp` \tSTRING,\n" +
                    "\t`cap1` \tTEXT,\n" +
                    "\t`cap2` \tTEXT,\n" +
                    "\t`cap3` \tTEXT\n" +
                    ");");

            //create view
            exc("CREATE VIEW qjanji AS SELECT tbljanji.idjanji, tbljanji.idpelanggan, tbljanji.tgljanji, tbljanji.jamjanji, tbljanji.status, tblpelanggan.pelanggan, tblpelanggan.alamatpel, tblpelanggan.telppel FROM tbljanji INNER JOIN tblpelanggan ON tbljanji.idpelanggan = tblpelanggan.idpelanggan");

            exc("CREATE VIEW qorder AS SELECT tblorder.idorder, tblorder.idpelanggan, tblorder.faktur, tblorder.tglorder, tblorder.total, tblorder.bayar, tblorder.kembali, tblorder.tipe, tblpelanggan.pelanggan, tblpelanggan.alamatpel, tblpelanggan.telppel FROM tblorder INNER JOIN tblpelanggan ON tblorder.idpelanggan = tblpelanggan.idpelanggan");

            exc("CREATE VIEW qorderdetail AS SELECT tblorderdetail.idorderdetail, tblorderdetail.idorder, tblorderdetail.idjasa, tblorderdetail.hargajual, tblorderdetail.jumlah, tbljasa.jasa, tbljasa.harga, tblorder.faktur, tblorder.tglorder, tblorder.total, tblorder.bayar, tblorder.kembali, tblorder.tipe, tblpelanggan.pelanggan, tblpelanggan.alamatpel, tblpelanggan.telppel FROM tblpelanggan INNER JOIN (tblorder INNER JOIN (tbljasa INNER JOIN tblorderdetail ON tbljasa.idjasa = tblorderdetail.idjasa) ON tblorder.idorder = tblorderdetail.idorder) ON tblpelanggan.idpelanggan = tblorder.idpelanggan");

            exc("CREATE VIEW qsalon AS SELECT tblorderdetail.idorderdetail, tblorderdetail.idorder, tblorderdetail.idjasa, tblorderdetail.hargajual, tblorderdetail.jumlah, tblorder.faktur, tblorder.tglorder, tblorder.tipe, tbljasa.jasa, tbljasa.harga FROM tbljasa, tblorder INNER JOIN tblorderdetail ON tblorder.idorder = tblorderdetail.idorder");

            //create trigger kurang_total
            exc("CREATE TRIGGER kurang_total AFTER DELETE ON tblorderdetail FOR EACH ROW BEGIN UPDATE tblorder SET total= total - (OLD.hargajual * OLD.jumlah) WHERE idorder = OLD.idorder; END");

            //create trigger tambah_total
            exc("CREATE TRIGGER tambah_total AFTER INSERT ON tblorderdetail FOR EACH ROW BEGIN UPDATE tblorder SET total= total + (NEW.hargajual * NEW.jumlah) WHERE idorder = NEW.idorder; END");

            //create Identitas
            exc("INSERT INTO tblidentitas VALUES (1, 'KomputerKit.com','Sidoarjo','0838 320 320 77','Terima Kasih','Sudah Datang','Di Salon Kami')");

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

    //Jasa

    public List<String> getIdJasa(){
        List<String> labels = new ArrayList<String>();
        String q=QuerySalon.select("tbljasa");
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(0));
            }while (c.moveToNext());
        }
        return labels;
    }

    public boolean insertJasa(String Jasa, String harga){
        ContentValues cv= new ContentValues();
        cv.put("jasa", Jasa );
        cv.put("harga", harga );
        long result= db.insert("tbljasa", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public List<String> getJasa(){
        List<String> labels = new ArrayList<String>();
        String q=QuerySalon.select("tbljasa");
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(1));
            }while (c.moveToNext());
        }
        return labels;
    }

    public Boolean deleteJasa(Integer idjasa){
        Cursor c = sq("SELECT * FROM tblorderdetail WHERE idjasa="+FunctionSalon.intToStr(idjasa));
        if (FunctionSalon.getCount(c)==0){
            db.delete("tbljasa","idjasa=?",new String[]{String.valueOf(idjasa)});
            return true;
        }else{
            return false;
        }
    }

    public Boolean updateJasa(int idJasa, String jasa, String harga) {
        ContentValues cv = new ContentValues();
        cv.put("jasa", jasa);
        cv.put("harga", harga);
        long result = db.update("tbljasa", cv, "idjasa=?", new String[]{String.valueOf(idJasa)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Pelanggan

    public List<String> getIdPelanggan(){
        List<String> labels = new ArrayList<String>();
        String q=QuerySalon.select("tblpelanggan");
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
        cv.put("alamatpel", alamat );
        cv.put("telppel", notelp );
        long result= db.insert("tblpelanggan", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public List<String> getPelanggan(){
        List<String> labels = new ArrayList<String>();
        String q=QuerySalon.select("tblpelanggan");
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(1));
            }while (c.moveToNext());
        }
        return labels;
    }

    public Boolean deletePelanggan(Integer idpelanggan){
        Cursor c = sq("SELECT * FROM tblorder WHERE idpelanggan ="+FunctionSalon.intToStr(idpelanggan));
        Cursor c1 = sq("SELECT * FROM tbljanji WHERE idpelanggan ="+FunctionSalon.intToStr(idpelanggan));
        if (FunctionSalon.getCount(c)==0 && FunctionSalon.getCount(c1)==0){
            db.delete("tblpelanggan","idpelanggan= ?",new String[]{String.valueOf(idpelanggan)});
            return true;
        }else{
            return false;
        }
    }

    public Boolean updatePelanggan(int idpelanggan, String pelanggan, String alamat, String notelp){
        ContentValues cv = new ContentValues();
        cv.put("pelanggan", pelanggan );
        cv.put("alamatpel", alamat );
        cv.put("telppel", notelp );
        long result = db.update("tblpelanggan", cv, "idpelanggan=?", new String[]{String.valueOf(idpelanggan)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Janji

    public boolean insertJanji(String idpelanggan, String tgljanji, String jamjanji, String status){
        ContentValues cv= new ContentValues();
        cv.put("idpelanggan", idpelanggan );
//        cv.put("pel", pel );
        cv.put("tgljanji", tgljanji);
        cv.put("jamjanji", jamjanji);
        cv.put("status", status );
        long result= db.insert("tbljanji", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean updateJanji(int idjanji, String idpelanggan, String tgljanji, String jamjanji, String status) {
        ContentValues cv = new ContentValues();
        cv.put("idpelanggan", idpelanggan );
//        cv.put("pel", pel );
        cv.put("tgljanji", tgljanji);
        cv.put("jamjanji", jamjanji);
        cv.put("status", status );
        long result = db.update("tbljanji", cv, "idjanji=?", new String[]{String.valueOf(idjanji)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean deleteJanji(Integer idjasa){
        if (db.delete("tbljanji","idjanji=?",new String[]{String.valueOf(idjasa)})==-1){
            return false;
        }else{
            return true;
        }
    }
}
