package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Kategori_Apotek_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategori_apotek_);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahKategori);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Kategori_Apotek_.this, Form_Tambah_kategori_Apotek_.class);
                startActivity(intent);
            }
        });
    }
}