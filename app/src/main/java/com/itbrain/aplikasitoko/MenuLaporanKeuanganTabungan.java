package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanKeuanganTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporankeuangantabungan);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanKeuanganTabungan.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }
}