package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}