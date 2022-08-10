package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPelangganPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pelanggan_pos_toko_kredit);
    }

    public void TambahPelanggan(View view) {
        Intent intent = new Intent(MenuPelangganPosTokoKredit.this, MenuTambahPelangganPosTokoKredit.class);
        startActivity(intent);
    }
}