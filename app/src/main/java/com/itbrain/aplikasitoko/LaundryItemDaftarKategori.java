package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LaundryItemDaftarKategori extends AppCompatActivity {
    TextView edtDaftarKategori;
    private String nama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemdaftarkategori);

        edtDaftarKategori = findViewById(R.id.edtDaftarPegawai);

        Bundle extras = getIntent().getExtras();
        edtDaftarKategori.setText(nama);
    }

}