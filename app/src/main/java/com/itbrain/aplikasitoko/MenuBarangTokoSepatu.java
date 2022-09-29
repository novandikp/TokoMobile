package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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

    public void Kembali(View view) {
        Intent intent = new Intent( MenuBarangTokoSepatu.this, MenuBarangTokoSepatu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}