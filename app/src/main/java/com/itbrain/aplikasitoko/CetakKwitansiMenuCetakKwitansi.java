package com.itbrain.aplikasitoko;

import static com.itbrain.aplikasitoko.R.layout.menu_cetak_kwitansi_cetakkwitansi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CetakKwitansiMenuCetakKwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(menu_cetak_kwitansi_cetakkwitansi);
    }

    public void Cetak(View view) {
        Intent intent = new Intent(CetakKwitansiMenuCetakKwitansi.this, MenuCetakCetakKwitansi.class);
        startActivity(intent);
    }
}