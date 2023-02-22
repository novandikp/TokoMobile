package com.itbrain.aplikasitoko.kasir;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.Form_Cetak_Struk;
import com.itbrain.aplikasitoko.R;

public class ActivityBayarKasir extends AppCompatActivity {

    String faktur, jumlah, pelanggan;
    double cashback, pay;
    View v;
    FConfigKasir config, temp;
    DatabaseKasir db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_kasir);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        pay = 0;


        faktur = getIntent().getStringExtra("faktur");
        FFunctionKasir.setText(v, R.id.faktur, faktur);

        setText();
        calculate();

        ImageButton imageButton = findViewById(R.id.kembali3);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Fokus ke jumlahbayar
        EditText eJmlBayar = (EditText) findViewById(R.id.jumlahbayar);
        eJmlBayar.requestFocus();

        final EditText in = (EditText) findViewById(R.id.jumlahbayar);
        in.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
    }


    public void cetak(View view) {
        startActivity(new Intent(this,ActivityCetakKasir.class));
    }

    public void calculate() {
        double masuk = FFunctionKasir.strToDouble(FFunctionKasir.getText(v, R.id.jumlahbayar));
        double jum = FFunctionKasir.strToDouble(jumlah);
        if (masuk > jum) {
            double kembali = masuk - jum;
            cashback = kembali;
            FFunctionKasir.setText(v, R.id.kembali, FFunctionKasir.removeE(kembali));
        } else if (masuk == jum) {
            cashback = 0;
            FFunctionKasir.setText(v, R.id.kembali, "0");
        } else {
            double kembali = jum - masuk;
            cashback = kembali;
            FFunctionKasir.setText(v, R.id.kembali, "-" + FFunctionKasir.removeE(kembali));
        }
    }

    public void setText() {
        Cursor c = db.sq(FQueryKasir.selectwhere("tblbayar") + FQueryKasir.sWhere("fakturbayar", faktur));
        pelanggan = temp.getCustom("idpelanggan", "");
        if (FFunctionKasir.getCount(c) > 0) {
            c.moveToNext();
            pay = FFunctionKasir.strToDouble(FFunctionKasir.getString(c, "jumlahbayar"));
            jumlah = FFunctionKasir.getString(c, "jumlahbayar");
            FFunctionKasir.setText(v, R.id.totalbayar, FFunctionKasir.removeE(jumlah));
        } else {
            Intent i = new Intent(this, Form_Penjualan_Kasir_.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void bayarPenjualan(View view) {
        Spinner met = (Spinner) findViewById(R.id.spinnerPenjualan);
        String pilih = met.getSelectedItem().toString();

        String metod = "";
        if (pilih.equals("Tunai")) {
            metod = "1";
        } else if (pilih.equals("Hutang")) {
            metod = "0";
        } else {
            Toast.makeText(this, "Silahkan Pilih Metode Pembayaran", Toast.LENGTH_SHORT).show();
        }

        String[] p = {FFunctionKasir.getDate("yyyyMMdd"), FFunctionKasir.getText(v, R.id.jumlahbayar), FFunctionKasir.doubleToStr(cashback), FFunctionKasir.getText(v, R.id.keterangan), faktur};
        String q = FQueryKasir.splitParam("UPDATE tblbayar SET tglbayar=?,bayar=?,kembali=?,flagbayar=" + metod + ",keterangan=? WHERE fakturbayar=?  ", p);

        double masuk = FFunctionKasir.strToDouble(FFunctionKasir.getText(v, R.id.jumlahbayar));
        double jum = FFunctionKasir.strToDouble(jumlah);
        if (metod.equals("1") && masuk < jum) {
            Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
        } else if (metod.equals("0") && masuk >= jum) {
            Toast.makeText(this, "Uang Pembayaran lebih, silahkan menggunakan metode pembayaran tunai", Toast.LENGTH_SHORT).show();
        } else if (pelanggan.equals("1") && metod.equals("0")) {
            Toast.makeText(this, "Pelanggan Kosong Tidak boleh hutang", Toast.LENGTH_SHORT).show();
        } else {
            if (db.exc(q)) {
                temp.setCustom("idpelanggan", "");
                temp.setCustom("idbarang", "");
                temp.setCustom("tanggal", "");
                temp.setCustom("fakturbayar", "");

                open();
            } else {
                Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void open() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda ingin untuk cetak Struk ?");
        alertDialogBuilder.setPositiveButton("Cetak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        Intent i = new Intent(ActivityBayarKasir.this, ActivityCetakKasir.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("fakturbayar", faktur);
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityBayarKasir.this, Form_Penjualan_Kasir_.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}