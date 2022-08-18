package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuIdentitasUsahaAntrianPortable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuidentitasusahaantrianportable);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuIdentitasUsahaAntrianPortable.this, AntrianPortableMenuUtama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}