package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LaundryMenuUtamaMaster extends AppCompatActivity {
    TextView namatoko,alamattoko,notelptoko;
//    private String nama;
//    private String key_name="nama";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenuutamamaster);
        namatoko=findViewById(R.id.NamaToko);
        alamattoko=findViewById(R.id.AlamatToko);
        notelptoko=findViewById(R.id.NomorTeleponToko);
        Intent intent = getIntent();
        String namatokoh = intent.getStringExtra("key_nama");
        String alamattokoh = intent.getStringExtra("key_alamat");
        String notelptokoh = intent.getStringExtra("key_telp");
        namatoko.setText(namatokoh);
        alamattoko.setText(alamattokoh);
        notelptoko.setText(notelptokoh);

//        namatoko = findViewById(R.id.NamaToko);
//        txtnamatoko = getIntent().getExtras().getString("Value");
//        namatoko.setText(txtnamatoko);
//        alamattoko = findViewById(R.id.AlamatToko);
//        txtnamatoko = getIntent().getExtras().getString("Value");
//        namatoko.setText(txtnamatoko);
//        notelptoko = findViewById(R.id.NomorTeleponToko);
//        txtnotelptoko = getIntent().getExtras().getString("Value");
//        notelptoko.setText(txtnotelptoko);
//
//        namatoko = (TextView) findViewById(R.id.NamaToko);
//
//        Bundle extras = getIntent().getExtras();
//        nama = extras.getString(key_name);
//        namatoko.setText(nama);
    }

    public void Master(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuMaster.class);
        startActivity(intent);
    }

    public void Transaksi(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuTransaksi.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, LaundryMenuLaporan.class);
        startActivity(intent);
    }

    public void Utilitas(View view) {
        Intent intent = new Intent(LaundryMenuUtamaMaster.this, MenuUtilitasLaundry.class);
        startActivity(intent);
    }
}