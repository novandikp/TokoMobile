package com.itbrain.aplikasitoko.kasir;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseKasir extends SQLiteOpenHelper {
    Context a ;
    SQLiteDatabase db ;

    public DatabaseKasir(Context context, FConfigKasir config) {
        super(context, config.getDb(), null, config.getVersion());
        a = context ;
        db = this.getWritableDatabase() ;
        cektbl();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
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
            Cursor c =  db.rawQuery(query, null) ;
            return c ;
        } catch (Exception e){
            return null ;
        }
    }

//    public boolean update(){
//        try{
//            String addkolom = "ALTER TABLE tblpenjualan " +
//                                "ADD keterangan TEXT;" ;
//
//            String deleteqpenj = "DROP VIEW qpenjualan" ;
//
//            String qpenjualan = "CREATE VIEW IF NOT EXISTS qpenjualan AS SELECT  " +
//                    "tblpenjualan.idpenjualan, " +
//                    "tblpenjualan.fakturbayar, " +
//                    "tblpenjualan.tgljual, " +
//                    "tblpenjualan.hargajual, " +
//                    "tblpenjualan.jumlahjual, " +
//                    "tblpenjualan.idbarang, " +
//                    "tblpenjualan.flagjual, " +
//                    "tblpenjualan.labarugi, " +
//                    "tblpenjualan.keterangan as ketpenjualan, " +
//                    "qbarang.barang, " +
//                    "qbarang.idkategori, " +
//                    "qbarang.hargabeli, " +
//                    "qbarang.hargajual, " +
//                    "qbarang.stok, " +
//                    "qbarang.titipan, " +
//                    "qbarang.kategori, " +
//                    "qbayar.tglbayar, " +
//                    "qbayar.jumlahbayar, " +
//                    "qbayar.bayar, " +
//                    "qbayar.kembali, " +
//                    "qbayar.idpelanggan, " +
//                    "qbayar.pelanggan, " +
//                    "qbayar.telp, " +
//                    "qbayar.alamat, " +
//                    "qbayar.flagbayar, " +
//                    "qbayar.keterangan " +
//                    "FROM qbayar INNER JOIN (qbarang INNER JOIN tblpenjualan ON qbarang.idbarang = tblpenjualan.idbarang) ON qbayar.fakturbayar = tblpenjualan.fakturbayar ; " ;
//
//            String setkolom = "update tblpenjualan set keterangan='-'" ;
//
//            if (exc(addkolom) && exc(deleteqpenj) && exc(qpenjualan) && exc(setkolom)) {
//                return true ;
//            } else {
//                return false ;
//            }
//        }catch (Exception e){
//            return false ;
//        }
//    }
    
    public Boolean cektbl(){
        try {
            exc("CREATE TABLE `tblbarang` ( " +
                    " `idbarang` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `barang` INTEGER UNIQUE, " +
                    " `idkategori` INTEGER, " +
                    " `hargabeli` REAL, " +
                    " `hargajual` REAL, " +
                    " `stok` REAL, " +
                    " `titipan` INTEGER " +
                    ");") ;

            exc("CREATE TABLE IF NOT EXISTS `tblbayar` ( " +
                    " `fakturbayar` TEXT, " +
                    " `tglbayar` INTEGER, " +
                    " `jumlahbayar` REAL, " +
                    " `bayar` REAL, " +
                    " `kembali` REAL, " +
                    " `idpelanggan` INTEGER, " +
                    " `keterangan` TEXT, " +
                    " `flagbayar` INTEGER, " +
                    " PRIMARY KEY(`fakturbayar`) " +
                    ");") ;

            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` ( " +
                    " `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `nama` TEXT, " +
                    " `alamat` TEXT, " +
                    " `telp` TEXT, " +
                    " `caption1` TEXT, " +
                    " `caption2` TEXT, " +
                    " `caption3` TEXT " +
                    ");") ;


            exc("CREATE TABLE IF NOT EXISTS `tblkategori` ( " +
                    " `idkategori` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `kategori` TEXT UNIQUE" +
                    ");") ;
            exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` ( " +
                    " `idpelanggan` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `pelanggan` TEXT, " +
                    " `telp` TEXT, " +
                    " `alamat` TEXT " +
                    ");") ;
            exc("CREATE TABLE IF NOT EXISTS `tblpenjualan` ( " +
                    " `idpenjualan` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `fakturbayar` TEXT, " +
                    " `tgljual` INTEGER, " +
                    " `hargajual` REAL, " +
                    " `jumlahjual` REAL, " +
                    " `idbarang` INTEGER, " +
                    " `flagjual` INTEGER, " +
                    " `keterangan` TEXT, " +
                    " `labarugi` REAL " +
                    ");") ;

            exc("CREATE TABLE IF NOT EXISTS `tblreturn` (" +
                    "`idreturn`INTEGER," +
                    "`idbarang`INTEGER," +
                    "`fakturbayar`TEXT," +
                    "`tglreturn`TEXT," +
                    "`jumlah`INTEGER," +
                    "PRIMARY KEY(`idreturn`)" +
                    ");") ;





            exc("CREATE VIEW IF NOT EXISTS qbarang AS SELECT " +
                    "tblbarang.idbarang, " +
                    "tblbarang.barang, " +
                    "tblbarang.idkategori, " +
                    "tblbarang.hargabeli, " +
                    "tblbarang.hargajual, " +
                    "tblbarang.stok, " +
                    "tblbarang.titipan, " +
                    "tblkategori.kategori " +
                    "FROM tblkategori INNER JOIN tblbarang ON tblkategori.idkategori = tblbarang.idkategori;") ;

            exc("CREATE VIEW IF NOT EXISTS qbayar AS SELECT " +
                    "tblbayar.fakturbayar, " +
                    "tblbayar.tglbayar, " +
                    "tblbayar.jumlahbayar, " +
                    "tblbayar.bayar, " +
                    "tblbayar.kembali, " +
                    "tblbayar.idpelanggan, " +
                    "tblbayar.flagbayar, " +
                    "tblbayar.keterangan, " +
                    "tblpelanggan.pelanggan, " +
                    "tblpelanggan.telp, " +
                    "tblpelanggan.alamat " +
                    "FROM tblpelanggan INNER JOIN tblbayar ON tblpelanggan.idpelanggan = tblbayar.idpelanggan; ") ;

            exc("CREATE VIEW IF NOT EXISTS qpenjualan AS SELECT  " +
                    "tblpenjualan.idpenjualan, " +
                    "tblpenjualan.fakturbayar, " +
                    "tblpenjualan.tgljual, " +
                    "tblpenjualan.hargajual, " +
                    "tblpenjualan.jumlahjual, " +
                    "tblpenjualan.idbarang, " +
                    "tblpenjualan.flagjual, " +
                    "tblpenjualan.labarugi, " +
                    "tblpenjualan.keterangan as ketpenjualan, " +
                    "qbarang.barang, " +
                    "qbarang.idkategori, " +
                    "qbarang.hargabeli, " +
                    "qbarang.hargajual, " +
                    "qbarang.stok, " +
                    "qbarang.titipan, " +
                    "qbarang.kategori, " +
                    "qbayar.tglbayar, " +
                    "qbayar.jumlahbayar, " +
                    "qbayar.bayar, " +
                    "qbayar.kembali, " +
                    "qbayar.idpelanggan, " +
                    "qbayar.pelanggan, " +
                    "qbayar.telp, " +
                    "qbayar.alamat, " +
                    "qbayar.flagbayar, " +
                    "qbayar.keterangan " +
                    "FROM qbayar INNER JOIN (qbarang INNER JOIN tblpenjualan ON qbarang.idbarang = tblpenjualan.idbarang) ON qbayar.fakturbayar = tblpenjualan.fakturbayar ; ") ;

            exc("CREATE VIEW IF NOT EXISTS qreturn AS SELECT " +
                    "tblbarang.idbarang, " +
                    "tblbarang.barang, " +
                    "tblbarang.idkategori, " +
                    "tblbarang.hargabeli, " +
                    "tblbarang.hargajual, " +
                    "tblbarang.stok, " +
                    "tblbarang.titipan, " +
                    "tblreturn.idreturn, " +
                    "tblreturn.fakturbayar, " +
                    "tblreturn.tglreturn, " +
                    "tblreturn.jumlah " +
                    "FROM tblbarang INNER JOIN tblreturn ON tblbarang.idbarang = tblreturn.idbarang;") ;



            exc("CREATE TRIGGER IF NOT EXISTS kurang_stok AFTER INSERT ON tblpenjualan " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok - new.jumlahjual WHERE idbarang=new.idbarang ; " +
                    "END ;") ;

            exc("CREATE TRIGGER IF NOT EXISTS tambah_stok AFTER DELETE ON tblpenjualan " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok + old.jumlahjual WHERE idbarang=old.idbarang ; " +
                    "END ;") ;

            exc("CREATE TRIGGER IF NOT EXISTS tambah_stok_return AFTER INSERT ON tblreturn " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok + new.jumlah WHERE idbarang=new.idbarang ; " +
                    "END ;") ;

            exc("INSERT INTO tblpelanggan VALUES (1,'Kosong','0','')") ;
            exc("INSERT INTO tblidentitas VALUES (1,'KomputerKit.com','0838 320 320 77','0838 320 320 77','Terima Kasih','Sudah Berbelanja','Di Toko Kami')") ;
            return true ;
        }catch (Exception e){
            return false ;
        }
    } 
}
