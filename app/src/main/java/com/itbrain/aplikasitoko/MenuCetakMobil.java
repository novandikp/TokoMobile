package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuCetakMobil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucetakmobil);
    }

    public void CetakStruk(View view) {
        Intent intent = new Intent(MenuCetakMobil.this, MenuCetakStrukMobil.class);
        startActivity(intent);
    }
}