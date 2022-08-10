package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PosTokoKreditMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postokokreditmenumaster);
    }

    public void Identitas(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMaster.this, MenuIdentitasPosTokoKredit.class);
        startActivity(intent);
    }

    public void Kategori(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMaster.this, MenuKategoriPosTokoKredit.class);
        startActivity(intent);
    }

    public void Barang(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMaster.this, MenuBarangPosTokoKredit.class);
        startActivity(intent);
    }

    public void Pelanggan(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMaster.this, MenuPelangganPosTokoKredit.class);
        startActivity(intent);
    }
}