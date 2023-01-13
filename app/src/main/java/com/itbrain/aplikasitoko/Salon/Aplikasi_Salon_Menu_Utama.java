package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Salon_Menu_Utama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_salon_menu_utama);
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Utillitas.class);
        startActivity(intent);
    }
}