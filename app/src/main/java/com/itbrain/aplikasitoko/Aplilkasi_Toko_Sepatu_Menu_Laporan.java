package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Aplilkasi_Toko_Sepatu_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplilkasi_toko_sepatu_menu_laporan);
    }

    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanPelanggan.class);
        startActivity(intent);
    }

    public void LaporanBarang(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanBarang.class);
        startActivity(intent);
    }

    public void LaporanPenjualanPerPelanggan(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanPenjualanPerPelanggan.class);
        startActivity(intent);
    }

    public void LaporanPenjualanPerBarang(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanPenjualanPerBarang.class);
        startActivity(intent);
    }

    public void LaporanPenjualanPerKategori(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanPenjualanPerKategori.class);
        startActivity(intent);
    }

    public void LaporanPenjualanPerUkuran(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanPenjualanPerUkuran.class);
        startActivity(intent);
    }

    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanPendapatan.class);
        startActivity(intent);
    }

    public void LaporanHutang(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanHutang.class);
        startActivity(intent);
    }

    public void LaporanDetailHutang(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanDetailHutang.class);
        startActivity(intent);
    }

    public void LaporanReturn(View view) {
        Intent intent = new Intent(Aplilkasi_Toko_Sepatu_Menu_Laporan.this, TokoSepatuLaporanReturn.class);
        startActivity(intent);
    }

}