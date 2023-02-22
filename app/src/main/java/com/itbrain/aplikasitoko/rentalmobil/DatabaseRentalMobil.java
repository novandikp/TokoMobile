package com.itbrain.aplikasitoko.rentalmobil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseRentalMobil extends SQLiteOpenHelper {
    Context a;
    SQLiteDatabase db;
    public static String nama="dbrental";
    public  static  int versi=1;

    public DatabaseRentalMobil(Context context) {
        super(context, nama,null,versi);
        a = context ;
        db = this.getWritableDatabase() ;
       createTables();
        createTrigger();
        createView();
        setDefault();
        updateTIme();
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
       createTable("CREATE TABLE \"tbltoko\" ( `idtoko` INTEGER NOT NULL DEFAULT 1 PRIMARY KEY AUTOINCREMENT, `namatoko` TEXT DEFAULT 'KomputerKit.com', `alamattoko` TEXT DEFAULT 'Buduran', `notoko` TEXT DEFAULT 0888888, `caption1` TEXT DEFAULT 'Terima Kasih', `caption2` TEXT DEFAULT 'Telah Percaya', `caption3` TEXT DEFAULT 'dengan Rental Mobil kami' )");
       createTable("CREATE TABLE `tblmerk` ( `idmerk` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `merk` TEXT )");
       createTable("CREATE TABLE \"tblkendaraan\" ( `idkendaraan` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idmerk` INTEGER, `mobil` TEXT, `tahunkeluaran` INTEGER, `plat` TEXT, `harga` REAL, flagada INTEGER DEFAULT 0, FOREIGN KEY(`idmerk`) REFERENCES tblmerk(idmerk) ON UPDATE CASCADE ON DELETE RESTRICT )");
       createTable("CREATE TABLE `tblpegawai` ( `idpegawai` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `pegawai` TEXT, `alamatpegawai` TEXT, `nopegawai` TEXT, `ktppegawai` TEXT )");
       createTable("CREATE TABLE `tblpelanggan` ( `idpelanggan` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `pelanggan` TEXT, `notelp` TEXT, `alamat` TEXT, `noktp` TEXT )");
       createTable("CREATE TABLE `tblrental` ( `idrental` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idpelanggan` INTEGER, faktur TEXT, `tglrental` INTEGER, `total` REAL DEFAULT 0, `bayar` REAL DEFAULT 0, `kembali` REAL DEFAULT 0, `flagrental` INTEGER DEFAULT 0, `dp` REAL DEFAULT 0, `idpegawai` INTEGER, FOREIGN KEY(`idpelanggan`) REFERENCES tblpelanggan(idpelanggan) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idpegawai`) REFERENCES tblpegawai(idpegawai) ON UPDATE CASCADE ON DELETE RESTRICT )");
       createTable("CREATE TABLE `tblrentaldetail` ( `idrentaldetail` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `idrental` INTEGER, `idkendaraan` INTEGER, `tglmulai` INTEGER, `tglselesai` INTEGER, `kmmulai` INTEGER, `hargarental` REAL, `jumlahhari` INTEGER, flagkembali INTEGER DEFAULT 0, FOREIGN KEY(`idrental`) REFERENCES tblrental(idrental) ON UPDATE CASCADE ON DELETE RESTRICT, FOREIGN KEY(`idkendaraan`) REFERENCES tblkendaraan(idkendaraan) ON UPDATE CASCADE ON DELETE RESTRICT )");
        return true;
    }

    public void updateTIme(){
//        make table log update
        createTable("CREATE TABLE `tbllog` ( `idlog` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `log` TEXT)");
//        check updateTIme in tbllog
        Cursor c = sq("SELECT * FROM tbllog WHERE log='updateTime'");
        if(c.getCount()==0){
            exc("INSERT INTO tbllog (log) VALUES ('updateTime')");
            exc("alter table tblkendaraan add column `hargajam` REAL DEFAULT 0");
            exc("alter table tblkendaraan add column `hargamenit` REAL DEFAULT 0");
            exc("alter table tblrentaldetail add column `hargarentaljam` REAL DEFAULT 0");
            exc("alter table tblrentaldetail add column `hargarentalmenit` REAL DEFAULT 0");
            exc("alter table tblrentaldetail add column `jumlahjam` INTEGER DEFAULT 0");
            exc("alter table tblrentaldetail add column `jumlahmenit` INTEGER DEFAULT 0");
            exc("update tblkendaraan set hargajam=round(harga/24,0)");
            exc("update tblkendaraan set hargamenit=round(hargajam/60,0)");

            exc("PRAGMA foreign_keys=ON");
            exc("drop view view_rentaldetail");
            exc("drop view view_rental");
            exc("drop view view_kendaraan");
            exc("CREATE VIEW view_kendaraan AS SELECT \"tblkendaraan\".\"idkendaraan\", tblkendaraan.hargajam, tblkendaraan.hargamenit , \"tblkendaraan\".\"idmerk\", \"tblkendaraan\".\"mobil\", \"tblkendaraan\".\"tahunkeluaran\", \"tblkendaraan\".\"plat\", \"tblkendaraan\".\"harga\", \"tblmerk\".\"merk\" FROM \"tblkendaraan\", \"tblmerk\" WHERE \"tblkendaraan\".\"idmerk\" = \"tblmerk\".\"idmerk\"");
            exc("CREATE VIEW view_rental AS SELECT \"tblrental\".\"idrental\", \"tblrental\".\"idpelanggan\", tblrental.faktur, \"tblrental\".\"tglrental\", \"tblrental\".\"total\", \"tblrental\".\"bayar\", \"tblrental\".\"kembali\", \"tblrental\".\"flagrental\", \"tblrental\".\"dp\", \"tblrental\".\"idpegawai\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\", \"tblpelanggan\".\"noktp\", \"tblpegawai\".\"pegawai\", \"tblpegawai\".\"alamatpegawai\", \"tblpegawai\".\"nopegawai\", \"tblpegawai\".\"ktppegawai\" FROM \"tblrental\", \"tblpelanggan\", \"tblpegawai\" WHERE \"tblrental\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\" AND \"tblrental\".\"idpegawai\" = \"tblpegawai\".\"idpegawai\"");
            exc("CREATE VIEW view_rentaldetail AS SELECT \"tblrentaldetail\".\"idrentaldetail\", tblrentaldetail.hargarentaljam , tblrentaldetail.hargarentalmenit, tblrentaldetail.jumlahjam, tblrentaldetail.jumlahmenit , \"tblrentaldetail\".\"idrental\", tblrentaldetail.flagkembali,  \"tblrentaldetail\".\"idkendaraan\", \"tblrentaldetail\".\"tglmulai\", \"tblrentaldetail\".\"tglselesai\", \"tblrentaldetail\".\"kmmulai\", \"tblrentaldetail\".\"hargarental\", \"tblrentaldetail\".\"jumlahhari\", \"tblrental\".\"idpelanggan\", tblrental.faktur, \"tblrental\".\"tglrental\", \"tblrental\".\"total\", \"tblrental\".\"bayar\", \"tblrental\".\"kembali\", \"tblrental\".\"flagrental\", \"tblrental\".\"dp\", \"tblrental\".\"idpegawai\", \"tblpegawai\".\"pegawai\", \"tblpegawai\".\"alamatpegawai\", \"tblpegawai\".\"nopegawai\", \"tblpegawai\".\"ktppegawai\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\", \"tblpelanggan\".\"noktp\", \"tblkendaraan\".\"idmerk\", \"tblkendaraan\".\"mobil\", tblkendaraan.hargajam, tblkendaraan.hargamenit , \"tblkendaraan\".\"tahunkeluaran\", \"tblkendaraan\".\"plat\", \"tblkendaraan\".\"harga\", \"tblmerk\".\"merk\" FROM \"tblrentaldetail\", \"tblrental\", \"tblpegawai\", \"tblpelanggan\", \"tblkendaraan\", \"tblmerk\" WHERE \"tblrentaldetail\".\"idrental\" = \"tblrental\".\"idrental\" AND \"tblrental\".\"idpegawai\" = \"tblpegawai\".\"idpegawai\" AND \"tblrental\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\" AND \"tblrentaldetail\".\"idkendaraan\" = \"tblkendaraan\".\"idkendaraan\" AND \"tblkendaraan\".\"idmerk\" = \"tblmerk\".\"idmerk\"");


            exc("DROP TRIGGER kurang_total");
            exc("CREATE TRIGGER kurang_total AFTER DELETE ON tblrentaldetail FOR EACH ROW BEGIN UPDATE tblrental SET total = total-(OLD.jumlahhari*OLD.hargarental + OLD.jumlahjam * OLD.hargarentaljam + OLD.jumlahmenit * OLD.hargarentalmenit) WHERE idrental=OLD.idrental; end");
            exc("DROP TRIGGER tambah_total");
            exc("CREATE TRIGGER tambah_total AFTER INSERT ON tblrentaldetail FOR EACH ROW BEGIN UPDATE tblrental SET total = total+(new.jumlahhari*new.hargarental + NEW.jumlahjam * NEW.hargarentaljam + NEW.jumlahmenit * NEW.hargarentalmenit) WHERE idrental=new.idrental; end");
            exc("DROP TRIGGER update_total");
            exc("CREATE TRIGGER update_total AFTER UPDATE ON tblrentaldetail FOR EACH ROW WHEN new.jumlahhari>old.jumlahhari BEGIN UPDATE tblrental SET total = total+((NEW.jumlahhari-OLD.jumlahhari)*OLD.hargarental + (NEW.jumlahjam-OLD.jumlahjam) * OLD.hargarentaljam + (NEW.jumlahmenit-OLD.jumlahmenit) * OLD.hargarentalmenit) WHERE idrental=OLD.idrental; end");

        }
    }

    public boolean createTable(String querry){
        exc(querry);
        return  true;
    }

    public boolean createView(){
        exc("PRAGMA foreign_keys=ON");
        exc("CREATE VIEW view_kendaraan AS SELECT \"tblkendaraan\".\"idkendaraan\", \"tblkendaraan\".\"idmerk\", \"tblkendaraan\".\"mobil\", \"tblkendaraan\".\"tahunkeluaran\", \"tblkendaraan\".\"plat\", \"tblkendaraan\".\"harga\", \"tblmerk\".\"merk\" FROM \"tblkendaraan\", \"tblmerk\" WHERE \"tblkendaraan\".\"idmerk\" = \"tblmerk\".\"idmerk\"");
        exc("CREATE VIEW view_rental AS SELECT \"tblrental\".\"idrental\", \"tblrental\".\"idpelanggan\", tblrental.faktur, \"tblrental\".\"tglrental\", \"tblrental\".\"total\", \"tblrental\".\"bayar\", \"tblrental\".\"kembali\", \"tblrental\".\"flagrental\", \"tblrental\".\"dp\", \"tblrental\".\"idpegawai\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\", \"tblpelanggan\".\"noktp\", \"tblpegawai\".\"pegawai\", \"tblpegawai\".\"alamatpegawai\", \"tblpegawai\".\"nopegawai\", \"tblpegawai\".\"ktppegawai\" FROM \"tblrental\", \"tblpelanggan\", \"tblpegawai\" WHERE \"tblrental\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\" AND \"tblrental\".\"idpegawai\" = \"tblpegawai\".\"idpegawai\"");
        exc("CREATE VIEW view_rentaldetail AS SELECT \"tblrentaldetail\".\"idrentaldetail\", \"tblrentaldetail\".\"idrental\", tblrentaldetail.flagkembali,  \"tblrentaldetail\".\"idkendaraan\", \"tblrentaldetail\".\"tglmulai\", \"tblrentaldetail\".\"tglselesai\", \"tblrentaldetail\".\"kmmulai\", \"tblrentaldetail\".\"hargarental\", \"tblrentaldetail\".\"jumlahhari\", \"tblrental\".\"idpelanggan\", tblrental.faktur, \"tblrental\".\"tglrental\", \"tblrental\".\"total\", \"tblrental\".\"bayar\", \"tblrental\".\"kembali\", \"tblrental\".\"flagrental\", \"tblrental\".\"dp\", \"tblrental\".\"idpegawai\", \"tblpegawai\".\"pegawai\", \"tblpegawai\".\"alamatpegawai\", \"tblpegawai\".\"nopegawai\", \"tblpegawai\".\"ktppegawai\", \"tblpelanggan\".\"pelanggan\", \"tblpelanggan\".\"alamat\", \"tblpelanggan\".\"notelp\", \"tblpelanggan\".\"noktp\", \"tblkendaraan\".\"idmerk\", \"tblkendaraan\".\"mobil\", \"tblkendaraan\".\"tahunkeluaran\", \"tblkendaraan\".\"plat\", \"tblkendaraan\".\"harga\", \"tblmerk\".\"merk\" FROM \"tblrentaldetail\", \"tblrental\", \"tblpegawai\", \"tblpelanggan\", \"tblkendaraan\", \"tblmerk\" WHERE \"tblrentaldetail\".\"idrental\" = \"tblrental\".\"idrental\" AND \"tblrental\".\"idpegawai\" = \"tblpegawai\".\"idpegawai\" AND \"tblrental\".\"idpelanggan\" = \"tblpelanggan\".\"idpelanggan\" AND \"tblrentaldetail\".\"idkendaraan\" = \"tblkendaraan\".\"idkendaraan\" AND \"tblkendaraan\".\"idmerk\" = \"tblmerk\".\"idmerk\"");
         return true;
    }

    public boolean setDefault(){
        exc("insert into tbltoko (idtoko) values (1);");
        exc("INSERT INTO tblpelanggan (idpelanggan,pelanggan,alamat,notelp,noktp) VALUES (1,'Umum','Indonesia','0888','0000')");
        exc("INSERT INTO tblpegawai (idpegawai,pegawai,alamatpegawai,nopegawai,ktppegawai) VALUES (1,'Pemilik','Indonesia','0888','0000')");
        return  true;
    }


    public boolean createTrigger(){
        exc("CREATE TRIGGER kurang_total AFTER DELETE ON tblrentaldetail FOR EACH ROW BEGIN UPDATE tblrental SET total = total-(OLD.jumlahhari*OLD.hargarental) WHERE idrental=OLD.idrental; end");
        exc("CREATE TRIGGER tambah_total AFTER INSERT ON tblrentaldetail FOR EACH ROW BEGIN UPDATE tblrental SET total = total+(new.jumlahhari*new.hargarental) WHERE idrental=new.idrental; end");
        exc("CREATE TRIGGER update_total AFTER UPDATE ON tblrentaldetail FOR EACH ROW WHEN new.jumlahhari>old.jumlahhari BEGIN UPDATE tblrental SET total = total+((NEW.jumlahhari-OLD.jumlahhari)*OLD.hargarental) WHERE idrental=OLD.idrental; end");
        exc("CREATE TRIGGER flag_ada\n" +
                "AFTER INSERT ON tblrentaldetail \n" +
                "FOR EACH ROW \n" +
                "BEGIN\n" +
                "UPDATE tblkendaraan SET flagada = 1 where idkendaraan= new.idkendaraan;\n" +
                "end;");
        exc("CREATE TRIGGER flag_kembali\n" +
                "AFTER UPDATE ON tblrentaldetail \n" +
                "FOR EACH ROW \n" +
                "WHEN new.flagkembali =1\n" +
                "BEGIN\n" +
                "UPDATE tblkendaraan SET flagada = 0 where idkendaraan=old.idkendaraan;\n" +
                "end;");
        exc("CREATE TRIGGER ubahflagada\n" +
                "AFTER DELETE ON tblrentaldetail\n" +
                "FOR EACH ROW\n" +
                "begin \n" +
                "UPDATE tblkendaraan SET flagada=0 WHERE idkendaraan=old.idkendaraan;\n" +
                "end;");
        exc("CREATE TRIGGER update_flagrental after update on tblrental for each row when old.bayar + old.dp < new.total and old.dp!=0 begin update tblrental set flagrental = 1 where idrental=old.idrental; end");
        exc("CREATE TRIGGER update_lunas after update on tblrental for each row when old.bayar + old.dp >= new.total and old.dp!=0 begin update tblrental set flagrental = 3 where idrental=old.idrental; end");
        return true;
    }


    
}