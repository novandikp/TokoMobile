package com.itbrain.aplikasitoko.tokosepatu;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class Menu_Export_Excel_Sepatu extends AppCompatActivity {
    String Opsi;

    ModulTokoSepatu config,temp;
    DatabaseTokoSepatu db;
    String deviceid;
    SharedPreferences getPrefs;
    String[] header;
    String idAkun;
    private String inputFile;
    String nama;
    Boolean needDate = Boolean.valueOf(true);
    String path;

    Calendar calendar ;
    int year,month, day ;
    String ttglAwal,ttglAkhir;
    String rincian;
    int row = 0;

    String tglAkhir;
    String tglAwal;
    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;
    String type;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_export_excel_sepatu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Export Excel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getWindow().setSoftInputMode(3);
        this.config = new ModulTokoSepatu(getSharedPreferences("config", 0));
        this.temp = new ModulTokoSepatu(getSharedPreferences("temp", 0));
        this.db = new DatabaseTokoSepatu(this);
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
            ModulTokoSepatu.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulTokoSepatu.setText(v,R.id.ePath,"Internal Storage/Download");
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
        ModulTokoSepatu.setText(v,R.id.ePath,"Internal Storage/Download/");
    }


    public void export(View view) throws IOException, WriteException {
        if (type.equals("lappelanggan")){
            nama="Laporan Pelanggan";
            exPelanggan();
        }else if(type.equals("lapbarang")){
            nama="Laporan Barang";
            exBarang();
        }else if (type.equals("pendapatan")){
            nama="Laporan Pendapatan";
            exLaporanPendapatan();
        }else if (type.equals("hutang")){
            nama="Laporan Hutang";
            exLaporanHutang();
        }else if(type.equals("dethutang")){
            nama="Laporan Detail Hutang";
            exLaporanDetailHutang();
        }else if(type.equals("retur")){
            nama="Laporan Return";
            exLaporanRetur();
        }
        else if(type.equals("pengeluaran")){
            nama="Laporan Pengeluaran";
            exLaporanPengeluaran();
        }
        else if(type.equals("pemasukan")){
            nama="Laporan Pemasukan";
            exLaporanPemasukan();
        }
        else if(type.equals("keuangan")){
            nama="Laporan Keuangan";
            exLaporanKeuangan();
        }
        else{
            nama="Laporan Penjualan";
            exLaporanPenjualan();
        }

    }

    private void exLaporanPengeluaran() throws IOException, WriteException {
        File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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

        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tbltransaksi")+ModulTokoSepatu.sWhere("masuk","0")) ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String tgl = ModulTokoSepatu.getString(c,"tgltransaksi");
                String faktur = ModulTokoSepatu.getString(c,"fakturtran");
                String ket = ModulTokoSepatu.getString(c,"kettransaksi");
                String masuk = ModulTokoSepatu.getString(c,"masuk");
                String keluar = ModulTokoSepatu.getString(c,"keluar");
                String saldo = ModulTokoSepatu.getString(c,"saldo");




                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, tgl);
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, ket);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(masuk));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(keluar));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(saldo));


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }

    }

    private void exLaporanPemasukan() throws IOException, WriteException {
        File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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

        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tbltransaksi")+ModulTokoSepatu.sWhere("keluar","0")) ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String tgl = ModulTokoSepatu.getString(c,"tgltransaksi");
                String faktur = ModulTokoSepatu.getString(c,"fakturtran");
                String ket = ModulTokoSepatu.getString(c,"kettransaksi");
                String masuk = ModulTokoSepatu.getString(c,"masuk");
                String keluar = ModulTokoSepatu.getString(c,"keluar");
                String saldo = ModulTokoSepatu.getString(c,"saldo");




                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, tgl);
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, ket);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(masuk));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(keluar));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(saldo));


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }

    }

    private void exLaporanKeuangan() throws IOException, WriteException {


        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tbltransaksi")+ModulTokoSepatu.sBetween("tgltransaksi",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                String tgl = ModulTokoSepatu.getString(c,"tgltransaksi");
                String faktur = ModulTokoSepatu.getString(c,"fakturtran");
                String ket = ModulTokoSepatu.getString(c,"kettransaksi");
                String masuk = ModulTokoSepatu.getString(c,"masuk");
                String keluar = ModulTokoSepatu.getString(c,"keluar");
                String saldo = ModulTokoSepatu.getString(c,"saldo");




                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, tgl);
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, ket);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(masuk));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(keluar));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(saldo));


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }else{
            ModulTokoSepatu.showToast(this,"Tidak ada data");
        }

    }

    private void exLaporanRetur() throws IOException, WriteException {


        Cursor c = db.sq(ModulTokoSepatu.selectwhere("qretur")+ModulTokoSepatu.sBetween("tglretur",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Faktur",
                    "Tanggal Return",
                    "Barang",
                    "Jumlah Return"
            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulTokoSepatu.getString(c,"fakturbayar") ;
                String tglretur = ModulTokoSepatu.getString(c,"tglretur") ;
                String totalbayar = ModulTokoSepatu.getString(c,"jumlahretur") ;
                String barang = ModulTokoSepatu.getString(c,"barang")+"("+ModulTokoSepatu.getString(c,"ukuran")+")" ;




                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tglretur);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, totalbayar);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }else{
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLaporanDetailHutang() throws IOException, WriteException {


        Cursor c = db.sq(ModulTokoSepatu.selectwhere("qjual")+ModulTokoSepatu.sWhere("status","Utang")+" and "+ModulTokoSepatu.sBetween("tgljual",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,8);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Faktur",
                    "Pelanggan",
                    "Tanggal Bayar",
                    "Total Belanja",
                    "Jumlah Bayar",
                    "Total Hutang",
            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String pelanggan = ModulTokoSepatu.getString(c,"pelanggan") ;
                String faktur = ModulTokoSepatu.getString(c,"fakturbayar") ;
                String tnggalbayar = ModulTokoSepatu.getString(c,"tgljual") ;
                String totalbayar = ModulTokoSepatu.getString(c,"total") ;
                String jumlahbayar = ModulTokoSepatu.getString(c,"bayar") ;

                int hasil=ModulTokoSepatu.strToInt(ModulTokoSepatu.getString(c,"kembali"))*-1;
                String kembali = ModulTokoSepatu.intToStr(hasil) ;
                String metode = ModulTokoSepatu.getString(c,"status") ;
                if (ModulTokoSepatu.strToInt(kembali)<0){
                    kembali="0";
                }



                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, tnggalbayar);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(totalbayar));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(jumlahbayar));
                addLabel(sheet,col++, row, kembali);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }else{
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLaporanHutang() throws IOException, WriteException {


        Cursor c = db.sq(ModulTokoSepatu.selectwhere("tblpelanggan") + "hutang!=0 ORDER BY pelanggan ASC") ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Nama Pelanggan",
                    "Alamat",
                    "No Telp",
                    "Jumlah Hutang"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String pelanggan = ModulTokoSepatu.getString(c,"pelanggan") ;
                String alamat = ModulTokoSepatu.getString(c,"alamat") ;
                String notelp = ModulTokoSepatu.getString(c,"notelp") ;
                String jumlahhutang = ModulTokoSepatu.getString(c,"hutang") ;


                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, notelp);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(jumlahhutang));

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }else{
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        }
    }

    private void exLaporanPendapatan() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM qjual where fakturbayar IS NOT NULL and "+ModulTokoSepatu.sBetween("tgljual",ttglAwal,ttglAkhir) ) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,8);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Faktur",
                    "Tanggal Bayar",
                    "Total Belanja",
                    "Jumlah Bayar",
                    "Kembali",
                    "Nama Pelanggan",
                    "Metode Pembayaran"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String pelanggan = ModulTokoSepatu.getString(c,"pelanggan") ;
                String faktur = ModulTokoSepatu.getString(c,"fakturbayar") ;
                String tnggalbayar = ModulTokoSepatu.getString(c,"tgljual") ;
                String totalbayar = ModulTokoSepatu.getString(c,"total") ;
                String jumlahbayar = ModulTokoSepatu.getString(c,"bayar") ;
                String kembali = ModulTokoSepatu.getString(c,"kembali") ;
                String metode = ModulTokoSepatu.getString(c,"status") ;
                if (ModulTokoSepatu.strToInt(kembali)<0){
                    kembali="0";
                }



                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tnggalbayar);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(totalbayar));
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(jumlahbayar));
                addLabel(sheet,col++, row, kembali);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, metode);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }else{
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLaporanPenjualan() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM view_orderdetail where fakturbayar IS NOT NULL AND "+ModulTokoSepatu.sBetween("tgljual",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                    "Harga Jual",
                    "Jumlah Jual",
                    "Barang",
                    "Total",
                    "Nama Pelanggan",
                    "Metode Pembayaran"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulTokoSepatu.getString(c,"fakturbayar") ;
                String tgljual = ModulTokoSepatu.getString(c,"tgljual") ;
                String hargajual = ModulTokoSepatu.getString(c,"hargajual") ;
                String jumlahjual = ModulTokoSepatu.getString(c,"jumlah") ;
                String barang = ModulTokoSepatu.getString(c,"barang") ;
                String total = ModulTokoSepatu.intToStr(ModulTokoSepatu.strToInt(hargajual) * ModulTokoSepatu.strToInt(jumlahjual)) ;
                String pelanggan = ModulTokoSepatu.getString(c,"pelanggan") ;
                String keterangan = ModulTokoSepatu.getString(c,"status") ;

                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(hargajual));
                addLabel(sheet,col++, row, jumlahjual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, ModulTokoSepatu.removeE(total));

                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, keterangan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }else{
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exBarang() throws IOException, WriteException {


        Cursor c = db.sq(ModulTokoSepatu.select("qbarang")) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            String[] judul = {"No.","Barang","Kategori","Deskripsi","Stok Barang"} ;
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String barang = ModulTokoSepatu.getString(c,"barang") ;
                String kategori = ModulTokoSepatu.getString(c,"kategori") ;
                String deskripsi = ModulTokoSepatu.getString(c,"deskripsi") ;
                String stok = ModulTokoSepatu.getString(c,"stokbarang") ;

                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, kategori);
                addLabel(sheet,col++, row, deskripsi);
                addLabel(sheet,col++, row, stok);
                ;
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        }else {
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        }

    }

    public void exPelanggan() throws IOException, WriteException {


        Cursor c = db.sq(ModulTokoSepatu.select("tblpelanggan"));
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulTokoSepatu.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,4);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Pelanggan","Alamat","Nomor Telp"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulTokoSepatu.getString(c,"pelanggan") ;
                String alamat = ModulTokoSepatu.getString(c,"alamat") ;
                String telp = ModulTokoSepatu.getString(c,"notelp") ;

                addLabel(sheet,col++, row, ModulTokoSepatu.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulTokoSepatu.showToast(this,"Berhasil");
        } else {
            ModulTokoSepatu.showToast(this,"Tidak ada data");
        }
    }



    public void setHeader(DatabaseTokoSepatu db, CSVWriter csvWriter, int JumlahKolom){
        try {
            Cursor c = db.sq(ModulTokoSepatu.select("tbltoko")) ;
            c.moveToNext() ;

            setCenter(csvWriter,JumlahKolom,ModulTokoSepatu.getString(c,"namatoko"));
            setCenter(csvWriter,JumlahKolom,ModulTokoSepatu.getString(c,"alamattoko"));
            setCenter(csvWriter,JumlahKolom,ModulTokoSepatu.getString(c,"notoko"));
            FExcel_Sepatu.csvNextLine(csvWriter,2) ;
        }catch (Exception e){

        }
    }

    public void setHeader(DatabaseTokoSepatu db, WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.sq(ModulTokoSepatu.select("tbltoko")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,ModulTokoSepatu.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++,ModulTokoSepatu.getString(c,"alamattoko"),JumlahKolom);
            addLabel(sheet, row++,ModulTokoSepatu.getString(c,"notoko"),JumlahKolom);
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

    // Date Picker
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
            ttglAwal = ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day);


            ModulTokoSepatu.setText(v, R.id.tglAwal, ModulTokoSepatu.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {


            ttglAkhir = ModulTokoSepatu.setDatePickerNormal(thn,bln+1,day);

            ModulTokoSepatu.setText(v, R.id.tglAkhir, ModulTokoSepatu.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    void setTanggal(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String now = ModulTokoSepatu.setDatePickerNormal(year,month+1,day) ;
        ModulTokoSepatu.setText(v,R.id.tglAwal,now) ;
        ModulTokoSepatu.setText(v,R.id.tglAkhir,now) ;
        ttglAwal = ModulTokoSepatu.setDatePickerNormal(year,month+1,day);
        ttglAkhir = ModulTokoSepatu.setDatePickerNormal(year,month+1,day);
        ConstraintLayout tanggal = findViewById(R.id.cTanggal);
        if(type.equals("lappelanggan")){
            tanggal.setVisibility(View.INVISIBLE);
        } else if (type.equals("lapbarang")){
            tanggal.setVisibility(View.INVISIBLE);
        }else if (type.equals("hutang")){
            tanggal.setVisibility(View.INVISIBLE);
        }
    }

    public void tglAwal(View view) {
        setDate(1);
    }


    public void tglAkhir(View view) {
        setDate(2);
    }

}

