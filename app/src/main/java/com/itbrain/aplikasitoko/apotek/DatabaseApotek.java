package com.itbrain.aplikasitoko.apotek;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseApotek extends SQLiteOpenHelper {
    Context a;
    SQLiteDatabase db;
    public static String nama="aplikasiapotekplusdb";
    public  static  int versi=1;

    public DatabaseApotek(Context context) {
        super(context, nama,null,versi);
        a = context ;
        db = this.getWritableDatabase() ;
        createTables();

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

    public boolean exc(String query) {
        try {
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor sq(String query) {
        db = this.getReadableDatabase();
        try {
            Cursor c = db.rawQuery(query, null);
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    private void createTables(){
        exc("PRAGMA foreign_keys=ON");
        exc("CREATE TABLE `tblkategori` ( `idkategori` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `kategori` TEXT )");
        exc("CREATE TABLE `tblsatuan` ( `idsatuan` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `satuanbesar` TEXT, `satuankecil` TEXT, `nilai` INTEGER )");
        exc("CREATE TABLE \"tblbarang\" ( `idbarang` TEXT NOT NULL, `idkategori` INTEGER, `barang` TEXT, `stok` REAL DEFAULT 0, `ketbarang` TEXT, `idsatuan` INTEGER, FOREIGN KEY(`idkategori`) REFERENCES `tblkategori`(`idkategori`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idsatuan`) REFERENCES `tblsatuan`(`idsatuan`) ON UPDATE CASCADE ON DELETE RESTRICT, PRIMARY KEY(`idbarang`) )");
        exc("CREATE TABLE \"tblpelanggan\" ( `idpelanggan` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `pelanggan` TEXT, `alamat` TEXT, `notelp` TEXT , `hutang` REAL DEFAULT 0 )");
        exc("CREATE TABLE `tblsupplier` ( `idsupplier` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `supplier` TEXT, `alamatsupplier` TEXT, `nosupplier` TEXT, `utang` REAL DEFAULT 0 )");
        exc("CREATE TABLE \"tblorderbeli\" ( `idorderbeli` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `fakturorderbeli` TEXT DEFAULT 'kosong', `tglorderbeli` INTEGER, `totalorderbeli` REAL DEFAULT 0, `bayarbeli` REAL DEFAULT 0 , `kembalibeli` REAL DEFAULT 0, stat INTEGER DEFAULT 0, `idsupplier` INTEGER, FOREIGN KEY(`idsupplier`) REFERENCES `tblsupplier`(`idsupplier`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        exc("CREATE TABLE \"tblbelidetail\" ( `idbelidetail` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idbarang` TEXT, `expired` INTEGER, `batchnumber` TEXT, `jumlahbeli` REAL, `hargabeli` REAL, `harga_jual_satu` REAL, `harga_jual_dua` REAL, `satuanbeli` TEXT, `hpp_satu` REAL, `hpp_dua` REAL, `flagready` INTEGER DEFAULT 1, `idorderbeli` INTEGER, FOREIGN KEY(`idorderbeli`) REFERENCES `tblorderbeli`(`idorderbeli`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idbarang`) REFERENCES `tblbarang`(`idbarang`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        exc("CREATE TABLE \"tbljual\" ( `idjual` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `fakturjual` TEXT DEFAULT 'kosong', `tgljual` INTEGER, `total` REAL DEFAULT 0, `bayar` REAL DEFAULT 0, `kembali` REAL DEFAULT 0, `idpelanggan` INTEGER, FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        exc("CREATE TABLE \"tbldetailjual\" ( `iddetailjual` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idbarang` TEXT, `idjual` INTEGER, `batchnumber` TEXT, `jumlahjual` REAL, `hargajual` REAL, `satuanjual` TEXT, `laba` REAL, FOREIGN KEY(`idjual`) REFERENCES `tbljual`(`idjual`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idbarang`) REFERENCES `tblbarang`(`idbarang`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        exc("CREATE TABLE \"tbltoko\" ( `idtoko` INTEGER NOT NULL DEFAULT 1 PRIMARY KEY AUTOINCREMENT, `namatoko` TEXT DEFAULT 'Ahlikasir.com', `alamattoko` TEXT DEFAULT 'Buduran', `notoko` TEXT DEFAULT 0888888, `caption1` TEXT DEFAULT 'Terima Kasih', `caption2` TEXT DEFAULT 'Sudah Berbelanja', `caption3` TEXT DEFAULT 'Di Toko Kami' )");
        exc("CREATE TABLE `tbltransaksi` ( `idtransaksi` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `tgltransaksi` INTEGER, `fakturtran` TEXT, `kettransaksi` TEXT, `masuk` REAL DEFAULT 0, `keluar` REAL DEFAULT 0, `saldo` REAL DEFAULT 0 )");
        exc("insert into tbltoko (idtoko) values (1);");

        exc("CREATE TABLE \"tblhutang\" (\n" +
                "\t\"idhutang\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"idpelanggan\"\tINTEGER,\n" +
                "\t\"tglbayar\"\tINTEGER,\n" +
                "\t\"bayarhutang\"\tREAL,\n" +
                "\tFOREIGN KEY(\"idpelanggan\") REFERENCES \"tblpelanggan\"(\"idpelanggan\") ON UPDATE CASCADE ON DELETE RESTRICT\n" +
                ")");

        exc("CREATE TABLE \"tblutang\" (\n" +
                "\t\"idutang\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"idsupplier\"\tINTEGER,\n" +
                "\t\"tglbayar\"\tINTEGER,\n" +
                "\t\"bayarhutang\"\tREAL,\n" +
                "\tFOREIGN KEY(\"idsupplier\") REFERENCES \"tblsupplier\"(\"idsupplier\") ON UPDATE CASCADE ON DELETE RESTRICT\n" +
                ")");
        exc("CREATE VIEW qhutang AS SELECT \"tblhutang\".\"idhutang\", \"tblhutang\".\"bayarhutang\", \"tblhutang\".\"tglbayar\", \"tblhutang\".\"idpelanggan\", \"tblpelanggan\".\"idpelanggan\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\" FROM \"tblhutang\", \"tblpelanggan\" WHERE \"tblhutang\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\"");
        exc("CREATE VIEW qhutang1 AS SELECT \"tblutang\".\"idutang\", \"tblutang\".\"bayarhutang\", \"tblutang\".\"tglbayar\", \"tblutang\".\"idsupplier\", \"tblsupplier\".\"idsupplier\", \"tblsupplier\".\"supplier\", \"tblsupplier\".\"alamatsupplier\", \"tblsupplier\".\"nosupplier\" FROM \"tblutang\", \"tblsupplier\" WHERE \"tblutang\".\"idsupplier\" = \"tblsupplier\".\"idsupplier\"");

	exc("CREATE VIEW qbarang as SELECT tblbarang.idbarang, tblbarang.barang, tblbarang.idsatuan, tblbarang.idkategori, tblbarang.stok, tblbarang.ketbarang, tblsatuan.idsatuan, tblsatuan.satuanbesar, tblsatuan.satuankecil, tblsatuan.nilai, tblkategori.idkategori, tblkategori.kategori FROM tblsatuan INNER JOIN (tblkategori INNER JOIN tblbarang ON tblkategori.idkategori = tblbarang.idkategori) ON tblsatuan.idsatuan = tblbarang.idsatuan");
	exc("CREATE VIEW qbelidetail as SELECT tblbelidetail.idbelidetail, tblbelidetail.idbarang, tblbelidetail.batchnumber, tblbelidetail.expired, tblbelidetail.jumlahbeli, tblbelidetail.hargabeli, tblbelidetail.harga_jual_satu, tblbelidetail.harga_jual_dua, tblbelidetail.satuanbeli, tblbelidetail.hpp_satu, tblbelidetail.hpp_dua, tblbelidetail.flagready, tblbelidetail.idorderbeli, tblbarang.idbarang, tblbarang.barang, tblbarang.idsatuan, tblbarang.idkategori, tblbarang.stok, tblbarang.ketbarang, tblsatuan.idsatuan, tblsatuan.satuanbesar, tblsatuan.satuankecil, tblsatuan.nilai, tblkategori.idkategori, tblkategori.kategori, tblorderbeli.idorderbeli, tblorderbeli.idsupplier, tblorderbeli.fakturorderbeli, tblorderbeli.stat, tblorderbeli.tglorderbeli, tblorderbeli.totalorderbeli, tblsupplier.idsupplier, tblsupplier.supplier, tblsupplier.alamatsupplier, tblsupplier.nosupplier FROM tblsupplier INNER JOIN (tblsatuan INNER JOIN (tblorderbeli INNER JOIN (tblkategori INNER JOIN (tblbarang INNER JOIN tblbelidetail ON tblbarang.idbarang = tblbelidetail.idbarang) ON tblkategori.idkategori = tblbarang.idkategori) ON tblorderbeli.idorderbeli = tblbelidetail.idorderbeli) ON tblsatuan.idsatuan = tblbarang.idsatuan) ON tblsupplier.idsupplier = tblorderbeli.idsupplier");
	exc("CREATE VIEW qdetailjual as SELECT tbldetailjual.iddetailjual, tbldetailjual.idbarang, tbldetailjual.idjual, tbldetailjual.batchnumber, tbldetailjual.jumlahjual, tbldetailjual.hargajual, tbldetailjual.satuanjual, tbldetailjual.laba, tblbarang.idbarang, tblbarang.barang, tblbarang.idsatuan, tblbarang.idkategori, tblbarang.stok, tblbarang.ketbarang, tblsatuan.idsatuan, tblsatuan.satuanbesar, tblsatuan.satuankecil, tblsatuan.nilai, tblkategori.idkategori, tblkategori.kategori, tbljual.idjual, tbljual.idpelanggan, tbljual.fakturjual, tbljual.tgljual, tbljual.total, tbljual.bayar, tbljual.kembali, tblpelanggan.idpelanggan, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.notelp FROM tblpelanggan INNER JOIN (tbljual INNER JOIN ((tblsatuan INNER JOIN (tblkategori INNER JOIN tblbarang ON tblkategori.idkategori = tblbarang.idkategori) ON tblsatuan.idsatuan = tblbarang.idsatuan) INNER JOIN tbldetailjual ON tblbarang.idbarang = tbldetailjual.idbarang) ON tbljual.idjual = tbldetailjual.idjual) ON tblpelanggan.idpelanggan = tbljual.idpelanggan");
	exc("CREATE VIEW qjual as SELECT tbljual.idjual, tbljual.idpelanggan, tbljual.fakturjual, tbljual.tgljual, tbljual.total, tbljual.bayar, tbljual.kembali, tblpelanggan.idpelanggan, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.notelp FROM tblpelanggan INNER JOIN tbljual ON tblpelanggan.idpelanggan = tbljual.idpelanggan");
	exc("CREATE VIEW qorderbeli as SELECT tblorderbeli.idorderbeli, tblorderbeli.idsupplier, tblorderbeli.fakturorderbeli, tblorderbeli.tglorderbeli, tblorderbeli.stat, tblorderbeli.totalorderbeli, tblsupplier.idsupplier, tblsupplier.supplier, tblsupplier.alamatsupplier, tblsupplier.nosupplier FROM tblsupplier INNER JOIN tblorderbeli ON tblsupplier.idsupplier = tblorderbeli.idsupplier");
    exc("CREATE TRIGGER tambahtotalbeli after insert on tblbelidetail for each row begin update tblorderbeli set totalorderbeli = totalorderbeli+(new.jumlahbeli*new.hargabeli); end");
    exc("CREATE TRIGGER kurangtotalbeli after delete on tblbelidetail for each row begin update tblorderbeli set totalorderbeli = totalorderbeli-(old.jumlahbeli*old.hargabeli); end");
    exc("CREATE TRIGGER kurangtotaljual after delete on tbldetailjual for each row begin update tbljual set total = total-(old.jumlahjual*old.hargajual); end");
    exc("CREATE TRIGGER tambahtotaljual after insert on tbldetailjual for each row begin update tbljual set total = total+(new.jumlahjual*new.hargajual); end");
    exc("CREATE TRIGGER kuranghutang \n" +
                "AFTER INSERT ON tblhutang \n" +
                "FOR EACH ROW BEGIN UPDATE tblpelanggan set hutang=hutang-new.bayarhutang WHERE idpelanggan=new.idpelanggan; END");
        exc("CREATE TRIGGER kurangutang \n" +
                "AFTER INSERT ON tblutang \n" +
                "FOR EACH ROW BEGIN UPDATE tblsupplier set utang=utang-new.bayarhutang WHERE idsupplier=new.idsupplier; END");


        exc("INSERT INTO tblpelanggan (idpelanggan,pelanggan,alamat,notelp) VALUES (1,'Kosong','Kosong','Kosong')");
    exc("INSERT INTO tblsupplier (idsupplier,supplier,alamatsupplier,nosupplier) VALUES (1,'Kosong','Kosong','Kosong')");
    }



}
