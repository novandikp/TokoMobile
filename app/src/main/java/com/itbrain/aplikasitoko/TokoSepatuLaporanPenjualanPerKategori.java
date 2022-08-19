package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TokoSepatuLaporanPenjualanPerKategori extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toko_sepatu_laporan_penjualan_per_kategori);
    }

    public void Excel(View view) {
        Intent intent = new Intent(TokoSepatuLaporanPenjualanPerKategori.this, MenuLaporanExcelTokoSepatu.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( TokoSepatuLaporanPenjualanPerKategori.this, Aplikasi_Toko_Sepatu_Menu_Laporan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}