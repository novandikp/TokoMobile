package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuProsesAmbilTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuprosesambiltabungan);
    }

    public void Pengeluaran(View view) {
        Intent intent = new Intent(MenuProsesAmbilTabungan.this, MenuPengeluaranTabungan.class);
        startActivity(intent);
    }
}