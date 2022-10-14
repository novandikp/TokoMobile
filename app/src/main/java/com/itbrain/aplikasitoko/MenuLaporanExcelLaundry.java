package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class MenuLaporanExcelLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Modul.btnBack("Menu Excel Laundry",getSupportActionBar());
        setContentView(R.layout.menu_laporan_excel_laundry);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent (MenuLaporanExcelLaundry.this, MenuLaporanPegawaiLaundry.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return true;
    }
}