package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuKategoriPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_kategori_pos_toko_kredit);
    }

    public void TambahKategori(View view) {
        Intent intent = new Intent(MenuKategoriPosTokoKredit.this, MenuTambahKategoriPosTokoKredit.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuKategoriPosTokoKredit.this, PosTokoKreditMenuMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}