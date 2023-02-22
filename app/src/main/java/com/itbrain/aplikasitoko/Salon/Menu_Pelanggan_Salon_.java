package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Menu_Pelanggan_Salon_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pelanggan_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahPelangganSalon);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(Menu_Pelanggan_Salon_.this, Form_Tambah_Pelanggan_Salon_.class);
                startActivity(intent);
            }
        });
    }
}