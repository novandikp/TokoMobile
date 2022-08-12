package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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