package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AntrianPortableMenuUtama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antrianportablemenuutama);
    }

    public void Identitas(View view) {
        Intent intent = new Intent(AntrianPortableMenuUtama.this, MenuIdentitasUsahaAntrianPortable.class);
        startActivity(intent);
    }

    public void AmbilAntrian(View view) {
        Intent intent = new Intent(AntrianPortableMenuUtama.this, MenuAmbilNomorAntrianPortable.class);
        startActivity(intent);
    }

    public void PanggilAntrian(View view) {
        Intent intent = new Intent(AntrianPortableMenuUtama.this, NomorAntrian.class);
        startActivity(intent);
    }
}