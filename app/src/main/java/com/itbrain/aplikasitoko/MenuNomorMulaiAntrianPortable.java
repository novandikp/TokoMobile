package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuNomorMulaiAntrianPortable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_nomor_mulai_antrian_portable);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuNomorMulaiAntrianPortable.this, MenuAmbilNomorAntrianPortable.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}