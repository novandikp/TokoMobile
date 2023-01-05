package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

public class Aplikasi_Bengkel_Menu_Master_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_bengkel_menu_master_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Master_.this, Identitas_Bengkel_.class);
        startActivity(intent);
    }

    public void PindahKategori(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Master_.this, Kategori_Bengkel_.class);
        startActivity(intent);
    }

    public void PindahBarang(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Master_.this, Tambah_Barang_Bengkel_.class);
        startActivity(intent);
    }

    public void PindahJasa(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Master_.this, Tambah_Jasa_Bengkel_.class);
        startActivity(intent);
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Master_.this, Tambah_Pelanggan_Bengkel_.class);
        startActivity(intent);
    }

    public void PindahTeknisi(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Master_.this, Tambah_Teknisi_Bengkel_.class);
        startActivity(intent);
    }
}