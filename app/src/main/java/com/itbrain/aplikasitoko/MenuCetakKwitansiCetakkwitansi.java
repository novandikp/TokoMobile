package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuCetakKwitansiCetakkwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_cetak_kwitansi_cetakkwitansi);
    }

    public void Cetak(View view) {
        Intent intent = new Intent(MenuCetakKwitansiCetakkwitansi.this, CetakKwitansiMenuLaporan.class);
        startActivity(intent);
    }

    public void Simpan(View view) {
        Intent intent = new Intent(MenuCetakKwitansiCetakkwitansi.this, CetakKwitansiMenuLaporan.class);
        startActivity(intent);
    }
}