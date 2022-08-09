package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaundryMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenulaporan);
    }
    public void LaporanJasa(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, .class);
        startActivity(intent);
    }
    public void LaporanPegawai(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanLaundry.class);
        startActivity(intent);
    }
    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanLaundry.class);
        startActivity(intent);
    }
    public void LaporanLaundry(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanLaundry.class);
        startActivity(intent);
    }
    public void LaporanProsesLaundry(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuBayarHutangLaundry.class);
        startActivity(intent);
    }
    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuBayarHutangLaundry.class);
        startActivity(intent);
    }
}