package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LaundryMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenulaporan);
    }
    public void LaporanJasa(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanJasaLaundry.class);
        startActivity(intent);
    }
    public void LaporanPegawai(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanPegawaiLaundry.class);
        startActivity(intent);
    }
    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanPelangganLaundry.class);
        startActivity(intent);
    }
    public void LaporanLaundry(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanLaundry.class);
        startActivity(intent);
    }
    public void LaporanProsesLaundry(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanProsesLaundry.class);
        startActivity(intent);
    }
    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanPendapatanLaundry.class);
        startActivity(intent);
    }
    public void LaporanHutang(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanHutangLaundry.class);
        startActivity(intent);
    }
}