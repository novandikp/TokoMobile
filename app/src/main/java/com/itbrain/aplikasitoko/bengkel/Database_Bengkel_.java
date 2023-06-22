package com.itbrain.aplikasitoko.bengkel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database_Bengkel_ extends SQLiteOpenHelper {
    Context a;
    SQLiteDatabase db;
    public static String nama="dbappbengkel";
    public  static  int versi=1;

    public Database_Bengkel_(Context context) {
        super(context, nama,null,versi);
        a = context ;
        db = this.getWritableDatabase() ;
        createTables();
        createView();
        createTrigger();
        setDefault();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean exc(String query) {
        try {
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            Log.d("SQL RUN",e.getMessage());
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

    public boolean createTables(){
        exc("PRAGMA foreign_keys=ON");
        createTable("CREATE TABLE `tblkategori` ( `idkategori` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `kategori` TEXT );");
        createTable("CREATE TABLE tblpelanggan (`idpelanggan` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `pelanggan` TEXT, alamat TEXT, notelp TEXT , `hutang` REAL DEFAULT 0);");
        createTable("CREATE TABLE \"tbltoko\" ( `idtoko` INTEGER NOT NULL DEFAULT 1 PRIMARY KEY AUTOINCREMENT, `namatoko` TEXT DEFAULT 'KomputerKit.com', `alamattoko` TEXT DEFAULT 'Buduran', `notoko` TEXT DEFAULT 0888888, `caption1` TEXT DEFAULT 'Terima Kasih', `caption2` TEXT DEFAULT 'Sudah Berbelanja', `caption3` TEXT DEFAULT 'Di Toko Kami' )");
        createTable("CREATE TABLE `tblbarang` ( `idbarang` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idkategori` INTEGER, `barang` TEXT, `stok` INTEGER, `harga` REAL, `hargabeli` REAL DEFAULT 0, FOREIGN KEY(`idkategori`) REFERENCES `tblkategori`(`idkategori`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE \"tblteknisi\" ( `idteknisi` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `teknisi` TEXT, `alamatteknisi` TEXT, `noteknisi` TEXT )");
        createTable("CREATE TABLE \"tblorder\" ( `idorder` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `faktur` TEXT, `tglorder` INTEGER, `idpelanggan` INTEGER, `total` REAL DEFAULT 0, `bayar` REAL DEFAULT 0, `statusbayar` TEXT, `nopol` TEXT, `kembali` REAL DEFAULT 0, FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE \"tbldetailorder\" ( `iddetailorder` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idbarang` INTEGER, `jumlah` INTEGER, `hargajual` REAL, `idorder` INTEGER, `idteknisi` INTEGER, laba REAL, FOREIGN KEY(`idorder`) REFERENCES `tblorder`(`idorder`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idbarang`) REFERENCES `tblbarang`(`idbarang`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE \"tblhutang\" (\n" +
                "\t\"idhutang\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"idpelanggan\"\tINTEGER,\n" +
                "\t\"tglbayar\"\tINTEGER,\n" +
                "\t\"bayarhutang\"\tREAL,\n" +
                "\tFOREIGN KEY(\"idpelanggan\") REFERENCES \"tblpelanggan\"(\"idpelanggan\") ON UPDATE CASCADE ON DELETE RESTRICT\n" +
                ")");
        return true;
    }

    public boolean createTable(String querry){
        exc(querry);
        return  true;
    }

    public boolean createView(){
        exc("CREATE VIEW qorder AS SELECT \"tblorder\".\"idorder\", \"tblorder\".\"faktur\", \"tblorder\".\"tglorder\", \"tblorder\".\"idpelanggan\", \"tblorder\".\"total\", \"tblorder\".\"bayar\", \"tblorder\".\"kembali\", \"tblorder\".\"statusbayar\", \"tblorder\".\"nopol\", \"tblpelanggan\".\"idpelanggan\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\" FROM \"tblorder\", \"tblpelanggan\" WHERE \"tblorder\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\"");
        exc("CREATE VIEW qhutang AS SELECT \"tblhutang\".\"idhutang\", \"tblhutang\".\"bayarhutang\", \"tblhutang\".\"tglbayar\", \"tblhutang\".\"idpelanggan\", \"tblpelanggan\".\"idpelanggan\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\" FROM \"tblhutang\", \"tblpelanggan\" WHERE \"tblhutang\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\"");
        exc("CREATE VIEW qdetailjual AS SELECT \"tbldetailorder\".\"iddetailorder\", \"tbldetailorder\".\"idbarang\", \"tbldetailorder\".\"jumlah\", \"tbldetailorder\".\"hargajual\", \"tbldetailorder\".\"idorder\", \"tbldetailorder\".\"laba\", \"tbldetailorder\".\"idteknisi\", \"tblorder\".\"idorder\", \"tblorder\".\"faktur\", \"tblorder\".\"tglorder\", \"tblorder\".\"idpelanggan\", \"tblorder\".\"total\", \"tblorder\".\"bayar\", \"tblorder\".\"kembali\", \"tblorder\".\"statusbayar\", \"tblorder\".\"nopol\", \"tblpelanggan\".\"idpelanggan\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\", \"tblteknisi\".\"idteknisi\", \"tblteknisi\".\"teknisi\", \"tblteknisi\".\"alamatteknisi\", \"tblteknisi\".\"noteknisi\", \"tblbarang\".\"idbarang\", \"tblbarang\".\"idkategori\", \"tblbarang\".\"barang\", \"tblbarang\".\"stok\", \"tblbarang\".\"harga\", tblbarang.hargabeli, \"tblkategori\".\"idkategori\", \"tblkategori\".\"kategori\" FROM \"tbldetailorder\", \"tblorder\", \"tblpelanggan\", \"tblteknisi\", \"tblbarang\", \"tblkategori\" WHERE \"tbldetailorder\".\"idorder\" = \"tblorder\".\"idorder\" AND \"tblorder\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\" AND \"tbldetailorder\".\"idteknisi\" = \"tblteknisi\".\"idteknisi\" AND \"tbldetailorder\".\"idbarang\" = \"tblbarang\".\"idbarang\" AND \"tblbarang\".\"idkategori\" = \"tblkategori\".\"idkategori\"");
        exc("CREATE VIEW qbarang AS SELECT tblkategori.idkategori, tblkategori.kategori, tblbarang.idbarang, tblbarang.barang, tblbarang.stok, tblbarang.harga, tblbarang.hargabeli FROM tblbarang, tblkategori WHERE tblbarang.idkategori = tblkategori.idkategori");
        return true;
    }

    public boolean setDefault(){
        exc("insert into tbltoko (idtoko) values (1);");
        exc("INSERT INTO tblkategori (idkategori,kategori) values (1,'Jasa')");
        exc("INSERT INTO tblpelanggan (idpelanggan,pelanggan,alamat,notelp) values (1,'Kosong','Kosong','Kosong')");
        exc("INSERT INTO tblteknisi (idteknisi,teknisi,alamatteknisi,noteknisi) values (1,'Kosong','Kosong','Kosong')");
        return  true;
    }


    public boolean createTrigger(){
        exc("CREATE TRIGGER tambahstok BEFORE DELETE ON tbldetailorder FOR EACH ROW BEGIN UPDATE tblbarang SET stok = stok + OLD.jumlah WHERE idbarang=OLD.idbarang AND idkategori!=1; END");
        exc("CREATE TRIGGER kurangstok BEFORE INSERT ON tbldetailorder FOR EACH ROW BEGIN UPDATE tblbarang SET stok = stok - NEW.jumlah WHERE idbarang=NEW.idbarang AND idkategori!=1; END");
        exc("CREATE TRIGGER kurangstokupdate AFTER update ON tbldetailorder for each row WHEN new.jumlah>old.jumlah begin update tblbarang set stok = stok-(new.jumlah-old.jumlah) where idbarang=new.idbarang and idkategori!=1; end");
        exc("CREATE TRIGGER tambahtotal AFTER INSERT ON tbldetailorder FOR EACH ROW BEGIN UPDATE tblorder set total=total+(new.jumlah*new.hargajual) WHERE idorder=new.idorder; END");
        exc("CREATE TRIGGER kurangtotal AFTER DELETE ON tbldetailorder FOR EACH ROW BEGIN UPDATE tblorder set total=total-(old.jumlah*old.hargajual) WHERE idorder=old.idorder; END");
        exc("CREATE TRIGGER tambahupdatetotal AFTER UPDATE ON tbldetailorder FOR EACH ROW WHEN new.jumlah>old.jumlah BEGIN UPDATE tblorder set total=total+((new.jumlah-old.jumlah)*old.hargajual) WHERE idorder=old.idorder; END");
        exc("CREATE TRIGGER kuranghutang \n" +
                "AFTER INSERT ON tblhutang \n" +
                "FOR EACH ROW BEGIN UPDATE tblpelanggan set hutang=hutang-new.bayarhutang WHERE idpelanggan=new.idpelanggan; END");
        return true;
    }



}
