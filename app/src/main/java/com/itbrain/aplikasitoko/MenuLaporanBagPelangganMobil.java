package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanBagPelangganMobil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_bag_pelanggan_mobil);
    }

    public void ExportExcel(View view) {
        Intent intent = new Intent(MenuLaporanBagPelangganMobil.this, MenuLaporanExportExcelMobil.class);
        startActivity(intent);
    }
}