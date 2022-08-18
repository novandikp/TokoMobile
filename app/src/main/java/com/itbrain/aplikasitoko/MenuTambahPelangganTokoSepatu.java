package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuTambahPelangganTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_tambah_pelanggan_toko_sepatu);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuTambahPelangganTokoSepatu.this, MenuPelangganTokoSepatu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}