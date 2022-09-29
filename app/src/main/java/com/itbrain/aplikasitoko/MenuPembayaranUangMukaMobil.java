package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPembayaranUangMukaMobil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupembayaranuangmukamobil);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuPembayaranUangMukaMobil.this, MenuRentalKendaraanMobil.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}