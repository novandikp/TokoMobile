package com.itbrain.aplikasitoko.Laundry;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.apotek.Modul_Excel_Laundry;
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

public class Export_Exel_Laundry extends AppCompatActivity {
        String tab;
        View v;
        DatabaseLaundry db;
        String path;
        String nama;
    Calendar calendar ;
    int year,month, day ;
    String ttglAwal,ttglAkhir;
        SharedPreferences getPrefs;
        SharedPreferences.Editor editor;
        final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        int row = 0;

        private WritableCellFormat times;
        private WritableCellFormat timesBold;
        private WritableCellFormat timesBoldUnderline;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.export_exel_laundry);
            tab=getIntent().getStringExtra("tab");
            ImageButton i = findViewById(R.id.kembali16);
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            this.db = new DatabaseLaundry(this);
            this.v = findViewById(android.R.id.content);
            this.getPrefs = getSharedPreferences("dir",MODE_PRIVATE);
            this.path = getPrefs.getString("dirExport",Environment.getExternalStorageDirectory().toString() + "/KomputerKit/Laundry & Keuangan/");
            if(Build.VERSION.SDK_INT >= 29) {
                this.path = this.getExternalFilesDir("Laporan").toString()+"/";
                String codename= this.getPackageName();
                ModulLaundry.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
                //only api 21 above
            }else{
                this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
                ModulLaundry.setText(v,R.id.ePath,"Internal Storage/Download");
                //only api 21 down
            }
            editor=getPrefs.edit();
            try {
                new File(path).mkdirs();
            } catch (Exception e) {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId()==android.R.id.home){
                finish();
            }
            return super.onOptionsItemSelected(item);
        }

        public void export(View view) throws IOException, WriteException  {
            if (tab.equals("jasa")){
                nama="Laporan Jasa";
                exJasa();
            }else if(tab.equals("pegawai")){
                nama="Laporan Pegawai";
                exPegawai();
            }else if (tab.equals("pelanggan")){
                nama="Laporan Pelanggan";
                exPelanggan();
            }


        }
        private void exJasa() throws IOException, WriteException {


            Cursor c = db.sq(QueryLaundry.select("qjasa")) ;
            if(ModulLaundry.getCount(c) > 0){
                File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
                WorkbookSettings wbSettings = new WorkbookSettings();

                wbSettings.setLocale(new Locale("en", "EN"));

                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Report", 0);
                WritableSheet sheet = workbook.getSheet(0);

                createLabel(sheet);
                setHeader(db,sheet,5);
                excelNextLine(sheet,2) ;

                String[] judul = {  "No.",
                        "Kategori",
                        "Nama Jasa",
                        "Biaya per Satuan",
                        "Satuan"
                } ;
                setJudul(sheet,judul);
                int no = 1 ;
                while(c.moveToNext()){
                    int col = 0 ;
                    String kategori = ModulLaundry.getString(c,"kategori") ;
                    String jasa = ModulLaundry.getString(c,"jasa") ;
                    String biaya = ModulLaundry.getString(c,"biaya") ;
                    String satuan = ModulLaundry.getString(c,"satuan");

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, kategori);
                    addLabel(sheet,col++, row, jasa);
                    addLabel(sheet,col++, row, biaya);
                    addLabel(sheet,col++, row, satuan );

                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
            }
        }

        private void exPegawai() throws IOException, WriteException {


            Cursor c = db.sq(QueryLaundry.select("tblpegawai")) ;
            if(ModulLaundry.getCount(c) > 0){
                File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
                WorkbookSettings wbSettings = new WorkbookSettings();

                wbSettings.setLocale(new Locale("en", "EN"));

                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Report", 0);
                WritableSheet sheet = workbook.getSheet(0);

                createLabel(sheet);
                setHeader(db,sheet,5);
                excelNextLine(sheet,2) ;

                String[] judul = {  "No.",
                        "Nama Pegawai",
                        "Alamat",
                        "No Telp"
                } ;
                setJudul(sheet,judul);
                int no = 1 ;
                while(c.moveToNext()){
                    int col = 0 ;
                    String pegawai = ModulLaundry.getString(c,"pegawai") ;
                    String alamat = ModulLaundry.getString(c,"alamatpegawai") ;
                    String notelp = ModulLaundry.getString(c,"notelppegawai") ;

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, pegawai);
                    addLabel(sheet,col++, row, alamat);
                    addLabel(sheet,col++, row, notelp);

                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
            }
        }
        private void exPelanggan() throws IOException, WriteException {


            Cursor c = db.sq(QueryLaundry.select("tblpelanggan")) ;
            if(ModulLaundry.getCount(c) > 0){
                File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
                WorkbookSettings wbSettings = new WorkbookSettings();

                wbSettings.setLocale(new Locale("en", "EN"));

                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Report", 0);
                WritableSheet sheet = workbook.getSheet(0);

                createLabel(sheet);
                setHeader(db,sheet,5);
                excelNextLine(sheet,2) ;

                String[] judul = {  "No.",
                        "Nama Pelanggan",
                        "Alamat",
                        "No Telp"
                } ;
                setJudul(sheet,judul);
                int no = 1 ;
                while(c.moveToNext()){
                    int col = 0 ;
                    String pelanggan = ModulLaundry.getString(c,"pelanggan") ;
                    String alamat = ModulLaundry.getString(c,"alamat") ;
                    String notelp = ModulLaundry.getString(c,"notelp") ;

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, pelanggan);
                    addLabel(sheet,col++, row, alamat);
                    addLabel(sheet,col++, row, notelp);

                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
            }
        }

        private void exLaundry() throws IOException, WriteException {
            Cursor c = db.sq(QueryLaundry.selectwhere("qlaundry")+ QueryLaundry.sBetween("tgllaundry",ttglAwal,ttglAkhir)) ;
            if(ModulLaundry.getCount(c) > 0){
                File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                        "Pegawai",
                        "Pelanggan",
                        "Tanggal Pesan",
                        "Tanggal Selesai",
                        "Kategori",
                        "Jasa",
                        "Biaya per Satuan",
                        "Jumlah Unit",
                        "Jumlah",
                        "Total per Faktur",
                        "Status Laundry"
                } ;
                setJudul(sheet,judul);
                int no = 1 ;
                while(c.moveToNext()){
                    int col = 0 ;
                    String faktur = ModulLaundry.getString(c,"faktur") ;
                    String pegawai = ModulLaundry.getString(c,"pegawai") ;
                    String pelanggan = ModulLaundry.getString(c,"pelanggan") ;
                    String tglpesan = ModulLaundry.getString(c,"tgllaundry") ;
                    String tglselesai = ModulLaundry.getString(c,"tglselesai") ;
                    String kategori = ModulLaundry.getString(c,"kategori") ;
                    String jasa = ModulLaundry.getString(c,"jasa") ;
                    String biaya = ModulLaundry.getString(c,"biaya")+"/"+ ModulLaundry.getString(c,"satuan") ;
                    String jumlahunit = ModulLaundry.getString(c,"jumlahlaundry") ;
                    String jumlah = ModulLaundry.getString(c,"biayalaundry") ;
                    String total = ModulLaundry.getString(c,"total") ;
                    String status = ModulLaundry.getString(c,"statuslaundry") ;

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, faktur);
                    addLabel(sheet,col++, row, pegawai);
                    addLabel(sheet,col++, row, pelanggan);
                    addLabel(sheet,col++, row, ModulLaundry.dateToNormal(tglpesan));
                    addLabel(sheet,col++, row, ModulLaundry.dateToNormal(tglselesai));
                    addLabel(sheet,col++, row, kategori);
                    addLabel(sheet,col++, row, jasa);
                    addLabel(sheet,col++, row, biaya);
                    addLabel(sheet,col++, row, jumlahunit);
                    addLabel(sheet,col++, row, jumlah);
                    addLabel(sheet,col++, row, total);
                    addLabel(sheet,col++, row, status);

                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
            }
        }
        private void exProsesLaundry() throws IOException, WriteException {


            Cursor c = db.sq(QueryLaundry.selectwhere("qproses")+ QueryLaundry.sBetween("tgllaundry",ttglAwal,ttglAkhir)) ;
            if(ModulLaundry.getCount(c) > 0){
                File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                        "Pelanggan",
                        "Tanggal Pesan",
                        "Tanggal Selesai",
                        "Jumlah Unit Pesan",
                        "Total"
                } ;
                setJudul(sheet,judul);
                int no = 1 ;
                while(c.moveToNext()){
                    int col = 0 ;
                    String faktur = ModulLaundry.getString(c,"faktur") ;
                    String pelanggan = ModulLaundry.getString(c,"pelanggan") ;
                    String tglpesan = ModulLaundry.getString(c,"tgllaundry") ;
                    String tglselesai = ModulLaundry.getString(c,"tglselesai") ;
                    String jumlahunit = countJumlah(ModulLaundry.getString(c,"idlaundry")) ;
                    String total = ModulLaundry.getString(c,"total") ;

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, faktur);
                    addLabel(sheet,col++, row, pelanggan);
                    addLabel(sheet,col++, row, ModulLaundry.dateToNormal(tglpesan));
                    addLabel(sheet,col++, row, ModulLaundry.dateToNormal(tglselesai));
                    addLabel(sheet,col++, row, jumlahunit);
                    addLabel(sheet,col++, row, total);
                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
            }
        }
        private void exPendapatan() throws IOException, WriteException {

            Cursor cTunai = db.sq(QueryLaundry.selectwhere("qbayar")+ QueryLaundry.sWhere("statuslaundry","Selesai")+" AND bayar>0 AND "+ QueryLaundry.sBetween("tglbayar",ttglAwal,ttglAkhir)) ;
            Cursor cHutang = db.sq(QueryLaundry.selectwhere("qbayarhutang")+ QueryLaundry.sBetween("tglbayar",ttglAwal,ttglAkhir) );
            if(cTunai.getCount()>0||cHutang.getCount()>0){
                File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Report", 0);
                WritableSheet sheet = workbook.getSheet(0);
                createLabel(sheet);
                setHeader(db,sheet,5);
                excelNextLine(sheet,2) ;
                String[] judul = {  "No.",
                        "Metode",
                        "Tanggal",
                        "Pelanggan",
                        "Total",
                        "Bayar",
                        "Kembali"

                } ;
                setJudul(sheet,judul);
                int no = 1 ;
                while(cTunai.moveToNext()){
                    int col = 0 ;
                    String metode;
                    if (ModulLaundry.getString(cTunai,"statusbayar").equals("Tunai")){
                        metode="Tunai" ;
                    }else {
                        metode="Hutang";
                    }
                    String tglbayar = ModulLaundry.getString(cTunai,"tglbayar") ;
                    String pelanggan = ModulLaundry.getString(cTunai,"pelanggan") ;
                    String total = ModulLaundry.getString(cTunai,"total") ;
                    String bayar = ModulLaundry.getString(cTunai,"bayar") ;
                    String kembali = ModulLaundry.getString(cTunai,"kembali") ;

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, metode);
                    addLabel(sheet,col++, row, ModulLaundry.dateToNormal(tglbayar));
                    addLabel(sheet,col++, row, pelanggan);
                    addLabel(sheet,col++, row, total);
                    addLabel(sheet,col++, row, bayar);
                    addLabel(sheet,col++, row, kembali);

                    row++ ;
                    no++ ;
                }
                while(cHutang.moveToNext()){
                    int col = 0 ;
                    String metode="Bayar Hutang";
                    String tglbayar = ModulLaundry.getString(cHutang,"tglbayar") ;
                    String pelanggan = ModulLaundry.getString(cHutang,"pelanggan") ;
                    String total = ModulLaundry.getString(cHutang,"hutang") ;
                    String bayar = ModulLaundry.getString(cHutang,"bayar") ;
                    String kembali = ModulLaundry.getString(cHutang,"kembali") ;

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, metode);
                    addLabel(sheet,col++, row, ModulLaundry.dateToNormal(tglbayar));
                    addLabel(sheet,col++, row, pelanggan);
                    addLabel(sheet,col++, row, total);
                    addLabel(sheet,col++, row, bayar);
                    addLabel(sheet,col++, row, kembali);

                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }else{


                Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
            }
        }
        private void exHutang() throws IOException, WriteException {
            File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Pelanggan",
                    "Alamat",
                    "No Telp",
                    "Total Hutang"
            } ;
            setJudul(sheet,judul);

            Cursor c = db.sq(QueryLaundry.select("tblpelanggan")) ;
            if(ModulLaundry.getCount(c) > 0){
                int no = 1 ;
                while(c.moveToNext()){
                    int col = 0 ;
                    String pelanggan = ModulLaundry.getString(c,"pelanggan") ;
                    String alamat = ModulLaundry.getString(c,"alamat") ;
                    String notelp = ModulLaundry.getString(c,"notelp") ;
                    String hutang = ModulLaundry.getString(c,"hutang") ;

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, pelanggan);
                    addLabel(sheet,col++, row, alamat);
                    addLabel(sheet,col++, row, notelp);
                    addLabel(sheet,col++, row, hutang);
                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }
        }





        private void exKeuangan() throws IOException, WriteException {


            Cursor c = db.sq(QueryLaundry.selectwhere("tblkeuangan")+ QueryLaundry.sBetween("tgltransaksi",ttglAwal,ttglAkhir)) ;
            if(ModulLaundry.getCount(c) > 0){
                File file = new File(path+nama+" "+ ModulLaundry.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
                WorkbookSettings wbSettings = new WorkbookSettings();

                wbSettings.setLocale(new Locale("en", "EN"));

                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                workbook.createSheet("Report", 0);
                WritableSheet sheet = workbook.getSheet(0);

                createLabel(sheet);
                setHeader(db,sheet,5);
                excelNextLine(sheet,2) ;

                String[] judul = {  "No.",
                        "Jenis Transaksi",
                        "Tanggal",
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
                    String jenis = "";
                    if (!ModulLaundry.getString(c,"masuk").equals("0")|| ModulLaundry.getString(c,"keluar").equals("0")){
                        jenis="Pemasukan";
                    }else {
                        jenis="Pengeluaran";
                    }
                    String tanggal = ModulLaundry.getString(c,"tgltransaksi");
                    String faktur = ModulLaundry.getString(c,"faktur");
                    String keterangan= ModulLaundry.getString(c,"keterangantransaksi");
                    String masuk = ModulLaundry.getString(c,"masuk");
                    String keluar = ModulLaundry.getString(c,"keluar");
                    String saldo = ModulLaundry.getString(c,"saldo");

                    addLabel(sheet,col++, row, ModulLaundry.intToStr(no));
                    addLabel(sheet,col++, row, jenis);
                    addLabel(sheet,col++, row, tanggal);
                    addLabel(sheet,col++, row, faktur);
                    addLabel(sheet,col++, row, keterangan);
                    addLabel(sheet,col++, row, masuk);
                    addLabel(sheet,col++, row, keluar);
                    addLabel(sheet,col++, row, saldo);

                    row++ ;
                    no++ ;
                }
                workbook.write();
                workbook.close();
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
            }
        }
        private String countJumlah(String value){
            Cursor c=db.sq(QueryLaundry.sSum("tbllaundrydetail","jumlahlaundry")+" WHERE idlaundry="+value);
            c.moveToFirst();
            return ModulLaundry.getStringFromColumn(c,0);
        }

        public void setHeader(DatabaseLaundry db, CSVWriter csvWriter, int JumlahKolom){
            try {
                Cursor c = db.sq(QueryLaundry.select("tblidentitas")) ;
                c.moveToNext() ;

                setCenter(csvWriter,JumlahKolom, ModulLaundry.getString(c,"namatoko"));
                setCenter(csvWriter,JumlahKolom, ModulLaundry.getString(c,"alamattoko"));
                setCenter(csvWriter,JumlahKolom, ModulLaundry.getString(c,"notelptoko"));
                Modul_Excel_Laundry.csvNextLine(csvWriter,2) ;
            }catch (Exception e){

            }
        }

        public void setHeader(DatabaseLaundry db, WritableSheet sheet, int JumlahKolom){
            try {
                Cursor c = db.sq(QueryLaundry.select("tblidentitas")) ;
                c.moveToNext() ;

                addLabel(sheet, row++, ModulLaundry.getString(c,"namatoko"),JumlahKolom);
                addLabel(sheet, row++, ModulLaundry.getString(c,"alamattoko"),JumlahKolom);
                addLabel(sheet, row++, ModulLaundry.getString(c,"notelptoko"),JumlahKolom);
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
}
