package com.itbrain.aplikasitoko.Laundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.itbrain.aplikasitoko.R;

public class MenuPilihAktivitasKeuanganLaundry extends AppCompatActivity {
    DatabaseLaundry db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi_keuangan_laundry_2);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageButton i = findViewById(R.id.kembali21);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CardView cvPemasukan = findViewById(R.id.cvPemasukan);
        cvPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPilihAktivitasKeuanganLaundry.this, ActivityKeuanganTambah_laundry.class).putExtra("type", "pemasukan"));
            }
        });
        CardView cvPengeluaran = findViewById(R.id.cvPengeluaran);
        cvPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPilihAktivitasKeuanganLaundry.this, ActivityKeuanganKeluar_laundry.class).putExtra("type", "pengeluaran"));
            }
        });
    }
}