package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MobilMenuTransaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilmenutransaksi);
    }

    public void MenuRentalKendaraan(View view) {
        Intent intent = new Intent(MobilMenuTransaksi.this, MenuRentalKendaraanMobil.class);
        startActivity(intent);
    }

    public void MenuPengembalianKendaraan(View view) {
        Intent intent = new Intent(MobilMenuTransaksi.this, MenuPengembalianKendaraanMobil.class);
        startActivity(intent);
    }
}