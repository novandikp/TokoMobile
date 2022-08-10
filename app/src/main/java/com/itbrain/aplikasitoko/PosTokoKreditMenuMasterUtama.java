package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PosTokoKreditMenuMasterUtama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postokokreditmenumasterutama);
    }

    public void Master(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMasterUtama.this, PosTokoKreditMenuMaster.class);
        startActivity(intent);
    }

    public void Transaksi(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMasterUtama.this, PosTokoKreditMenuTransaksi.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMasterUtama.this, PosTokoKreditMenuLaporan.class);
        startActivity(intent);
    }

    public void Utilitas(View view) {
        Intent intent = new Intent(PosTokoKreditMenuMasterUtama.this, PosTokoKreditMenuUtilitas.class);
        startActivity(intent);
    }
}