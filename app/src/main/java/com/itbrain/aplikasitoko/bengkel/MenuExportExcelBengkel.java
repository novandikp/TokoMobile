package com.itbrain.aplikasitoko.bengkel;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

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


public class MenuExportExcelBengkel extends AppCompatActivity {
    String Opsi;

    ModulBengkel config,temp;
   Database_Bengkel_ db;
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

    Calendar calendar ;
    int year,month, day ;
    String ttglAwal,ttglAkhir;

    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;
    String type;
    View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_export_excel);


        getWindow().setSoftInputMode(3);
        this.config = new ModulBengkel(getSharedPreferences("config", 0));
        this.temp = new ModulBengkel(getSharedPreferences("temp", 0));
        this.db = new Database_Bengkel_(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";


        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        try {
            new File(Environment.getExternalStorageDirectory() + "/kasirsupermudah").mkdirs();
        } catch (Exception e) {
        }

        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();
            ModulBengkel.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulBengkel.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        setTanggal();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        ModulBengkel.setText(v, R.id.ePath,"Internal Storage/Download/");
    }


    public void export(View view) throws IOException, WriteException {

        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 001);
        }else {
            if (type.equals("pelanggan")){
                nama="Laporan Pelanggan";
                exPelanggan();
            }else if(type.equals("teknisi")){
                nama="Laporan Teknisi";
                exTeknisi();
            }else if (type.equals("servis")){
                nama="Laporan Servis";
                exLaporanServis();
            }else if (type.equals("bayar")){
                nama="Laporan Pembayaran";
                exBayar();
            }else if(type.equals("servisteknisi")){
                nama="Laporan Pendapatan Teknisi";
                exPendapatanTeknisi();
            }else if(type.equals("pendapatan")){
                nama="Laporan Pendapatan";
                exLaporanPendapatan();
            }
            else if(type.equals("jual")){
                nama="Laporan Penjualan Orderdil";
                exLaporanPenjualan();
            }
            else if(type.equals("hutang")){
                nama="Laporan Hutang";
                exHutang();
            }
            else if(type.equals("laba")){
                nama="Laporan Laba";
                exLaporanLaba();
            }
        }


    }

    private void exLaporanPendapatan() throws IOException, WriteException {


        Cursor c = db.sq(ModulBengkel.selectwhere("qdetailjual")+ModulBengkel.sBetween("tglorder",ttglAwal,ttglAkhir)) ;
        if(ModulBengkel.getCount(c) > 0){
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,9);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Faktur",
                    "Tanggal Order",
                    "Barang/Jasa",
                    "Jumlah",
                    "Harga",
                    "Pelanggan",
                    "Plat Nomor",
                    "Teknisi"
            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String idkategori = ModulBengkel.getString(c,"idkategori");
                String jumlah;
                String nopol;
                String teknisi;
                String hargajual;
                if (idkategori.equals("1")){
                     jumlah="-";
                     nopol=ModulBengkel.getString(c,"nopol");
                     teknisi=ModulBengkel.getString(c,"teknisi");
                     hargajual=ModulBengkel.getString(c,"hargajual");
                     double bagi = ModulBengkel.strToDouble(ModulBengkel.getString(c,"stok"))/100;
                     hargajual=ModulBengkel.removeE(ModulBengkel.strToDouble(hargajual)*bagi);
                }else{
                     jumlah = ModulBengkel.getString(c,"jumlah");
                     nopol="-";
                     teknisi="-";
                     hargajual=ModulBengkel.removeE(ModulBengkel.getString(c,"hargajual"));
                }
                String faktur = ModulBengkel.getString(c,"faktur") ;
                String tglorder = ModulBengkel.getString(c,"tglorder") ;
                String pelanggan = ModulBengkel.getString(c,"pelanggan") ;
                String barang = ModulBengkel.getString(c,"barang") ;



                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tglorder);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, jumlah);
                addLabel(sheet,col++, row, hargajual);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, nopol);
                addLabel(sheet,col++, row, teknisi);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        }else{
            Toast.makeText(this, "Tidak ada Data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exPendapatanTeknisi() throws IOException, WriteException {


        Cursor c = db.sq(ModulBengkel.selectwhere("qdetailjual")+" idkategori=1 and "+ModulBengkel.sBetween("tglorder",ttglAwal,ttglAkhir)+ModulBengkel.sOrderASC("teknisi")) ;
        if(ModulBengkel.getCount(c) > 0){
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,8);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Teknisi",
                    "Pendapatan",
                    "Faktur",
                    "Tanggal Order",
                    "Jasa",
                    "Pelanggan",
                    "Plat Nomor",

            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String idkategori = ModulBengkel.getString(c,"idkategori");
                String jumlah;
                String nopol;
                String teknisi;
                String hargajual;
                if (idkategori.equals("1")){
                    jumlah="-";
                    nopol=ModulBengkel.getString(c,"nopol");
                    teknisi=ModulBengkel.getString(c,"teknisi");
                    hargajual=ModulBengkel.getString(c,"hargajual");
                    double bagi =100- ModulBengkel.strToDouble(ModulBengkel.getString(c,"stok"));
                    bagi=bagi/100;
                    hargajual=ModulBengkel.removeE(ModulBengkel.strToDouble(hargajual)*bagi);
                }else{
                    jumlah = ModulBengkel.getString(c,"jumlah");
                    nopol="-";
                    teknisi="-";
                    hargajual=ModulBengkel.getString(c,"hargajual");
                }
                String faktur = ModulBengkel.getString(c,"faktur") ;
                String tglorder = ModulBengkel.getString(c,"tglorder") ;
                String pelanggan = ModulBengkel.getString(c,"pelanggan") ;
                String barang = ModulBengkel.getString(c,"barang") ;




                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, teknisi);
                addLabel(sheet,col++, row, hargajual);
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tglorder);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, nopol);


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        }else{
            ModulBengkel.showToast(this,"Tidak ada data");
        }

    }

    private void exBayar() throws IOException, WriteException {


        Cursor c = db.sq(ModulBengkel.selectwhere("qorder") + " statusbayar='lunas' and "+ModulBengkel.sBetween("tglorder",ttglAwal,ttglAkhir)) ;
        if(ModulBengkel.getCount(c) > 0){
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,6);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Pelanggan",
                    "Total",
                    "Bayar",
                    "Kembali"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String pelanggan = ModulBengkel.getString(c,"pelanggan") ;
                String faktur = ModulBengkel.getString(c,"faktur") ;
                String total = ModulBengkel.getString(c,"total") ;
                String bayar = ModulBengkel.getString(c,"bayar") ;
                String kembali = ModulBengkel.getString(c,"kembali");


                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, ModulBengkel.removeE(total));
                addLabel(sheet,col++, row, ModulBengkel.removeE(bayar));
                addLabel(sheet,col++, row, ModulBengkel.removeE(kembali));

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        }else{
            ModulBengkel.showToast(this,"Tidak ada data");
        }
    }



    private void exLaporanServis() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qdetailjual where idkategori=1 AND statusbayar='lunas' AND "+ModulBengkel.sBetween("tglorder",ttglAwal,ttglAkhir)) ;
        if(ModulBengkel.getCount(c) > 0){
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,10);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Tanggal Jual",
                    "Nama Servis",
                    "Harga Servis",
                    "Nama Pelanggan",
                    "Plat Nomor",
                    "Nama Teknisi",
                    "Metode Pembayaran"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulBengkel.getString(c,"faktur") ;
                String tgljual = ModulBengkel.getString(c,"tglorder") ;
                String hargajual = ModulBengkel.getString(c,"hargajual") ;
                String jumlahjual = ModulBengkel.getString(c,"jumlah") ;
                String barang = ModulBengkel.getString(c,"barang") ;
                String total = ModulBengkel.intToStr(ModulBengkel.strToInt(hargajual) * ModulBengkel.strToInt(jumlahjual)) ;
                String pelanggan = ModulBengkel.getString(c,"pelanggan") ;
                String teknisi = ModulBengkel.getString(c,"teknisi");
                String nopol=ModulBengkel.getString(c,"nopol");
                String keterangan = "Tunai" ;

                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, ModulBengkel.removeE(hargajual));
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, nopol);
                addLabel(sheet,col++, row, teknisi);
                addLabel(sheet,col++, row, keterangan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        }else{
            ModulBengkel.showToast(this,"Tidak ada Data");
        }
    }

    private void exLaporanPenjualan() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qdetailjual where idkategori!=1 AND statusbayar='lunas' AND "+ModulBengkel.sBetween("tglorder",ttglAwal,ttglAkhir)) ;
        if(ModulBengkel.getCount(c) > 0){
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                    "Barang",
                    "Harga Jual",
                    "Jumlah Jual",
                    "Total",
                    "Nama Pelanggan"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulBengkel.getString(c,"faktur") ;
                String tgljual = ModulBengkel.getString(c,"tglorder") ;
                String hargajual = ModulBengkel.getString(c,"hargajual") ;
                String jumlahjual = ModulBengkel.getString(c,"jumlah") ;
                String barang = ModulBengkel.getString(c,"barang") ;
                String total = ModulBengkel.intToStr(ModulBengkel.strToInt(hargajual) * ModulBengkel.strToInt(jumlahjual)) ;
                String pelanggan = ModulBengkel.getString(c,"pelanggan") ;


                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, ModulBengkel.removeE(hargajual));
                addLabel(sheet,col++, row, jumlahjual);

                addLabel(sheet,col++, row, ModulBengkel.removeE(total));

                addLabel(sheet,col++, row, pelanggan);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        }else{
            ModulBengkel.showToast(this,"Tidak ada data");
        }
    }

    private void exLaporanLaba() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qdetailjual where idkategori!=1 AND statusbayar='lunas' AND "+ModulBengkel.sBetween("tglorder",ttglAwal,ttglAkhir)) ;
        if(ModulBengkel.getCount(c) > 0){
            int no = 1 ;
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                    "Barang",
                    "Harga Jual",
                    "Jumlah Jual",
                    "Total",
                    "Laba",
                    "Nama Pelanggan"} ;
            setJudul(sheet,judul);
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulBengkel.getString(c,"faktur") ;
                String tgljual = ModulBengkel.getString(c,"tglorder") ;
                String hargajual = ModulBengkel.getString(c,"hargajual") ;
                String jumlahjual = ModulBengkel.getString(c,"jumlah") ;
                String barang = ModulBengkel.getString(c,"barang") ;
                String laba = ModulBengkel.getString(c,"laba") ;
                String total = ModulBengkel.intToStr(ModulBengkel.strToInt(hargajual) * ModulBengkel.strToInt(jumlahjual)) ;
                String pelanggan = ModulBengkel.getString(c,"pelanggan") ;


                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, ModulBengkel.removeE(hargajual));
                addLabel(sheet,col++, row, jumlahjual);

                addLabel(sheet,col++, row, ModulBengkel.removeE(total));
                addLabel(sheet,col++, row, ModulBengkel.removeE(laba));

                addLabel(sheet,col++, row, pelanggan);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        }else{
            ModulBengkel.showToast(this,"Tidak ada data");
        }
    }



    public void exPelanggan() throws IOException, WriteException {


        Cursor c = db.sq(ModulBengkel.select("tblpelanggan"));
        if(ModulBengkel.getCount(c) > 0){
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

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
                String nama = ModulBengkel.getString(c,"pelanggan") ;
                String alamat = ModulBengkel.getString(c,"alamat") ;
                String telp = ModulBengkel.getString(c,"notelp") ;
                String hutang = ModulBengkel.getString(c,"hutang");

                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                addLabel(sheet,col++, row, hutang);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        } else {
            ModulBengkel.showToast(this,"Tidak ada data");
        }
    }


    public void exHutang() throws IOException, WriteException {


        Cursor c = db.sq(ModulBengkel.selectwhere("qhutang")+ModulBengkel.sBetween("tglbayar",ttglAwal,ttglAkhir));
        if(ModulBengkel.getCount(c) > 0){
            int no = 1 ;
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

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
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulBengkel.getString(c,"pelanggan") ;
                String alamat = ModulBengkel.getString(c,"tglbayar") ;
                String telp = ModulBengkel.getString(c,"bayarhutang") ;


                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        } else {
            ModulBengkel.showToast(this,"Tidak ada data");
        }
    }

    public void exTeknisi() throws IOException, WriteException {


        Cursor c = db.sq(ModulBengkel.select("tblteknisi"));
        if(ModulBengkel.getCount(c) > 0){
            File file = new File(path+nama+" "+ModulBengkel.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,4);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Teknisi","Alamat","Nomor Telp"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulBengkel.getString(c,"teknisi") ;
                String alamat = ModulBengkel.getString(c,"alamatteknisi") ;
                String telp = ModulBengkel.getString(c,"noteknisi") ;

                addLabel(sheet,col++, row, ModulBengkel.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulBengkel.showToast(this,"Berhasil");
        } else {
            ModulBengkel.showToast(this,"Tidak ada data");
        }
    }



    public void setHeader(Database_Bengkel_ db, WritableSheet csvWriter, int JumlahKolom){
        try {
            Cursor c = db.sq(ModulBengkel.select("tbltoko")) ;
            c.moveToNext() ;

            setCenter((CSVWriter) csvWriter,JumlahKolom,ModulBengkel.getString(c,"namatoko"));
            setCenter((CSVWriter) csvWriter,JumlahKolom,ModulBengkel.getString(c,"alamattoko"));
            setCenter((CSVWriter) csvWriter,JumlahKolom,ModulBengkel.getString(c,"notoko"));
            FExcelBengkel.csvNextLine((CSVWriter) csvWriter,2) ;
        }catch (Exception e){

        }
    }

    public void setHeader( WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.sq(ModulBengkel.select("tbltoko")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,ModulBengkel.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++,ModulBengkel.getString(c,"alamattoko"),JumlahKolom);
            addLabel(sheet, row++,ModulBengkel.getString(c,"notoko"),JumlahKolom);
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
        String now = ModulBengkel.setDatePickerNormal(year,month+1,day) ;
        ModulBengkel.setText(v,R.id.tglAwal,now) ;
        ModulBengkel.setText(v,R.id.tglAkhir,now) ;
        ttglAwal = ModulBengkel.setDatePickerNormal(year,month+1,day);
        ttglAkhir = ModulBengkel.setDatePickerNormal(year,month+1,day);
        ConstraintLayout tanggal = findViewById(R.id.cTanggal);
        if(type.equals("pelanggan")){
            tanggal.setVisibility(View.INVISIBLE);
        } else if (type.equals("teknisi")){
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
            ttglAwal = ModulBengkel.setDatePickerNormal(thn,bln+1,day);


            ModulBengkel.setText(v, R.id.tglAwal, ModulBengkel.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {


            ttglAkhir = ModulBengkel.setDatePickerNormal(thn,bln+1,day);

            ModulBengkel.setText(v, R.id.tglAkhir, ModulBengkel.setDatePickerNormal(thn, bln + 1, day));
        }
    };

}
