package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TabunganMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabungan_laporan_master);
    }

    public void LaporanKeuangan(View view) {
        Intent intent = new Intent(TabunganMenuLaporan.this, MenuLaporanKeuanganTabungan.class);
        startActivity(intent);
    }

    public void LaporanAnggota(View view) {
        Intent intent = new Intent(TabunganMenuLaporan.this, MenuLaporanAnggotaTabungan.class);
        startActivity(intent);
    }

    public void LaporanJenisSimpanan(View view) {
        Intent intent = new Intent(TabunganMenuLaporan.this, MenuLaporanJenisSimpananTabungan.class);
        startActivity(intent);
    }

    public void LaporanSimpanan(View view) {
        Intent intent = new Intent(TabunganMenuLaporan.this, MenulaporanSimpananTabungan.class);
        startActivity(intent);
    }

    public void LaporanTransaksiPerAnggota(View view) {
        Intent intent = new Intent(TabunganMenuLaporan.this, MenuLaporanTransaksiPerAnggotaTabungan.class);
        startActivity(intent);
    }
}