package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityPenjualanKredit extends AppCompatActivity implements DialogKredit.ListenerDialogKredit, DatePickerDialog.OnDateSetListener {

    String faktur = "";
    FConfigKredit config, temp;
    FKoneksiKredit db;
    View v;
    int year, month, day;
    Calendar calendar;
    String brg, plgn, tanggal, tanggalTempo, harga, totalbayar = "";
    EditText eTglJatuhTempo, eJumlah;
    RecyclerView recJual;
    List<String> daftarOrderDetail;
    ListPenjualan adapterOrderDetail;
    double rawHarga, rawTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        plgn = temp.getCustom("idpelanggan_kredit", "");
        if (TextUtils.isEmpty(plgn)) {
            temp.setCustom("idpelanggan", "1");
        }

        faktur = temp.getCustom("fakturbayar_kredit", "");
        if (TextUtils.isEmpty(faktur)) {
            getFaktur();
        } else {
            FFunctionKredit.setText(v, R.id.faktur, faktur);
        }

        Button btnCariTgl = findViewById(R.id.cariTanggal);
        btnCariTgl.setOnClickListener(v -> setDate(1));

        Button btnCariTglTempo = findViewById(R.id.cariTanggalTempo);
        btnCariTglTempo.setOnClickListener(v -> setDate(2));

        Button barang = findViewById(R.id.cariBarang);
        barang.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPenjualanKredit.this, MenuCariBarangKredit.class);
            i.putExtra("owner", ActivityPenjualanKredit.class);
            i.putExtra("type", "barang");
            startActivity(i);
        });

        Button pelanggan = findViewById(R.id.cariPelanggan);
        pelanggan.setOnClickListener(v -> {
            Intent i = new Intent(ActivityPenjualanKredit.this, MenuCariPelangganKredit.class);
            i.putExtra("owner", ActivityPenjualanKredit.class);
            i.putExtra("type", "pelanggan");
            startActivity(i);
        });

        Button btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(view -> {
            String jumlah = FFunctionKredit.getText(v, R.id.jumlah);
            int jum = FFunctionKredit.strToInt(jumlah);
            jum++;
            FFunctionKredit.setText(v, R.id.jumlah, FFunctionKredit.intToStr(jum));
        });

        Button btnMin = findViewById(R.id.btnMin);
        btnMin.setOnClickListener(view -> {
            String jumlah = FFunctionKredit.getText(v, R.id.jumlah);
            int jum = FFunctionKredit.strToInt(jumlah);
            if (jum > 1) {
                jum--;
                FFunctionKredit.setText(v, R.id.jumlah, FFunctionKredit.intToStr(jum));
            }
        });

        EditText eJumlah = findViewById(R.id.jumlah);
        eJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    rawTotal = rawHarga * Double.parseDouble(s.toString());
                } else {
                    rawTotal = rawHarga * 1;
                    eJumlah.setText("1");
                }
                FFunctionKredit.setText(v, R.id.total, ModuleKredit.numFormat(rawTotal));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setText();
        cekButtonCari();
    }

    void init() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        eJumlah = findViewById(R.id.jumlah);

        v = findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);

        // RecyclerView stufff
        daftarOrderDetail = new ArrayList<>();
        adapterOrderDetail = new ListPenjualan(this, daftarOrderDetail);
        recJual = findViewById(R.id.recJual);
        recJual.setAdapter(adapterOrderDetail);
        recJual.setLayoutManager(new LinearLayoutManager(this));
        recJual.setHasFixedSize(true);
    }

    @Override
    public void onBackPressed() {
        keluar();
    }

    public void hitung() {
        String jumlahbarang = FFunctionKredit.getText(v, R.id.jumlah);
        String harga = FFunctionKredit.getText(v, R.id.harga);
        Integer i = 0x9;

        if (!TextUtils.isEmpty(harga)) {
            double total = FFunctionKredit.strToDouble(totalbayar) + (FFunctionKredit.strToDouble(jumlahbarang) * FFunctionKredit.strToDouble(harga));
            String hasil = FFunctionKredit.doubleToStr(total);
            FFunctionKredit.setText(v, R.id.total, FFunctionKredit.removeE(hasil));
        }
    }

    public void cekButtonCari() {
        Button btnCariPelanggan = findViewById(R.id.cariPelanggan);
        Button btnCariTanggal = findViewById(R.id.cariTanggal);

        boolean shouldEnabled = recJual.getAdapter().getItemCount() == 0;
        btnCariPelanggan.setEnabled(shouldEnabled);
        btnCariTanggal.setEnabled(shouldEnabled);
    }

    public void selesai(View itemView) {
        String eTgl = FFunctionKredit.getText(v, R.id.tanggal);
        String eTglJatuhTempo = FFunctionKredit.getText(v, R.id.tanggalJatuhTempo);
        String eBrg = FFunctionKredit.getText(v, R.id.barang);
        String ePlgn = FFunctionKredit.getText(v, R.id.pelanggan);
        String eHrg = FFunctionKredit.getText(v, R.id.harga);
        String eJmlh = FFunctionKredit.getText(v, R.id.jumlah);
        String keterangan = FFunctionKredit.getText(v, R.id.etKeterangan);

        // Validasi
        if (TextUtils.isEmpty(eBrg) || TextUtils.isEmpty(ePlgn) || TextUtils.isEmpty(eHrg) || TextUtils.isEmpty(eJmlh) || FFunctionKredit.strToDouble(eJmlh) == 0) {
            Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor tblbarang = db.sq("SELECT * FROM tblbarang WHERE idbarang = " + brg + "");
        tblbarang.moveToFirst();
        if (Integer.parseInt(eJumlah.getText().toString()) > tblbarang.getInt(tblbarang.getColumnIndex("stok"))) {
            ModuleKredit.info(this, "Jumlah melebihi stok yang tersedia");
            return;
        }

        // Proses
        Bundle args = new Bundle();
        args.putString("faktur", faktur);
        args.putString("tglJatuhTempo", convertDate(eTglJatuhTempo));
        args.putDouble("jumlahkredit", rawTotal);
        args.putString("tglbayar", convertDate(eTgl));
        args.putInt("idpelanggan", Integer.parseInt(plgn));

        DialogKredit dialogKredit = new DialogKredit();
        dialogKredit.setArguments(args);
        dialogKredit.show(getSupportFragmentManager(), "takada");
    }

    public String getTotal() {
        Cursor c = db.sq(FQueryKredit.selectwhere("tblbayar_kredit") + FQueryKredit.sWhere("fakturbayar", faktur));
        String total = "";
        if (c.getCount() > 0) {
            c.moveToNext();
            total = removeE(FFunctionKredit.getString(c, "jumlahbayar"));
            totalbayar = total;
            return total;
        } else {
            total = "0";
            totalbayar = "0";
            return total;
        }
    }

    public void setText() {
        brg = temp.getCustom("idbarang_kredit", "");
        plgn = temp.getCustom("idpelanggan_kredit", "");
        tanggal = temp.getCustom("tanggal_kredit", "");
        tanggalTempo = temp.getCustom("tanggal_tempo", "");

        if (!TextUtils.isEmpty(brg)) {
            getBarang();
        } else {
            FFunctionKredit.setText(v, R.id.barang, "");
            FFunctionKredit.setText(v, R.id.total, "0");
        }
        if (!TextUtils.isEmpty(plgn)) {
            getPelanggan();
        } else {
            FFunctionKredit.setText(v, R.id.pelanggan, "");
        }
        // set tanggal
        if (!TextUtils.isEmpty(tanggal)) {
            FFunctionKredit.setText(v, R.id.tanggal, tanggal);
        } else {
            FFunctionKredit.setText(v, R.id.tanggal, FFunctionKredit.getDate("dd/MM/yyyy"));
        }
        // set tanggal tempo
        if (!TextUtils.isEmpty(tanggalTempo)) {
            FFunctionKredit.setText(v, R.id.tanggalJatuhTempo, tanggalTempo);
        } else {
            FFunctionKredit.setText(v, R.id.tanggalJatuhTempo, FFunctionKredit.getDate("dd/MM/yyyy"));
        }
        FFunctionKredit.setText(v, R.id.faktur, faktur);
    }

    private void getFaktur() {
        try {
            String q = FQueryKredit.select("tblbayar_kredit") + FQueryKredit.sOrderDESC("fakturbayar");
            Cursor c = db.sq(q);
            int count = c.getCount();
            if (count == 0) {
                faktur = nol(8) + "1";
            } else {
                c.moveToNext();
                int last = Integer.parseInt(FFunctionKredit.getString(c, "fakturbayar")); // 2
                int fix = last + 1;
                int leng = String.valueOf(fix).length();
                faktur = nol(9 - leng) + fix;
            }
            temp.setCustom("fakturbayar_kredit", faktur);
            FFunctionKredit.setText(v, R.id.faktur, faktur);
        } catch (Exception e) {
            Toast.makeText(this, "Membuat Faktur Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public String nol(int total) {
        String n = "";
        for (int i = 0; i < total; i++) {
            n += "0";
        }
        return n;
    }

    public void simpan(View view) {
        String eTgl = FFunctionKredit.getText(v, R.id.tanggal);
        String eBrg = FFunctionKredit.getText(v, R.id.barang);
        String ePlgn = FFunctionKredit.getText(v, R.id.pelanggan);
        String eHrg = FFunctionKredit.getText(v, R.id.harga);
        String eJmlh = FFunctionKredit.getText(v, R.id.jumlah);
        String keterangan = FFunctionKredit.getText(v, R.id.etKeterangan);

        if (TextUtils.isEmpty(eBrg) || TextUtils.isEmpty(ePlgn) || TextUtils.isEmpty(eHrg) || TextUtils.isEmpty(eJmlh) || FFunctionKredit.strToDouble(eHrg) == 0 || FFunctionKredit.strToDouble(eJmlh) == 0) {
            Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
        } else {
            double bay = FFunctionKredit.strToDouble(eHrg) * FFunctionKredit.strToDouble(eJmlh);
            String qBayar, qpenj;
            String[] bayar;
            String[] penj = {faktur,
                    convertDate(eTgl),
                    eHrg,
                    eJmlh,
                    brg,
                    "0",
                    labarugi(eHrg),
                    keterangan};
            String q = FQueryKredit.selectwhere("tblbayar_kredit") + FQueryKredit.sWhere("fakturbayar", faktur);
            Cursor c = db.sq(q);
            if (c.getCount() == 0) {
                bayar = new String[]{faktur, FFunctionKredit.doubleToStr(bay), plgn, convertDate(eTgl), "0"};
                qBayar = FQueryKredit.splitParam("INSERT INTO tblbayar_kredit (fakturbayar,jumlahbayar,idpelanggan,tglbayar,flagbayar) VALUES (?,?,?,?,?)", bayar);

                qpenj = FQueryKredit.splitParam("INSERT INTO tblpenjualan_kredit (fakturbayar,tgljual,hargajual,jumlahjual,idbarang,flagjual,labarugi, keterangan) VALUES (?,?,?,?,?,?,?,?)", penj);
            } else {
                qBayar = "UPDATE tblbayar_kredit SET jumlahbayar = jumlahbayar + " + bay + " WHERE fakturbayar=" + FFunctionKredit.quote(faktur);
                qpenj = FQueryKredit.splitParam("INSERT INTO tblpenjualan_kredit (fakturbayar,tgljual,hargajual,jumlahjual,idbarang,flagjual,labarugi,keterangan) VALUES (?,?,?,?,?,?,?,?)", penj);
            }

            // Cek stok
            Cursor c2 = db.sq(FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idbarang", brg));
            if (c2.getCount() > 0) {
                c2.moveToNext();
                String stok = FFunctionKredit.getString(c2, "stok");
                if (FFunctionKredit.strToDouble(stok) >= FFunctionKredit.strToDouble(FFunctionKredit.getText(v, R.id.jumlah))) {
                    if (db.exc(qBayar) && db.exc(qpenj)) {
                        Toast.makeText(this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show();

                        FConfigKredit FConfigKredit = new FConfigKredit(getSharedPreferences("temp", ActivityPenjualanTunaiKredit.MODE_PRIVATE));
                        FConfigKredit.setCustom("idbarang_kredit", "");
                        FConfigKredit.setCustom("tanggal_kredit", "");
                        FConfigKredit.setCustom("tanggal_tempo", "");
                        FFunctionKredit.setText(v, R.id.harga, "");
                        FFunctionKredit.setText(v, R.id.etKeterangan, "");
                        FFunctionKredit.setText(v, R.id.jumlah, "1");

                        loadList();
                        setText();
                        cekButtonCari();
                    } else {
                        Toast.makeText(this, "Pembelian Gagal, Silahkan Restart HP Anda.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Stok Tidak Cukup untuk Pemesanan", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void hapus(View view) {
        String[] id = view.getTag().toString().split("__");
        open(id[0], id[1]);
    }

    public void getBarang() {
        String q = FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idbarang", brg);
        Cursor c = db.sq(q);
        c.moveToNext();

        rawHarga = c.getDouble(c.getColumnIndex("hargajual"));
        rawTotal = rawHarga * Double.parseDouble(eJumlah.getText().toString());
        FFunctionKredit.setText(v, R.id.barang, FFunctionKredit.getString(c, "barang"));
        FFunctionKredit.setText(v, R.id.harga, ModuleKredit.numFormat(rawHarga));
        FFunctionKredit.setText(v, R.id.total, ModuleKredit.numFormat(rawHarga));
    }

    public String removeE(String value) {
        double hasil = FFunctionKredit.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(hasil);
    }

    public void getPelanggan() {
        String q = FQueryKredit.selectwhere("tblpelanggan") + FQueryKredit.sWhere("idpelanggan", plgn);
        Cursor c = db.sq(q);
        c.moveToNext();
        FFunctionKredit.setText(v, R.id.pelanggan, FFunctionKredit.getString(c, "pelanggan"));
    }

    public String convertDate(String date) {
        String[] a = date.split("/");
        return a[2] + "-" + a[1] + "-" + a[0];
    }

    public String labarugi(String harga) {
        String q = FQueryKredit.selectwhere("tblbarang") + FQueryKredit.sWhere("idbarang", brg);
        Cursor c = db.sq(q);
        c.moveToNext();
        double jual = FFunctionKredit.strToDouble(harga);
        double beli = FFunctionKredit.strToDouble(FFunctionKredit.getString(c, "hargabeli"));
        double hasil = jual - beli;
//        if(jual >= beli){
//            hasil = jual - beli ;
//        } else if(beli > jual){
//            hasil = beli - jual ;
//        }
        return FFunctionKredit.doubleToStr(hasil);
    }

    public void loadList() {
        try {
            daftarOrderDetail.clear();

            String q = FQueryKredit.selectwhere("qpenjualan_kredit") + FQueryKredit.sWhere("fakturbayar", faktur);
            Cursor c = db.sq(q);
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    String campur = FFunctionKredit.getString(c, "idpenjualan") + "__" + FFunctionKredit.getString(c, "fakturbayar") + "__" +
                            FFunctionKredit.getString(c, "barang") + "__" + removeE(kali(FFunctionKredit.getString(c, "hargajual:1"), FFunctionKredit.getString(c, "jumlahjual")))
                            + "__" + FFunctionKredit.getString(c, "jumlahjual") + "__" + FFunctionKredit.getString(c, "ketpenjualan") + " ";
                    daftarOrderDetail.add(campur);
                }
            }
            adapterOrderDetail.notifyDataSetChanged();
            String tot = getTotal();
            FFunctionKredit.setText(v, R.id.total, FFunctionKredit.numberFormat(tot));
        } catch (Exception e) {
            Toast.makeText(this, "Aplikasi Error. Silahkan restart HP Anda. atau silahkan hubungi komputerkit.dev@gmail.com", Toast.LENGTH_SHORT).show();
        }
    }

    public String kali(String a, String b) {
        try {
            return FFunctionKredit.doubleToStr(FFunctionKredit.strToDouble(a) * FFunctionKredit.strToDouble(b));
        } catch (Exception e) {
            return "0";
        }
    }

    //dialog
    public void open(final String id, final String min) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus");
        alertDialogBuilder.setPositiveButton("Hapus", (arg0, arg1) -> {
            //yes
            double hasil = FFunctionKredit.strToDouble(getTotal()) - FFunctionKredit.strToDouble(min);
            if (db.exc("UPDATE tblbayar_kredit SET jumlahbayar=" + FFunctionKredit.doubleToStr(hasil) + " WHERE fakturbayar=" + FFunctionKredit.quote(faktur)) &&
                    db.exc("DELETE FROM tblpenjualan_kredit WHERE idpenjualan=" + id)) {
                loadList();
                cekButtonCari();
            } else {
                Toast.makeText(ActivityPenjualanKredit.this, "Hapus Gagal", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("batal", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void keluar() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan keluar");
        alertDialogBuilder.setPositiveButton("Iya", (arg0, arg1) -> {
            ModuleKredit.goToActivity(this, ActivityTransaksiKredit.class, true);
        });

        alertDialogBuilder.setNegativeButton("Batal", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //end dialog

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
        DatePickerDialog.OnDateSetListener listenerToListen = id == 1 ? date : dateTempoListener;
        return new DatePickerDialog(this, listenerToListen, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateTempoListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FConfigKredit FConfigKredit = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
            FConfigKredit.setCustom("tanggal_tempo", FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));

            FFunctionKredit.setText(v, R.id.tanggalJatuhTempo, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FConfigKredit FConfigKredit = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
            FConfigKredit.setCustom("tanggal_kredit", FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));

            FFunctionKredit.setText(v, R.id.tanggal, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    @Override
    public void onKreditConfirmed() {
        String eTgl = FFunctionKredit.getText(v, R.id.tanggal);
        String eBrg = FFunctionKredit.getText(v, R.id.barang);
        String ePlgn = FFunctionKredit.getText(v, R.id.pelanggan);
        String eHrg = FFunctionKredit.getText(v, R.id.harga);
        String eJmlh = FFunctionKredit.getText(v, R.id.jumlah);
        String keterangan = FFunctionKredit.getText(v, R.id.etKeterangan);

        // Insert ke tblbayar dan tblpenjualan
        double bay = FFunctionKredit.strToDouble(eHrg) * FFunctionKredit.strToDouble(eJmlh);
        String qBayar, qpenj;
        String[] bayar;
        String[] penj = {faktur,
                convertDate(eTgl),
                eHrg,
                eJmlh,
                brg,
                "0",
                labarugi(eHrg),
                keterangan};

        bayar = new String[]{faktur, FFunctionKredit.doubleToStr(bay), plgn, convertDate(eTgl), "0"};
        qBayar = FQueryKredit.splitParam("INSERT INTO tblbayar_kredit (fakturbayar,jumlahbayar,idpelanggan,tglbayar,flagbayar) VALUES (?,?,?,?,?)", bayar);
        qpenj = FQueryKredit.splitParam("INSERT INTO tblpenjualan_kredit (fakturbayar,tgljual,hargajual,jumlahjual,idbarang,flagjual,labarugi, keterangan) VALUES (?,?,?,?,?,?,?,?)", penj);
        db.exc(qBayar);
        db.exc(qpenj);

        temp.setCustom("idpelanggan_kredit", "");
        temp.setCustom("idbarang_kredit", "");
        temp.setCustom("tanggal_kredit", "");
        temp.setCustom("tanggal_tempo", "");
        temp.setCustom("fakturbayar_kredit", "");

        ModuleKredit.yesNoDialog(this, "Pemberitahuan", "Apakah anda ingin cetak struk ?", (dialog, which) -> {
            Intent i = new Intent(this, ActivityCetakKredit.class);
            i.putExtra("faktur", faktur);
            i.putExtra("owner", ActivityPenjualanKredit.class);
            startActivity(i);
        }, (dialog, which) -> ModuleKredit.goToActivity(this, ActivityPenjualanKredit.class, true));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        eTglJatuhTempo.setText(currentDateString);
//        tanggalOrder = year + "-" + (month + 1 < 10 ? "0" + (month + 1) : month + 1) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        Log.i("info", "date from penjualan kredit");
    }

    //End Date Picker
}
