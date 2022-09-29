package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TabunganMenuMasterUtama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabunganmenumasterutama);
    }
    public void TabunganMenuMaster(View view) {
        Intent intent = new Intent(TabunganMenuMasterUtama.this, TabunganMenuMaster.class);
        startActivity(intent);
    }

    public void TabunganMenuTransaksi(View view) {
        Intent intent = new Intent(TabunganMenuMasterUtama.this, TabunganMenuTransaksi.class);
        startActivity(intent);
    }

    public void TabunganMenuLaporan(View view) {
        Intent intent = new Intent(TabunganMenuMasterUtama.this, TabunganMenuLaporan.class);
        startActivity(intent);
    }

    public void TabunganMenuUtilitas(View view) {
        Intent intent = new Intent(TabunganMenuMasterUtama.this, TabunganMenuUtilitas.class);
        startActivity(intent);
    }

}