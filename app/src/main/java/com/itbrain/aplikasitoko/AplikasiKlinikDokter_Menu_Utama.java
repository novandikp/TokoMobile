package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AplikasiKlinikDokter_Menu_Utama extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_klinik_menu_utama);
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Utilitas.class);
        startActivity(intent);
    }


}