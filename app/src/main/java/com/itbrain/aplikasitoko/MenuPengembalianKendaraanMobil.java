package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPengembalianKendaraanMobil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menupengembaliankendaraanmobil);
    }

    public void CetakStruk(View view) {
        Intent intent = new Intent(MenuPengembalianKendaraanMobil.this, MenuCetakMobil.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuPengembalianKendaraanMobil.this, MobilMenuTransaksi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}