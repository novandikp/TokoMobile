package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuDaftarJasaCetakKwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_jasa_cetak_kwitansi);
    }

    public void TambahJasa(View view) {
        Intent intent = new Intent(MenuDaftarJasaCetakKwitansi.this, MenuTambahJasaAntrianPortable.class);
        startActivity(intent);
    }
}