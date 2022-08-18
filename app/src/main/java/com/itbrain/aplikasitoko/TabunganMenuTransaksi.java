package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TabunganMenuTransaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabunganmenutransaksi);
    }

    public void BuatSimpanan(View view) {
        Intent intent = new Intent(TabunganMenuTransaksi.this, MenuDaftarSimpananTabungan.class);
        startActivity(intent);
    }

    public void ProsesSimpan(View view) {
        Intent intent = new Intent(TabunganMenuTransaksi.this, MenuProsesSimpanTabungan.class);
        startActivity(intent);
    }

    public void ProsesAmbil(View view) {
        Intent intent = new Intent(TabunganMenuTransaksi.this, MenuProsesAmbilTabungan.class);
        startActivity(intent);
    }

    public void Keuangan(View view) {
        Intent intent = new Intent(TabunganMenuTransaksi.this, MenuDetailKeuanganTabungan.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( TabunganMenuTransaksi.this, TabunganMenuMasterUtama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}