package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPelangganTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pelanggan_toko_sepatu);
    }

    public void Kategori(View view) {
        Intent intent = new Intent(MenuPelangganTokoSepatu.this, MenuTambahPelangganTokoSepatu.class);
        startActivity(intent);
    }
}