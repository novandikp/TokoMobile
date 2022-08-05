package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDaftarAnggotaTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_anggota_tabungan);
    }

    public void TambahDataAnggota(View view) {
        Intent intent = new Intent(MenuDaftarAnggotaTabungan.this, MenuAnggotaTabungan.class);
        startActivity(intent);
    }
}