package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class AplikasiKlinik_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_klinik_menu_laporan);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void LaporanPasien(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Laporan.this, Laporan_Pasien_Klinik_.class);
        startActivity(intent);
    }

    public void LaporanBagiHasil(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Laporan.this, Laporan_Bagi_Hasil_Klinik_.class);
        startActivity(intent);
    }

    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Laporan.this, Laporan_Pendapatan_Klinik_.class);
        startActivity(intent);
    }

    public void LaporanPeriksa(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Laporan.this, Laporan_Periksa_Klinik_.class);
        startActivity(intent);
    }

    public void LaporanJasa(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Laporan.this, Laporan_Jasa_Klinik_.class);
        startActivity(intent);
    }

    public void LaporanDokter(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Laporan.this, Laporan_Dokter_Klinik_.class);
        startActivity(intent);
    }
}