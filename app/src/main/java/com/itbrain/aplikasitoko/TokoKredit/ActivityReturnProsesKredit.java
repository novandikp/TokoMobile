package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class ActivityReturnProsesKredit extends AppCompatActivity {

    String idpenjualan, idbarang, faktur, jumlah;
    View v;
    FConfigKredit config;
    FKoneksiKredit db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_proses_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);

        idpenjualan = getIntent().getStringExtra("idpenjualan");
        setText();
    }

    public void setText() {
        Cursor c = db.sq(FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("idpenjualan", idpenjualan));
        c.moveToNext();
        idbarang = FFunctionKredit.getString(c, "idbarang:1");
        String barang = FFunctionKredit.getString(c, "barang");
        jumlah = FFunctionKredit.getString(c, "jumlahjual");
        faktur = FFunctionKredit.getString(c, "fakturbayar");

        FFunctionKredit.setText(v, R.id.eJumBarang, jumlah);
        FFunctionKredit.setText(v, R.id.efaktur, faktur);
        FFunctionKredit.setText(v, R.id.eBarang, barang);
    }

    public void proses(View view) {
        String tgl = FFunctionKredit.getDate("yyyy-MM-dd");
        String jum = FFunctionKredit.getText(v, R.id.eJumReturn);
        int hasil = FFunctionKredit.strToInt(jumlah) - FFunctionKredit.strToInt(jum);
        if (FFunctionKredit.strToInt(jum) <= FFunctionKredit.strToInt(jumlah)) {
            if (!TextUtils.isEmpty(jum)) {
                String[] p = {idbarang,
                        faktur,
                        tgl,
                        jum};
                String q = FQueryKredit.splitParam("INSERT INTO tblreturn (idbarang,fakturbayar,tglreturn,jumlah)VALUES (?,?,?,?)", p);
                String q1 = "UPDATE tblpenjualan SET jumlahjual=" + FFunctionKredit.quote(FFunctionKredit.intToStr(hasil)) + " WHERE idpenjualan=" + idpenjualan;
                if (db.exc(q) && db.exc(q1)) {
                    Toast.makeText(this, "Return Berhasil", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, ActivityReturnKredit.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Return Gagal", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Mohon isi dengan benar", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Jumlah Barang melebihi pemesanan", Toast.LENGTH_SHORT).show();
        }

    }
}
