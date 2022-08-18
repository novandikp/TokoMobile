package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanMobilBagPegawai extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporanmobilbagpegawai);
    }

    public void Export(View view) {
        Intent intent = new Intent(MenuLaporanMobilBagPegawai.this, MenuLaporanExportExcelMobil.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuLaporanMobilBagPegawai.this, MobilMenuLaporan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}