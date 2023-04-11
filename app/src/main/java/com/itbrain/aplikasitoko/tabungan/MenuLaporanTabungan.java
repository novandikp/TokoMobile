package com.itbrain.aplikasitoko.tabungan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;


public class MenuLaporanTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan_tabungan);
//        ModulTabungan.btnBack(R.string.title_laporan, getSupportActionBar());

        ImageButton imageButton = findViewById(R.id.kembaliLaporan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void iLaporan(String extra) {
        startActivity(new Intent(this, MenuLaporanKeuanganTabungan.class).putExtra("type", extra));
    }

    private void iLaporan2(String extra) {
        startActivity(new Intent(this, MenuLaporanTransaksiTabungan.class).putExtra("type", extra));
    }


    public void lapTransaksi(View view) {
        iLaporan2("transaksi");
    }

    public void lapKeuangan(View view) {
        iLaporan("keuangan");
    }

    public void lapJenis(View view) {
        startActivity(new Intent(this, MenuLaporanJenisSimpananTabungan.class));
    }

    public void lapAnggota(View view) {
        startActivity(new Intent(this, MenuLaporanAnggotaTabungan.class));
    }

    public void lapSimpanan(View view) {
        startActivity(new Intent(this, MenulaporanSimpananTabungan.class));
    }
}


