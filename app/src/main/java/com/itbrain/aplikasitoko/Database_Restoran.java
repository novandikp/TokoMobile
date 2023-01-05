package com.itbrain.aplikasitoko;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Restoran extends SQLiteOpenHelper {
    Context ctx;
    SQLiteDatabase db;
    public Database_Restoran(Context context) {
        super(context, FPref.db, null, FPref.version);
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
        String bawapulang=ModulRestoran.getResString(ctx,R.string.bawapulang);
        String tamu=ModulRestoran.getResString(ctx,R.string.tamu);
        String tanpaketerangan=ModulRestoran.getResString(ctx,R.string.tanpaketerangan);
        try {

            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` (" +
                    "`id`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`namatoko`TEXT," +
                    "`notelp`INTEGER," +
                    "`alamat`TEXT," +
                    "`caption1`TEXT," +
                    "`caption2`TEXT," +
                    "`caption3`TEXT," +
                    "`pajak`INTEGER" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` (" +
                    "`idpelanggan`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`namapelanggan`TEXT," +
                    "`notelppelanggan`INTEGER," +
                    "`alamatpelanggan`TEXT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblmeja` (" +
                    "`idmeja`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`meja`TEXT," +
                    "`statusmeja`INTEGER DEFAULT '0'" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblkategori` (" +
                    "`idkategori`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`kategori`TEXT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblmakanan` (" +
                    "`idmakanan`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`idkategori`INTEGER," +
                    "`makanan`TEXT," +
                    "`harga`REAL," +
                    "FOREIGN KEY(`idkategori`) REFERENCES `tblkategori`(`idkategori`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblorder` (" +
                    "`faktur`TEXT," +
                    "`idmeja`INTEGER," +
                    "`idpelanggan`INTEGER," +
                    "`tglorder`INTEGER," +
                    "`total`REAL DEFAULT '0'," +
                    "`bayar`REAL," +
                    "`kembali`REAL," +
                    "`status`INTEGER," +
                    "FOREIGN KEY(`idmeja`) REFERENCES `tblmeja`(`idmeja`) ON UPDATE CASCADE ON DELETE RESTRICT," +
                    "FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");
            exc("CREATE TABLE IF NOT EXISTS `tblorderdetail` (" +
                    "`idorderdetail`INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`faktur`TEXT," +
                    "`idmakanan`INTEGER," +
                    "`jumlahorder`REAL," +
                    "`hargaorder`REAL," +
                    "`keterangan`TEXT DEFAULT '"+tanpaketerangan+"'," +
                    "FOREIGN KEY(`faktur`) REFERENCES `tblorder`(`faktur`) ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ");");
            exc("CREATE VIEW IF NOT EXISTS qmakanan AS SELECT " +
                    "tblmakanan.idmakanan, " +
                    "tblmakanan.idkategori, " +
                    "tblkategori.kategori, " +
                    "tblmakanan.makanan, " +
                    "tblmakanan.harga " +
                    "FROM tblmakanan " +
                    "INNER JOIN tblkategori ON tblmakanan.idkategori=tblkategori.idkategori");
            exc("CREATE VIEW IF NOT EXISTS qorderdetail AS SELECT " +
                    "tblorderdetail.idorderdetail, " +
                    "tblorderdetail.faktur, " +
                    "tblorderdetail.idmakanan, " +
                    "tblmakanan.makanan, " +
                    "tblorderdetail.jumlahorder, " +
                    "tblorderdetail.hargaorder, " +
                    "tblorderdetail.keterangan " +
                    "FROM tblorderdetail " +
                    "INNER JOIN tblmakanan ON tblorderdetail.idmakanan=tblmakanan.idmakanan");
            exc("CREATE VIEW IF NOT EXISTS qorder AS SELECT " +
                    "tblorder.idmeja, " +
                    "tblmeja.meja, " +
                    "tblorder.idpelanggan, " +
                    "tblpelanggan.namapelanggan, " +
                    "tblorder.tglorder, " +
                    "tblorder.total, " +
                    "tblorder.bayar, " +
                    "tblorder.kembali, " +
                    "tblorder.status, " +
                    "tblorderdetail.idorderdetail, " +
                    "tblorderdetail.faktur, " +
                    "tblmakanan.idkategori, " +
                    "tblkategori.kategori, " +
                    "tblorderdetail.idmakanan, " +
                    "tblmakanan.makanan, " +
                    "tblmakanan.harga, " +
                    "tblorderdetail.jumlahorder, " +
                    "tblorderdetail.hargaorder, " +
                    "tblorderdetail.keterangan " +
                    "FROM tblorderdetail " +
                    "INNER JOIN tblorder ON tblorderdetail.faktur=tblorder.faktur " +
                    "INNER JOIN tblmeja ON tblorder.idmeja=tblmeja.idmeja " +
                    "INNER JOIN tblpelanggan ON tblorder.idpelanggan=tblpelanggan.idpelanggan " +
                    "INNER JOIN tblmakanan ON tblorderdetail.idmakanan=tblmakanan.idmakanan " +
                    "INNER JOIN tblkategori ON tblmakanan.idkategori=tblkategori.idkategori");
            exc("CREATE VIEW IF NOT EXISTS qorderinfo AS SELECT " +
                    "tblorder.faktur, " +
                    "tblorder.idmeja, " +
                    "tblmeja.meja, " +
                    "tblmeja.statusmeja, " +
                    "tblorder.idpelanggan, " +
                    "tblpelanggan.namapelanggan, " +
                    "tblorder.tglorder, " +
                    "tblorder.total, " +
                    "tblorder.bayar, " +
                    "tblorder.kembali, " +
                    "tblorder.status " +
                    "FROM tblorder " +
                    "INNER JOIN tblmeja ON tblorder.idmeja=tblmeja.idmeja " +
                    "INNER JOIN tblpelanggan ON tblorder.idpelanggan=tblpelanggan.idpelanggan");
            exc("CREATE TRIGGER IF NOT EXISTS kurang_order " +
                    "AFTER DELETE ON tblorder " +
                    "FOR EACH ROW BEGIN DELETE FROM tblorderdetail " +
                    "WHERE tblorderdetail.faktur = OLD.faktur; " +
                    "END");
            exc("INSERT INTO tblidentitas (id,namatoko,notelp,alamat,caption1,caption2,caption3,pajak) VALUES (1,'KomputerKit.com',83832032077,'Sidoarjo','Terima Kasih','Sudah Berbelanja','Di Toko Kami',10)");
            exc("INSERT INTO tblpelanggan (idpelanggan,namapelanggan,notelppelanggan,alamatpelanggan) VALUES (0,'"+tamu+"','','')");
            exc("INSERT INTO tblmeja (idmeja,meja,statusmeja) VALUES (0,'"+bawapulang+"',0)");
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
