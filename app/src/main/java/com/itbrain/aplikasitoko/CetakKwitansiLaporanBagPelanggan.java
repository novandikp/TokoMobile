package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CetakKwitansiLaporanBagPelanggan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cetakkwitansilaporanbagpelanggan);
    }

    public void Excel(View view) {
        Intent intent = new Intent(CetakKwitansiLaporanBagPelanggan.this, MenuLaporanExportExcelCetakKwitansi.class);
        startActivity(intent);
    }
}