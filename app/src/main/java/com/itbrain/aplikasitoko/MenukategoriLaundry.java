package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenukategoriLaundry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_kategori_laundry);
    }

    public void UbahKategori(View view) {
        Intent intent = new Intent(MenukategoriLaundry.this, MenuKategoriJasaLaundry.class);
        startActivity(intent);
    }
}