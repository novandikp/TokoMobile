package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDaftarJenisSimpananTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_jenis_simpanan_tabungan);
    }

    public void Tambah(View view) {
        Intent intent = new Intent(MenuDaftarJenisSimpananTabungan.this, MenuJenisSimpananTabungan.class);
        startActivity(intent);
    }
}