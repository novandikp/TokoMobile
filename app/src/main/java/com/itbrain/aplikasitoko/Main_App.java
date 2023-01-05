package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//import com.itbrain.aplikasitoko.databinding.AplikasiApotekPlusKeuanganMenuUtamaBinding;
//import com.itbrain.aplikasitoko.databinding.AplikasiSalonMenuUtamaBinding;

public class Main_App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);
    }

    public void PindahBengkel(View view) {
        Intent intent = new Intent(Main_App.this, Aplikasi_Bengkel_Menu_Utama_.class);
        startActivity(intent);
    }

    public void PindahRestoran(View view) {
        Intent intent = new Intent(Main_App.this, ApllikasiRestoran_Menu_Utama.class);
        startActivity(intent);
    }

    public void PindahKlinik(View view) {
        Intent intent = new Intent(Main_App.this, AplikasiKlinikDokter_Menu_Utama.class);
        startActivity(intent);
    }

    public void PindahTokoKain(View view) {
        Intent intent = new Intent(Main_App.this, Aplikasi_TokoKain_Menu_Utama.class);
        startActivity(intent);
    }

    public void PindahKasir(View view) {
        Intent intent = new Intent(Main_App.this, Aplikasi_Kasir_Super_Mudah_Menu_Utama.class);
        startActivity(intent);
    }

    public void PindahApotekPluss(View view) {
        Intent intent = new Intent(Main_App.this, Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.class);
        startActivity(intent);
    }

    public void PindahSalon(View view) {
        Intent intent = new Intent(Main_App.this, Aplikasi_Salon_Menu_Utama.class);
        startActivity(intent);
    }
}