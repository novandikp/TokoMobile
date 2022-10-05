package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class MenuBayarHutangLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Modul.btnBack("Menu Bayar Hutang",getSupportActionBar());
        setContentView(R.layout.menu_bayar_hutang_laundry);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent (MenuBayarHutangLaundry.this, LaundryMenuTransaksi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return true;
    }
}