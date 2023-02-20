package com.itbrain.aplikasitoko.klinik;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseKlinik extends SQLiteOpenHelper {
    Context a;
    SQLiteDatabase db;
    public static String nama="dbklinik";
    public  static  int versi=1;

    public DatabaseKlinik(Context context) {
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
            Log.e("sqler", "sq: "+e.getMessage() );
            return null;
        }
    }

    public boolean createTables(){
       createTable("CREATE TABLE \"tbltoko\" ( `idtoko` INTEGER NOT NULL DEFAULT 1 PRIMARY KEY AUTOINCREMENT, `namatoko` TEXT DEFAULT 'KomputerKit.com', `alamattoko` TEXT DEFAULT 'Buduran', `notoko` TEXT DEFAULT 0888888, `caption1` TEXT DEFAULT 'Terima Kasih', `caption2` TEXT DEFAULT 'Sudah Datang', `caption3` TEXT DEFAULT 'Di Klinik Kami' )");
       createTable("CREATE TABLE `tblpasien` ( `idpasien` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `pasien` TEXT, `jk` TEXT, `alamat` TEXT, `notelp` TEXT, `umur` INTEGER, `goldarah` TEXT, `nik` TEXT, `nobpjs` TEXT )");
       createTable("CREATE TABLE `tbldokter` ( `iddokter` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `dokter` TEXT, `alamatdokter` TEXT, `nodokter` TEXT )");
       createTable("CREATE TABLE `tbljasa` ( `idjasa` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `jasa` TEXT, `harga` REAL, `bagihasil` INTEGER )");
       createTable("CREATE TABLE `tbljanji` ( `idjanji` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `tgljanji` INTEGER, `idpasien` INTEGER, `iddokter` INTEGER, `jamjanji` INTEGER, `keperluan` TEXT, FOREIGN KEY(`iddokter`) REFERENCES `tbldokter`(`iddokter`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idpasien`) REFERENCES `tblpasien`(`idpasien`) ON UPDATE CASCADE ON DELETE RESTRICT )");
       createTable("CREATE TABLE `tblperiksa` ( `idperiksa` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, fakturperiksa TEXT, `tglperiksa` INTEGER, `idpasien` INTEGER, `total` REAL DEFAULT 0, `bayar` REAL DEFAULT 0, `kembali` REAL DEFAULT 0, `umurperiksa` TEXT, `flagperiksa` INTEGER DEFAULT 0, FOREIGN KEY(`idpasien`) REFERENCES `tblpasien`(`idpasien`) ON UPDATE CASCADE ON DELETE RESTRICT )");
       createTable("CREATE TABLE `tbldetailperiksa` ( `iddetailperiksa` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idperiksa` INTEGER, `iddokter` INTEGER, `idjasa` INTEGER, `biaya` INTEGER, `keterangan` TEXT, FOREIGN KEY(`idjasa`) REFERENCES `tbljasa`(`idjasa`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`iddokter`) REFERENCES `tbldokter`(`iddokter`) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idperiksa`) REFERENCES `tblperiksa`(`idperiksa`) ON UPDATE CASCADE ON DELETE RESTRICT )");
       return true;
    }

    public boolean createTable(String querry){
        exc(querry);
        return  true;
    }

    public boolean createView(){
        exc("PRAGMA foreign_keys=ON");
        exc("CREATE VIEW view_detailperiksa AS SELECT \"tbldetailperiksa\".\"iddetailperiksa\", \"tbldetailperiksa\".\"idperiksa\", \"tbldetailperiksa\".\"iddokter\", \"tbldetailperiksa\".\"idjasa\", \"tbldetailperiksa\".\"biaya\", \"tbldetailperiksa\".\"keterangan\", \"tblperiksa\".\"tglperiksa\", tblperiksa.fakturperiksa,\"tblperiksa\".\"idpasien\", \"tblperiksa\".\"total\", \"tblperiksa\".\"bayar\", \"tblperiksa\".\"kembali\", \"tblperiksa\".\"umurperiksa\", \"tblperiksa\".\"flagperiksa\", \"tbljasa\".\"jasa\", \"tbljasa\".\"harga\", \"tbljasa\".\"bagihasil\", \"tbldokter\".\"dokter\", \"tbldokter\".\"alamatdokter\", \"tbldokter\".\"nodokter\", tblpasien.idpasien, tblpasien.pasien FROM \"tbldetailperiksa\", \"tbldokter\", tblpasien, \"tbljasa\", \"tblperiksa\" WHERE \"tbldetailperiksa\".\"iddokter\" = \"tbldokter\".\"iddokter\" AND \"tbldetailperiksa\".\"idjasa\" = \"tbljasa\".\"idjasa\" AND tblperiksa.idpasien = tblpasien.idpasien AND \"tbldetailperiksa\".\"idperiksa\" = \"tblperiksa\".\"idperiksa\"");
        exc("CREATE VIEW view_janji AS SELECT \"tbljanji\".\"idjanji\", \"tbljanji\".\"tgljanji\", \"tbljanji\".\"idpasien\", \"tbljanji\".\"iddokter\", \"tbljanji\".\"jamjanji\", \"tbljanji\".\"keperluan\", \"tbldokter\".\"dokter\", \"tbldokter\".\"alamatdokter\", \"tbldokter\".\"nodokter\", \"tblpasien\".\"pasien\", \"tblpasien\".\"jk\", \"tblpasien\".\"alamat\", \"tblpasien\".\"notelp\", \"tblpasien\".\"umur\", \"tblpasien\".\"goldarah\", \"tblpasien\".\"nik\", \"tblpasien\".\"nobpjs\" FROM \"tbljanji\", \"tbldokter\", \"tblpasien\" WHERE \"tbljanji\".\"iddokter\" = \"tbldokter\".\"iddokter\" AND \"tbljanji\".\"idpasien\" = \"tblpasien\".\"idpasien\" ");
        exc("CREATE VIEW view_periksa AS SELECT \"tblperiksa\".\"idperiksa\", \"tblperiksa\".\"tglperiksa\", tblperiksa.flagperiksa, tblperiksa.fakturperiksa, tblperiksa.umurperiksa , \"tblperiksa\".\"idpasien\", \"tblperiksa\".\"total\", \"tblperiksa\".\"bayar\", \"tblperiksa\".\"kembali\", \"tblpasien\".\"pasien\", \"tblpasien\".\"jk\", \"tblpasien\".\"alamat\", \"tblpasien\".\"notelp\", \"tblpasien\".\"umur\", \"tblpasien\".\"goldarah\", \"tblpasien\".\"nik\", \"tblpasien\".\"nobpjs\" FROM \"tblperiksa\", \"tblpasien\" WHERE \"tblperiksa\".\"idpasien\" = \"tblpasien\".\"idpasien\"");
        return true;
    }

    public boolean setDefault(){
        exc("insert into tbltoko (idtoko) values (1);");
       exc("INSERT INTO tbldokter (iddokter,dokter,alamatdokter,nodokter) VALUES (1,'Pemilik','Sidoarjo','888888')");
       exc("INSERT INTO tblpasien (idpasien,pasien,alamat,notelp,jk,umur,goldarah,nik,nobpjs) VALUES (1,'Umum','Sidoarjo','08888888','L',20011227,'O','00000000000','0000000000')");

        return  true;
    }


    public boolean createTrigger(){
       exc("CREATE TRIGGER kurangbiaya AFTER DELETE ON tbldetailperiksa FOR EACH ROW begin UPDATE tblperiksa SET total = total-old.biaya WHERE idperiksa=old.idperiksa; end");
        exc("CREATE TRIGGER tambahbiaya AFTER INSERT ON tbldetailperiksa FOR EACH ROW begin UPDATE tblperiksa SET total = total+new.biaya WHERE idperiksa=new.idperiksa; end");
        return true;
    }


    
}