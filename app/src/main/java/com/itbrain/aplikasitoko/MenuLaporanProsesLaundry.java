package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanProsesLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_proses_laundry);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanProsesLaundry.this, MenuLaporanExcelLaundry.class);
        startActivity(intent);
    }
}