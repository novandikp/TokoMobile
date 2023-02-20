package com.itbrain.aplikasitoko.klinik;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

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


public class MenuExportExcelKlinik extends AppCompatActivity {
    String Opsi;
    Calendar calendar ;
    int year,month, day ;
    String ttglAwal,ttglAkhir;
    ModulKlinik config,temp;
    DatabaseKlinik db;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_export_excel_klinik);


        getWindow().setSoftInputMode(3);
        this.config = new ModulKlinik(getSharedPreferences("config", 0));
        this.temp = new ModulKlinik(getSharedPreferences("temp", 0));
        this.db = new DatabaseKlinik(this);
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
            ModulKlinik.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulKlinik.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        setTanggal();

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        ModulKlinik.setText(v,R.id.ePath,"Internal Storage/Download/");
    }


    public void export(View view) throws IOException, WriteException {
        if (type.equals("pasien")){
            nama="Laporan Pasien";
            exPasien();
        }else if(type.equals("dokter")){
            nama="Laporan Dokter";
            exDokter();
       }else if (type.equals("jasa")){
            nama="Laporan Jasa";
            exJasa();
        }else if(type.equals("pendapatan2")){
            nama="Laporan Bagi Hasil";
            exPendapatanDokter();
       }else if(type.equals("pendapatan")){
            nama="Laporan Pendapatan";
            exLaporanPendapatan();
        }else if(type.equals("periksa")) {
            nama = "Laporan Periksa";
            exPeriksa();
        }
    }

    private void exLaporanKeuangan() throws IOException, WriteException {


        Cursor c = db.sq(ModulKlinik.selectwhere("tbltransaksi")+ModulKlinik.sBetween("tgltransaksi",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulKlinik.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                String tgl = ModulKlinik.getString(c,"tgltransaksi");
                String faktur = ModulKlinik.getString(c,"fakturtran");
                String ket = ModulKlinik.getString(c,"kettransaksi");
                String masuk = ModulKlinik.getString(c,"masuk");
                String keluar = ModulKlinik.getString(c,"keluar");
                String saldo = ModulKlinik.getString(c,"saldo");




                addLabel(sheet,col++, row, ModulKlinik.intToStr(no));
                addLabel(sheet,col++, row, tgl);
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, ket);
                addLabel(sheet,col++, row, ModulKlinik.removeE(masuk));
                addLabel(sheet,col++, row, ModulKlinik.removeE(keluar));
                addLabel(sheet,col++, row, ModulKlinik.removeE(saldo));


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulKlinik.showToast(this,"Berhasil");
        }

    }

     private void exLaporanPendapatan() throws IOException, WriteException {


        Cursor c = db.sq(ModulKlinik.selectwhere("view_detailperiksa")+ModulKlinik.sWhere("flagperiksa","2")+" AND "+
                ModulKlinik.sBetween("tglperiksa",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulKlinik.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                    "Tgl Periksa",
                    "Jasa",
                    "Keterangan",
                    "Pendapatan",
                    "Pasien",
                    "Dokter",

            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;


                String faktur = ModulKlinik.getString(c,"fakturperiksa") ;
                String tglorder = ModulKlinik.getString(c,"tglperiksa") ;

                String jasa = ModulKlinik.getString(c,"jasa") ;
                String keterangan = ModulKlinik.getString(c,"keterangan");
                String hargajual=ModulKlinik.getString(c,"biaya");
                if (!ModulKlinik.getString(c,"iddokter").equals("1")){
                    double bagi = ModulKlinik.strToDouble(ModulKlinik.getString(c,"bagihasil"))/100;
                    hargajual=ModulKlinik.removeE(ModulKlinik.strToDouble(hargajual)*bagi);
                }else{
                    hargajual=ModulKlinik.removeE(hargajual);
                }

                String pasien = ModulKlinik.getString(c,"pasien") ;
                String dokter = ModulKlinik.getString(c,"dokter");


                addLabel(sheet,col++, row, ModulKlinik.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tglorder);
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, keterangan);
                addLabel(sheet,col++, row, hargajual);
                addLabel(sheet,col++, row, pasien);
                addLabel(sheet,col++, row, dokter);


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulKlinik.showToast(this,"Berhasil");
        }else{
            ModulKlinik.showToast(this,"TIdak ada data");
        }

    }

    private void exPendapatanDokter() throws IOException, WriteException {


        Cursor c = db.sq(ModulKlinik.selectwhere("view_detailperiksa") +ModulKlinik.sWhere("flagperiksa","2")+" AND "+
                ModulKlinik.sBetween("tglperiksa",ttglAwal,ttglAkhir)+ModulKlinik.sOrderASC("dokter")) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulKlinik.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,8);
            excelNextLine(sheet,2) ;

            String[] judul = {  "No.",
                    "Dokter",
                    "Pendapatan",
                    "Faktur",
                    "Tanggal Periksa",
                    "Pasien",
                    "Jasa",
                    "Keterangan"
            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String dokter = ModulKlinik.getString(c,"dokter");
                String hargajual=ModulKlinik.getString(c,"biaya");
                if (!ModulKlinik.getString(c,"iddokter").equals("1")){
                    double bagi =100- ModulKlinik.strToDouble(ModulKlinik.getString(c,"bagihasil"));
                    bagi=bagi/100;
                    hargajual=ModulKlinik.removeE(ModulKlinik.strToDouble(hargajual)*bagi);
                }else{
                    hargajual=ModulKlinik.removeE(hargajual);
                }

                String faktur = ModulKlinik.getString(c,"fakturperiksa") ;
                String tglorder = ModulKlinik.getString(c,"tglperiksa") ;
                String pelanggan = ModulKlinik.getString(c,"pasien") ;
                String barang = ModulKlinik.getString(c,"jasa") ;
                String keterangan = ModulKlinik.getString(c,"keterangan");




                addLabel(sheet,col++, row, ModulKlinik.intToStr(no));
                addLabel(sheet,col++, row, dokter);
                addLabel(sheet,col++, row, hargajual);
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tglorder);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, barang);
                addLabel(sheet,col++, row, keterangan);


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulKlinik.showToast(this,"Berhasil");
        }else{
            ModulKlinik.showToast(this,"TIdak ada data");
        }

    }

    private void exJasa() throws IOException, WriteException {


        Cursor c = db.sq(ModulKlinik.select("tbljasa") ) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulKlinik.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,4);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Jasa",
                    "Biaya",
                    "Bagi Hasil"};
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String jasa = ModulKlinik.getString(c,"jasa");
                String biaya = ModulKlinik.getString(c,"harga");
                String bagi = ModulKlinik.getString(c,"bagihasil")+ "%";


                addLabel(sheet,col++, row, ModulKlinik.intToStr(no));
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, ModulKlinik.removeE(biaya));
                addLabel(sheet,col++, row, bagi);


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulKlinik.showToast(this,"Berhasil");
        }else{
            ModulKlinik.showToast(this,"Tidak ada data");
        }
    }





    private void exPeriksa() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM view_detailperiksa where flagperiksa=2  AND "+
                ModulKlinik.sBetween("tglperiksa",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulKlinik.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,10);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Tanggal Periksa",
                    "Jasa",
                    "Biaya",
                    "Keterangan",
                    "Pasien",
                    "Umur Periksa",
                    "Dokter",
                    "Metode Pembayaran"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulKlinik.getString(c,"fakturperiksa") ;
                String tgljual = ModulKlinik.getString(c,"tglperiksa") ;
                String jasa = ModulKlinik.getString(c,"jasa");
                String biaya = ModulKlinik.getString(c,"biaya") ;
                String keterangan = ModulKlinik.getString(c,"keterangan") ;
                String pasien = ModulKlinik.getString(c,"pasien") ;
                String umur = ModulKlinik.getString(c,"umurperiksa") ;
                String dokter = ModulKlinik.getString(c,"dokter");
                String status = "Tunai";

                addLabel(sheet,col++, row, ModulKlinik.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tgljual);
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, ModulKlinik.removeE(biaya));
                addLabel(sheet,col++, row, keterangan);
                addLabel(sheet,col++, row, pasien);
                addLabel(sheet,col++, row, umur);
                addLabel(sheet,col++, row, dokter);
                addLabel(sheet,col++, row, status);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulKlinik.showToast(this,"Berhasil");
        }else{
            ModulKlinik.showToast(this,"Tidak ada data");
        }
    }


    public void exPasien() throws IOException, WriteException {


        Cursor c = db.sq(ModulKlinik.selectwhere("tblpasien")+" idpasien!=1");
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulKlinik.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,9);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Pasien",
                    "Jenis Kelamin",
                    "Umur",
                    "Alamat",
                    "Nomor Telp",
                    "Golongan Darah",
                    "NIK",
                    "No BPJS"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulKlinik.getString(c,"pasien") ;
                String jk = ModulKlinik.getString(c,"jk").equals("L") ? "Laki-laki":"Perempuan";
                String umur = ModulKlinik.getUmur(ModulKlinik.getString(c,"umur"));
                String alamat = ModulKlinik.getString(c,"alamat") ;
                String telp = ModulKlinik.getString(c,"notelp") ;
                String  goldar = ModulKlinik.getString(c,"goldarah");
                String nik = ModulKlinik.getString(c,"nik");
                String bpjs = ModulKlinik.getString(c,"nobpjs");

                addLabel(sheet,col++, row, ModulKlinik.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, jk);
                addLabel(sheet,col++, row, umur);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                addLabel(sheet,col++, row, goldar);
                addLabel(sheet,col++, row, nik);
                addLabel(sheet,col++, row, bpjs);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulKlinik.showToast(this,"Berhasil");
        } else {
            ModulKlinik.showToast(this,"Tidak ada data");
        }
    }

    public void exDokter() throws IOException, WriteException {


        Cursor c = db.sq(ModulKlinik.selectwhere("tbldokter")+" iddokter!=1");
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulKlinik.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,4);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Dokter","Alamat","Nomor Telp"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulKlinik.getString(c,"dokter") ;
                String alamat = ModulKlinik.getString(c,"alamatdokter") ;
                String telp = ModulKlinik.getString(c,"nodokter") ;

                addLabel(sheet,col++, row, ModulKlinik.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulKlinik.showToast(this,"Berhasil");
        } else {
            ModulKlinik.showToast(this,"Tidak ada data");
        }
    }



    public void setHeader(DatabaseKlinik db, CSVWriter csvWriter,int JumlahKolom){
        try {
            Cursor c = db.sq(ModulKlinik.select("tbltoko")) ;
            c.moveToNext() ;

            setCenter(csvWriter,JumlahKolom,ModulKlinik.getString(c,"namatoko"));
            setCenter(csvWriter,JumlahKolom,ModulKlinik.getString(c,"alamattoko"));
            setCenter(csvWriter,JumlahKolom,ModulKlinik.getString(c,"notoko"));
            FExcel_Klinik.csvNextLine(csvWriter,2) ;
        }catch (Exception e){

        }
    }

    public void setHeader(DatabaseKlinik db, WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.sq(ModulKlinik.select("tbltoko")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,ModulKlinik.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++,ModulKlinik.getString(c,"alamattoko"),JumlahKolom);
            addLabel(sheet, row++,ModulKlinik.getString(c,"notoko"),JumlahKolom);
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
            ttglAwal = ModulKlinik.setDatePickerNormal(thn,bln+1,day);


            ModulKlinik.setText(v, R.id.tglAwal, ModulKlinik.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {


            ttglAkhir = ModulKlinik.setDatePickerNormal(thn,bln+1,day);

            ModulKlinik.setText(v, R.id.tglAkhir, ModulKlinik.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    void setTanggal(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String now = ModulKlinik.setDatePickerNormal(year,month+1,day) ;
        ModulKlinik.setText(v,R.id.tglAwal,now) ;
        ModulKlinik.setText(v,R.id.tglAkhir,now) ;
        ttglAwal = ModulKlinik.setDatePickerNormal(year,month+1,day);
        ttglAkhir = ModulKlinik.setDatePickerNormal(year,month+1,day);
        ConstraintLayout tanggal = findViewById(R.id.cTanggal);
//        if(type.equals("pasien")){
            tanggal.setVisibility(View.INVISIBLE);
//        } else if (type.equals("dokter")){
            tanggal.setVisibility(View.INVISIBLE);
//        }else if(type.equals("jasa")){
            tanggal.setVisibility(View.INVISIBLE);
//        }
    }

    public void tglAwal(View view) {
        setDate(1);
    }


    public void tglAkhir(View view) {
        setDate(2);
    }

}
