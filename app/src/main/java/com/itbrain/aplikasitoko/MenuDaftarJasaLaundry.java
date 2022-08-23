package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDaftarJasaLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_jasa_laundry);
    }

    public void UbahJasa(View view) {
        Intent intent = new Intent(MenuDaftarJasaLaundry.this, MenuUbahJasaLaundry.class);
        startActivity(intent);
    }
}