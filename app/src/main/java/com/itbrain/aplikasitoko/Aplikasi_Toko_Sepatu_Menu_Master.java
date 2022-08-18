package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Aplikasi_Toko_Sepatu_Menu_Master extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_toko_sepatu_menu_master);
    }

    public void Identitas(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Master.this, MenuIdentitasTokoSepatu.class);
        startActivity(intent);
    }

    public void Kategori(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Master.this, MenuKategoriTokoSepatu.class);
        startActivity(intent);
    }

    public void Barang(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Master.this, MenuBarangTokoSepatu.class);
        startActivity(intent);
    }
    public void Pelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Master.this, MenuPelangganTokoSepatu.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( Aplikasi_Toko_Sepatu_Menu_Master.this, Aplikasi_Toko_Sepatu_Menu_Utama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}