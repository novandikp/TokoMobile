package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuBarangPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_barang_pos_toko_kredit);
    }

    public void TambahBarang(View view) {
        Intent intent = new Intent(MenuBarangPosTokoKredit.this, MenuTambahBarangPosTokoKredit.class);
        startActivity(intent);
    }
}