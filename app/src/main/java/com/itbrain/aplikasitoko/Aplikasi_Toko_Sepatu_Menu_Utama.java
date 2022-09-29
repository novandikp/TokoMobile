package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Aplikasi_Toko_Sepatu_Menu_Utama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_toko_sepatu_menu_utama);
    }

    public void Master(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Utama.this, Aplikasi_Toko_Sepatu_Menu_Master.class);
        startActivity(intent);
    }

    public void Transaksi(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Utama.this, Aplikasi_Toko_Sepatu_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Utama.this, Aplikasi_Toko_Sepatu_Menu_Laporan.class);
        startActivity(intent);
    }
}