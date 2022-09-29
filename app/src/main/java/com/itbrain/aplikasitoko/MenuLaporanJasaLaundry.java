package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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