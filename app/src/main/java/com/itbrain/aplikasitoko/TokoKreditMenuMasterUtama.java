package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TokoKreditMenuMasterUtama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toko_kredit_menu_master_utama);
    }

    public void Master(View view) {
        Intent intent = new Intent(TokoKreditMenuMasterUtama.this, PosTokoKreditMenuMaster.class);
        startActivity(intent);
    }

    public void Transaksi(View view) {
        Intent intent = new Intent(TokoKreditMenuMasterUtama.this, PosTokoKreditMenuTransaksi.class);
        startActivity(intent);
    }

    public void Laporan(View view) {
        Intent intent = new Intent(TokoKreditMenuMasterUtama.this, PosTokoKreditMenuLaporan.class);
        startActivity(intent);
    }

    public void Utilitas(View view) {
        Intent intent = new Intent(TokoKreditMenuMasterUtama.this, PosTokoKreditMenuUtilitas.class);
        startActivity(intent);
    }
}