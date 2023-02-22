package com.itbrain.aplikasitoko.rentalmobil;


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
import android.widget.ImageView;

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


public class MenuExportExcel_Mobil extends AppCompatActivity {
    String Opsi;

    ModulRentalMobil config,temp;
    DatabaseRentalMobil db;
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
        setContentView(R.layout.activity_menu_export_excel_mobil);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        getWindow().setSoftInputMode(3);
        this.config = new ModulRentalMobil(getSharedPreferences("config", 0));
        this.temp = new ModulRentalMobil(getSharedPreferences("temp", 0));
        this.db = new DatabaseRentalMobil(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";


        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());



        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();
            ModulRentalMobil.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulRentalMobil.setText(v,R.id.ePath,"Internal Storage/Download");
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
        ModulRentalMobil.setText(v,R.id.ePath,"Internal Storage/Download/");
    }


    public void export(View view) throws IOException, WriteException {
        if (type.equals("pelanggan")){
            nama="Laporan Pelanggan";
            exPelanggan();
        }else if(type.equals("pegawai")){
            nama="Laporan Pegawai";
            exPegawai();
        }else if (type.equals("kendaraan")){
            nama="Laporan Kendaraan";
            exKendaraan();
        }else if(type.equals("pendapatan")){
            nama="Laporan Pendapatan";
            exLaporanPendapatan();
        }else if(type.equals("rental")) {
            nama = "Laporan Rental";
            exRental();
        }

    }


    private void exLaporanPendapatan() throws IOException, WriteException {


        Cursor c = db.sq(ModulRentalMobil.selectwhere("view_rental")+ModulRentalMobil.sWhere("flagrental","3")+ModulRentalMobil.andBet("tglrental",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulRentalMobil.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                    "Tgl Rental",
                    "Nama Pelanggan",
                    "Total",
                    "Bayar",
                    "Uang Muka",
                    "Kembali",
                    "Status"

            } ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;


                String faktur = ModulRentalMobil.getString(c,"faktur") ;
                String tglorder = ModulRentalMobil.getString(c,"tglrental") ;
                String pelanggan = ModulRentalMobil.getString(c,"pelanggan");
                String total = ModulRentalMobil.getString(c,"total");
                String bayar = ModulRentalMobil.getString(c,"bayar");
                String dp = ModulRentalMobil.getString(c,"dp");
                String kembali = ModulRentalMobil.getString(c,"kembali");
                String flagrental = ModulRentalMobil.strToDouble(ModulRentalMobil.getString(c,"flagrental"))>1 ? "Lunas" : "Belum Lunas";


                addLabel(sheet,col++, row, ModulRentalMobil.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, tglorder);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, ModulRentalMobil.removeE(total));
                addLabel(sheet,col++, row, ModulRentalMobil.removeE(bayar));
                addLabel(sheet,col++, row, ModulRentalMobil.removeE(dp));
                addLabel(sheet,col++, row, ModulRentalMobil.removeE(kembali));
                addLabel(sheet,col++, row, flagrental);



                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulRentalMobil.showToast(this,"Berhasil");
        }else{
            ModulRentalMobil.showToast(this,"Tidak ada data");
        }

    }



    private void exKendaraan() throws IOException, WriteException {


        Cursor c = db.sq(ModulRentalMobil.select("view_kendaraan") ) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulRentalMobil.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,6);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Mobil",
                    "Merk",
                    "Plat",
                    "Tahun Keluaran",
                    "Biaya per Hari"};
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
               String mobil = ModulRentalMobil.getString(c,"mobil");
               String merk = ModulRentalMobil.getString(c,"merk");
               String plat = ModulRentalMobil.getString(c,"plat");
               String tahun = ModulRentalMobil.getString(c,"tahunkeluaran");
               String biaya = ModulRentalMobil.getString(c,"harga");


                addLabel(sheet,col++, row, ModulRentalMobil.intToStr(no));
                addLabel(sheet,col++, row, mobil);
                addLabel(sheet,col++, row, merk);
                addLabel(sheet,col++, row, plat);
                addLabel(sheet,col++, row, tahun);
                addLabel(sheet,col++, row, ModulRentalMobil.removeE(biaya));


                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulRentalMobil.showToast(this,"Berhasil");
        }else{
            ModulRentalMobil.showToast(this,"Tidak Ada Data");
        }
    }





    private void exRental() throws IOException, WriteException {


        Cursor c = db.sq("SELECT * FROM view_rentaldetail WHERE "+ModulRentalMobil.sBetween("tglrental",ttglAwal,ttglAkhir)) ;
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulRentalMobil.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,11);
            excelNextLine(sheet,2) ;

            String[] judul = {"No","Faktur",
                    "Pelanggan",
                    "Mobil",
                    "Plat",
                    "Tahun Keluaran",
                    "Tanggal Mulai",
                    "Tanggal Selesai",
                    "Biaya per hari",
                    "Jumlah hari",
                    "Total"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = ModulRentalMobil.getString(c,"faktur") ;
                String pelanggan = ModulRentalMobil.getString(c,"pelanggan");
                String mobil = ModulRentalMobil.getString(c,"mobil");
                String plat = ModulRentalMobil.getString(c,"plat");
                String tahunkeluaran = ModulRentalMobil.getString(c,"tahunkeluaran");
                String tanggalmulai = ModulRentalMobil.getString(c,"tglmulai");
                String tanggalselesai = ModulRentalMobil.getString(c,"tglselesai");
                String harga = ModulRentalMobil.getString(c,"hargarental");
                String jumlahhari = ModulRentalMobil.getString(c,"jumlahhari");
                double total = ModulRentalMobil.strToDouble(harga)*ModulRentalMobil.strToDouble(jumlahhari);

                addLabel(sheet,col++, row, ModulRentalMobil.intToStr(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, mobil);
                addLabel(sheet,col++, row, plat);
                addLabel(sheet,col++, row, tahunkeluaran);
                addLabel(sheet,col++, row, tanggalmulai);
                addLabel(sheet,col++, row, tanggalselesai);
                addLabel(sheet,col++, row, ModulRentalMobil.removeE(harga));
                addLabel(sheet,col++, row, jumlahhari);
                addLabel(sheet,col++, row, ModulRentalMobil.removeE(total));
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulRentalMobil.showToast(this,"Berhasil");
        }else{
            ModulRentalMobil.showToast(this,"Tidak ada Data");
        }
    }


    public void exPelanggan() throws IOException, WriteException {


        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblpelanggan")+" idpelanggan!=1");
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulRentalMobil.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Pelanggan","Alamat","Nomor Telp","NIK"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulRentalMobil.getString(c,"pelanggan") ;
                String alamat = ModulRentalMobil.getString(c,"alamat") ;
                String telp = ModulRentalMobil.getString(c,"notelp") ;
                String nik = ModulRentalMobil.getString(c,"noktp");


                addLabel(sheet,col++, row, ModulRentalMobil.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                addLabel(sheet,col++, row, nik);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulRentalMobil.showToast(this,"Berhasil");
        } else {
            ModulRentalMobil.showToast(this,"Tidak ada data");
        }
    }

    public void exPegawai() throws IOException, WriteException {


        Cursor c = db.sq(ModulRentalMobil.selectwhere("tblpegawai")+" idpegawai!=1");
        if(c.getCount() > 0){
            File file = new File(path+nama+" "+ModulRentalMobil.getDate("dd-MM-yyyy_HHmmss")+".xls") ;

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.getSheet(0);

            createLabel(sheet);
            setHeader(db,sheet,5);
            excelNextLine(sheet,2) ;

            String[] judul = {"No.", "Nama Pegawai","Alamat","Nomor Telp","NIK"} ;
            setJudul(sheet,judul);
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = ModulRentalMobil.getString(c,"pegawai") ;
                String alamat = ModulRentalMobil.getString(c,"alamatpegawai") ;
                String telp = ModulRentalMobil.getString(c,"nopegawai") ;
                String ktp = ModulRentalMobil.getString(c,"ktppegawai");


                addLabel(sheet,col++, row, ModulRentalMobil.intToStr(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
                addLabel(sheet,col++, row, ktp);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            ModulRentalMobil.showToast(this,"Berhasil");
        } else {
            ModulRentalMobil.showToast(this,"Tidak ada data");
        }
    }



    public void setHeader(DatabaseRentalMobil db, CSVWriter csvWriter,int JumlahKolom){
        try {
            Cursor c = db.sq(ModulRentalMobil.select("tbltoko")) ;
            c.moveToNext() ;

            setCenter(csvWriter,JumlahKolom,ModulRentalMobil.getString(c,"namatoko"));
            setCenter(csvWriter,JumlahKolom,ModulRentalMobil.getString(c,"alamattoko"));
            setCenter(csvWriter,JumlahKolom,ModulRentalMobil.getString(c,"notoko"));
            FExcel_Mobil.csvNextLine(csvWriter,2) ;
        }catch (Exception e){

        }
    }

    public void setHeader(DatabaseRentalMobil db, WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.sq(ModulRentalMobil.select("tbltoko")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,ModulRentalMobil.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++,ModulRentalMobil.getString(c,"alamattoko"),JumlahKolom);
            addLabel(sheet, row++,ModulRentalMobil.getString(c,"notoko"),JumlahKolom);
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
            ttglAwal = ModulRentalMobil.setDatePickerNormal(thn,bln+1,day);


            ModulRentalMobil.setText(v, R.id.tglAwal, ModulRentalMobil.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {


            ttglAkhir = ModulRentalMobil.setDatePickerNormal(thn,bln+1,day);

            ModulRentalMobil.setText(v, R.id.tglAkhir, ModulRentalMobil.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    void setTanggal(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String now = ModulRentalMobil.setDatePickerNormal(year,month+1,day) ;
        ModulRentalMobil.setText(v, R.id.tglAwal,now) ;
        ModulRentalMobil.setText(v,R.id.tglAkhir,now) ;
        ttglAwal = ModulRentalMobil.setDatePickerNormal(year,month+1,day);
        ttglAkhir = ModulRentalMobil.setDatePickerNormal(year,month+1,day);
        ConstraintLayout tanggal = findViewById(R.id.cTanggal);
        if(type.equals("pelanggan")){
            tanggal.setVisibility(View.INVISIBLE);
        } else if (type.equals("pegawai")){
            tanggal.setVisibility(View.INVISIBLE);
        }else if (type.equals("kendaraan")){
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
