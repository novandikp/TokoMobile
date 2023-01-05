package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Aplikasi_Apotek_Plus_Keuangan_Menu_Utama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_apotek_plus_keuangan_menu_utama);
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas.class);
        startActivity(intent);
    }
}