package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanJasaLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_jasa_laundry);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanJasaLaundry.this, MenuLaporanExcelLaundry.class);
        startActivity(intent);
    }
}