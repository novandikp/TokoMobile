package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Item_Pilih_Meja_Restoran_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_pilih_meja_restoran_);

        Button button = findViewById(R.id.LanjutKonfirmasi);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Item_Pilih_Meja_Restoran_.this, Form_Konfirmasi_Restoran.class);
                startActivity(intent);
            }
        });
    }
}