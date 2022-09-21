package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaundryMenuTransaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenutransaksi);
    }

    public void TerimaLaundry(View view) {
        Intent intent = new Intent(LaundryMenuTransaksi.this, MenuTerimaLaundry.class);
        startActivity(intent);
    }

    public void ProsesLaundry(View view) {
        Intent intent = new Intent(LaundryMenuTransaksi.this, MenuDaftarProsesLaundry.class);
        startActivity(intent);
    }

    public void BayarHutang(View view) {
        Intent intent = new Intent(LaundryMenuTransaksi.this, MenuBayarHutangLaundry.class);
        startActivity(intent);
    }
}