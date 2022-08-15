package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TokoSepatuLaporanPenjualanPerUkuran extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toko_sepatu_laporan_penjualan_per_ukuran);
    }

    public void Excel(View view) {
        Intent intent = new Intent(TokoSepatuLaporanPenjualanPerUkuran.this, MenuLaporanExcelTokoSepatu.class);
        startActivity(intent);
    }
}