package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuRentalKendaraanMobil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menurentalkendaraanmobil);
    }
    public void PembayaranUangMuka(View view) {
        Intent intent = new Intent(MenuRentalKendaraanMobil.this, MenuPembayaranUangMukaMobil.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuRentalKendaraanMobil.this, MobilMenuTransaksi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}