package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LaundryItemDaftarKategori extends AppCompatActivity {
    TextView edtDaftarKategori;
    private String nama;
    private String KEY_NAME = "NAMA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemdaftarkategori);

        edtDaftarKategori = findViewById(R.id.edtDaftarKategori);

        Bundle extras = getIntent().getExtras();
        nama = extras.getString(KEY_NAME);
        edtDaftarKategori.setText(nama);
    }
}