package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TambahKain;

public class Aplikasi_Toko_Kain_Menu_Master extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_toko_kain_menu_master);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master.this, Identitas_Toko_Kain.class);
        startActivity(intent);
    }

    public void PindahKategori(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master.this, Kategori_Toko_Kain_.class);
        startActivity(intent);
    }

    public void PindahKain(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master.this, TambahKain.class);
        startActivity(intent);
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master.this, Pelanggan_Toko_Kain_.class);
        startActivity(intent);
    }
}