package com.itbrain.aplikasitoko.tabungan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;
//import com.komputerkit.aplikasitabunganplus.Kunci.LisensiBaru;
//import com.komputerkit.aplikasitabunganplus.util.NumberTextWatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MenuTransaksiProsesSimpanTabungan extends AppCompatActivity {

    private TextInputEditText mEdtSimpanan;
    private TextInputEditText mEdtNoTransaksi;
    private TextInputEditText mEdtTglTransaksi;
    private TextInputEditText mEdtNominal;
    private TextInputEditText mEdtSisaSaldo;
    private TextView mTvAnggota;
    private TextView mTvJenis;
    private TextView mTvBunga;
    private ImageButton mIbtnSimpanan;
    private ImageButton mIbtnTgl;
    private TextInputLayout mTextInputLayout14;
    private CardView mCardView;
    private RecyclerView mRecList;
    private Switch mSwitchBunga;
    private TextView mTvSaldo;
    String type;
    String idsimpanan, simpanan, saldo = "0";
    DatabaseTabungan db;

    Calendar calendar;
    int year, month, day;

    //BillingProcessor bp;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi_proses_simpan_tabungan);
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        bp = new BillingProcessor(this, Pref.licenseKey, this);
//        bp.initialize();
//        checkPurchase();
        type = getIntent().getStringExtra("type");
        db = new DatabaseTabungan(this);
        String title = "";
        if (type.equals("simpan")) {
            title = ModulTabungan.getResString(this, R.string.menutransaksi_box2);
        } else {
            title = ModulTabungan.getResString(this, R.string.menutransaksi_box3);
        }
//        ModulTabungan.btnBack(title, getSupportActionBar());

        idsimpanan = "0";
        simpanan = getResources().getString(R.string.pilihsimpanan);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        ImageButton imageButton = findViewById(R.id.kembaliProses);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // search nama simpanan
        mIbtnSimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MenuTransaksiProsesSimpanTabungan.this, MenuTransaksiCariTabungan.class).putExtra("type", "simpanan"), 2000);
            }
        });
        //isi detail

        // show current nomor transaksi
        mEdtNoTransaksi.setText("1");

        // record tanggal transaksi
        mEdtTglTransaksi.setText(String.valueOf(ModulTabungan.getDate2(calendar, "dd/MM/yyyy")));
        mIbtnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog datePicker = new DatePickerDialog(MenuTransaksiProsesSimpanTabungan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int thn, int bln, int tgl) {
                        year = thn;
                        month = bln;
                        day = tgl;
                        calendar.set(year, month, day);
                        mEdtTglTransaksi.setText(String.valueOf(ModulTabungan.getDate2(calendar, "dd/MM/yyyy")));
                        if (!type.equals("simpan")) {
                            checkBunga(saldo);
                        }
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        // record nominal
        mEdtNominal.requestFocus();
        mEdtNominal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtNominal.getText().toString())) < 0) {
                    mEdtNominal.setText("0");
                }
            }
        });
        mEdtNominal.addTextChangedListener(new NumberTextWatcher(mEdtNominal,new Locale("in","ID"),8));
        mTextInputLayout14.setHint(type.equals("simpan") ? ModulTabungan.getResString(this, R.string.menutransaksiproses_form4) : ModulTabungan.getResString(this, R.string.menutransaksiproses_form4a));

        // show sisa saldo
        mEdtSisaSaldo.setText("0");
    }


//    public void checkPurchase(){
//
//        if (LisensiBaru.checkLisence(this)){
//            status =true;
//
//
//        }else {
//            status=false;
//
//        }
//
//
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getTransaksi(String idsimpanan,String simpanan,String anggota) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();

        Cursor c = db.sq(QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan) + QueryTabungan.sOrderDESC("notransaksi"));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = ModulTabungan.getString(c, "idtransaksi") + "__" +
                        ModulTabungan.getString(c, "idsimpanan") + "__" +
                        ModulTabungan.getString(c, "notransaksi") + "__" +
                        ModulTabungan.getString(c, "tgltransaksi") + "__" +
                        ModulTabungan.getString(c, "masuk") + "__" +
                        ModulTabungan.getString(c, "keluar") + "__" +
                        ModulTabungan.getString(c, "saldo") + "__" +
                        ModulTabungan.getString(c, "kode");
                arrayList.add(campur);
            }
        }

        AdapterListTransaksi adapter = new AdapterListTransaksi(this, arrayList, false);
        adapter.setSimpanan(simpanan,anggota);
        mRecList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000) {
            idsimpanan = data.getStringExtra("idsimpanan");
            simpanan = data.getStringExtra("namasimpanan");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEdtSimpanan.setText(simpanan);
        refreshList();
        if (!idsimpanan.equals("0")) {


            mCardView.setVisibility(View.VISIBLE);
            mEdtNominal.setEnabled(true);
            mEdtNominal.setText("0");

            Cursor c = db.sq(QueryTabungan.selectwhere("qsimpanan") + QueryTabungan.sWhere("idsimpanan", idsimpanan));
            c.moveToLast();
            mTvAnggota.setText(ModulTabungan.getResString(this, R.string.menutransaksiproses_tv1) + " " + ModulTabungan.getString(c, "namaanggota"));
            mTvJenis.setText(ModulTabungan.getResString(this, R.string.menutransaksiproses_tv2) + " " + ModulTabungan.getString(c, "jenis"));
            mTvBunga.setText(ModulTabungan.getResString(this, R.string.menutransaksiproses_tv3) + " " + ModulTabungan.numFormat(ModulTabungan.getString(c, "bunga")) + "%");
            getTransaksi(idsimpanan,ModulTabungan.getString(c,"simpanan"),ModulTabungan.getString(c,"namaanggota"));
            Cursor cNoTransaksi = db.sq(QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan) + QueryTabungan.sOrderDESC("notransaksi"));
            if (cNoTransaksi.getCount() > 0) {
                cNoTransaksi.moveToFirst();
                @SuppressLint("Range") int notransaksi = cNoTransaksi.getCount() > 0 ? cNoTransaksi.getInt(cNoTransaksi.getColumnIndex("notransaksi")) : 0;
                mEdtNoTransaksi.setText(String.valueOf(notransaksi + 1));
                saldo = ModulTabungan.getString(cNoTransaksi, "saldo");
                mEdtSisaSaldo.setText(ModulTabungan.removeE(saldo));
            }
//            if (type.equals("simpan")) {
//                mEdtNominal.addTextChangedListener(new NumberTextWatcherSimpan(mEdtNominal, new Locale("in", "ID"), 2, saldo, mEdtSisaSaldo));
//            } else {
//                checkBunga(saldo);
//                mEdtNominal.addTextChangedListener(new NumberTextWatcherKembali(mEdtNominal, new Locale("in", "ID"), 2, saldo, mEdtSisaSaldo));
//            }
        }

        //checkPurchase();
    }

    private void checkBunga(final String tSaldo) {
        Double bungaPerHari = 0.0;
        Integer diff = 0;
        Cursor c = db.sq(QueryTabungan.selectwhere("qsimpanan") + QueryTabungan.sWhere("idsimpanan", idsimpanan));
        if (c.getCount() > 0) {
            c.moveToLast();
            bungaPerHari = ((ModulTabungan.getDouble(c, "bunga") / 100) * Double.valueOf(tSaldo)) / 30;
            Cursor cBunga = db.sq(QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan) + " AND " + QueryTabungan.sWhere("kode", "B") + QueryTabungan.sOrderDESC("notransaksi"));
            if (cBunga.getCount() > 0) {
                cBunga.moveToFirst();
                diff = 1 + ModulTabungan.getDifferenceDays(ModulTabungan.dateToDate(ModulTabungan.getString(cBunga, "tgltransaksi")), calendar.getTime()).intValue();
                if (diff > 0) {
                    mSwitchBunga.setVisibility(View.VISIBLE);
                    mSwitchBunga.setText(ModulTabungan.getResString(MenuTransaksiProsesSimpanTabungan.this, R.string.menutransaksiproses_switch1) + diff);
                } else {
                    mSwitchBunga.setVisibility(View.GONE);
                }
            } else {
                Cursor cTabungan = db.sq(QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan) + " AND " + QueryTabungan.sWhere("kode", "T") + QueryTabungan.sOrderDESC("notransaksi"));
                if (cTabungan.getCount() > 0) {
                    cTabungan.moveToFirst();
                    diff = 1 + ModulTabungan.getDifferenceDays(ModulTabungan.dateToDate(ModulTabungan.getString(cTabungan, "tgltransaksi")), calendar.getTime()).intValue();
                    if (diff > 0) {
                        mSwitchBunga.setVisibility(View.VISIBLE);
                        mSwitchBunga.setText(ModulTabungan.getResString(MenuTransaksiProsesSimpanTabungan.this, R.string.menutransaksiproses_switch1) + diff);
                    } else {
                        mSwitchBunga.setVisibility(View.GONE);
                    }
                } else {

                }
            }
        }
        final Double finalBungaPerHari = bungaPerHari;
        final Integer finalDiff = diff;
        mSwitchBunga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Double mmBunga = finalBungaPerHari * finalDiff;
                    Double mmSaldo = mmBunga + Double.valueOf(tSaldo);
                    saldo = ModulTabungan.justRemoveE(String.valueOf(mmSaldo));
                    mEdtNominal.setText("0");
                    mEdtSisaSaldo.setText(ModulTabungan.removeE(saldo));
                } else {
                    Cursor cSaldo = db.sq(QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan) + QueryTabungan.sOrderDESC("notransaksi"));
                    cSaldo.moveToFirst();
                    saldo = ModulTabungan.getString(cSaldo, "saldo");
                    mEdtSisaSaldo.setText(ModulTabungan.removeE(saldo));
                }
//                if (type.equals("simpan")) {
//                    mEdtNominal.addTextChangedListener(new NumberTextWatcherSimpan(mEdtNominal, new Locale("in", "ID"), 2, saldo, mEdtSisaSaldo));
//                } else {
//                    mEdtNominal.addTextChangedListener(new NumberTextWatcherKembali(mEdtNominal, new Locale("in", "ID"), 2, saldo, mEdtSisaSaldo));
//                }
            }
        });
    }

    private void initView() {
        mEdtSimpanan = (TextInputEditText) findViewById(R.id.edtSimpanan);
        mEdtNoTransaksi = (TextInputEditText) findViewById(R.id.edtNoTransaksi);
        mEdtTglTransaksi = (TextInputEditText) findViewById(R.id.edtTglTransaksi);
        mEdtNominal = (TextInputEditText) findViewById(R.id.edtNominal);
        mEdtSisaSaldo = (TextInputEditText) findViewById(R.id.edtSisaSaldo);
        mTvAnggota = (TextView) findViewById(R.id.tvAnggota);
        mTvJenis = (TextView) findViewById(R.id.tvJenis);
        mTvBunga = (TextView) findViewById(R.id.tvBunga);
        mIbtnSimpanan = (ImageButton) findViewById(R.id.ibtnSimpanan);
        mIbtnTgl = (ImageButton) findViewById(R.id.ibtnTgl);
        mTextInputLayout14 = (TextInputLayout) findViewById(R.id.textInputLayout14);
        mCardView = (CardView) findViewById(R.id.cardView);
        mRecList = (RecyclerView) findViewById(R.id.recList);
        mSwitchBunga = (Switch) findViewById(R.id.switchBunga);
        mTvSaldo = (TextView) findViewById(R.id.tvSaldo);
    }

    public void simpan(View view) {
        if (ModulTabungan.cekTransaksi(this, idsimpanan) || (!ModulTabungan.cekTransaksi(this, idsimpanan) && status)) {
            String saldo;
            if (type.equals("simpan")){
                Double tSaldo=ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtSisaSaldo.getText().toString()))+ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtNominal.getText().toString()));
                saldo=String.valueOf(tSaldo);
            }else {
                Double tSaldo=ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtSisaSaldo.getText().toString()))-ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtNominal.getText().toString()));
                saldo=String.valueOf(tSaldo);
            }
            final String[] qBaru = {
                    idsimpanan,
                    mEdtNoTransaksi.getText().toString(),
                    ModulTabungan.convertDate(mEdtTglTransaksi.getText().toString()),
                    ModulTabungan.parseDF(mEdtNominal.getText().toString()),
                    saldo,
                    "T"
            };
            if (idsimpanan.equals("0") ||
                    Double.valueOf(ModulTabungan.parseDF(mEdtNominal.getText().toString())) <= 0 ||
                    Double.valueOf(saldo) < 0 ||
                    (Double.valueOf(ModulTabungan.parseDF(mEdtNominal.getText().toString())) > Double.valueOf(ModulTabungan.parseDF(ModulTabungan.getText(this.findViewById(android.R.id.content),R.id.tvSaldo))) &&
                            type.equals("ambil")) ) {
                if (Double.valueOf(ModulTabungan.parseDF(mEdtNominal.getText().toString())) > Double.valueOf(ModulTabungan.parseDF(ModulTabungan.getText(this.findViewById(android.R.id.content),R.id.tvSaldo))) &&
                        type.equals("ambil")){
                    Toast.makeText(this, getString(R.string.saldotidakcukup), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, R.string.formnull, Toast.LENGTH_SHORT).show();
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.konfirmasitransaksi)
                        .setMessage(R.string.konfirmasitransaksi_desc)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (type.equals("simpan")) {
                                    String q = QueryTabungan.splitParam("INSERT INTO tbltransaksi (idsimpanan,notransaksi,tgltransaksi,masuk,saldo,kode) VALUES (?,?,?,?,?,?)", qBaru);
                                    if (db.exc(q)) {
                                        Toast.makeText(MenuTransaksiProsesSimpanTabungan.this, R.string.berhasil, Toast.LENGTH_SHORT).show();
                                        insertKeuanganMasuk(qBaru[2],qBaru[3],simpanan,mTvAnggota.getText().toString(),qBaru[3]);
                                        confirmCetak(MenuTransaksiProsesSimpanTabungan.this, idsimpanan, mEdtNoTransaksi.getText().toString());
                                    } else {
                                        Toast.makeText(MenuTransaksiProsesSimpanTabungan.this, R.string.gagal, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    String q = QueryTabungan.splitParam("INSERT INTO tbltransaksi (idsimpanan,notransaksi,tgltransaksi,keluar,saldo,kode) VALUES (?,?,?,?,?,?)", qBaru);
                                    if (db.exc(q)) {
                                        Toast.makeText(MenuTransaksiProsesSimpanTabungan.this, R.string.berhasil, Toast.LENGTH_SHORT).show();
                                        insertKeuanganKeluar(qBaru[2],qBaru[3],simpanan,mTvAnggota.getText().toString(),qBaru[3]);
                                        confirmCetak(MenuTransaksiProsesSimpanTabungan.this, idsimpanan, mEdtNoTransaksi.getText().toString());
                                    } else {
                                        Toast.makeText(MenuTransaksiProsesSimpanTabungan.this, R.string.gagal, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.batal, null)
                        .create();
                dialog.show();
            }
        } else {
            ModulTabungan modul = new ModulTabungan(this);
            modul.inApp();
        }
    }



    private void confirmCetak(final Context context, final String idsimpanan, final String notransaksi) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(R.string.printconfirmation)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                        context.startActivity(new Intent(context, CetakActivityTabungan.class).putExtra("idsimpanan", idsimpanan).putExtra("notransaksi", notransaksi));
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                })
                .create();
        dialog.show();
    }

//    @Override
//    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        String saldo;
//        if (type.equals("simpan")){
//            Double tSaldo=ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtSisaSaldo.getText().toString()))+ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtNominal.getText().toString()));
//            saldo=String.valueOf(tSaldo);
//        }else {
//            Double tSaldo=ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtSisaSaldo.getText().toString()))-ModulTabungan.strToDouble(ModulTabungan.parseDF(mEdtNominal.getText().toString()));
//            saldo=String.valueOf(tSaldo);
//        }
//        final String[] qBaru = {
//                idsimpanan,
//                mEdtNoTransaksi.getText().toString(),
//                ModulTabungan.convertDate(mEdtTglTransaksi.getText().toString()),
//                ModulTabungan.parseDF(mEdtNominal.getText().toString()),
//                saldo,
//                "T"
//        };
//        if (idsimpanan.equals("0") ||
//                Double.valueOf(ModulTabungan.parseDF(mEdtNominal.getText().toString())) <= 0 ||
//                Double.valueOf(saldo) < 0 ||
//                (Double.valueOf(ModulTabungan.parseDF(mEdtNominal.getText().toString())) > Double.valueOf(ModulTabungan.parseDF(ModulTabungan.getText(this.findViewById(android.R.id.content),R.id.tvSaldo))) &&
//                        type.equals("ambil")) ) {
//            if (Double.valueOf(ModulTabungan.parseDF(mEdtNominal.getText().toString())) > Double.valueOf(ModulTabungan.parseDF(ModulTabungan.getText(this.findViewById(android.R.id.content),R.id.tvSaldo))) &&
//                    type.equals("ambil")){
//                Toast.makeText(this, getString(R.string.saldotidakcukup), Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, R.string.formnull, Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            AlertDialog dialog = new AlertDialog.Builder(this)
//                    .setTitle(R.string.konfirmasitransaksi)
//                    .setMessage(R.string.konfirmasitransaksi_desc)
//                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (type.equals("simpan")) {
//                                String q = QueryTabungan.splitParam("INSERT INTO tbltransaksi (idsimpanan,notransaksi,tgltransaksi,masuk,saldo,kode) VALUES (?,?,?,?,?,?)", qBaru);
//                                if (db.exc(q)) {
//                                    Toast.makeText(MenuTransaksiProses.this, R.string.berhasil, Toast.LENGTH_SHORT).show();
//                                    insertKeuanganMasuk(qBaru[2],qBaru[3],simpanan,mTvAnggota.getText().toString(),qBaru[3]);
//                                    confirmCetak(MenuTransaksiProses.this, idsimpanan, mEdtNoTransaksi.getText().toString());
//                                } else {
//                                    Toast.makeText(MenuTransaksiProses.this, R.string.gagal, Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                String q = QueryTabungan.splitParam("INSERT INTO tbltransaksi (idsimpanan,notransaksi,tgltransaksi,keluar,saldo,kode) VALUES (?,?,?,?,?,?)", qBaru);
//                                if (db.exc(q)) {
//                                    Toast.makeText(MenuTransaksiProses.this, R.string.berhasil, Toast.LENGTH_SHORT).show();
//                                    insertKeuanganKeluar(qBaru[2],qBaru[3],simpanan,mTvAnggota.getText().toString(),qBaru[3]);
//                                    confirmCetak(MenuTransaksiProses.this, idsimpanan, mEdtNoTransaksi.getText().toString());
//                                } else {
//                                    Toast.makeText(MenuTransaksiProses.this, R.string.gagal, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    })
//                    .setNegativeButton(R.string.batal, null)
//                    .create();
//            dialog.show();
//        }
//    }

    public void refreshList() {
        Cursor cSaldo = db.sq(QueryTabungan.select("tblkeuangan"));
        cSaldo.moveToLast();
        if (cSaldo.getCount() > 0) {
            saldo = ModulTabungan.removeE(ModulTabungan.getString(cSaldo, "saldo"));
        } else {
            saldo = "0";
        }
        mTvSaldo.setText(saldo);
    }
    @SuppressLint("Range")
    public void insertKeuanganMasuk(String tgl, String diterima, String simpanan, String anggota, String nominal){
        Cursor cKeuangan=db.sq(QueryTabungan.select("tblkeuangan"));
        cKeuangan.moveToLast();
        double saldo=0;
        if (cKeuangan.getCount()>0){
            saldo=cKeuangan.getDouble(cKeuangan.getColumnIndex("saldo"))+ModulTabungan.strToDouble(diterima);
        }else{
            saldo=0+ModulTabungan.strToDouble(diterima);
        }
        String faktur="";
        Cursor cTransaksi=db.sq(QueryTabungan.select("tbltransaksi"));
        cTransaksi.moveToLast();
        if (cTransaksi.getCount()>0){
            if (cTransaksi.getInt(0)<10){
                faktur=ModulTabungan.getResString(this,R.string.fakturproses)+"0"+ModulTabungan.getString(cTransaksi,"idtransaksi");
            }else {
                faktur=ModulTabungan.getResString(this,R.string.fakturproses)+cTransaksi.getString(0);
            }
        }
        String[] pKeuangan={
                tgl,
                faktur,
                getKeterangan(simpanan,anggota,nominal),
                diterima,
                String.valueOf(saldo)
        };
        if (db.exc(QueryTabungan.splitParam("INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,masuk,saldo) VALUES (?,?,?,?,?)",pKeuangan))){

        }else {
            Toast.makeText(this, "Gagal Menambahkan Data Keuangan", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    public void insertKeuanganKeluar(String tgl, String diterima, String simpanan, String anggota, String nominal){
        Cursor cKeuangan=db.sq(QueryTabungan.select("tblkeuangan"));
        cKeuangan.moveToLast();
        double saldo=0;
        if (cKeuangan.getCount()>0){
            saldo=cKeuangan.getDouble(cKeuangan.getColumnIndex("saldo"))-ModulTabungan.strToDouble(diterima);
        }else{
            saldo=0-ModulTabungan.strToDouble(diterima);
        }
        String faktur="";
        Cursor cTransaksi=db.sq(QueryTabungan.select("tbltransaksi"));
        cTransaksi.moveToLast();
        if (cTransaksi.getCount()>0){
            if (cTransaksi.getInt(0)<10){
                faktur=ModulTabungan.getResString(this,R.string.fakturproses)+"0"+ModulTabungan.getString(cTransaksi,"idtransaksi");
            }else {
                faktur=ModulTabungan.getResString(this,R.string.fakturproses)+cTransaksi.getString(0);
            }
        }
        String[] pKeuangan={
                tgl,
                faktur,
                getKeterangan(simpanan,anggota,nominal),
                diterima,
                String.valueOf(saldo)
        };
        if (db.exc(QueryTabungan.splitParam("INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,keluar,saldo) VALUES (?,?,?,?,?)",pKeuangan))){

        }else {
            Toast.makeText(this, "Gagal Menambahkan Data Keuangan", Toast.LENGTH_SHORT).show();
        }
    }
    private String getKeterangan(String pelanggan,String nominal,String administrasi){
        String[] a=getResources().getString(R.string.ketproses).split("\\?");
        return a[0]+pelanggan+a[1]+nominal+a[2]+ModulTabungan.numFormat(administrasi);
    }

    public void menuPemasukan(View view) {
        startActivity(new Intent(MenuTransaksiProsesSimpanTabungan.this,MenuTransaksiKeuanganTambahTabungan.class).putExtra("type","pemasukan"));
    }
}

class AdapterListTransaksi extends RecyclerView.Adapter<AdapterListTransaksi.ListTransaksiVH> {
    Context context;
    ArrayList<String> data;
    Boolean fromLaporan;
    DatabaseTabungan db;
    String simpanan;
    String anggota;
    public void setSimpanan(String simpanan,String anggota) {
        this.simpanan = simpanan;
        this.anggota = anggota;
    }

    public AdapterListTransaksi(Context context, ArrayList<String> data, Boolean fromLaporan) {
        this.context = context;
        this.data = data;
        this.fromLaporan = fromLaporan;
    }

    @NonNull
    @Override
    public ListTransaksiVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_transaksi_proses_tabungan, viewGroup, false);
        db = new DatabaseTabungan(context);
        return new ListTransaksiVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTransaksiVH holder, int i) {
        final String[] row = data.get(i).split("__");
        String[] firstRow = data.get(0).split("__");
        holder.mTvTanggal.setText(gettext(R.string.listtransaksiproses_tv1) + ModulTabungan.dateToNormal(row[3]));
        holder.mTvNoTransaksi.setText(gettext(R.string.listtransaksiproses_tv1) + row[2]);
        String ket = row[7].equals("T") ? gettext(R.string.listtransaksiproses_tv3t) : gettext(R.string.listtransaksiproses_tv3b);
        holder.mTvKeterangan.setText(gettext(R.string.listtransaksiproses_tv3) + ket);
        holder.mTvMasuk.setText(gettext(R.string.listtransaksiproses_tv4) + ModulTabungan.removeE(row[4]));
        holder.mTvKeluar.setText(gettext(R.string.listtransaksiproses_tv5) + ModulTabungan.removeE(row[5]));
        holder.mTvSaldo.setText(gettext(R.string.listtransaksiproses_tv6) + ModulTabungan.removeE(row[6]));
        holder.mIbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.confirmdelete)
                        .setMessage(R.string.alerthapus)
                        .setPositiveButton(R.string.hapus, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String qDelete = "DELETE FROM tbltransaksi WHERE idtransaksi=" + row[0];
                                if (db.exc(qDelete)) {
//                                    Cursor qDeleteKeuangan=db.sq(Query.selectwhere("tblkeuangan")+Query.sWhere("faktur",faktur));
//                                    if (qDeleteKeuangan.getCount()>0){
//
//                                    }
                                    String faktur;
                                    if (Integer.valueOf(row[0])<10){
                                        faktur=ModulTabungan.getResString(context,R.string.fakturproses)+"0"+row[0];
                                    }else {
                                        faktur=ModulTabungan.getResString(context,R.string.fakturproses)+row[0];
                                    }
                                    try {
                                        if(row[4] == null){
                                            row[4] = "0";
                                        }
                                        if(ModulTabungan.strToDouble(row[4]) > 0){
                                            insertKeuanganKeluar(ModulTabungan.getDate("yyyyMMdd"),row[4],simpanan,anggota,row[4],row[0]);
                                        }else{
                                            insertKeuanganMasuk(ModulTabungan.getDate("yyyyMMdd"),row[5],simpanan,anggota,row[5],row[0]);
                                        }
                                    }catch (Exception e){

                                    }
                                    Toast.makeText(context, R.string.berhasilhapus, Toast.LENGTH_SHORT).show();
                                    ((MenuTransaksiProsesSimpanTabungan) context).onResume();
                                    ((MenuTransaksiProsesSimpanTabungan) context).getTransaksi(row[1],simpanan,anggota);
                                    ((MenuTransaksiProsesSimpanTabungan) context).refreshList();
                                } else {
                                    Toast.makeText(context, R.string.gagal, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.batal, null)
                        .create();
                dialog.show();
            }
        });
        if (row[0].equals(firstRow[0]) && !fromLaporan) {
            holder.mIbtnDelete.setVisibility(View.VISIBLE);
        }else{
            holder.mIbtnDelete.setVisibility(View.GONE);
        }

    }


    @SuppressLint("Range")
    private void insertKeuanganKeluar(String tgl, String diterima, String simpanan, String anggota, String nominal, String idtransaksi){
        Cursor cKeuangan=db.sq(QueryTabungan.select("tblkeuangan"));
        cKeuangan.moveToLast();
        double saldo=0;
        if (cKeuangan.getCount()>0){
            saldo=cKeuangan.getDouble(cKeuangan.getColumnIndex("saldo"))-ModulTabungan.strToDouble(diterima);
        }else{
            saldo=0-ModulTabungan.strToDouble(diterima);
        }
        String faktur="";
        faktur=ModulTabungan.getResString(context,R.string.fakturproses)+"HAPUS-"+idtransaksi;
        String[] pKeuangan={
                tgl,
                faktur,
                getKeterangan(simpanan,anggota,nominal),
                diterima,
                String.valueOf(saldo)
        };
        if (db.exc(QueryTabungan.splitParam("INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,keluar,saldo) VALUES (?,?,?,?,?)",pKeuangan))){

        }else {
        }
    }
    @SuppressLint("Range")
    private void insertKeuanganMasuk(String tgl, String diterima, String simpanan, String anggota, String nominal, String idtransaksi){
        Cursor cKeuangan=db.sq(QueryTabungan.select("tblkeuangan"));
        cKeuangan.moveToLast();
        double saldo=0;
        if (cKeuangan.getCount()>0){
            saldo=cKeuangan.getDouble(cKeuangan.getColumnIndex("saldo"))+ModulTabungan.strToDouble(diterima);
        }else{
            saldo=0+ModulTabungan.strToDouble(diterima);
        }
        String faktur="";
        faktur=ModulTabungan.getResString(context,R.string.fakturproses)+"HAPUS-"+idtransaksi;
        String[] pKeuangan={
                tgl,
                faktur,
                getKeterangan(simpanan,anggota,nominal),
                diterima,
                String.valueOf(saldo)
        };
        if (db.exc(QueryTabungan.splitParam("INSERT INTO tblkeuangan (tgltransaksi,faktur,keterangantransaksi,masuk,saldo) VALUES (?,?,?,?,?)",pKeuangan))){

        }else {
        }
    }
    private String getKeterangan(String pelanggan,String nominal,String administrasi){
        String[] a=context.getResources().getString(R.string.ketproses).split("\\?");
        return a[0]+pelanggan+a[1]+nominal+a[2]+ModulTabungan.numFormat(administrasi);
    }

    private String gettext(@StringRes int string) {
        return context.getResources().getString(string) + " ";
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ListTransaksiVH extends RecyclerView.ViewHolder {
        private TextView mTvTanggal, mTvNoTransaksi, mTvMasuk, mTvKeluar, mTvSaldo, mTvKeterangan;
        private ImageButton mIbtnDelete;

        public ListTransaksiVH(@NonNull View itemView) {
            super(itemView);
            mTvTanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
            mTvNoTransaksi = (TextView) itemView.findViewById(R.id.tvNoTransaksi);
            mTvMasuk = (TextView) itemView.findViewById(R.id.tvMasuk);
            mTvKeluar = (TextView) itemView.findViewById(R.id.tvKeluar);
            mTvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);
            mTvKeterangan = (TextView) itemView.findViewById(R.id.tvKeterangan);
            mIbtnDelete = (ImageButton) itemView.findViewById(R.id.ibtnDelete);
        }
    }
}
