package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuDaftarProsesLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_proses_laundry);
    }

    public void Bayar(View view) {
        Intent intent = new Intent(MenuDaftarProsesLaundry.this, ItemDaftarProsesLaundry.class);
        startActivity(intent);
    }
}