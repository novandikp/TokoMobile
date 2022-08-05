package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanMobilBagPegawai extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporanmobilbagpegawai);
    }

    public void ExportExcel(View view) {
        Intent intent = new Intent(MenuLaporanMobilBagPegawai.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }
}