package com.itbrain.aplikasitoko.TokoKredit;

import static com.itbrain.aplikasitoko.TokoKredit.FFunctionKredit.removeE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
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
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ActivityExportExcelKredit extends AppCompatActivity {

    static final String ITEM_EXPORT = "com.itbrain.aplikasitoko.inlimit";
    String deviceid;
    SharedPreferences getPrefs;
    boolean bexport = false;

    String type, path, nama, tglAwal, tglAkhir, Opsi, idAkun, rincian;
    String[] header;
    FConfigKredit config, temp;
    View v;
    FKoneksiKredit db;
    Boolean needDate = true;
    int row = 0;
//    CekInApp cekInApp;
    // private HolderBaru holderBaru;

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat timesBold;
    private WritableCellFormat times;
    private String inputFile;
    private static final String TAG = "Kit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_excel_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        v = this.findViewById(android.R.id.content);
        type = getIntent().getStringExtra("type");
        path = Environment.getExternalStorageDirectory().toString() + "/Download/";
//        cekInApp = new CekInApp(this);

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        deviceid = FFunctionKredit.getDecrypt(getPrefs.getString(FFunctionKredit.getEncrypt("deviceid"), ""));
        bexport = getPrefs.getBoolean("inLimit", false);

        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/kasirsupermudah");
            file.mkdirs();
        } catch (Exception e) {

        }

        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();
            FFunctionKredit.setText(v, R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FFunctionKredit.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
    }

    public void setText() {
        FFunctionKredit.setText(v, R.id.ePath, "Internal Storage/Download/");
    }

//    public void browse(View v) {
//        Intent i = new Intent(this, ActivityExportBrowse.class);
//        i.putExtra("type", type);
//        startActivity(i);
//    }

    public void export(View view) throws IOException, WriteException {
        switch (type) {
            case "pelanggan":
                nama = "Laporan Pelanggan";
                exPelanggan();
                break;
            case "barang":
                nama = "Laporan Barang";
                exBarang();
                break;
            case "laporanpenjualan":
                nama = "Laporan Penjualan";
                exLaporanPenjualan();
                break;
            case "pendapatan":
                nama = "Laporan Pendapatan";
                exLaporanPendapatan();
                break;
            case "return":
                nama = "Laporan Return";
                exReturn();
                break;
            case "labarugi":
                nama = "Laporan Laba Rugi";
                exLabaRugi();
                break;
            case "hutang":
                nama = "Laporan Hutang";
                exHutang();
                break;
            case "kredit":
                nama = "Laporan Kredit";
                exKredit();
            case "keuangan":
                nama = "Laporan Keuangan";
                exKeuangan();
                break;
        }
        row = 0;
    }

    void exKeuangan() throws WriteException, IOException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 4);
        excelNextLine(sheet, 2);

        String[] judul = {"No Transaksi", "Tanggal", "Keterangan", "masuk", "keluar", "saldo"};
        setJudul(sheet, judul);

        Cursor c = db.sq("SELECT * FROM tblkeuangan");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                int col = 0;
                String no_transaksi = FFunctionKredit.getString(c, "no_transaksi");
                String tglkeuangan = FFunctionKredit.getString(c, "tglkeuangan");
                String ket = FFunctionKredit.getString(c, "keterangan");
                String masuk = ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("masuk")));
                String keluar = ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("keluar")));
                String saldo = ModuleKredit.numFormat(c.getDouble(c.getColumnIndex("saldo")));

                addLabel(sheet, col++, row, no_transaksi);
                addLabel(sheet, col++, row, tglkeuangan);
                addLabel(sheet, col++, row, ket);
                addLabel(sheet, col++, row, masuk);
                addLabel(sheet, col++, row, keluar);
                addLabel(sheet, col++, row, saldo);
                row++;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    void exKredit() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 11);
        excelNextLine(sheet, 2);

        String[] judul = {
                "Faktur",
                "Pelanggan",
                "Tgl Kredit",
                "Jumlah Kredit",
                "Lama Kredit",
                "Angsuran",
                "Denda Terlambat",
                "Tgl Jatuh Tempo",
                "Saldo Kredit",
                "Status"
        };
        setJudul(sheet, judul);

        Cursor c = db.sq("SELECT * FROM qkredit");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                int col = 0;
                String faktur = FFunctionKredit.getString(c, "faktur");
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String tglkredit = FFunctionKredit.getString(c, "tglkredit");
                String jumlahkredit = ModuleKredit.numFormat(FFunctionKredit.getDouble(c, "angsuran") * FFunctionKredit.getDouble(c, "lamakredit"));
                String lamakredit = FFunctionKredit.getString(c, "lamakredit");
                String angsuran = ModuleKredit.numFormat(FFunctionKredit.getDouble(c, "angsuran"));
                String denda = ModuleKredit.numFormat(FFunctionKredit.getDouble(c, "denda"));
                String tgljatuhtempo = FFunctionKredit.getString(c, "tgljatuhtempo");
                String saldokredit = ModuleKredit.numFormat(FFunctionKredit.getDouble(c, "saldokredit"));
                String status = FFunctionKredit.getInt(c, "flag_kredit") == 1 ? "Lunas" : "Belum Lunas";

                String[] cols = new String[]{faktur, pelanggan, tglkredit, jumlahkredit, lamakredit, angsuran, denda, tgljatuhtempo, saldokredit, status};
                for (String column : cols) {
                    addLabel(sheet, col++, row, column);
                }
                row++;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void exHutang() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 7);
        excelNextLine(sheet, 2);

        String[] judul = {"No.", "Faktur", "Pelanggan", "Tanggal Bayar", "Total Belanja", "Jumlah Bayar", "Jumlah Hutang"};
        setJudul(sheet, judul);

        Cursor c = db.sq(FQueryKredit.selectwhere("qbayar") + FQueryKredit.sWhere("flagbayar", "0"));
        if (c.getCount() > 0) {
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String fakturbayar = FFunctionKredit.getString(c, "fakturbayar");
                String tglbayar = FFunctionKredit.getString(c, "tglbayar");
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String jumlahbayar = FFunctionKredit.getString(c, "jumlahbayar");
                String bayar = FFunctionKredit.getString(c, "bayar");
                String kembali = FFunctionKredit.getString(c, "kembali");

                addLabel(sheet, col++, row, FFunctionKredit.intToStr(no));
                addLabel(sheet, col++, row, fakturbayar);
                addLabel(sheet, col++, row, pelanggan);
                addLabel(sheet, col++, row, tglbayar);
                addLabel(sheet, col++, row, FFunctionKredit.removeE(jumlahbayar));
                addLabel(sheet, col++, row, FFunctionKredit.removeE(bayar));
                addLabel(sheet, col++, row, FFunctionKredit.removeE(kembali));
                row++;
                no++;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak ada Data", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLabaRugi() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 6);
        excelNextLine(sheet, 2);

        String[] judul = {"No.", "Faktur", "Barang", "Jumlah Barang", "Jumlah Laba Rugi per Barang", "Jumlah Total Laba Rugi"};
        setJudul(sheet, judul);

        Cursor c = db.sq(FQueryKredit.select("qpenjualan"));
        if (c.getCount() > 0) {
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String barang = FFunctionKredit.getString(c, "barang");
                String jumlahjual = FFunctionKredit.getString(c, "jumlahjual");
                String labarugi = FFunctionKredit.getString(c, "labarugi");
                double total = FFunctionKredit.strToDouble(jumlahjual) * FFunctionKredit.strToDouble(labarugi);

                addLabel(sheet, col++, row, FFunctionKredit.intToStr(no));
                addLabel(sheet, col++, row, faktur);
                addLabel(sheet, col++, row, barang);
                addLabel(sheet, col++, row, jumlahjual);
                addLabel(sheet, col++, row, FFunctionKredit.removeE(labarugi));
                addLabel(sheet, col++, row, FFunctionKredit.removeE(total));
                row++;
                no++;
            }
        }
        workbook.write();
        workbook.close();
        Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
    }

    private void exReturn() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 5);
        excelNextLine(sheet, 2);

        String[] judul = {"No.", "Faktur", "Tanggal Return", "Barang", "Jumlah Return"};
        setJudul(sheet, judul);

        Cursor c = db.sq(FQueryKredit.select("qreturn"));
        if (c.getCount() > 0) {
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String barang = FFunctionKredit.getString(c, "barang");
                String fakturbayar = FFunctionKredit.getString(c, "fakturbayar");
                String tglreturn = FFunctionKredit.getString(c, "tglreturn");
                String jumlah = FFunctionKredit.getString(c, "jumlah");

                addLabel(sheet, col++, row, FFunctionKredit.intToStr(no));
                addLabel(sheet, col++, row, fakturbayar);
                addLabel(sheet, col++, row, tglreturn);
                addLabel(sheet, col++, row, barang);
                addLabel(sheet, col++, row, jumlah);
                row++;
                no++;
            }
        }
        workbook.write();
        workbook.close();
        Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
    }

    private void exLaporanPendapatan() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 8);
        excelNextLine(sheet, 2);

        String[] judul = {"No.",
                "Faktur",
                "Tanggal Bayar",
                "Total Belanja",
                "Jumlah Bayar",
                "Kembali",
                "Nama Pelanggan",
                "Metode Pembayaran"};
        setJudul(sheet, judul);

        Cursor c = db.sq(FQueryKredit.select("qbayar"));
        if (c.getCount() > 0) {
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String tnggalbayar = FFunctionKredit.getString(c, "tglbayar");
                String totalbayar = FFunctionKredit.getString(c, "jumlahbayar");
                String jumlahbayar = FFunctionKredit.getString(c, "bayar");
                String kembali = FFunctionKredit.getString(c, "kembali");
                String metode = FFunctionKredit.getString(c, "flagbayar");

                if (metode.equals("1")) {
                    metode = "Tunai";
                } else {
                    kembali = "-" + FFunctionKredit.removeE(kembali);
                    metode = "Hutang";
                }

                addLabel(sheet, col++, row, FFunctionKredit.intToStr(no));
                addLabel(sheet, col++, row, faktur);
                addLabel(sheet, col++, row, tnggalbayar);
                addLabel(sheet, col++, row, FFunctionKredit.removeE(totalbayar));
                addLabel(sheet, col++, row, FFunctionKredit.removeE(jumlahbayar));
                addLabel(sheet, col++, row, kembali);
                addLabel(sheet, col++, row, pelanggan);
                addLabel(sheet, col++, row, metode);
                row++;
                no++;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        }

    }

    private void exLaporanPenjualan() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 11);
        excelNextLine(sheet, 2);

        String[] judul = {"No", "Faktur",
                "Tanggal Jual",
                "Harga Jual",
                "Jumlah Jual",
                "Barang",
                "Total",
                "Laba Rugi per Barang",
                "Total Laba Rugi",
                "Nama Pelanggan",
                "Keterangan"};
        setJudul(sheet, judul);

        Cursor c = db.sq(FQueryKredit.select("qpenjualan"));
        if (c.getCount() > 0) {
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String tgljual = FFunctionKredit.getString(c, "tgljual");
                String hargajual = FFunctionKredit.getString(c, "hargajual:1");
                String jumlahjual = FFunctionKredit.getString(c, "jumlahjual");
                String barang = FFunctionKredit.getString(c, "barang");
                String labarugi = FFunctionKredit.getString(c, "labarugi");
                String total = FFunctionKredit.intToStr(FFunctionKredit.strToInt(hargajual) * FFunctionKredit.strToInt(jumlahjual));
                String totallabarugi = FFunctionKredit.intToStr(FFunctionKredit.strToInt(labarugi) * FFunctionKredit.strToInt(jumlahjual));
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String keterangan = FFunctionKredit.getString(c, "ketpenjualan");

                addLabel(sheet, col++, row, FFunctionKredit.intToStr(no));
                addLabel(sheet, col++, row, faktur);
                addLabel(sheet, col++, row, tgljual);
                addLabel(sheet, col++, row, FFunctionKredit.removeE(hargajual));
                addLabel(sheet, col++, row, jumlahjual);
                addLabel(sheet, col++, row, barang);
                addLabel(sheet, col++, row, FFunctionKredit.removeE(total));
                addLabel(sheet, col++, row, FFunctionKredit.removeE(labarugi));
                addLabel(sheet, col++, row, FFunctionKredit.removeE(totallabarugi));
                addLabel(sheet, col++, row, pelanggan);
                addLabel(sheet, col++, row, keterangan);
                row++;
                no++;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        }
    }

    public void exPelanggan() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        setHeader(sheet, 4);
        excelNextLine(sheet, 2);

        String[] judul = {"No.", "Nama Pelanggan", "Alamat", "Nomor Telp"};
        setJudul(sheet, judul);

        Cursor c = db.sq(FQueryKredit.select("tblpelanggan") + " where idpelanggan <> 1");
        if (c.getCount() > 0) {
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String nama = FFunctionKredit.getString(c, "pelanggan");
                String alamat = FFunctionKredit.getString(c, "alamat");
                String telp = FFunctionKredit.getString(c, "telp");

                addLabel(sheet, col++, row, FFunctionKredit.intToStr(no));
                addLabel(sheet, col++, row, nama);
                addLabel(sheet, col++, row, alamat);
                addLabel(sheet, col++, row, telp);
                row++;
                no++;
            }
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void exBarang() throws IOException, WriteException {
        File file = new File(path + nama + " " + FFunctionKredit.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        createLabel();
        String[] judul = {"No.", "Barang", "Kategori", "Harga Beli", "Harga Jual", "Stok", "Konsinyasi"};
        setHeader(sheet, 7);
        excelNextLine(sheet, 2);
        setJudul(sheet, judul);

        Cursor c = db.sq(FQueryKredit.select("qbarang"));
        if (c.getCount() > 0) {
            int no = 1;
            while (c.moveToNext()) {
                int col = 0;
                String barang = FFunctionKredit.getString(c, "barang");
                String kategori = FFunctionKredit.getString(c, "kategori");
                String hargabeli = FFunctionKredit.getString(c, "hargabeli");
                String hargajual = FFunctionKredit.getString(c, "hargajual");
                String stok = FFunctionKredit.getString(c, "stok");
                String titipan = FFunctionKredit.getString(c, "titipan");
                if (titipan.equals("0")) {
                    titipan = "Kulakan";
                } else {
                    titipan = "Titipan";
                }
                addLabel(sheet, col++, row, FFunctionKredit.intToStr(no));
                addLabel(sheet, col++, row, barang);
                addLabel(sheet, col++, row, kategori);
                addLabel(sheet, col++, row, FFunctionKredit.removeE(hargabeli));
                addLabel(sheet, col++, row, FFunctionKredit.removeE(hargajual));
                addLabel(sheet, col++, row, stok);
                addLabel(sheet, col++, row, titipan);
                row++;
                no++;
            }
        }
        workbook.write();
        workbook.close();
        Toast.makeText(this, "Export Excel Berhasil", Toast.LENGTH_SHORT).show();
    }

    public void setHeader(FKoneksiKredit db, CSVWriter csvWriter, int JumlahKolom) {
        try {
            Cursor c = db.sq(FQueryKredit.select("tblidentitas"));
            c.moveToNext();

            setCenter(csvWriter, JumlahKolom, FFunctionKredit.getString(c, "nama"));
            setCenter(csvWriter, JumlahKolom, FFunctionKredit.getString(c, "alamat"));
            setCenter(csvWriter, JumlahKolom, FFunctionKredit.getString(c, "telp"));
            FExcelKredit.csvNextLine(csvWriter, 2);
        } catch (Exception e) {

        }
    }

    public void setHeader(WritableSheet sheet, int JumlahKolom) {
        try {
            Cursor c = db.sq(FQueryKredit.select("tblidentitas"));
            c.moveToNext();

            addLabel(sheet, row++, FFunctionKredit.getString(c, "nama"), JumlahKolom);
            addLabel(sheet, row++, FFunctionKredit.getString(c, "alamat"), JumlahKolom);
            addLabel(sheet, row++, FFunctionKredit.getString(c, "telp"), JumlahKolom);
        } catch (Exception e) {

        }
    }

    public void setCenter(CSVWriter csvWriter, int JumlahKolom, String value) {
        try {
            int baru;
            if (JumlahKolom % 2 == 1) {
                baru = JumlahKolom - 1;
            } else {
                baru = JumlahKolom;
            }
            int i;
            String[] a = new String[baru];
            for (i = 0; i < baru / 2; i++) {
                a[i] = "";
            }
            a[i] = value;
            csvWriter.writeNext(a);
        } catch (Exception e) {

        }
    }

    private void createContent(WritableSheet sheet) throws WriteException {
        int startSum = row + 1;
        // Write a few number
        for (int i = 1; i < 10; i++) {
            // First column
            addNumber(sheet, 0, row, i + 10);
            // Second column
            addNumber(sheet, 1, row++, i * i);
        }

        int endSum = row;
        // Lets calculate the sum of it
        StringBuffer buf = new StringBuffer();
        buf.append("SUM(A" + startSum + ":A" + endSum + ")");
        Formula f = new Formula(0, row, buf.toString());
        sheet.addCell(f);
        buf = new StringBuffer();
        buf.append("SUM(B" + startSum + ":B" + endSum + ")");
        f = new Formula(1, row, buf.toString());
        sheet.addCell(f);
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        Label label;
        label = new Label(column, row, s, timesBold);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           Integer integer) throws WriteException {
        Number number;
        number = new Number(column, row, integer, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    private void addLabel(WritableSheet sheet, int row, String s, int JumlahKolom)
            throws WriteException {
        Label label;
        JumlahKolom--;
        WritableCellFormat newFormat = null;
        newFormat = new WritableCellFormat(timesBold);
        newFormat.setAlignment(Alignment.CENTRE);
        label = new Label(0, row, s, newFormat);
        sheet.addCell(label);
        sheet.mergeCells(0, row, JumlahKolom, row); // parameters -> col1,row1,col2,row2
    }

    private void createLabel() throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.SINGLE);
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

    public Boolean excelNextLine(WritableSheet sheet, int total) {
        try {
            for (int i = 0; i < total; i++) {
                addLabel(sheet, 0, row++, "");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setJudul(WritableSheet sheet, String[] val) throws WriteException {
        int col = 0;
        for (int i = 0; i < val.length; i++) {
            addCaption(sheet, col++, row, val[i]);
        }
        row++;
    }

}
