package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

public class ExportExcel_Kwitansi extends AppCompatActivity {
    ConfigKwitansi temp;
    DatabaseCetakKwitansi db;
    String nama;
    String path;
    EditText ePath;

    int row = 0;

    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;
    String type;

    SharedPreferences pref;
    SharedPreferences.Editor editPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exportexcel_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pref=getSharedPreferences("dir",MODE_PRIVATE);
        editPref=pref.edit();
        ePath= findViewById(R.id.ePath);

        this.temp = new ConfigKwitansi(getSharedPreferences("temp", 0));
        this.db = new DatabaseCetakKwitansi(this);
        this.type = getIntent().getStringExtra("type");
        this.path = Environment.getExternalStorageDirectory().toString() + "/EasyPrintReceipt/";

        try {
            new File(Environment.getExternalStorageDirectory() + "/EasyPrintReceipt/").mkdirs();
        } catch (Exception e) {
        }
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();

            ePath.setText("Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ePath.setText("Internal Storage/Download");
            //only api 21 down
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void export(View view) throws IOException, WriteException {
        if (type.equals("pelanggan")){
            nama="Laporan Pelanggan Kwitansi";
            exPelanggan();
        }else if(type.equals("jasa")){
            nama="Laporan Jasa Kwitansi";
            exJasa();
        }else if (type.equals("transaksi")) {
            nama = "Laporan Transaksi kwitansi";
            exLaporanTransaksi();
        }
    }

    private void exLaporanTransaksi() throws IOException, WriteException {
        File file = new File(path+nama+" "+getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        setHeader(db,sheet,9);
        excelNextLine(sheet,2) ;

        String[] judul = {
                "No.",
                "Faktur",
                "Tanggal Transaksi",
                "Jasa",
                "Jumlah",
                "Harga",
                "Total",
                "Keterangan",
                "Nama Pelanggan"};
        setJudul(sheet,judul);

        Cursor c = db.sq("SELECT * FROM vtransaksi WHERE status>0") ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String pelanggan = c.getString(c.getColumnIndex("penerima"));
                String jasatransaksi = c.getString(c.getColumnIndex("jasatransaksi"));
                String faktur = c.getString(c.getColumnIndex("faktur"));
                String jumlah = c.getString(c.getColumnIndex("jumlah"));
                String harga = c.getString(c.getColumnIndex("harga"));
                double total = Double.parseDouble(harga)*Double.parseDouble(jumlah) ;
                String tgl = c.getString(c.getColumnIndex("tgltransaksi"));
                String keter = c.getString(c.getColumnIndex("keterangan"));
                String ket;
                if (keter.equals("")){
                    ket = " -";
                } else {
                    ket = keter;
                }

                addLabel(sheet,col++, row, String.valueOf(no));
                addLabel(sheet,col++, row, faktur);
                addLabel(sheet,col++, row, LaporanTransaksi_Kwitansi.dateToNormal(tgl));
                addLabel(sheet,col++, row, jasatransaksi);
                addLabel(sheet,col++, row, jumlah);
                addLabel(sheet,col++, row, CariJasa_Kwitansi.removeE(harga));
                addLabel(sheet,col++, row, CariPelanggan_Kwitansi.removeE(String.valueOf(total)));
                addLabel(sheet,col++, row, ket);
                addLabel(sheet,col++, row, pelanggan);

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

    public void exJasa() throws IOException, WriteException {
        File file = new File(path+nama+" "+getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        String[] judul = {"No.", "Nama Jasa"};
        setHeader(db,sheet,2);
        excelNextLine(sheet,2) ;
        setJudul(sheet,judul);

        Cursor c = db.sq("SELECT * FROM tbljasa") ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String jasa = c.getString(c.getColumnIndex("jasa"));

                addLabel(sheet,col++, row, String.valueOf(no));
                addLabel(sheet,col++, row, jasa);



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
        File file = new File(path+nama+" "+getDate("dd-MM-yyyy_HHmmss")+".xls") ;

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

        Cursor c = db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan>0");
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String nama = c.getString(c.getColumnIndex("pelanggan"));
                String alamat = c.getString(c.getColumnIndex("alamat"));
                String telp = c.getString(c.getColumnIndex("notelp"));

                addLabel(sheet,col++, row, String.valueOf(no));
                addLabel(sheet,col++, row, nama);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, telp);
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

    public void setHeader(DatabaseCetakKwitansi db, WritableSheet sheet,int JumlahKolom){
        try {
            Cursor c = db.sq("SELECT * FROM tblidentitas") ;
            c.moveToNext() ;

            addLabel(sheet, row++,c.getString(c.getColumnIndex("nama")),JumlahKolom);
            addLabel(sheet, row++,c.getString(c.getColumnIndex("alamatid")),JumlahKolom);
            addLabel(sheet, row++,c.getString(c.getColumnIndex("cap2")),JumlahKolom);
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

    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }

    public void browse(View view) {
        //        showListItemDialog("Pilih Direktori",Environment.getExternalStorageDirectory().getAbsolutePath()+"/", SimpleFilePickerDialog.CompositeMode.FOLDER_ONLY_DIRECT_CHOICE_SELECTION, "PICK_FOLDER");
    }
}