package com.itbrain.aplikasitoko;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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