package com.itbrain.aplikasitoko;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main_App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backupan_main_app);
    }

    public void Tabungan(View view) {
        Intent intent = new Intent(Main_App.this, TabunganMenuMasterUtama.class);
        startActivity(intent);
    }

    public void RentalMobil(View view) {
        Intent intent = new Intent(Main_App.this, MobilMenuUtamaMaster.class);
        startActivity(intent);
    }

    public void AntrianPortable(View view) {
        Intent intent = new Intent(Main_App.this, AntrianPortableMenuUtama.class);
        startActivity(intent);
    }

    public void Kwitansi(View view) {
        Intent intent = new Intent(Main_App.this, CetakKwitansiMenuMasterUtama.class);
        startActivity(intent);
    }

    public void TokoSepatu(View view) {
        Intent intent = new Intent(Main_App.this, Aplikasi_Toko_Sepatu_Menu_Utama.class);
        startActivity(intent);
    }

    public void Laundry(View view) {
        Intent intent = new Intent(Main_App.this, LaundryMenuUtamaMaster.class);
        startActivity(intent);
    }

    public void Kredit(View view) {
        Intent intent = new Intent(Main_App.this, TokoKreditMenuMasterUtama.class);
        startActivity(intent);
    }
}
