package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}