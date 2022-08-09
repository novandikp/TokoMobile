package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanPegawaiLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_pegawai_laundry);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanPegawaiLaundry.this, MenuLaporanExcelLaundry.class);
        startActivity(intent);
    }
}