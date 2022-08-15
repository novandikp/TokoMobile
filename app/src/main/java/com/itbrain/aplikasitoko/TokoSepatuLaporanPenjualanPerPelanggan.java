package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TokoSepatuLaporanPenjualanPerPelanggan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toko_sepatu_laporan_penjualan_per_pelanggan);
    }

    public void Excel(View view) {
        Intent intent = new Intent(TokoSepatuLaporanPenjualanPerPelanggan.this, MenuLaporanExcelTokoSepatu.class);
        startActivity(intent);
    }
}