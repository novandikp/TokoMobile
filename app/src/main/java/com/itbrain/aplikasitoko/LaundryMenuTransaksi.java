package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LaundryMenuTransaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Modul.btnBack("Menu Transaksi",getSupportActionBar());
        setContentView(R.layout.laundrymenutransaksi);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent (LaundryMenuTransaksi.this, LaundryMenuUtamaMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return true;
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