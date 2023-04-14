package com.itbrain.aplikasitoko.Laundry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseLaundry extends SQLiteOpenHelper {
    Context ctx;
    SQLiteDatabase db;
    public static String nama="db_laundry_keuangan";
    public DatabaseLaundry(@Nullable Context context) {
        super(context, PrefLaundry.db,null, PrefLaundry.version);
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
                    "`total`REAL DEFAULT 0," +
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
            exc("CREATE TABLE IF NOT EXISTS `tblkeuangan` (" +
                    "`idtransaksi`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`tgltransaksi`INTEGER," +
                    "`faktur`TEXT," +
                    "`keterangantransaksi`TEXT," +
                    "`masuk`REAL DEFAULT '0'," +
                    "`keluar`REAL DEFAULT '0'," +
                    "`saldo`REAL DEFAULT '0'" +
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
            exc("INSERT INTO tblidentitas VALUES (1,'KomputerKit.com','Sidoarjo','0838 320 320 77','Terima Kasih','Sudah Berbelanja','Di Toko Kami')") ;
            exc("INSERT INTO tblpegawai VALUES (0,'Pemilik',' ',' ')");
            exc("INSERT INTO tblpelanggan VALUES (0,'Umum',' ',' ',0)");
            exc("DELETE FROM tblkategori WHERE idkategori=0");
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public List<String> getKategori(){
        List<String> labels = new ArrayList<String>();
        labels.clear();
        String q= QueryLaundry.select("tblkategori");
        Cursor c = sq(q);
        if (ModulLaundry.getCount(c)>0){
            labels.add("Semua Kategori");
            while (c.moveToNext()) {
                labels.add(ModulLaundry.getStringFromColumn(c,1));
            }
        }else {
            labels.add("Data Kosong");
        }
        return labels;
    }
    public List<String> getIdKategori(){
        List<String> labels = new ArrayList<String>();
        labels.clear();
        String q= QueryLaundry.select("tblkategori");
        Cursor c = sq(q);
        if (ModulLaundry.getCount(c)>0){
            labels.add("0");
            while (c.moveToNext()) {
                labels.add(ModulLaundry.getStringFromColumn(c,0));
            }
        }else {
            labels.add("0");
        }
        return labels;
    }
    public int getKategoriPosition(String idkategori){
        String q= QueryLaundry.selectwhere("tblkategori")+"idkategori<"+idkategori;
        Cursor c=sq(q);
        return ModulLaundry.getCount(c);
    }
}
