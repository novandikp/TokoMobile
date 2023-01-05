package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Aplikasi_Toko_Kain_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_toko_kain_menu_laporan);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Laporan.this, Laporan_Pelanggan_Toko_Kain.class);
        startActivity(intent);
    }

    public void LaporanBarang(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Laporan.this, Laporan_Barang_Toko_Kain_.class);
        startActivity(intent);
    }

    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Laporan.this, Laporan_Pendapatan_Toko_Kain_.class);
        startActivity(intent);
    }

    public void LaporanOrder(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Laporan.this, Laporan_Order_Toko_Kain_.class);
        startActivity(intent);
    }
}