package com.itbrain.aplikasitoko.Salon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;

public class ActivityPembayaranProsesSalon extends AppCompatActivity {

    Toolbar appbar;
    View v;
    DatabaseSalon db;
    SharedPreferences getPrefs ;
    ConfigSalon config, temp;
    double pay, cashback;
    String faktur, jumlah, pelanggan, deviceid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_proses_salon);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        FunctionSalon.btnBack("Pembayaran",getSupportActionBar());

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //deviceid = FunctionSalon.getDecrypt(getPrefs.getString("deviceid","")) ;

        v = this.findViewById(android.R.id.content);
        config = new ConfigSalon(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ConfigSalon(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseSalon(this);
        pay = 0;

        faktur = getIntent().getStringExtra("faktur");
        FunctionSalon.setText(v, R.id.faktur, faktur);

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
        setText();
        calculate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void calculate() {
        double masuk = FunctionSalon.strToDouble(FunctionSalon.getText(v, R.id.jumlahbayar));
        double jum = FunctionSalon.strToDouble(jumlah);
        if (masuk > jum) {
            double kembali = masuk - jum;
            cashback = kembali;
            FunctionSalon.setText(v, R.id.kembali, FunctionSalon.removeE(kembali));
        } else if (masuk == jum) {
            double kembali = 0;
            cashback = kembali;
            FunctionSalon.setText(v, R.id.kembali, FunctionSalon.removeE(kembali));
        } else {
            double kembali = jum - masuk;
            cashback = kembali;
            FunctionSalon.setText(v, R.id.kembali, "-" + FunctionSalon.removeE(kembali));
        }
    }

    public void setText() {
        Cursor c = db.sq(QuerySalon.selectwhere("tblorder") + QuerySalon.sWhere("faktur", faktur));
        pelanggan = temp.getCustom("idpelanggan", "");
        if (FunctionSalon.getCount(c) > 0) {
            c.moveToNext();
            pay = FunctionSalon.strToDouble(FunctionSalon.getString(c, "total"));
            jumlah = FunctionSalon.getString(c, "total");
            FunctionSalon.setText(v, R.id.totalbayar, FunctionSalon.removeE(jumlah));
        } else {
            Intent i = new Intent(this, Form_Pembayaran_Langsung_Salon_.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void bayar(View view) {
        String[] p = {FunctionSalon.getText(v, R.id.jumlahbayar), FunctionSalon.doubleToStr(cashback), faktur};
        String q = QuerySalon.splitParam("UPDATE tblorder SET bayar=?,kembali=? WHERE faktur=? ",p);

        double masuk = FunctionSalon.strToDouble(FunctionSalon.getText(v, R.id.jumlahbayar));
        double jum = FunctionSalon.strToDouble(jumlah);
        if (masuk < jum) {
            Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
        } else {
            if (db.exc(q)) {
                temp.setCustom("idpelanggan", "");
                temp.setCustom("faktur", "");
                temp.setCustom("tglorder", "");
                tambahlimit();
                open();
            } else {
                Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void open() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda Ingin Mencetak Struk?");
        builder.setPositiveButton("Cetak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        Intent i = new Intent(ActivityPembayaranProsesSalon.this, MenuCetakSalon.class);
                        i.putExtra("faktur", faktur);
                        i.putExtra("type", "bayar");
                        startActivity(i);
                    }
                });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityPembayaranProsesSalon.this, Aplikasi_Salon_Menu_Transaksi.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        builder.show();
    }

    private void tambahlimit(){
        boolean status = Aplikasi_Salon_Menu_Transaksi.status;
        if (!status){
            int batas = FunctionSalon.strToInt(config.getCustom("bayar", "1"))+1;
            config.setCustom("bayar", FunctionSalon.intToStr(batas));
        }
    }
}
