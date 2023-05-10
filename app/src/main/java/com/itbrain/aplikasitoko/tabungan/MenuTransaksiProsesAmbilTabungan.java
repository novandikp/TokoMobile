package com.itbrain.aplikasitoko.tabungan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MenuTransaksiProsesAmbilTabungan extends AppCompatActivity {

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
        setContentView(R.layout.activity_menu_transaksi_proses_ambil_tabungan);
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
                startActivityForResult(new Intent(MenuTransaksiProsesAmbilTabungan.this, MenuTransaksiCariTabungan.class).putExtra("type", "simpanan"), 2000);
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
                Dialog datePicker = new DatePickerDialog(MenuTransaksiProsesAmbilTabungan.this, new DatePickerDialog.OnDateSetListener() {
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
                    mSwitchBunga.setText(ModulTabungan.getResString(MenuTransaksiProsesAmbilTabungan.this, R.string.menutransaksiproses_switch1) + diff);
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
                        mSwitchBunga.setText(ModulTabungan.getResString(MenuTransaksiProsesAmbilTabungan.this, R.string.menutransaksiproses_switch1) + diff);
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
                                        Toast.makeText(MenuTransaksiProsesAmbilTabungan.this, R.string.berhasil, Toast.LENGTH_SHORT).show();
                                        insertKeuanganMasuk(qBaru[2],qBaru[3],simpanan,mTvAnggota.getText().toString(),qBaru[3]);
                                        confirmCetak(MenuTransaksiProsesAmbilTabungan.this, idsimpanan, mEdtNoTransaksi.getText().toString());
                                    } else {
                                        Toast.makeText(MenuTransaksiProsesAmbilTabungan.this, R.string.gagal, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    String q = QueryTabungan.splitParam("INSERT INTO tbltransaksi (idsimpanan,notransaksi,tgltransaksi,keluar,saldo,kode) VALUES (?,?,?,?,?,?)", qBaru);
                                    if (db.exc(q)) {
                                        Toast.makeText(MenuTransaksiProsesAmbilTabungan.this, R.string.berhasil, Toast.LENGTH_SHORT).show();
                                        insertKeuanganKeluar(qBaru[2],qBaru[3],simpanan,mTvAnggota.getText().toString(),qBaru[3]);
                                        confirmCetak(MenuTransaksiProsesAmbilTabungan.this, idsimpanan, mEdtNoTransaksi.getText().toString());
                                    } else {
                                        Toast.makeText(MenuTransaksiProsesAmbilTabungan.this, R.string.gagal, Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(MenuTransaksiProsesAmbilTabungan.this,MenuTransaksiKeuanganTambahTabungan.class).putExtra("type","pemasukan"));
    }
}

