package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuKategoriTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_kategori_toko_sepatu);
    }

    public void TambahKategori(View view) {
        Intent intent = new Intent(MenuKategoriTokoSepatu.this, MenuTambahkategoriTokoSepatu.class);
        startActivity(intent);
    }
}