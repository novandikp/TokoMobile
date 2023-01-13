package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Kasir_Super_Mudah_Menu_Utama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_kasir_super_mudah_menu_utama);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Utama.this, Apllikasi_Kasir_Super_Mudah_Menu_Utilitas.class);
        startActivity(intent);
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Utama.this, Aplikasi_Kasir_Super_Mudah_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Utama.this, Aplikasi_Kasir_Super_Mudah_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Utama.this, Aplikasi_Kasir_Super_Mudah_Menu_Laporan.class);
        startActivity(intent);
    }
}