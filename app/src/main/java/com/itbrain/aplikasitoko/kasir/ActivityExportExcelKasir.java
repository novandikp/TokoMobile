package com.itbrain.aplikasitoko.kasir;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

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
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ActivityExportExcelKasir extends AppCompatActivity {

    static final String ITEM_EXPORT = "com.komputerkit.olshoppluskeuangan.inlimit" ;
    String deviceid ;
    SharedPreferences getPrefs ;
    boolean bexport = false ;
    Calendar calendar ;
    int year,month, day ;

    String type,path,nama,tglAwal,tglAkhir,Opsi,idAkun,rincian;
    String[] header;
    FConfigKasir config,temp ;
    View v ;
    DatabaseKasir db ;
    Boolean needDate = true;
    int row = 0 ;
    CekInAppKasir cekInApp ;
    //    private HolderBaru holderBaru;

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat timesBold;
    private WritableCellFormat times;
    private String inputFile;
    private static final String TAG = "Kit";
    String ttglAwal,ttglAkhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_export_excel_kasir);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKasir(getSharedPreferences("config",this.MODE_PRIVATE)) ;
        temp = new FConfigKasir(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new DatabaseKasir(this,config) ;
        v = this.findViewById(android.R.id.content);
        type = getIntent().getStringExtra("type") ;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        path = Environment.getExternalStorageDirectory().toString() + "/Download/" ;
        cekInApp = new CekInAppKasir(this) ;

        ImageButton imageButton = findViewById(R.id.kembaliExcel);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
      //  deviceid = FFunctionKasir.getDecrypt(getPrefs.getString(FFunctionKasir.getEncrypt("deviceid"),"")) ;
        bexport = getPrefs.getBoolean("inLimit",false) ;
        jenis();
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/kasirsupermudah") ;
            file.mkdirs() ;
        }catch (Exception e){

        }

        setText() ;
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();
            FFunctionKasir.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FFunctionKasir.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
    }

    public void setText(){
        FFunctionKasir.setText(v,R.id.ePath,"Internal Storage/Download/");
        String now = FFunctionKasir.setDatePickerNormal(year,month+1,day) ;
        FFunctionKasir.setText(v,R.id.tglAwal,now) ;
        FFunctionKasir.setText(v,R.id.tglAkhir,now) ;
        ttglAwal = FFunctionKasir.setDatePicker(year,month+1,day);
        ttglAkhir = FFunctionKasir.setDatePicker(year,month+1,day);
    }

//    public void browse(View v){
//        Intent i = new Intent(this,ActivityExportBrowse.class) ;
//        i.putExtra("type",type) ;
//        startActivity(i);
//    }

    public void jenis(){
        ConstraintLayout tanggal = findViewById(R.id.cTanggal);
            tanggal.setVisibility(View.INVISIBLE);
            tanggal.setVisibility(View.INVISIBLE);
        }

    public void export(View view) throws IOException, WriteException {

        if(type.equals("pelanggan")){
            nama = "Laporan Pelanggan" ;
            exPelanggan() ;
        } else if (type.equals("barang")){
            nama = "Laporan Barang" ;
            exBarang();
        } else if (type.equals("laporanpenjualan")){
            nama = "Laporan Penjualan" ;
            exLaporanPenjualan();
        } else if (type.equals("pendapatan")){
            nama = "Laporan Pendapatan" ;
            exLaporanPendapatan();
        } else if (type.equals("return")){
            nama = "Laporan Return" ;
            exReturn();
        } else if (type.equals("labarugi")){
            nama = "Laporan Laba Rugi" ;
            exLabaRugi() ;
        } else if (type.equals("hutang")) {
            nama = "Laporan Hutang";
            exHutang();
        }
        row = 0 ;
    }


    private void exHutang() throws IOException, WriteException {


        Cursor c = db.sq(FQueryKasir.selectwhere("qbayar")+FQueryKasir.sWhere("flagbayar","0")+" AND "+FQueryKasir.sBetween("tglbayar",ttglAwal,ttglAkhir)) ;
        if(FFunctionKasir.getCount(c) > 0){
            File file = new File(path+nama+" "+FFunctionKasir.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,7);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.","Faktur","Pelanggan","Tanggal Bayar","Total Belanja","Jumlah Bayar","Jumlah Hutang"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String fakturbayar = FFunctionKasir.getString(c,"fakturbayar") ;
                String tglbayar = FFunctionKasir.getString(c,"tglbayar") ;
                String pelanggan = FFunctionKasir.getString(c,"pelanggan") ;
                String jumlahbayar = FFunctionKasir.getString(c,"jumlahbayar") ;
                String bayar = FFunctionKasir.getString(c,"bayar") ;
                String kembali = FFunctionKasir.getString(c,"kembali") ;

                addLabel(sheet,col++, row, FFunctionKasir.intToStr(no));
                addLabel(sheet,col++, row, fakturbayar);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, FFunctionKasir.dateToNormal(tglbayar));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(jumlahbayar));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(bayar));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(kembali));
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak ada Data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLabaRugi() throws IOException, WriteException {

        String query = FQueryKasir.selectExcel("qpenjualan","tgljual",ttglAwal,ttglAkhir);
        Cursor c = db.sq(query) ;
        if(FFunctionKasir.getCount(c) > 0){
            File file = new File(path+nama+" "+FFunctionKasir.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,6);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.","Faktur","Barang","Jumlah Barang","Jumlah Laba Rugi per Barang","Jumlah Total Laba Rugi"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = FFunctionKasir.getString(c,"fakturbayar") ;
                String barang = FFunctionKasir.getString(c,"barang") ;
                String jumlahjual = FFunctionKasir.getString(c,"jumlahjual") ;
                String labarugi = FFunctionKasir.getString(c,"labarugi") ;
                double total = FFunctionKasir.strToDouble(jumlahjual)*FFunctionKasir.strToDouble(labarugi) ;

                addLabel(sheet,col++, row, FFunctionKasir.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, jumlahjual);
                addLabel(sheet,col++, row, FFunctionKasir.removeE(labarugi));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(total));
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exReturn() throws IOException, WriteException {

        String query = FQueryKasir.selectExcel("qreturn","tglreturn",ttglAwal,ttglAkhir);
        Cursor c = db.sq(query) ;
        if(FFunctionKasir.getCount(c) > 0){
            File file = new File(path+nama+" "+FFunctionKasir.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.","Faktur","Tanggal Return","Barang","Jumlah Return"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String barang = FFunctionKasir.getString(c,"barang") ;
                String fakturbayar = FFunctionKasir.getString(c,"fakturbayar") ;
                String tglreturn = FFunctionKasir.getString(c,"tglreturn") ;
                String jumlah = FFunctionKasir.getString(c,"jumlah") ;

                addLabel(sheet,col++, row, FFunctionKasir.intToStr(no));
                addLabel(sheet,col++, row, fakturbayar);
                addLabel(sheet,col++, row, FFunctionKasir.dateToNormal(tglreturn));
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, jumlah);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLaporanPendapatan() throws IOException, WriteException {

        String query = FQueryKasir.selectExcel("qbayar","tglbayar",ttglAwal,ttglAkhir);
        Cursor c = db.sq(query) ;
        if(FFunctionKasir.getCount(c) > 0){
            File file = new File(path+nama+" "+FFunctionKasir.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                String pelanggan = FFunctionKasir.getString(c,"pelanggan") ;
                String faktur = FFunctionKasir.getString(c,"fakturbayar") ;
                String tnggalbayar = FFunctionKasir.getString(c,"tglbayar") ;
                String totalbayar = FFunctionKasir.getString(c,"jumlahbayar") ;
                String jumlahbayar = FFunctionKasir.getString(c,"bayar") ;
                String kembali = FFunctionKasir.getString(c,"kembali") ;
                String metode = FFunctionKasir.getString(c,"flagbayar") ;

                if (metode.equals("1")){
                    kembali = "" + FFunctionKasir.removeE(kembali) ;
                    metode = "Tunai" ;
                } else {
                    kembali = "-" + FFunctionKasir.removeE(kembali) ;
                    metode = "Hutang" ;
                }

                addLabel(sheet,col++, row, FFunctionKasir.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, FFunctionKasir.dateToNormal(tnggalbayar));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(totalbayar));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(jumlahbayar));
                addLabel(sheet,col++, row, kembali);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, metode);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLaporanPenjualan() throws IOException, WriteException {

        String query = FQueryKasir.selectExcel("qpenjualan","tgljual",ttglAwal,ttglAkhir);
        Cursor c = db.sq(query) ;
        if(FFunctionKasir.getCount(c) > 0){
            File file = new File(path+nama+" "+FFunctionKasir.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,11);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Tanggal Jual",
                    "Harga Jual",
                    "Jumlah Jual",
                    "Barang",
                    "Total",
                    "Laba Rugi per Barang",
                    "Total Laba Rugi",
                    "Nama Pelanggan",
                    "Keterangan"} ;
            setJudul(sheet,judul);

            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = FFunctionKasir.getString(c,"fakturbayar") ;
                String tgljual = FFunctionKasir.getString(c,"tgljual") ;
                String hargajual = FFunctionKasir.getString(c,"hargajual:1") ;
                String jumlahjual = FFunctionKasir.getString(c,"jumlahjual") ;
                String barang = FFunctionKasir.getString(c,"barang") ;
                String labarugi = FFunctionKasir.getString(c,"labarugi") ;
                String total = FFunctionKasir.doubleToStr(FFunctionKasir.strToDouble(hargajual) * FFunctionKasir.strToDouble(jumlahjual)) ;
                String totallabarugi = FFunctionKasir.doubleToStr(FFunctionKasir.strToDouble(labarugi) * FFunctionKasir.strToDouble(jumlahjual)) ;
                String pelanggan = FFunctionKasir.getString(c,"pelanggan") ;
                String keterangan = FFunctionKasir.getString(c,"ketpenjualan") ;

                addLabel(sheet,col++, row, FFunctionKasir.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, FFunctionKasir.dateToNormal(tgljual));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(hargajual));
                addLabel(sheet,col++, row, jumlahjual);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, FFunctionKasir.removeE(total));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(labarugi));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(totallabarugi));
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, keterangan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exPelanggan() throws IOException, WriteException {


        Cursor c = db.sq(FQueryKasir.select("tblpelanggan") + " where idpelanggan <> 1") ;
        if(FFunctionKasir.getCount(c) > 0){
            File file = new File(path+nama+" "+FFunctionKasir.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                String nama = FFunctionKasir.getString(c,"pelanggan") ;
                String alamat = FFunctionKasir.getString(c,"alamat") ;
                String telp = FFunctionKasir.getString(c,"telp") ;

                addLabel(sheet,col++, row, FFunctionKasir.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this,"Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exBarang() throws IOException, WriteException {


        Cursor c = db.sq(FQueryKasir.select("qbarang")) ;
        if(FFunctionKasir.getCount(c) > 0){
            File file = new File(path+nama+" "+FFunctionKasir.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            String[] judul = {"No.","Barang","Kategori","Harga Beli","Harga Jual","Stok","Konsinyasi"} ;
            setHeader(db,sheet,7);
            excelNextLine(sheet,2) ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String barang = FFunctionKasir.getString(c,"barang") ;
                String kategori = FFunctionKasir.getString(c,"kategori") ;
                String hargabeli = FFunctionKasir.getString(c,"hargabeli") ;
                String hargajual = FFunctionKasir.getString(c,"hargajual") ;
                String stok = FFunctionKasir.getString(c,"stok") ;
                String titipan = FFunctionKasir.getString(c,"titipan") ;
                if(titipan.equals("0")){
                    titipan = "Kulakan" ;
                } else {
                    titipan = "Titipan" ;
                }
                addLabel(sheet,col++, row, FFunctionKasir.intToStr(no));
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, kategori);
                addLabel(sheet,col++, row, FFunctionKasir.removeE(hargabeli));
                addLabel(sheet,col++, row, FFunctionKasir.removeE(hargajual));
                addLabel(sheet,col++, row, stok);
                addLabel(sheet,col++, row, titipan);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }

    }

    public void setHeader(DatabaseKasir db, CSVWriter csvWriter,int JumlahKolom){
        try {
            Cursor c = db.sq(FQueryKasir.select("tblidentitas")) ;
            c.moveToNext() ;

            setCenter(csvWriter,JumlahKolom,FFunctionKasir.getString(c,"nama"));
            setCenter(csvWriter,JumlahKolom,FFunctionKasir.getString(c,"alamat"));
            setCenter(csvWriter,JumlahKolom,FFunctionKasir.getString(c,"telp"));
//            FExcel.csvNextLine(csvWriter,2) ;
        }catch (Exception e){

        }
    }

    public void setHeader(DatabaseKasir db, WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.sq(FQueryKasir.select("tblidentitas")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,FFunctionKasir.getString(c,"nama"),JumlahKolom);
            addLabel(sheet, row++,FFunctionKasir.getString(c,"alamat"),JumlahKolom);
            addLabel(sheet, row++,FFunctionKasir.getString(c,"telp"),JumlahKolom);
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
        Number number;
        number = new Number(column, row, integer, times);
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
        cv.setAutosize(true);
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

    public void setJudul(WritableSheet sheet,String[] val) throws WriteException {
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
            ttglAwal = FFunctionKasir.setDatePicker(thn,bln+1,day);


            FFunctionKasir.setText(v, R.id.tglAwal, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {


            ttglAkhir = FFunctionKasir.setDatePicker(thn,bln+1,day);

            FFunctionKasir.setText(v, R.id.tglAkhir, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    public void tglAwal(View view) {
        setDate(1);
    }


    public void tglAkhir(View view) {
        setDate(2);
    }

}
