package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuIdentitasCetakKwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuidentitascetakkwitansi);
    }

    public void Kembali(View view) {
        Intent intent = new Intent(MenuIdentitasCetakKwitansi.this, CetakKwitansiMenuMaster.class);
        startActivity(intent);
    }
}