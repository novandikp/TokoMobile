package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Kasir_Super_Mudah_Menu_Master extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_kasir_super_mudah_menu_master);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Master.this, Identitas_Kasir_Super_Mudah.class);
        startActivity(intent);
    }

    public void PindahKategori(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Master.this, Form_Tambah_Kategori_Kasir_.class);
        intent.putExtra("type","kategori");
        startActivity(intent);
    }

    public void PindahBarang(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Master.this, Tambah_Barang_Kasir.class);
        intent.putExtra("type","kategori");
        startActivity(intent);
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Master.this, Tambah_Pelanggan_Toko_Kasir_.class);
        startActivity(intent);
    }
}