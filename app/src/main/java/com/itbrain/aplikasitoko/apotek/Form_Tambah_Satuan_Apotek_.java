package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Form_Tambah_Satuan_Apotek_ extends AppCompatActivity {

    ModulApotek config, temp;
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    boolean stat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_satuan_apotek_);
        v=this.findViewById(android.R.id.content);
        db=new DatabaseApotek(this);
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void tambahpelanggan() {
        String satuanbesar = ModulApotek.getText(v, R.id.eSatuanBesar).toString();
        String satuankecil = ModulApotek.getText(v, R.id.eSatuanKecil).toString();
        String nilai = ModulApotek.unNumberFormat(ModulApotek.getText(v, R.id.eNilai));


        if (!TextUtils.isEmpty(satuanbesar) && !TextUtils.isEmpty(satuankecil) && !TextUtils.isEmpty(nilai)) {
            String[] p = {satuanbesar, satuankecil, nilai};
            String q = ModulApotek.splitParam("INSERT INTO tblsatuan (satuanbesar,satuankecil,nilai) values(?,?,?)", p);
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal Menambah " + ", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void simpan(View view) {
        tambahpelanggan();
    }


}
