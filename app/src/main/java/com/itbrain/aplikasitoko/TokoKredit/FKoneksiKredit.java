package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

/**
 * Created by KomputerKit on 31/05/2017.
 */

class a extends SQLiteOpenHelper {

    public a(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

public class FKoneksiKredit extends SQLiteOpenHelper {

    Context a;
    SQLiteDatabase db;

    public FKoneksiKredit(Context context, FConfigKredit config) {
        super(context, config.getDb(), null, config.getVersion());
        a = context;
        db = this.getWritableDatabase();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }

    public boolean exc(String query) {
        try {
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            Log.e("err", e.getMessage());
            return false;
        }
    }

    public Cursor sq(String query) {
        try {
            return db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e("err", e.getMessage());
            return null;
        }
    }

    void cektbl() {
        try {
            exc("CREATE TABLE IF NOT EXISTS `tblkeuangan` (\n" +
                    "\t`idkeuangan`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`no_transaksi`\tINTEGER,\n" +
                    "\t`tglkeuangan`\tTEXT,\n" +
                    "\t`faktur`\tTEXT,\n" +
                    "\t`keterangan`\tINTEGER,\n" +
                    "\t`masuk`\tREAL,\n" +
                    "\t`keluar`\tREAL,\n" +
                    "\t`saldo`\tREAL\n" +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblkredit` (\n" +
                    "\t`idkredit`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`idpelanggan`\tINTEGER,\n" +
                    "\t`faktur`\tTEXT,\n" +
                    "\t`tglkredit`\tTEXT,\n" +
                    "\t`jumlahkredit`\tREAL,\n" +
                    "\t`lamakredit`\tINTEGER,\n" +
                    "\t`angsuran`\tREAL,\n" +
                    "\t`jumlahcicilan`\tREAL,\n" +
                    "\t`denda`\tREAL,\n" +
                    "\t`tgljatuhtempo`\tTEXT,\n" +
                    "\t`flag_kredit`\tINTEGER,\n" +
                    "\t`saldokredit`\tREAL\n" +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tbltagihan` (\n" +
                    "\t`idtagihan`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`idkredit`\tINTEGER,\n" +
                    "\t`jumlahangsuran`\tREAL,\n" +
                    "\t`cicilan`\tINTEGER,\n" +
                    "\t`jumlahdenda`\tREAL,\n" +
                    "\t`tglbayar`\tTEXT,\n" +
                    "\t`tgltempo`\tTEXT,\n" +
                    "\t`bayardenda`\tINTEGER,\n" +
                    "\t`jumlahterlambat`\tINTEGER,\n" +
                    "\t`flag_tagihan`\tINTEGER\n" +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblbarang` ( " +
                    " `idbarang` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `barang` INTEGER UNIQUE, " +
                    " `idkategori` INTEGER, " +
                    " `hargabeli` REAL, " +
                    " `hargajual` REAL, " +
                    " `stok` REAL, " +
                    " `titipan` INTEGER " +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblbayar` ( " +
                    " `fakturbayar` TEXT, " +
                    " `tglbayar` TEXT, " +
                    " `jumlahbayar` REAL, " +
                    " `bayar` REAL, " +
                    " `kembali` REAL, " +
                    " `idpelanggan` INTEGER, " +
                    " `keterangan` TEXT, " +
                    " `flagbayar` INTEGER, " +
                    " PRIMARY KEY(`fakturbayar`) " +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblbayar_kredit` ( " +
                    " `fakturbayar` TEXT, " +
                    " `tglbayar` TEXT, " +
                    " `jumlahbayar` REAL, " +
                    " `bayar` REAL, " +
                    " `kembali` REAL, " +
                    " `idpelanggan` INTEGER, " +
                    " `keterangan` TEXT, " +
                    " `flagbayar` INTEGER, " +
                    " PRIMARY KEY(`fakturbayar`) " +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblidentitas` ( " +
                    " `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `nama` TEXT, " +
                    " `alamat` TEXT, " +
                    " `telp` TEXT, " +
                    " `caption1` TEXT, " +
                    " `caption2` TEXT, " +
                    " `caption3` TEXT " +
                    ");");


            exc("CREATE TABLE IF NOT EXISTS `tblkategori` ( " +
                    " `idkategori` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `kategori` TEXT UNIQUE" +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` ( " +
                    " `idpelanggan` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `pelanggan` TEXT, " +
                    " `telp` TEXT, " +
                    " `alamat` TEXT " +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblpenjualan` ( " +
                    " `idpenjualan` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `fakturbayar` TEXT, " +
                    " `tgljual` TEXT, " +
                    " `hargajual` REAL, " +
                    " `jumlahjual` REAL, " +
                    " `idbarang` INTEGER, " +
                    " `flagjual` INTEGER, " +
                    " `keterangan` TEXT, " +
                    " `labarugi` REAL " +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblpenjualan_kredit` ( " +
                    " `idpenjualan` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " `fakturbayar` TEXT, " +
                    " `tgljual` TEXT, " +
                    " `hargajual` REAL, " +
                    " `jumlahjual` REAL, " +
                    " `idbarang` INTEGER, " +
                    " `flagjual` INTEGER, " +
                    " `keterangan` TEXT, " +
                    " `labarugi` REAL " +
                    ");");

            exc("CREATE TABLE IF NOT EXISTS `tblreturn` (" +
                    "`idreturn` INTEGER," +
                    "`idbarang` INTEGER," +
                    "`fakturbayar` TEXT," +
                    "`tglreturn` TEXT," +
                    "`jumlah` INTEGER," +
                    "PRIMARY KEY(`idreturn`)" +
                    ");");

            exc("CREATE VIEW IF NOT EXISTS qbarang AS SELECT " +
                    "tblbarang.idbarang, " +
                    "tblbarang.barang, " +
                    "tblbarang.idkategori, " +
                    "tblbarang.hargabeli, " +
                    "tblbarang.hargajual, " +
                    "tblbarang.stok, " +
                    "tblbarang.titipan, " +
                    "tblkategori.kategori " +
                    "FROM tblkategori INNER JOIN tblbarang ON tblkategori.idkategori = tblbarang.idkategori;");

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
                    "FROM tblpelanggan INNER JOIN tblbayar ON tblpelanggan.idpelanggan = tblbayar.idpelanggan; ");

            exc("CREATE VIEW IF NOT EXISTS qbayar_kredit AS SELECT " +
                    "tblbayar_kredit.fakturbayar, " +
                    "tblbayar_kredit.tglbayar, " +
                    "tblbayar_kredit.jumlahbayar, " +
                    "tblbayar_kredit.bayar, " +
                    "tblbayar_kredit.kembali, " +
                    "tblbayar_kredit.idpelanggan, " +
                    "tblbayar_kredit.flagbayar, " +
                    "tblbayar_kredit.keterangan, " +
                    "tblpelanggan.pelanggan, " +
                    "tblpelanggan.telp, " +
                    "tblpelanggan.alamat " +
                    "FROM tblbayar_kredit INNER JOIN tblpelanggan USING(idpelanggan)");

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
                    "FROM qbayar INNER JOIN (qbarang INNER JOIN tblpenjualan ON qbarang.idbarang = tblpenjualan.idbarang) ON qbayar.fakturbayar = tblpenjualan.fakturbayar; ");

            exc("CREATE VIEW IF NOT EXISTS qpenjualan_kredit AS SELECT  " +
                    "tblpenjualan_kredit.idpenjualan, " +
                    "tblpenjualan_kredit.fakturbayar, " +
                    "tblpenjualan_kredit.tgljual, " +
                    "tblpenjualan_kredit.hargajual, " +
                    "tblpenjualan_kredit.jumlahjual, " +
                    "tblpenjualan_kredit.idbarang, " +
                    "tblpenjualan_kredit.flagjual, " +
                    "tblpenjualan_kredit.labarugi, " +
                    "tblpenjualan_kredit.keterangan as ketpenjualan, " +
                    "qbarang.barang, " +
                    "qbarang.idkategori, " +
                    "qbarang.hargabeli, " +
                    "qbarang.hargajual, " +
                    "qbarang.stok, " +
                    "qbarang.titipan, " +
                    "qbarang.kategori, " +
                    "qbayar_kredit.tglbayar, " +
                    "qbayar_kredit.jumlahbayar, " +
                    "qbayar_kredit.bayar, " +
                    "qbayar_kredit.kembali, " +
                    "qbayar_kredit.idpelanggan, " +
                    "qbayar_kredit.pelanggan, " +
                    "qbayar_kredit.telp, " +
                    "qbayar_kredit.alamat, " +
                    "qbayar_kredit.flagbayar, " +
                    "qbayar_kredit.keterangan " +
                    "FROM tblpenjualan_kredit INNER JOIN qbarang USING(idbarang) INNER JOIN qbayar_kredit USING(fakturbayar)");

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
                    "FROM tblbarang INNER JOIN tblreturn ON tblbarang.idbarang = tblreturn.idbarang;");

            exc("CREATE VIEW IF NOT EXISTS qkredit AS SELECT " +
                    "tblkredit.idkredit," +
                    "tblkredit.idpelanggan," +
                    "tblkredit.faktur," +
                    "tblpelanggan.pelanggan," +
                    "tblkredit.tglkredit," +
                    "tblkredit.jumlahkredit," +
                    "tblkredit.lamakredit," +
                    "tblkredit.angsuran," +
                    "tblkredit.jumlahcicilan," +
                    "tblkredit.denda," +
                    "tblkredit.tgljatuhtempo," +
                    "tblkredit.flag_kredit," +
                    "tblkredit.saldokredit FROM tblkredit INNER JOIN tblpelanggan using(idpelanggan)");

            exc("CREATE VIEW IF NOT EXISTS qtagihan AS SELECT " +
                    "tbltagihan.idtagihan," +
                    "tbltagihan.idkredit," +
                    "tblkredit.faktur," +
                    "tbltagihan.jumlahangsuran," +
                    "tbltagihan.cicilan," +
                    "tbltagihan.jumlahdenda," +
                    "tbltagihan.tglbayar," +
                    "tbltagihan.tgltempo," +
                    "tbltagihan.bayardenda," +
                    "tbltagihan.jumlahterlambat," +
                    "tbltagihan.flag_tagihan FROM tbltagihan INNER JOIN tblkredit USING(idkredit)");

            exc("CREATE TRIGGER IF NOT EXISTS kurang_stok AFTER INSERT ON tblpenjualan " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok - new.jumlahjual WHERE idbarang=new.idbarang ; " +
                    "END ;");

            exc("CREATE TRIGGER IF NOT EXISTS tambah_stok AFTER DELETE ON tblpenjualan " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok + old.jumlahjual WHERE idbarang=old.idbarang ; " +
                    "END ;");

            // Trigger Kredit
            exc("CREATE TRIGGER IF NOT EXISTS kurang_stok_kredit AFTER INSERT ON tblpenjualan_kredit " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok - new.jumlahjual WHERE idbarang=new.idbarang ; " +
                    "END ;");

            exc("CREATE TRIGGER IF NOT EXISTS tambah_stok_kredit AFTER DELETE ON tblpenjualan_kredit " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok + old.jumlahjual WHERE idbarang=old.idbarang ; " +
                    "END ;");

            exc("CREATE TRIGGER IF NOT EXISTS tambah_stok_return AFTER INSERT ON tblreturn " +
                    "BEGIN " +
                    "UPDATE tblbarang SET stok=tblbarang.stok + new.jumlah WHERE idbarang=new.idbarang ; " +
                    "END ;");

            exc("INSERT INTO tblpelanggan VALUES (1,'Kosong','0','')");
            exc("INSERT INTO tblidentitas VALUES (1,'KomputerKit.com', 'Perum Graha Kuncarah H29','0838 320 320 77','Terima Kasih','Sudah Berbelanja','Di Toko Kami')");
        } catch (Exception e) {
            Log.e("err", e.getMessage());
        }
    }
}
