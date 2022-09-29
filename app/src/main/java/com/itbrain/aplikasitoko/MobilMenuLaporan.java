package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MobilMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilmenulaporan);
    }

    public void LaporanKendaraan(View view) {
        Intent intent = new Intent(MobilMenuLaporan.this, DaftarLaporanKendaraanMobil.class);
        startActivity(intent);
    }

    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(MobilMenuLaporan.this, MenuLaporanBagPelangganMobil.class);
        startActivity(intent);
    }

    public void LaporanPegawai(View view) {
        Intent intent = new Intent(MobilMenuLaporan.this, MenuLaporanMobilBagPegawai.class);
        startActivity(intent);
    }

    public void LaporanRental(View view) {
        Intent intent = new Intent(MobilMenuLaporan.this, MenuLaporanMobilBagRental.class);
        startActivity(intent);
    }

    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(MobilMenuLaporan.this, MenuLaporanMobilBagPendapatan.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MobilMenuLaporan.this, MobilMenuUtamaMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}