package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDaftarPelangganLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_pelanggan_laundry);
    }

    public void UbahPelanggan(View view) {
        Intent intent = new Intent(MenuDaftarPelangganLaundry.this, MenuPelangganLaundry.class);
        startActivity(intent);
    }
}