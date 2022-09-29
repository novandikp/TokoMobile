package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CetakKwitansiMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cetakkwitansimenulaporan);
    }

    public void LaporanJasa(View view) {
        Intent intent = new Intent(CetakKwitansiMenuLaporan.this, CetakKwitansiLaporanBagJasa.class);
        startActivity(intent);
    }

    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(CetakKwitansiMenuLaporan.this, CetakKwitansiLaporanBagPelanggan.class);
        startActivity(intent);
    }

    public void LaporanTransaksi(View view) {
        Intent intent = new Intent(CetakKwitansiMenuLaporan.this, CetakKwitansiLaporanBagTransaksi.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( CetakKwitansiMenuLaporan.this, CetakKwitansiMenuMasterUtama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}