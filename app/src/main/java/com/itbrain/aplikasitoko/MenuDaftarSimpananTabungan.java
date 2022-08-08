package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDaftarSimpananTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_simpanan_tabungan);
    }

    public void BuatSimpanan(View view) {
        Intent intent = new Intent(MenuDaftarSimpananTabungan.this, MenuBuatSimpanantabungan.class);
        startActivity(intent);
    }
}