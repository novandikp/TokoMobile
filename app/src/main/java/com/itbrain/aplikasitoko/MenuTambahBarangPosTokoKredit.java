package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuTambahBarangPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_tambah_barang_pos_toko_kredit);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuTambahBarangPosTokoKredit.this, MenuBarangPosTokoKredit.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}