package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

class LaundryitemDaftarPelanggan extends AppCompatActivity {
    TextView NamaPelanggan,Alamat,NomorTelepon,Hutang;
    private String namapelanggan,alamatpelanggan,notelppelanggan,hutang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemdaftarpelanggan);

        NamaPelanggan = findViewById(R.id.NamaPelanggan);
        Alamat = findViewById(R.id.AlamatToko);
        NamaPelanggan = findViewById(R.id.NotelpToko);
        Hutang = findViewById(R.id.txtHutang);

        Bundle extras = getIntent().getExtras();
        NamaPelanggan.setText(namapelanggan);
        Alamat.setText(alamatpelanggan);
        NomorTelepon.setText(notelppelanggan);
        Hutang.setText(hutang);
    }
}