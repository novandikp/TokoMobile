package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Tambah_Barang_Kasir extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_barang_kasir);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahBarang);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tambah_Barang_Kasir.this, Form_Tambah_Barang_Kasir_.class);
                startActivity(intent);
            }
        });
    }
}