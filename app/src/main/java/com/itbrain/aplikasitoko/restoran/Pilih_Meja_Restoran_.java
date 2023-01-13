package com.itbrain.aplikasitoko.restoran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Pilih_Meja_Restoran_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_meja_restoran_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void KonfirmasiPembayaran(View view) {
        Intent intent = new Intent(Pilih_Meja_Restoran_.this, Item_Pilih_Meja_Restoran_.class);
        startActivity(intent);
    }
}