package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CetakKwitansiLaporanBagJasa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cetakkwitansilaporanbagjasa);
    }

    public void Excel(View view) {
        Intent intent = new Intent(CetakKwitansiLaporanBagJasa.this, MenuLaporanExportExcelCetakKwitansi.class);
        startActivity(intent);
    }
}