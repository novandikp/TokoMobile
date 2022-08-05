package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuJenisSimpanan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_jenis_simpanan);
    }

    public void MenuJenisSimpanan(View view) {
        Intent intent = new Intent(MenuJenisSimpanan.this, MenuJenisSimpananTabungan.class);
        startActivity(intent);
    }
}