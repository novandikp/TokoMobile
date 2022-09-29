package com.itbrain.aplikasitoko;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

class LaundryItemIdentitas extends AppCompatActivity {
    TextView Nama,Alamat,Notelp;
    private String nama,alamat,notelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundry_item_identitas);

        Nama = findViewById(R.id.NamaToko);
        Alamat = findViewById(R.id.AlamatToko);
        Notelp = findViewById(R.id.nomorTeleponToko);

        Bundle extras = getIntent().getExtras();
        Nama.setText(nama);
        Alamat.setText(alamat);
        Alamat.setText(notelp);
    }
}