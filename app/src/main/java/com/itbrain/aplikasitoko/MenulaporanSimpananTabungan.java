package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenulaporanSimpananTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporansimpanantabungan);
    }

    public void Excel(View view) {
        Intent intent = new Intent( MenulaporanSimpananTabungan.this, MenuLaporanExportExcelTabungan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenulaporanSimpananTabungan.this, TabunganMenuLaporan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}