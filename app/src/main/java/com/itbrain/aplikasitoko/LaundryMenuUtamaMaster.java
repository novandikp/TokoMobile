package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LaundryMenuUtamaMaster extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenuutamamaster);
    }
    public void Master(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuMaster.class);
        startActivity(intent);
    }

    public void Transaksi(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuTransaksi.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuLaporan.class);
        startActivity(intent);
    }

    public void Utilitas(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, MenuUtilitasLaundry.class);
        startActivity(intent);
    }
}