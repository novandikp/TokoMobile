package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class
MenuLaporanMobilBagPendapatan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporanmobilbagpendapatan);
    }

    public void ExportExcel(View view) {
        Intent intent = new Intent(MenuLaporanMobilBagPendapatan.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }
}