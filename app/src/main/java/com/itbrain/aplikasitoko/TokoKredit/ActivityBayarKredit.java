
package com.itbrain.aplikasitoko.TokoKredit;

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

import com.itbrain.aplikasitoko.R;

public class ActivityBayarKredit extends AppCompatActivity {

    String faktur, jumlah, pelanggan;
    double cashback, pay;
    View v;
    FConfigKredit config, temp;
    FKoneksiKredit db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        temp = new FConfigKredit(getSharedPreferences("temp", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        pay = 0;

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        faktur = getIntent().getStringExtra("faktur");
        FFunctionKredit.setText(v, R.id.faktur, faktur);

        setText();
        calculate();

        final EditText in = findViewById(R.id.jumlahbayar);
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
        in.requestFocus();
    }

    public void calculate() {
        if (FFunctionKredit.getText(v, R.id.jumlahbayar).length() == 0) {
            double jumlahBayar = FFunctionKredit.strToDouble(jumlah);
            double cashback = 0 - jumlahBayar;
            FFunctionKredit.setText(v, R.id.kembali, FFunctionKredit.removeE(cashback));
        } else {
            double masuk = FFunctionKredit.strToDouble(FFunctionKredit.getText(v, R.id.jumlahbayar));
            double jum = FFunctionKredit.strToDouble(jumlah);
            if (masuk > jum) {
                double kembali = masuk - jum;
                cashback = kembali;
                FFunctionKredit.setText(v, R.id.kembali, FFunctionKredit.removeE(kembali));
            } else if (masuk == jum) {
                cashback = 0;
                FFunctionKredit.setText(v, R.id.kembali, "0");
            } else {
                double kembali = jum - masuk;
                cashback = kembali;
                FFunctionKredit.setText(v, R.id.kembali, "-" + FFunctionKredit.removeE(kembali));
            }
        }
    }

    public void setText() {
        Cursor c = db.sq(FQueryKredit.selectwhere("tblbayar") + FQueryKredit.sWhere("fakturbayar", faktur));
        pelanggan = temp.getCustom("idpelanggan", "");
        if (c.getCount() > 0) {
            c.moveToNext();
            pay = FFunctionKredit.strToDouble(FFunctionKredit.getString(c, "jumlahbayar"));
            jumlah = FFunctionKredit.getString(c, "jumlahbayar");
            FFunctionKredit.setText(v, R.id.totalbayar, FFunctionKredit.removeE(jumlah));
        } else {
            Intent i = new Intent(this, ActivityPenjualanTunaiKredit.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void bayar(View view) {
        Spinner met = findViewById(R.id.spinner);
        String pilih = met.getSelectedItem().toString();

        String metod = "";
        switch (pilih) {
            case "Tunai":
                metod = "1";
                break;
            case "Hutang":
                metod = "0";
                break;
            default:
                Toast.makeText(this, "Silahkan Pilih Metode Pembayaran", Toast.LENGTH_SHORT).show();
                break;
        }

        String[] p = {FFunctionKredit.getDate("yyyy-MM-dd"), FFunctionKredit.getText(v, R.id.jumlahbayar), FFunctionKredit.doubleToStr(cashback), FFunctionKredit.getText(v, R.id.keterangan), faktur};
        String q = FQueryKredit.splitParam("UPDATE tblbayar SET tglbayar=?,bayar=?,kembali=?,flagbayar=" + metod + ",keterangan=? WHERE fakturbayar=?  ", p);

        double masuk = FFunctionKredit.strToDouble(FFunctionKredit.getText(v, R.id.jumlahbayar));
        double jum = FFunctionKredit.strToDouble(jumlah);
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

        if (metod.equals("1")) {
            // Keuangan
            Cursor tblbayar = db.sq("SELECT * FROM tblbayar WHERE fakturbayar = '" + faktur + "'");
            Cursor tblkeuangan = db.sq("SELECT * FROM tblkeuangan ORDER BY no_transaksi DESC");
            tblbayar.moveToFirst();

            int no_transaksi = 1;
            double jumlahMasuk = tblbayar.getDouble(tblbayar.getColumnIndex("jumlahbayar"));
            double saldoBaru = jumlahMasuk;
            if (tblkeuangan.getCount() > 0) {
                tblkeuangan.moveToFirst();
                no_transaksi = tblkeuangan.getInt(tblkeuangan.getColumnIndex("no_transaksi")) + 1;

                double saldoterakhir = tblkeuangan.getDouble(tblkeuangan.getColumnIndex("saldo"));
                saldoBaru = saldoterakhir + jumlahMasuk;
            }
            String keuangan = "INSERT INTO tblkeuangan(no_transaksi, tglkeuangan, faktur, keterangan, masuk, keluar, saldo) " +
                    "VALUES(" + no_transaksi + ", '" + ModuleKredit.getCurrentDate() + "', '" + faktur + "', 'Penjualan', " + jumlahMasuk + ", 0, " + saldoBaru + " )";
            db.exc(keuangan);
        }
    }

    public void open() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda ingin untuk cetak Struk ?");
        alertDialogBuilder.setPositiveButton("Cetak", (arg0, arg1) -> {
            //yes
            Intent i = new Intent(this, ActivityCetakSatuKredit.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("fakturbayar", faktur);
            i.putExtra("owner", ActivityPenjualanTunaiKredit.class);
            startActivity(i);
        });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityBayarKredit.this, ActivityPenjualanTunaiKredit.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}