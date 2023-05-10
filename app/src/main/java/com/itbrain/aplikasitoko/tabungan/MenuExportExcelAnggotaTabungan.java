package com.itbrain.aplikasitoko.tabungan;

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

public class MenuExportExcelAnggotaTabungan extends AppCompatActivity {
    String tab;
    View v;
    DatabaseTabungan db;
    String path;
    String nama;
    PrefTabungan pref;
    final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    int row = 0;

    private WritableCellFormat times;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBoldUnderline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_export_excel_tabungan);

        ImageButton imageButton = findViewById(R.id.kembaliExportExcelTabungan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tab=getIntent().getStringExtra("type");
//        ModulTabungan.btnBack("Export Excel",getSupportActionBar());
        this.db = new DatabaseTabungan(this);
        this.v = findViewById(android.R.id.content);
        pref = new PrefTabungan(getSharedPreferences("dir",MODE_PRIVATE));
        this.path = pref.getCustom("dirExport",Environment.getExternalStorageDirectory().toString() + "/KomputerKit/Aplikasi Tabungan Plus Keuangan/");
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulTabungan.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulTabungan.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        try {
            new File(path).mkdirs();
        } catch (Exception e) {
            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void export(View view) throws IOException, WriteException {
        nama = getText(R.string.laporan_anggota).toString();
        exAnggota();

    }

    private void exAnggota() throws IOException, WriteException{
        File file = new File(path+nama+" "+ModulTabungan.getDate("dd-MM-yyyy_HHmmss")+".xls") ;
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel(sheet);
        setHeader(db,sheet,5);
        excelNextLine(sheet,2) ;

        String[] judul = {  "No.",
                "Nama Anggota",
                "Alamat",
                "No Telp"
        } ;
        setJudul(sheet,judul);

        Cursor c = db.sq(QueryTabungan.select("tblanggota")) ;
        if(c.getCount() > 0){
            int no = 1 ;
            while(c.moveToNext()){
                int col = 0 ;
                String anggota = ModulTabungan.getString(c,"namaanggota") ;
                String alamat = ModulTabungan.getString(c,"alamatanggota") ;
                String notelp = "0"+ModulTabungan.getString(c,"notelpanggota") ;;

                addLabel(sheet,col++, row, ModulTabungan.intToStr(no));
                addLabel(sheet,col++, row, anggota);
                addLabel(sheet,col++, row, alamat);
                addLabel(sheet,col++, row, notelp);
                row++ ;
                no++ ;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, R.string.berhasil, Toast.LENGTH_SHORT).show();
        }
    }

    public void setHeader(DatabaseTabungan db, CSVWriter csvWriter, int JumlahKolom){
        try {
            Cursor c = db.sq(QueryTabungan.select("tblidentitas")) ;
            c.moveToNext() ;

            setCenter(csvWriter,JumlahKolom,ModulTabungan.getString(c,"namatoko"));
            setCenter(csvWriter,JumlahKolom,ModulTabungan.getString(c,"alamat"));
            setCenter(csvWriter,JumlahKolom,ModulTabungan.getString(c,"notelp"));
            ModulTabunganExcel.csvNextLine(csvWriter,2) ;
        }catch (Exception e){

        }
    }

    public void setHeader(DatabaseTabungan db, WritableSheet sheet, int JumlahKolom){
        try {
            Cursor c = db.sq(QueryTabungan.select("tblidentitas")) ;
            c.moveToNext() ;

            addLabel(sheet, row++,ModulTabungan.getString(c,"namatoko"),JumlahKolom);
            addLabel(sheet, row++,ModulTabungan.getString(c,"alamat"),JumlahKolom);
            addLabel(sheet, row++,ModulTabungan.getString(c,"notelp"),JumlahKolom);
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

//    @Override
//    public void showListItemDialog(String title, String folderPath, SimpleFilePickerDialog.CompositeMode mode, String dialogTag) {
//        SimpleFilePickerDialog.build(folderPath,mode)
//                .title(title)
//                .neut(R.string.kembali)
//                .neg(R.string.buka)
//                .pos(R.string.pilihfolder)
//                .show(this,dialogTag);
//    }
//
//    @Override
//    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
//        switch (dialogTag){
//            case "PICK_FOLDER":
//                if (extras.containsKey(SimpleFilePickerDialog.SELECTED_SINGLE_PATH)){
//                    String selectedSinglePath = extras.getString(SimpleFilePickerDialog.SELECTED_SINGLE_PATH);
//                    if(!selectedSinglePath.isEmpty()){
//                        pref.setCustom("dirExport",selectedSinglePath+"/");
//                        this.path = pref.getCustom("dirExport",Environment.getExternalStorageDirectory().toString() + "/KomputerKit/Aplikasi Tabungan/");
//                        ModulTabungan.setText(v,R.id.ePath,path);
//                    }
//                }
//                break;
//        }
//        return false;
//    }

    public void browse(View view) {

    }
}
