package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

class LaundryItemDaftarJasa extends AppCompatActivity {
    TextView Jasa,Biaya;
    private String jasa,biaya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemdaftarjasa);

        Jasa = findViewById(R.id.edtJasa);
        Biaya = findViewById(R.id.edtSatuan);

        Bundle extras = getIntent().getExtras();
        Jasa.setText(jasa);
        Biaya.setText(biaya);
    }
}