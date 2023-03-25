package com.itbrain.aplikasitoko.tokosepatu;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseTokoSepatu extends SQLiteOpenHelper {
    Context a;
    SQLiteDatabase db;
    public static String nama="dbsepatuplus";
    public  static  int versi=1;

    public DatabaseTokoSepatu(Context context) {
        super(context, nama,null,versi);
        a = context ;
        db = this.getWritableDatabase() ;
       createTables();
        createTrigger();
        createView();
        setDefault();
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

    public boolean createTables(){
        createTable("CREATE TABLE `tblkategori` ( `idkategori` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `kategori` TEXT );");
        createTable("CREATE TABLE tblpelanggan (`idpelanggan` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `pelanggan` TEXT, alamat TEXT, notelp TEXT , `hutang` REAL DEFAULT 0);");
        createTable("CREATE TABLE \"tblbarang\" ( `idbarang` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idkategori` INTEGER, `barang` TEXT, `deskripsi` TEXT, `stokbarang` INTEGER DEFAULT 0, FOREIGN KEY(`idkategori`) REFERENCES `tblkategori`(`idkategori`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE \"tbltoko\" ( `idtoko` INTEGER NOT NULL DEFAULT 1 PRIMARY KEY AUTOINCREMENT, `namatoko` TEXT DEFAULT 'Ahlikasir.com', `alamattoko` TEXT DEFAULT 'Buduran', `notoko` TEXT DEFAULT 0888888, `caption1` TEXT DEFAULT 'Terima Kasih', `caption2` TEXT DEFAULT 'Sudah Berbelanja', `caption3` TEXT DEFAULT 'Di Toko Kami' )");
        createTable("CREATE TABLE `tblukuran` ( `idukuran` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idbarang` INTEGER, `ukuran` TEXT, `stok` INTEGER, `harga` REAL, FOREIGN KEY (idbarang) REFERENCES tblbarang(idbarang) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE \"tbljual\" ( `idjual` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idpelanggan` INTEGER, `tgljual` INTEGER, fakturbayar TEXT ,`total` REAL DEFAULT 0, `potongan` REAL DEFAULT 0, `bayar` REAL DEFAULT 0, `kembali` REAL DEFAULT 0, `status` TEXT, FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE \"tbldetailjual\" ( `iddetailjual` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idjual` INTEGER, `idukuran` INTEGER, `jumlah` INTEGER DEFAULT 0, `keterangan` TEXT , `hargajual` REAL,`flagretur` INTEGER DEFAULT 0, FOREIGN KEY(`idukuran`) REFERENCES `tblukuran`(`idukuran`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idjual`) REFERENCES `tbljual`(`idjual`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE \"tblbayarhutang\" ( `idbayarhutang` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idpelanggan` INTEGER, `jumlahbayar` REAL, `tglbayar` INTEGER, FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) on update cascade on delete restrict )");
        createTable("CREATE TABLE \"tblretur\" ( `idreturn` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `iddetailjual` INTEGER, `jumlahretur` INTEGER, `tglretur` INTEGER, FOREIGN KEY(`iddetailjual`) REFERENCES `tbldetailjual`(`iddetailjual`) ON UPDATE CASCADE ON DELETE RESTRICT )");
        createTable("CREATE TABLE `tbltransaksi` ( `idtransaksi` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `tgltransaksi` INTEGER, `fakturtran` TEXT, `kettransaksi` TEXT, `masuk` REAL DEFAULT 0, `keluar` REAL DEFAULT 0, `saldo` REAL DEFAULT 0 )");
        return true;
    }

    public boolean createTable(String querry){
        exc(querry);
        return  true;
    }

    public boolean createView(){
        exc("PRAGMA foreign_keys=ON");
        exc("CREATE VIEW qukuran AS SELECT tblukuran.idukuran, tblukuran.idbarang, tblukuran.ukuran, tblukuran.stok, tblukuran.harga, tblbarang.idkategori, tblbarang.barang, tblbarang.deskripsi, tblbarang.stokbarang, tblkategori.kategori\n" +
                "FROM tblkategori INNER JOIN (tblbarang INNER JOIN tblukuran ON tblbarang.idbarang = tblukuran.idbarang) ON tblkategori.idkategori = tblbarang.idkategori;");
        exc("CREATE VIEW qbarang AS SELECT tblbarang.idbarang, tblbarang.idkategori, tblbarang.barang, tblbarang.deskripsi, tblbarang.stokbarang, tblkategori.kategori\n" +
                "FROM tblkategori INNER JOIN tblbarang ON tblkategori.idkategori = tblbarang.idkategori;\n");

        exc("CREATE VIEW qdetailjual AS SELECT tbldetailjual.iddetailjual, tbldetailjual.idjual, tbldetailjual.idukuran, tbldetailjual.jumlah, tbldetailjual.keterangan, tbldetailjual.hargajual, tbldetailjual.flagretur, tbljual.idpelanggan, tbljual.tgljual, tbljual.total, tbljual.bayar, tbljual.kembali, tbljual.status, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.notelp, tblukuran.idbarang, tblukuran.ukuran, tblukuran.stok, tblukuran.harga, tblbarang.idkategori, tblbarang.barang, tblbarang.deskripsi, tblbarang.stokbarang, tblkategori.kategori\n" +
                "FROM (tblkategori INNER JOIN (tblbarang INNER JOIN tblukuran ON tblbarang.idbarang = tblukuran.idbarang) ON tblkategori.idkategori = tblbarang.idkategori) INNER JOIN (tblpelanggan INNER JOIN (tbljual INNER JOIN tbldetailjual ON tbljual.idjual = tbldetailjual.idjual) ON tblpelanggan.idpelanggan = tbljual.idpelanggan) ON tblukuran.idukuran = tbldetailjual.idukuran;");
        exc("CREATE VIEW view_orderdetail as SELECT tbldetailjual.iddetailjual, tbldetailjual.idjual, tbldetailjual.idukuran, tbldetailjual.jumlah, tbldetailjual.keterangan, tbldetailjual.hargajual, tbldetailjual.flagretur, tblukuran.idukuran, tblukuran.idbarang, tblukuran.ukuran, tblukuran.stok, tblukuran.harga, tblbarang.idbarang, tblbarang.idkategori, tblbarang.barang, tblbarang.deskripsi, tblbarang.stokbarang, tblkategori.idkategori, tblkategori.kategori, tbljual.idjual, tbljual.idpelanggan, tbljual.fakturbayar, tbljual.tgljual, tbljual.total, tbljual.bayar, tbljual.kembali, tbljual.status, tblpelanggan.idpelanggan, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.notelp, tblpelanggan.hutang FROM (tblkategori INNER JOIN (tblbarang INNER JOIN tblukuran ON tblbarang.idbarang = tblukuran.idbarang) ON tblkategori.idkategori = tblbarang.idkategori) INNER JOIN (tblpelanggan INNER JOIN (tbljual INNER JOIN tbldetailjual ON tbljual.idjual = tbldetailjual.idjual) ON tblpelanggan.idpelanggan = tbljual.idpelanggan) ON tblukuran.idukuran = tbldetailjual.idukuran");
        exc("CREATE VIEW qretur AS SELECT tblretur.idreturn, tblretur.iddetailjual, tblretur.tglretur, tblretur.jumlahretur, tbldetailjual.iddetailjual, tbldetailjual.idjual, tbldetailjual.idukuran, tbldetailjual.jumlah, tbldetailjual.keterangan, tbldetailjual.hargajual, tblukuran.idukuran, tblukuran.idbarang, tblukuran.ukuran, tblukuran.stok, tblukuran.harga, tblbarang.idbarang, tblbarang.idkategori, tblbarang.barang, tblbarang.deskripsi, tblbarang.stokbarang, tblkategori.idkategori, tblkategori.kategori, tbljual.idjual, tbljual.idpelanggan, tbljual.tgljual, tbljual.total, tbljual.bayar, tbljual.kembali, tbljual.status, tbljual.fakturbayar, tblpelanggan.idpelanggan, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.notelp\n" +
                "FROM (tblkategori INNER JOIN (tblbarang INNER JOIN tblukuran ON tblbarang.idbarang = tblukuran.idbarang) ON tblkategori.idkategori = tblbarang.idkategori) INNER JOIN (tblpelanggan INNER JOIN (tbljual INNER JOIN (tbldetailjual INNER JOIN tblretur ON tbldetailjual.iddetailjual = tblretur.iddetailjual) ON tbljual.idjual = tbldetailjual.idjual) ON tblpelanggan.idpelanggan = tbljual.idpelanggan) ON tblukuran.idukuran = tbldetailjual.idukuran;\n");
        exc("CREATE VIEW qjual AS SELECT tbljual.idjual, tbljual.idpelanggan, tbljual.tgljual, tbljual.total, tbljual.potongan, tbljual.bayar, tbljual.kembali,tbljual.fakturbayar , tbljual.status, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.notelp FROM tblpelanggan INNER JOIN tbljual ON tblpelanggan.idpelanggan = tbljual.idpelanggan");
        return true;
    }

    public boolean setDefault(){
        exc("insert into tbltoko (idtoko) values (1);");
        exc("INSERT INTO tblkategori (idkategori,kategori) values (1,'Pria')");
        exc("INSERT INTO tblbarang (idbarang,idkategori,barang,deskripsi) values (1,1,'Sneaker','Deskripsi barang')");
        exc("INSERT INTO tblukuran (idukuran,idbarang,ukuran,stok,harga)  values (1,1,'39',1,500000.0");
        exc("INSERT INTO tblpelanggan (idpelanggan,pelanggan,alamat,notelp) values (1,'Kosong','Kosong','Kosong')");
        return  true;
    }


    public boolean createTrigger(){
        exc("CREATE TRIGGER setharga AFTER INSERT ON tbldetailjual FOR EACH ROW BEGIN UPDATE tbldetailjual SET hargajual = (select harga from tblukuran where idukuran=new.idukuran) where iddetailjual=new.iddetailjual; END");
        exc("CREATE TRIGGER tambahstokbarang AFTER INSERT ON tblukuran FOR EACH ROW BEGIN UPDATE tblbarang SET stokbarang = stokbarang + NEW.stok WHERE idbarang=NEW.idbarang; END");
        exc("CREATE TRIGGER kurangstokbarang BEFORE DELETE ON tblukuran FOR EACH ROW BEGIN UPDATE tblbarang SET stokbarang = stokbarang - OLD.stok WHERE idbarang=OLD.idbarang; END");
        exc("CREATE TRIGGER kurangstok AFTER INSERT ON tbldetailjual FOR EACH ROW BEGIN UPDATE tblukuran SET stok = stok - NEW.jumlah WHERE idukuran=new.idukuran; END");
        exc("CREATE TRIGGER tambahstok BEFORE DELETE ON tbldetailjual FOR EACH ROW BEGIN UPDATE tblukuran SET stok = stok + OLD.jumlah WHERE idukuran=OLD.idukuran; END");
        exc("CREATE TRIGGER kurangtotal AFTER DELETE ON tbldetailjual FOR EACH ROW BEGIN UPDATE tbljual set total=total-(OLD.jumlah*OLD.hargajual) WHERE idjual=OLD.idjual; END");
//        exc("CREATE TRIGGER tambahtotal AFTER INSERT ON tbldetailjual FOR EACH ROW BEGIN UPDATE tbljual set total=total+(new.jumlah*new.hargajual) WHERE idjual=new.idjual; END");
        exc("CREATE TRIGGER tambahstokupdate AFTER update ON tbldetailjual for each row WHEN new.jumlah>old.jumlah begin update tblukuran set stok = stok-(new.jumlah-old.jumlah) where idukuran=old.idukuran; end");
        exc("CREATE TRIGGER kurangstokupdate AFTER update ON tbldetailjual for each row WHEN new.jumlah<old.jumlah begin update tblukuran set stok = stok+(old.jumlah-new.jumlah) where idukuran=old.idukuran; end");
        exc("CREATE TRIGGER tambahupdatetotal AFTER UPDATE ON tbldetailjual FOR EACH ROW WHEN new.jumlah>old.jumlah BEGIN UPDATE tbljual set total=total+((new.jumlah-old.jumlah)*old.hargajual) WHERE idjual=old.idjual; END");
        exc("CREATE TRIGGER kurangupdatetotal AFTER UPDATE ON tbldetailjual FOR EACH ROW WHEN new.jumlah<old.jumlah BEGIN UPDATE tbljual set total=total-((old.jumlah-new.jumlah)*old.hargajual) WHERE idjual=old.idjual; END");
        exc("CREATE TRIGGER \n" +
                "tambahupdatestkbr\n" +
                " AFTER UPDATE ON tblukuran \n" +
                " FOR EACH ROW \n" +
                " WHEN new.stok>old.stok \n" +
                " BEGIN UPDATE tblbarang \n" +
                " set stokbarang=stokbarang+(new.stok-old.stok) WHERE idbarang=old.idbarang; END");
        exc("CREATE TRIGGER \n" +
                "kurangupdatestkbr\n" +
                " AFTER UPDATE ON tblukuran \n" +
                " FOR EACH ROW \n" +
                " WHEN new.stok<old.stok \n" +
                " BEGIN UPDATE tblbarang \n" +
                " set stokbarang=stokbarang-(old.stok-new.stok) WHERE idbarang=old.idbarang; END");
        exc("CREATE TRIGGER isihutang after update on tbljual for each row when new.kembali<0 begin update tblpelanggan set hutang=hutang+(new.kembali*-1) where idpelanggan=new.idpelanggan; end");
        exc("CREATE TRIGGER bayarhutang after insert on tblbayarhutang for each row  begin update tblpelanggan set hutang=hutang-new.jumlahbayar where idpelanggan=new.idpelanggan; end");
        exc("CREATE TRIGGER kelunasan after update on tblpelanggan for each row when new.hutang<0 begin update tblpelanggan set hutang=0 where idpelanggan=new.idpelanggan; end");

        exc("CREATE TRIGGER retur after insert on tblretur for each row begin update tbldetailjual set flagretur =1, jumlah=jumlah-new.jumlahretur where iddetailjual=new.iddetailjual; end");
        return true;
    }


    
}