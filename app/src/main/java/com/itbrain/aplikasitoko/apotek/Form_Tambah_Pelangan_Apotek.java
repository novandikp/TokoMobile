package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Form_Tambah_Pelangan_Apotek extends AppCompatActivity {
    ModulApotek config, temp;
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_pelanggan_apotek);

        config = new ModulApotek(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulApotek(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
    }


    private void tambahpelanggan() {
        String nama = ModulApotek.getText(v, R.id.nPelanggan);
        String alamat = ModulApotek.getText(v, R.id.aPelanggan);
        String notelp = ModulApotek.getText(v, R.id.nTelepon);


        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp)) {
            String[] p = {ModulApotek.upperCaseFirst(nama), ModulApotek.upperCaseFirst(alamat), notelp};
            String q = ModulApotek.splitParam("INSERT INTO tblpelanggan (pelanggan,alamat,notelp) values(?,?,?)", p);
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

    public void simpann(View view) {
        tambahpelanggan();
    }
}