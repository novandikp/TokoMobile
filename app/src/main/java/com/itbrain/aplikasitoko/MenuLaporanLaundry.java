package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_laundry);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanLaundry.this, MenuLaporanExcelLaundry.class);
        startActivity(intent);
    }
}