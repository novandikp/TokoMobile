package com.itbrain.aplikasitoko.TokoKain;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.IOException;
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

public class ActivityExportExcel_Toko_Kain extends AppCompatActivity {
    

    String tab,tgl;
    View v;
    DatabaseTokoKain db;
    String path;
    String nama;
    SharedPreferences getPrefs;
    SharedPreferences.Editor editor;
    int row = 0;

    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_excel_kain);

        ImageButton imageButton = findViewById(R.id.kembalitod2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            tab=getIntent().getStringExtra("tab");
            tgl=getIntent().getStringExtra("tgl");
//            KumFunTokoKain.btnBack("Export Excel",getSupportActionBar());
            this.db = new DatabaseTokoKain(this);
            this.v = findViewById(android.R.id.content);
            this.getPrefs = getSharedPreferences("dir",MODE_PRIVATE);
            this.path = getPrefs.getString("dirExport",getExternalFilesDir(null).getAbsolutePath()+"/" + "/Laporan/");
            KumFunTokoKain.setText(v,R.id.ePath,path);
            editor=getPrefs.edit();

            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        if (tab.equals("kain")){
            nama="Laporan Kain";
            exKain();
        }else if (tab.equals("pelanggan")){
            nama="Laporan Pelanggan";
            exPelanggan();
        }else if (tab.equals("order")){
            nama="Laporan Order";
            exLaundry();
        }else if(tab.equals("pendapatan")){
            nama="Laporan Pendapatan";
            exPendapatan();
        }
    }

    private void exKain() throws IOException, WriteException {
        File file = new File(path+nama+" "+KumFunTokoKain.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                "Nama Kain",
                "Biaya per Meter"
        } ;
        setJudul(sheet,judul);

        Cursor c = db.sq(FQueryTokoKain.select("qkain")) ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String kategori = KumFunTokoKain.getString(c,"kategori") ;
                String kain = KumFunTokoKain.getString(c,"kain") ;
                String biaya = KumFunTokoKain.getString(c,"biaya") ;

                addLabel(sheet,col++, row, KumFunTokoKain.intToStr(no));
                addLabel(sheet,col++, row, kategori);
                addLabel(sheet,col++, row, kain);
                addLabel(sheet,col++, row, biaya);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        }
    }
    private void exPelanggan() throws IOException, WriteException {
        File file = new File(path+nama+" "+KumFunTokoKain.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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

        Cursor c = db.sq(FQueryTokoKain.select("tblpelanggan")) ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String pelanggan = KumFunTokoKain.getString(c,"namapelanggan") ;
                String alamat = KumFunTokoKain.getString(c,"alamatpelanggan") ;
                String notelp = KumFunTokoKain.getString(c,"telppelanggan") ;

                addLabel(sheet,col++, row, KumFunTokoKain.intToStr(no));
                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, notelp);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        }
    }
    private void exLaundry() throws IOException, WriteException {
        File file = new File(path+nama+" "+KumFunTokoKain.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
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
                "Tanggal Order",
                "Kategori",
                "Kain",
                "Biaya per Meter",
                "Panjang",
                "Lebar",
                "Jumlah",
                "Total per Faktur"
        } ;
        setJudul(sheet,judul);

        String[] tanggal=tgl.split("__");
        Cursor c = db.sq(FQueryTokoKain.selectwhere("qorder")+"("+FQueryTokoKain.sBetween("tglorder",tanggal[0],tanggal[1])+")") ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String faktur = KumFunTokoKain.getString(c,"faktur") ;
                String pelanggan = KumFunTokoKain.getString(c,"namapelanggan") ;
                String tglpesan = KumFunTokoKain.getString(c,"tglorder") ;
                String kategori = KumFunTokoKain.getString(c,"kategori") ;
                String kain = KumFunTokoKain.getString(c,"kain") ;
                String biaya = KumFunTokoKain.getString(c,"biaya");
                String panjang = KumFunTokoKain.getString(c,"panjang") ;
                String lebar = KumFunTokoKain.getString(c,"lebar") ;
                String jumlah = KumFunTokoKain.getString(c,"jumlah") ;
                String total = KumFunTokoKain.getString(c,"hargaakhir") ;

                addLabel(sheet,col++, row, KumFunTokoKain.intToStr(no));
                addLabel(sheet,col++, row, faktur);

                addLabel(sheet,col++, row, pelanggan);
                addLabel(sheet,col++, row, KumFunTokoKain.dateToNormal(tglpesan));
                addLabel(sheet,col++, row, kategori);
                addLabel(sheet,col++, row, kain);
                addLabel(sheet,col++, row, biaya);
                addLabel(sheet,col++, row, panjang);
                addLabel(sheet,col++, row, lebar);
                addLabel(sheet,col++, row, jumlah);
                addLabel(sheet,col++, row, total);

                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        }
    }
    private void exPendapatan() throws IOException, WriteException {
        File file = new File(path+nama+" "+KumFunTokoKain.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);
        createLabel(sheet);
        setHeader(db,sheet,5);
        excelNextLine(sheet,2) ;
        String[] judul = {  "No.",
                "Tanggal",
                "Pelanggan",
                "Total",
                "Bayar",
                "Kembali"

        } ;
        setJudul(sheet,judul);

        String[] tanggal=tgl.split("__");
        Cursor cTunai = db.sq(FQueryTokoKain.selectwhere("qbayar")+"("+FQueryTokoKain.sBetween("tglbayar",tanggal[0],tanggal[1])+")") ;
        if(cTunai.getCount()>0){
            int no = 1 ;
            while(cTunai.moveToNext()){
                int col = 0 ;
                String tglbayar = KumFunTokoKain.getString(cTunai,"tglbayar") ;
                String pelanggan = KumFunTokoKain.getString(cTunai,"namapelanggan") ;
                String total = KumFunTokoKain.getString(cTunai,"total") ;
                String bayar = KumFunTokoKain.getString(cTunai,"bayar") ;
                String kembali = KumFunTokoKain.getString(cTunai,"kembali") ;

                addLabel(sheet,col++, row, KumFunTokoKain.intToStr(no));
                addLabel(sheet,col++, row, KumFunTokoKain.dateToNormal(tglbayar));
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
        }
    }


    public void setHeader(DatabaseTokoKain db, WritableSheet sheet, int JumlahKolom){
        try {
            Cursor c = db.sq(FQueryTokoKain.select("tblidentitas")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,KumFunTokoKain.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++,KumFunTokoKain.getString(c,"alamattoko"),JumlahKolom);
            addLabel(sheet, row++,KumFunTokoKain.getString(c,"notelptoko"),JumlahKolom);
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


    public void browse(View view) {
    }
}

