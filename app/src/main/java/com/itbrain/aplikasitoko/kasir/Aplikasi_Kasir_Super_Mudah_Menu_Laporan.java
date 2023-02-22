package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Kasir_Super_Mudah_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_kasir_super_mudah_menu_laporan);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Pelanggan_Kasir_.class);
        startActivity(intent);
    }

    public void LaporanBarang(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Barang_Kasir_.class);
        startActivity(intent);
    }

    public void LaporanPenjualanperPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Penjualan_per_Pelanggan_Kasir_.class);
        startActivity(intent);
    }

    public void LaporanPenjualanperBarang(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Penjualan_per_Barang_Kasir.class);
        startActivity(intent);
    }

    public void LaporanPenjualanperKategori(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Penjualan_per_Kategori_Kasir.class);
        startActivity(intent);
    }

    public void LaporanPenjualanperJenis(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Penjualan_per_Jenis_Kasir.class);
        startActivity(intent);
    }

    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Pendapatan_Kasir_.class);
        startActivity(intent);
    }

    public void LaporanReturn(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Return_Kasir_.class);
        startActivity(intent);
    }

    public void LaporanLabaRugi(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Laba_Rugi_Kasir_.class);
        startActivity(intent);
    }

    public void LaporanHutang(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Laporan.this, Laporan_Hutang_Kasir_.class);
        startActivity(intent);
    }
}