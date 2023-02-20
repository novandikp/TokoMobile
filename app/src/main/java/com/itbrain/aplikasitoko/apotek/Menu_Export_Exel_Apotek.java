package com.itbrain.aplikasitoko.apotek;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.itbrain.aplikasitoko.R;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class Menu_Export_Exel_Apotek extends AppCompatActivity {
    String Opsi;

    ModulApotek config,temp;
    DatabaseApotek db;

    String deviceid;
    SharedPreferences getPrefs;
    String[] header;
    String idAkun;
    private String inputFile;
    String nama;
    Boolean needDate = Boolean.valueOf(true);
    String path;

    String rincian;
    int row = 0;

    String tglAkhir;
    String tglAwal;
    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;
    String type;
    View v;

    Calendar calendar ;
    int year,month, day ;
    String ttglAwal,ttglAkhir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_export_excel_apotek);

        ImageButton imageButton = findViewById(R.id.kembali3);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().setSoftInputMode(3);
        this.config = new ModulApotek(getSharedPreferences("config", 0));
        this.temp = new ModulApotek(getSharedPreferences("temp", 0));
        this.db = new DatabaseApotek(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";


        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        try {
            new File(Environment.getExternalStorageDirectory() + "/Download").mkdirs();
        } catch (Exception e) {
        }

        setTanggal();
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();
            ModulApotek.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulApotek.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void export(View view) throws IOException, WriteException {
        if (type.equals("pelanggan")){
            nama="Laporan Pelanggan";
            exPelanggan();
        }else if(type.equals("supplier")){
            nama="Laporan Supplier";
            exSupplier();
        }else if (type.equals("obat")){
            nama="Laporan Obat";
            exLaporanBarang();
        }else if (type.equals("pembelian")){
            nama="Laporan Pembelian";
            exLaporanPembelian();
        }else if(type.equals("penjualan")){
            nama="Laporan Penjualan";
            exLaporanPenjualan();
        }else if(type.equals("laba")){
            nama="Laporan Pendapatan";
            exLaporanPendapatan();
        }else if(type.equals("hutang")){
            nama="Laporan Pembayaran Hutang";
            exHutang();
        }else if(type.equals("piutang")){
            nama="Laporan Pembayaran Piutang";
            exPiutang();
        }
        else if(type.equals("expired")){
            nama="Laporan Obat Expired";
            exExpired();
        }
        else if(type.equals("keuangan")){
            nama="Laporan Keuangan";
            exLaporanKeuangan();
        }

    }


    public void exExpired() throws IOException, WriteException {

        String q = ModulApotek.select("qbarang") + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String jumlahjual,barang,total,pelanggan,expired;
            String faktur = ModulApotek.getString(c,"barang") ;
            String tgljual = ModulApotek.getString(c,"stok")+" "+ ModulApotek.getString(c,"satuanbesar") ;
            String hargajual = ModulApotek.getString(c,"satuanbesar")+"/"+ ModulApotek.getString(c,"satuankecil") ; ;
            String idbarang = ModulApotek.getString(c,"idbarang");
            Cursor b = db.sq(ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbarang",idbarang)+" ORDER BY idbelidetail");
            if (b.getCount()>0){
                b.moveToLast();
                String idorederbeli = ModulApotek.getString(b,"idbelidetail");
                Cursor d = db.sq(ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbelidetail",idorederbeli)+" AND "+ ModulApotek.sBetween("expired", ModulApotek.getDate("dd/MM/yyyy"), ModulApotek.getExpiredEnd(ModulApotek.strToInt(config.getCustom("expired","30")))));
                if (d.getCount()>0){
                    File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

                    WorkbookSettings wbSettings = new WorkbookSettings();

                    wbSettings.setLocale(new Locale("en", "EN"));

                    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                    workbook.createSheet("Report", 0);
                    WritableSheet sheet = workbook.getSheet(0);

                    createLabel(sheet);
                    setHeader(db,sheet,9);
                    excelNextLine(sheet,2) ;

                    String[] judul = {"No","Obat",
                            "Stok",
                            "Satuan",
                            "Harga Jual Satu",
                            "Harga Jual Dua",
                            "HPP Satu",
                            "HPP Dua","Expired"} ;
                    setJudul(sheet,judul);
                    int no = 1 ;
                    while (d.moveToNext()){

                        int col = 0 ;
                        jumlahjual = ModulApotek.removeE(ModulApotek.getString(b,"harga_jual_satu")) ;
                        barang = ModulApotek.removeE(ModulApotek.getString(b,"harga_jual_dua")) ;
                        total = ModulApotek.removeE(ModulApotek.getString(b,"hpp_satu") );
                        pelanggan = ModulApotek.removeE(ModulApotek.getString(b,"hpp_dua")) ;
                        expired = ModulApotek.getString(b,"expired");

                        addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                        addLabel(sheet,col++, row, faktur);
                        addLabel(sheet,col++, row, tgljual);
                        addLabel(sheet,col++, row, hargajual);
                        addLabel(sheet,col++, row, jumlahjual);
                        addLabel(sheet,col++, row, barang);

                        addLabel(sheet,col++, row, total);

                        addLabel(sheet,col++, row, pelanggan);
                        addLabel(sheet,col++, row, expired);

                        row++ ;
                        no++ ;

                    }
                    workbook.write();
                    workbook.close();
                    ModulApotek.showToast(this,"Berhasil");
                }else{
                    ModulApotek.showToast(this,"Tidak ada Data");
                }





            }}


    }

    public void exPiutang() throws IOException, WriteException {


        Cursor c = db.sq(ModulApotek.selectwhere("qhutang")+ ModulApotek.sBetween("tglbayar",ttglAwal,ttglAkhir));
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,4);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Pelanggan","Tanggal Bayar","Pembayaran"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulApotek.getString(c,"pelanggan") ;
                String alamat = ModulApotek.getString(c,"tglbayar") ;
                String telp = ModulApotek.getString(c,"bayarhutang") ;


                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        } else {
            ModulApotek.showToast(this,"Tidak ada data");
        }
    }

    public void exHutang() throws IOException, WriteException {


        Cursor c = db.sq(ModulApotek.selectwhere("qhutang1")+ ModulApotek.sBetween("tglbayar",ttglAwal,ttglAkhir));
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,4);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Supplier","Tanggal Bayar","Pembayaran"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulApotek.getString(c,"supplier") ;
                String alamat = ModulApotek.getString(c,"tglbayar") ;
                String telp = ModulApotek.getString(c,"bayarhutang") ;


                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        } else {
            ModulApotek.showToast(this,"Tidak ada data");
        }
    }

    private void exLaporanKeuangan() throws IOException, WriteException {


        Cursor c = db.sq(ModulApotek.selectwhere("tbltransaksi")+ ModulApotek.sBetween("tgltransaksi",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,7);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Tanggal Transaksi",
                    "Faktur",
                    "Keterangan",
                    "Masuk",
                    "Keluar",
                    "Saldo"
            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String tgl = ModulApotek.getString(c,"tgltransaksi");
                String faktur = ModulApotek.getString(c,"fakturtran");
                String ket = ModulApotek.getString(c,"kettransaksi");
                String masuk = ModulApotek.getString(c,"masuk");
                String keluar = ModulApotek.getString(c,"keluar");
                String saldo = ModulApotek.getString(c,"saldo");




                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, tgl);
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, ket);
                addLabel(sheet,col++, row, ModulApotek.removeE(masuk));
                addLabel(sheet,col++, row, ModulApotek.removeE(keluar));
                addLabel(sheet,col++, row, ModulApotek.removeE(saldo));


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        }else{
            ModulApotek.showToast(this,"Tidak ada data");
        }

    }


    private void exLaporanBarang() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qbarang") ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,8);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Obat",
                    "Stok",
                    "Satuan",
                    "Harga Jual Satu",
                    "Harga Jual Dua",
                    "HPP Satu",
                    "HPP Dua"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulApotek.getString(c,"barang") ;
                String tgljual = ModulApotek.getString(c,"stok")+" "+ ModulApotek.getString(c,"satuanbesar") ;
                String hargajual = ModulApotek.getString(c,"satuanbesar")+"/"+ ModulApotek.getString(c,"satuankecil") ; ;
                String idbarang = ModulApotek.getString(c,"idbarang");
                Cursor b = db.sq(ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbarang",idbarang));
                String jumlahjual,barang,total,pelanggan;
                try{
                    if (b.getCount()>0){
                        b.moveToNext();
                        jumlahjual = ModulApotek.removeE(ModulApotek.getString(b,"harga_jual_satu")) ;
                        barang = ModulApotek.removeE(ModulApotek.getString(b,"harga_jual_dua")) ;
                        total = ModulApotek.removeE(ModulApotek.getString(b,"hpp_satu") );
                        pelanggan = ModulApotek.removeE(ModulApotek.getString(b,"hpp_dua")) ;
                    }else{
                        jumlahjual = "Belum tersedia" ;
                        barang = "Belum tersedia";
                        total = "Belum tersedia" ;
                        pelanggan = "Belum tersedia";
                    }
                }catch (Exception e){
                    jumlahjual = "Belum tersedia" ;
                    barang = "Belum tersedia";
                    total = "Belum tersedia" ;
                    pelanggan = "Belum tersedia";
                }


                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, hargajual);
                addLabel(sheet,col++, row, jumlahjual);
                addLabel(sheet,col++, row, barang);

                addLabel(sheet,col++, row, total);

                addLabel(sheet,col++, row, pelanggan);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        }else{
            ModulApotek.showToast(this,"Tidak ada data");
        }
    }

    private void exLaporanPendapatan() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qdetailjual where bayar!=0 and "+ ModulApotek.sBetween("tgljual",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,8);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Tanggal Jual",
                    "Obat",
                    "Laba per Jual",
                    "Jumlah Jual",
                    "Total Laba",
                    "Nama Pelanggan"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulApotek.getString(c,"fakturjual") ;
                String tgljual = ModulApotek.getString(c,"tgljual") ;
                String hargajual = ModulApotek.getString(c,"laba") ;
                String jumlahjual = ModulApotek.getString(c,"jumlahjual") ;
                String barang = ModulApotek.getString(c,"barang") ;
                String satuan = ModulApotek.getString(c,"satuanjual");
                String total = ModulApotek.doubleToStr(ModulApotek.strToDouble(hargajual) * ModulApotek.strToDouble(jumlahjual)) ;
                String pelanggan = ModulApotek.getString(c,"pelanggan") ;
                String keterangan = "Tunai" ;

                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, ModulApotek.sederhana(hargajual));
                addLabel(sheet,col++, row, ModulApotek.unchangeComma(jumlahjual)+" "+satuan);

                addLabel(sheet,col++, row, ModulApotek.sederhana(total));

                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, keterangan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        }else{
            ModulApotek.showToast(this,"Tidak ada data");
        }
    }


    private void exLaporanPembelian() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qbelidetail where stat!=0 and "+ ModulApotek.sBetween("tglorderbeli",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,9);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Tanggal Beli",
                    "Obat",
                    "Harga Beli",
                    "Jumlah Beli",
                    "Total",
                    "Nama Supplier",
                    "Batch Number"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulApotek.getString(c,"fakturorderbeli") ;
                String tgljual = ModulApotek.getString(c,"tglorderbeli") ;
                String hargajual = ModulApotek.getString(c,"hargabeli") ;
                String jumlahjual = ModulApotek.getString(c,"jumlahbeli") ;
                String barang = ModulApotek.getString(c,"barang") ;
                String satuan = ModulApotek.getString(c,"satuanbeli");
                String total = ModulApotek.doubleToStr(ModulApotek.strToDouble(hargajual) * ModulApotek.strToDouble(jumlahjual)) ;
                String pelanggan = ModulApotek.getString(c,"supplier") ;
                String keterangan = ModulApotek.getString(c,"batchnumber") ;

                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, ModulApotek.removeE(hargajual));
                addLabel(sheet,col++, row, ModulApotek.unchangeComma(jumlahjual)+" "+satuan);

                addLabel(sheet,col++, row, ModulApotek.removeE(total));

                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, keterangan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        }else{
            ModulApotek.showToast(this,"Tidak ada data");
        }
    }

    private void exLaporanPenjualan() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qdetailjual where bayar!=0 and "+ ModulApotek.sBetween("tgljual",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,9);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Tanggal Jual",
                    "Obat",
                    "Harga Jual",
                    "Jumlah Jual",
                    "Total",
                    "Nama Pelanggan",
                    "Batch Number"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulApotek.getString(c,"fakturjual") ;
                String tgljual = ModulApotek.getString(c,"tgljual") ;
                String hargajual = ModulApotek.getString(c,"hargajual") ;
                String jumlahjual = ModulApotek.getString(c,"jumlahjual") ;
                String barang = ModulApotek.getString(c,"barang") ;
                String satuan = ModulApotek.getString(c,"satuanjual");
                String total = ModulApotek.doubleToStr(ModulApotek.strToDouble(hargajual) * ModulApotek.strToDouble(jumlahjual)) ;
                String pelanggan = ModulApotek.getString(c,"pelanggan") ;
                String keterangan = ModulApotek.getString(c,"batchnumber") ;

                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, ModulApotek.removeE(hargajual));
                addLabel(sheet,col++, row, ModulApotek.unchangeComma(jumlahjual)+" "+satuan);

                addLabel(sheet,col++, row, ModulApotek.removeE(total));

                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, keterangan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        }
    }


    public void exPelanggan() throws IOException, WriteException {


        Cursor c = db.sq(ModulApotek.selectwhere("tblpelanggan")+" idpelanggan!=1");
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Pelanggan","Alamat","Nomor Telp","Hutang"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulApotek.getString(c,"pelanggan") ;
                String alamat = ModulApotek.getString(c,"alamat") ;
                String telp = ModulApotek.getString(c,"notelp") ;
                String utang = ModulApotek.getString(c,"hutang") ;

                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                addLabel(sheet,col++, row, ModulApotek.removeE(utang));
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        } else {
            ModulApotek.showToast(this,"Tidak ada data");
        }
    }

    public void exSupplier() throws IOException, WriteException {


        Cursor c = db.sq(ModulApotek.selectwhere("tblsupplier")+" idsupplier!=1");
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ ModulApotek.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Supplier","Alamat","Nomor Telp","Hutang"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulApotek.getString(c,"supplier") ;
                String alamat = ModulApotek.getString(c,"alamatsupplier") ;
                String telp = ModulApotek.getString(c,"nosupplier") ;
                String utang = ModulApotek.getString(c,"utang") ;

                addLabel(sheet,col++, row, ModulApotek.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                addLabel(sheet,col++, row, ModulApotek.removeE(utang));
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulApotek.showToast(this,"Berhasil");
        } else {
            ModulApotek.showToast(this,"Tidak ada data");
        }
    }



    public void setHeader(DatabaseApotek db, CSVWriter csvWriter, int JumlahKolom){
        try {
            Cursor c = db.sq(ModulApotek.select("tbltoko")) ;
            c.moveToNext() ;

            setCenter(csvWriter,JumlahKolom, ModulApotek.getString(c,"namatoko"));
            setCenter(csvWriter,JumlahKolom, ModulApotek.getString(c,"alamattoko"));
            setCenter(csvWriter,JumlahKolom, ModulApotek.getString(c,"notoko"));
            FExcel_Apotek.csvNextLine(csvWriter,2) ;
        }catch (Exception e){

        }
    }

    public void setHeader(DatabaseApotek db, WritableSheet sheet, int JumlahKolom){
        try {
            Cursor c = db.sq(ModulApotek.select("tbltoko")) ;
            c.moveToNext() ;

            addLabel(sheet, row++, ModulApotek.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++, ModulApotek.getString(c,"alamattoko"),JumlahKolom);
            addLabel(sheet, row++, ModulApotek.getString(c,"notoko"),JumlahKolom);
        }catch (Exception e){

        }
    }

    public void setCenter(CSVWriter csvWriter, int JumlahKolom, String value){
        try {
            int baru ;
            if(JumlahKolom%2 == 1){
                baru = JumlahKolom-1 ;
            } else {
                baru = JumlahKolom ;
            }
            int i ;
            String[] a = new String[baru];
            for(i = 0 ; i < baru/2 ; i++){
                a[i] = "" ;
            }
            a[i] = value ;
            csvWriter.writeNext(a);
        } catch (Exception e){

        }
    }

    private void createContent(WritableSheet sheet) throws WriteException,
            RowsExceededException {
        int startSum = row+1;
        // Write a few number
        for (int i = 1; i < 10; i++) {
            // First column
            addNumber(sheet, 0, row , i + 10);
            // Second column
            addNumber(sheet, 1, row++, i * i);
        }

        int endSum = row;
        // Lets calculate the sum of it
        StringBuffer buf = new StringBuffer();
        buf.append("SUM(A"+startSum+":A"+endSum+")");
        Formula f = new Formula(0, row, buf.toString());
        sheet.addCell(f);
        buf = new StringBuffer();
        buf.append("SUM(B"+startSum+":B"+endSum+")");
        f = new Formula(1, row, buf.toString());
        sheet.addCell(f);
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBold);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           Integer integer) throws WriteException, RowsExceededException {
        jxl.write.Number number;
        number = new jxl.write.Number(column, row, integer, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    private void addLabel(WritableSheet sheet, int row, String s,int JumlahKolom)
            throws WriteException, RowsExceededException {
        Label label;
        JumlahKolom-- ;
        WritableCellFormat newFormat = null;
        newFormat = new WritableCellFormat(timesBold) ;
        newFormat.setAlignment(Alignment.CENTRE) ;
        label = new Label(0, row, s, newFormat) ;
        sheet.addCell(label);
        sheet.mergeCells(0,row,JumlahKolom,row) ; // parameters -> col1,row1,col2,row2
    }

    private void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automaticall                y wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        WritableFont times10ptBold = new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD, false);
        timesBold = new WritableCellFormat(times10ptBold);
        // Lets automatically wrap the cells
        timesBold.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(timesBold);

//        cv.setAutosize(true);
    }

    public Boolean excelNextLine(WritableSheet sheet, int total){
        try {
            for (int i = 0 ; i < total ; i++){
                addLabel(sheet,0,row++,"");
            }
            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public void setJudul(WritableSheet sheet, String[] val) throws WriteException {
        int col = 0 ;
        for (int i = 0 ; i < val.length ; i++){
            addCaption(sheet,col++,row,val[i]);
        }
        row++ ;
    }

    void setTanggal(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String now = ModulApotek.setDatePickerNormal(year,month+1,day) ;
        ModulApotek.setText(v,R.id.tglAwal,now) ;
        ModulApotek.setText(v,R.id.tglAkhir,now) ;
        ttglAwal = ModulApotek.setDatePickerNormal(year,month+1,day);
        ttglAkhir = ModulApotek.setDatePickerNormal(year,month+1,day);
        ConstraintLayout tanggal = findViewById(R.id.cTanggal);
        if(type.equals("pelanggan")){
            tanggal.setVisibility(View.INVISIBLE);
        } else if (type.equals("supplier")){
            tanggal.setVisibility(View.INVISIBLE);
        }else if (type.equals("barang")){
            tanggal.setVisibility(View.INVISIBLE);
        }else if (type.equals("obat")){
            tanggal.setVisibility(View.INVISIBLE);
        }else if (type.equals("expired")){
            tanggal.setVisibility(View.INVISIBLE);
        }
    }

    public void tglAwal(View view) {
        setDate(1);
    }


    public void tglAkhir(View view) {
        setDate(2);
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, year, month, day);
        }else if (id == 2) {
            return new DatePickerDialog(this, date1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ttglAwal = ModulApotek.setDatePickerNormal(thn,bln+1,day);


            ModulApotek.setText(v, R.id.tglAwal, ModulApotek.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {


            ttglAkhir = ModulApotek.setDatePickerNormal(thn,bln+1,day);

            ModulApotek.setText(v, R.id.tglAkhir, ModulApotek.setDatePickerNormal(thn, bln + 1, day));
        }
    };

}
