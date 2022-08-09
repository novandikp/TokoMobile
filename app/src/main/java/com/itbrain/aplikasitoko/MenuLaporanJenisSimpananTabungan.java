package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanJenisSimpananTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_jenis_simpanan_tabungan);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanJenisSimpananTabungan.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }
}