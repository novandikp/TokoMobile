package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuBarangTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_barang_toko_sepatu);
    }

    public void TambahBarang(View view) {
        Intent intent = new Intent(MenuBarangTokoSepatu.this, MenuTambahBarangTokoSepatu.class);
        startActivity(intent);
    }
}