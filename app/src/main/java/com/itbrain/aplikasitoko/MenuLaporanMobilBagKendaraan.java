package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanMobilBagKendaraan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_mobil_bag_kendaraan);
    }

    public void ExportExcel(View view) {
        Intent intent = new Intent(MenuLaporanMobilBagKendaraan.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }
}