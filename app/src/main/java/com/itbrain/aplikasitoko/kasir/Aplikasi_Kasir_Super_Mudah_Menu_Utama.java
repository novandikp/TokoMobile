package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Kasir_Super_Mudah_Menu_Utama extends AppCompatActivity {

    DatabaseKasir db;
    FConfigKasir config;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_kasir_super_mudah_menu_utama);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        db.cektbl();
        v = this.findViewById(android.R.id.content);

        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText();
    }


    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Utama.this, Apllikasi_Kasir_Super_Mudah_Menu_Utilitas.class);
        startActivity(intent);
    }


    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Utama.this, Aplikasi_Kasir_Super_Mudah_Menu_Master.class);
        startActivity(intent);
    }

    public void setText() {
        try{
            Cursor c = db.sq("SELECT * FROM tblidentitas WHERE id=1");
            c.moveToNext();
            FFunctionKasir.setText(v, R.id.namaAplikasi, FFunctionKasir.getString(c, "nama"));
            FFunctionKasir.setText(v, R.id.alamatAplikasi, FFunctionKasir.getString(c, "alamat"));
        }catch (Exception e){
            FFunctionKasir.setText(v, R.id.namaAplikasi, "Komputerkit");
            FFunctionKasir.setText(v, R.id.alamatAplikasi, "Buduran");
        }

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