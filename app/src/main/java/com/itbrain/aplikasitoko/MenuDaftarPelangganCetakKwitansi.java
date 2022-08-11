package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDaftarPelangganCetakKwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_pelanggan_cetak_kwitansi);
    }

    public void TambahPelanggan(View view) {
        Intent intent = new Intent(MenuDaftarPelangganCetakKwitansi.this, MenuTambahPelangganCetakKwitansi.class);
        startActivity(intent);
    }
}