package com.itbrain.aplikasitoko;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

class LaundryitemDaftarPegawai extends AppCompatActivity {
    TextView NamaPegawai,AlamatPegawai,NoTelpPegawai;
    private String namapegawai,alamatpegawai,notelppegawai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemdaftarpegawai);

        NamaPegawai = findViewById(R.id.namaPegawai);
        AlamatPegawai = findViewById(R.id.alamatPegawai);
        NoTelpPegawai = findViewById(R.id.notelpPegawai);

        Bundle extras = getIntent().getExtras();
        NamaPegawai.setText(namapegawai);
        AlamatPegawai.setText(alamatpegawai);
        NoTelpPegawai.setText(notelppegawai);
    }
}