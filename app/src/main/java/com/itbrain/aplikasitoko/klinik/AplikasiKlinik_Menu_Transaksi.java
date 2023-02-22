package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class AplikasiKlinik_Menu_Transaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_klinik_menu_transaksi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void PeriksaPasien(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Transaksi.this, Menu_pemeriksaan_klinik.class);
        intent.putExtra("type","transaksi");
        startActivity(intent);
    }

    public void Pembayaran(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Transaksi.this, Menu_Bayar_Tambah_Klinik_.class);
        startActivity(intent);
    }

    public void BuatJanji(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Transaksi.this, Tambah_Janji_Klinik.class);
        startActivity(intent);
    }
}