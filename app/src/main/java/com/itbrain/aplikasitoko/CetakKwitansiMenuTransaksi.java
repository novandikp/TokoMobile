package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CetakKwitansiMenuTransaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cetakkwitansimenutransaksi);
    }

    public void CetakKwitansi(View view) {
        Intent intent = new Intent(CetakKwitansiMenuTransaksi.this, CetakKwitansiMenuCetakKwitansi.class);
        startActivity(intent);
    }

    public void CetakUlang(View view) {
        Intent intent = new Intent(CetakKwitansiMenuTransaksi.this, CetakKwitansiMenuCetakUlang.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( CetakKwitansiMenuTransaksi.this, CetakKwitansiMenuMasterUtama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}