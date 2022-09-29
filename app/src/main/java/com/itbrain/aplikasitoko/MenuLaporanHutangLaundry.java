package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuLaporanHutangLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_hutang_laundry);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanHutangLaundry.this, MenuLaporanExcelLaundry.class);
        startActivity(intent);
    }
}