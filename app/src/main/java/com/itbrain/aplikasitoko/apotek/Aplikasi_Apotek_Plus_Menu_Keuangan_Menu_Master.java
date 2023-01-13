package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_apotek_plus_menu_keuangan_menu_master);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.this, Identitas_Apotek.class);
        startActivity(intent);
    }

    public void PindahKategori(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.this, Kategori_Apotek_.class);
        startActivity(intent);
    }

    public void PindahSatuan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.this, Satuan_Apotek_.class);
        startActivity(intent);
    }

    public void PindahObat(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.this, Form_Cari_Obat.class);
        startActivity(intent);
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.this, Cari_Pelangan_Apotek.class);
        startActivity(intent);
    }

    public void PindahSupplier(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.this, Cari_Supplier_Apotek.class);
        startActivity(intent);
    }
}