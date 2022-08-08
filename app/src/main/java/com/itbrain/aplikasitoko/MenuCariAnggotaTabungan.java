package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuCariAnggotaTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_cari_anggota_tabungan);
    }

    public void TambahDataAnggota(View view) {
        Intent intent = new Intent(MenuCariAnggotaTabungan.this, MenuTambahDataAnggotaTabungan.class);
        startActivity(intent);
    }

    public void BuatSimpanan(View view) {
        Intent intent = new Intent(MenuCariAnggotaTabungan.this, MenuBuatSimpanantabungan.class);
        startActivity(intent);
    }
}