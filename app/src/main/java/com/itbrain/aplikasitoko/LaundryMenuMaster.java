package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LaundryMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenumaster);
    }

    public void IdentitasToko(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenuIdentitasLaundry.class);
        startActivity(intent);
    }

    public void Pegawai(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenuDaftarPegawaiLaundry.class);
        startActivity(intent);
    }

    public void Pelanggan(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenuDaftarPelangganLaundry.class);
        startActivity(intent);
    }

    public void Kategori(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenukategoriLaundry.class);
        startActivity(intent);
    }

    public void Jasa(View view) {
    Intent intent = new Intent(LaundryMenuMaster.this, MenuDaftarJasaLaundry.class);
    startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( LaundryMenuMaster.this, LaundryMenuUtamaMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}