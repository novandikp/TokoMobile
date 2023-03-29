package com.itbrain.aplikasitoko.tokosepatu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Menu_Tambah_Pelanggan_Toko_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config, temp;
    DatabaseTokoSepatu db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tambah_pelanggan_sepatu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Pelanggan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new ModulTokoSepatu(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseTokoSepatu(this);
        v = this.findViewById(android.R.id.content);
    }


    private void tambahpelanggan() {
        String nama = ModulTokoSepatu.getText(v, R.id.ePelanggan);
        String alamat = ModulTokoSepatu.getText(v, R.id.eAlamat);
        String notelp = ModulTokoSepatu.getText(v, R.id.eNoTelp);


        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(notelp)) {
            String[] p = {ModulTokoSepatu.upperCaseFirst(nama), ModulTokoSepatu.upperCaseFirst(alamat), notelp};
            String q = ModulTokoSepatu.splitParam("INSERT INTO tblpelanggan (pelanggan,alamat,notelp) values(?,?,?)", p);
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
