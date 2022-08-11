package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CetakKwitansiMenuMasterUtama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cetakkwitansimenumasterutama);
    }

    public void Master(View view) {
        Intent intent = new Intent(CetakKwitansiMenuMasterUtama.this, CetakKwitansiMenuMaster.class);
        startActivity(intent);
    }

    public void Transaksi(View view) {
        Intent intent = new Intent(CetakKwitansiMenuMasterUtama.this, CetakKwitansiMenuTransaksi.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
        Intent intent = new Intent(CetakKwitansiMenuMasterUtama.this, CetakKwitansiMenuLaporan.class);
        startActivity(intent);
    }
}