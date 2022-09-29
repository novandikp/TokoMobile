package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuAmbilNomorAntrianPortable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuambilnomorantrianportable);
    }

    public void NomorMulai(View view) {
        Intent intent = new Intent(MenuAmbilNomorAntrianPortable.this, MenuNomorMulaiAntrianPortable.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuAmbilNomorAntrianPortable.this, AntrianPortableMenuUtama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}