package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuJasaAntrianPortable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_jasa_antrian_portable);
    }

    public void TambahJasa(View view) {
        Intent intent = new Intent(MenuJasaAntrianPortable.this, MenuTambahJasaAntrianPortable.class);
        startActivity(intent);
    }
}