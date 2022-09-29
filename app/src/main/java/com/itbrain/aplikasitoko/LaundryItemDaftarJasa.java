package com.itbrain.aplikasitoko;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

class LaundryItemDaftarJasa extends AppCompatActivity {
    TextView Jasa,Biaya,Satuan;
    private String jasa,biaya,satuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemdaftarjasa);

        Jasa = findViewById(R.id.edtDaftarKategori);
        Biaya = findViewById(R.id.edtSatuan);
        Satuan = findViewById(R.id.edtjenisSatuan);

        Bundle extras = getIntent().getExtras();
        Jasa.setText(jasa);
        Biaya.setText(biaya);
        Satuan.setText(satuan);
    }
}