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

    public void Kembali(View view) {
        Intent intent = new Intent( MenuKategoriTokoSepatu.this, Aplikasi_Toko_Sepatu_Menu_Master.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}