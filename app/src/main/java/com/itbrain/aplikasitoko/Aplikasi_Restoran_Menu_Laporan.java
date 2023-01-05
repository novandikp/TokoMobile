package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.databinding.AplikasiRestoranMenuLaporanBinding;

public class Aplikasi_Restoran_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_restoran_menu_laporan);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahKeuangan(View view) {
        Intent intent = new Intent(Aplikasi_Restoran_Menu_Laporan.this, Laporan_Keuangan_Restoran_.class);
        startActivity(intent);
    }

    public void PindahLaporanPenjualanFaktur(View view) {
        Intent intent = new Intent(Aplikasi_Restoran_Menu_Laporan.this, Laporan_Penjualan_Per_Fakturn_Restoran_.class);
        startActivity(intent);
    }

    public void PindahPenjualanMakanan(View view) {
        Intent intent = new Intent(Aplikasi_Restoran_Menu_Laporan.this, Laporan_Penjualan_Makanan_Restoran_.class);
        startActivity(intent);
    }

    public void PindahMakanan(View view) {
        Intent intent = new Intent(Aplikasi_Restoran_Menu_Laporan.this, Laporan_Makanan_Restoran_.class);
        startActivity(intent);
    }

    public void PindahKategori(View view) {
        Intent intent = new Intent(Aplikasi_Restoran_Menu_Laporan.this, Laporan_Kategori_Restoran_.class);
        startActivity(intent);
    }

    public void PindahMeja(View view) {
        Intent intent = new Intent(Aplikasi_Restoran_Menu_Laporan.this, Laporan_Meja_Restoran_.class);
        startActivity(intent);
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Restoran_Menu_Laporan.this, Laporan_Pelanggan_Restoran_.class);
        startActivity(intent);
    }
}