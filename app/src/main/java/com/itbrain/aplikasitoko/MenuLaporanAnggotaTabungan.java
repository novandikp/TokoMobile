package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanAnggotaTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporananggotatabungan);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanAnggotaTabungan.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }
}