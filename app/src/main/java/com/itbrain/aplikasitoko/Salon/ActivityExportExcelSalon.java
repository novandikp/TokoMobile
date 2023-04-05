package com.itbrain.aplikasitoko.Salon;

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

public class ActivityExportExcelSalon extends AppCompatActivity {

    Toolbar appbar;

    ConfigSalon config, temp;
    DatabaseSalon db;
    SharedPreferences getPrefs;
    String nama;
    String path;
    Calendar calendar;
    int year, month, day;
    String ttglAwal, ttglAkhir;

    int row = 0;

    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;
    String type;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_excel_salon);

        getWindow().setSoftInputMode(3);
        this.config = new ConfigSalon(getSharedPreferences("config", 0));
        this.temp = new ConfigSalon(getSharedPreferences("temp", 0));
        this.db = new DatabaseSalon(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        nama = "Export Excel";

        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        try {
            new File(Environment.getExternalStorageDirectory() + "/POSSalon").mkdirs();
        } catch (Exception e) {
        }
        setText();
        setTanggal();
        FunctionSalon.btnBack(nama, getSupportActionBar());
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();
            FunctionSalon.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FunctionSalon.setText(v,R.id.ePath,"Internal Storage/Download");
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

    public void setText(){
    FunctionSalon.setText(v,R.id.ePath,"Internal Storage/Download/");
    }


    public void export(View view) throws IOException, WriteException {
      if (type.equals("jasa")){
            nama="Laporan Jasa Salon";
            exJasa();
        } else if (type.equals("pelanggan")){
            nama="Laporan Pelanggan Salon";
            exPelanggan();
        }else if(type.equals("janji")){
            nama="Laporan Booking Salon";
            exJanji();
        }else if(type.equals("transaksi")){
            nama="Laporan Transaksi";
            exTransaksi();
        }else if(type.equals("pendapatan")){
            nama="Laporan Pendapatan";
            exPendapatan();
        }
    }

    public void exJasa() throws IOException, WriteException {


        Cursor c = db.sq(QuerySalon.select("tbljasa"));
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+FunctionSalon.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,3);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Jasa","Harga Jasa"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String jasa = FunctionSalon.getString(c,"jasa") ;
                String harga = FunctionSalon.getString(c,"harga") ;

                addLabel(sheet,col++, row, FunctionSalon.intToStr(no));
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, FunctionSalon.removeE(harga));
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exPelanggan() throws IOException, WriteException {


        Cursor c = db.sq(QuerySalon.select("tblpelanggan"));
        if (c.getCount() > 0) {
            File file = new File(path + nama + " " + FunctionSalon.getDate("dd-MM-yyyy_HHmmss") + ".xls");

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db, sheet, 4);
            excelNextLine(sheet, 2);

            String[] judul = {"No.", "Nama Pelanggan", "Alamat", "Nomor Telp"};
            setJudul(sheet, judul);
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String nama = FunctionSalon.getString(c, "pelanggan");
                String alamat = FunctionSalon.getString(c, "alamatpel");
                String telp = FunctionSalon.getString(c, "telppel");

                addLabel(sheet, col++, row, FunctionSalon.intToStr(no));
                addLabel(sheet, col++, row, nama);
                addLabel(sheet, col++, row, alamat);
                addLabel(sheet, col++, row, telp);
                row++;
                no++;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exJanji() throws IOException, WriteException {


        Cursor c = db.sq(QuerySalon.selectwhere("qjanji")+QuerySalon.sBetween("tgljanji",ttglAwal,ttglAkhir));
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+FunctionSalon.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,6);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Pelanggan", "Nomor Telepon","Tanggal Booking","Jam Booking","Status Kedatangan"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = FunctionSalon.getString(c,"pelanggan") ;
                String notelp = FunctionSalon.getString(c,"telppel") ;
                String tanggal = FunctionSalon.getString(c,"tgljanji") ;
                String jam = FunctionSalon.getString(c,"jamjanji") ;
                String status = FunctionSalon.getString(c,"status") ;

                String stat;
                if (status.equals("0")){
                    stat = "Belum Datang";
                } else {
                    stat = "Sudah Datang";
                }

                addLabel(sheet,col++, row, FunctionSalon.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, notelp);
                addLabel(sheet,col++, row, FunctionSalon.dateToNormal(tanggal));
                addLabel(sheet,col++, row, FunctionSalon.timeToNormal(jam));
                addLabel(sheet,col++, row, stat);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void exTransaksi() throws IOException, WriteException {


        Cursor c = db.sq(QuerySalon.selectwhere("qorderdetail")+QuerySalon.sBetween("tglorder",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+FunctionSalon.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,10);
            excelNextLine(sheet,2) ;

            String[] judul = {
                    "No.",
                    "Faktur",
                    "Tanggal Order",
                    "Nama Pelanggan",
                    "Jasa",
                    "Harga Jual",
                    "Jumlah Jual",
                    "Total Bayar",
                    "Jumlah Bayar",
                    "Kembali"};
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = FunctionSalon.getString(c, "faktur");
                String tglorder = FunctionSalon.getString(c, "tglorder");
                String pelanggan = FunctionSalon.getString(c, "pelanggan");
                String jasa = FunctionSalon.getString(c, "jasa");
                String hargajual = FunctionSalon.getString(c, "hargajual");
                String jumlahjual = FunctionSalon.getString(c, "jumlah");
                String totalbayar = FunctionSalon.getString(c, "total");
                String jumlahbayar = FunctionSalon.getString(c, "bayar");
                String kembali = FunctionSalon.getString(c, "kembali");

                addLabel(sheet,col++, row, FunctionSalon.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, FunctionSalon.dateToNormal(tglorder));
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, FunctionSalon.removeE(hargajual));
                addLabel(sheet,col++, row, jumlahjual);
                addLabel(sheet,col++, row, FunctionSalon.removeE(totalbayar));
                addLabel(sheet,col++, row, FunctionSalon.removeE(jumlahbayar));
                addLabel(sheet,col++, row, FunctionSalon.removeE(kembali));

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void exPendapatan() throws IOException, WriteException {


        Cursor c = db.sq(QuerySalon.selectwhere("qorderdetail")+QuerySalon.sBetween("tglorder",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+FunctionSalon.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,8);
            excelNextLine(sheet,2) ;

            String[] judul = {
                    "No.",
                    "Faktur",
                    "Tanggal Order",
                    "Nama Pelanggan",
                    "Nama Jasa",
                    "Total Bayar",
                    "Jumlah Bayar",
                    "Kembali"};
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = FunctionSalon.getString(c, "faktur");
                String tnggalorder = FunctionSalon.getString(c, "tglorder");
                String pelanggan = FunctionSalon.getString(c, "pelanggan");
                String jasa = FunctionSalon.getString(c, "jasa");
                String totalbayar = FunctionSalon.getString(c, "total");
                String jumlahbayar = FunctionSalon.getString(c, "bayar");
                String kembali = FunctionSalon.getString(c, "kembali");

                addLabel(sheet,col++, row, FunctionSalon.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, FunctionSalon.dateToNormal(tnggalorder));
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, jasa);
                addLabel(sheet,col++, row, FunctionSalon.removeE(totalbayar));
                addLabel(sheet,col++, row, FunctionSalon.removeE(jumlahbayar));
                addLabel(sheet,col++, row, FunctionSalon.removeE(kembali));
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void setHeader(DatabaseSalon db, WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.sq(QuerySalon.select("tblidentitas")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,FunctionSalon.getString(c,"nama"),JumlahKolom);
            addLabel(sheet, row++,FunctionSalon.getString(c,"alamat"),JumlahKolom);
            addLabel(sheet, row++,FunctionSalon.getString(c,"telp"),JumlahKolom);
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
            ttglAwal = FunctionSalon.convertDate(FunctionSalon.setDatePickerNormal(thn,bln+1,day));


            FunctionSalon.setText(v, R.id.tglAwal, FunctionSalon.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {


            ttglAkhir = FunctionSalon.convertDate(FunctionSalon.setDatePickerNormal(thn,bln+1,day));

            FunctionSalon.setText(v, R.id.tglAkhir, FunctionSalon.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    void setTanggal(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String now = FunctionSalon.setDatePickerNormal(year,month+1,day) ;
        FunctionSalon.setText(v,R.id.tglAwal,now) ;
        FunctionSalon.setText(v,R.id.tglAkhir,now) ;
        ttglAwal = FunctionSalon.convertDate(FunctionSalon.setDatePickerNormal(year,month+1,day));
        ttglAkhir = FunctionSalon.convertDate(FunctionSalon.setDatePickerNormal(year,month+1,day));
        ConstraintLayout tanggal = findViewById(R.id.cTanggal);
//       if(type.equals("pelanggan")){
            tanggal.setVisibility(View.INVISIBLE);
 //       } else if (type.equals("jasa")){
            tanggal.setVisibility(View.INVISIBLE);
        }

 //  }

    public void tglAwal(View view) {
        setDate(1);
    }


    public void tglAkhir(View view) {
        setDate(2);
    }

}



