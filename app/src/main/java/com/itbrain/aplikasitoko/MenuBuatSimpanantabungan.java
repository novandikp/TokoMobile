package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuBuatSimpanantabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menubuatsimpanantabungan);
    }

    public void CariAnggota(View view) {
        Intent intent = new Intent(MenuBuatSimpanantabungan.this, MenuCariAnggotaTabungan.class);
        startActivity(intent);
    }
}