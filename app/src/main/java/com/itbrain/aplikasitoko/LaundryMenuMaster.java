package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaundryMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenumaster);
    }

    public void IdentitasToko(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenuIdentitasTabungan.class);
        startActivity(intent);
    }

    public void Pegawai(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenuIdentitasTabungan.class);
        startActivity(intent);
    }

    public void Pelanggan(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenuIdentitasTabungan.class);
        startActivity(intent);
    }

    public void Kategori(View view) {
        Intent intent = new Intent(LaundryMenuMaster.this, MenuIdentitasTabungan.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
    Intent intent = new Intent(LaundryMenuMaster.this, MenuIdentitasTabungan.class);
    startActivity(intent);
}

}