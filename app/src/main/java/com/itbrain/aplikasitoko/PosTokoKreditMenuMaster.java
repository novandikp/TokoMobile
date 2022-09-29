package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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

    public void Kembali(View view) {
        Intent intent = new Intent( PosTokoKreditMenuMaster.this, PosTokoKreditMenuMasterUtama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}