package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TokoSepatuLaporanReturn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toko_sepatu_laporan_return);
    }

    public void Excel(View view) {
        Intent intent = new Intent(TokoSepatuLaporanReturn.this, MenuLaporanExcelTokoSepatu.class);
        startActivity(intent);
    }
}