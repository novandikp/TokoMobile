package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DaftarKendaraanMobil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_kendaraan_mobil);
    }

    public void Tambah(View view) {
        Intent intent = new Intent(DaftarKendaraanMobil.this, MenuKendaraanMobil.class);
        startActivity(intent);
    }
}