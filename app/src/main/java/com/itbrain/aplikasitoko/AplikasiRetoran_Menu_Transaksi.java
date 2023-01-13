package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.restoran.Pilih_Meja_Restoran_;

public class AplikasiRetoran_Menu_Transaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_retoran_menu_transaksi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void TerimaPesanan(View view) {
        Intent intent = new Intent(AplikasiRetoran_Menu_Transaksi.this, Pilih_Meja_Restoran_.class);
        startActivity(intent);
    }

    public void PindahKauangan(View view) {
        Intent intent = new Intent(AplikasiRetoran_Menu_Transaksi.this, Form_Keuangan.class);
        startActivity(intent);
    }
}