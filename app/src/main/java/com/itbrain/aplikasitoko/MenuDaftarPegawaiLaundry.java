package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDaftarPegawaiLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_pegawai_laundry);
    }

    public void UbahPegawai(View view) {
        Intent intent = new Intent(MenuDaftarPegawaiLaundry.this, MenuPegawaiLaundry.class);
        startActivity(intent);
    }
}