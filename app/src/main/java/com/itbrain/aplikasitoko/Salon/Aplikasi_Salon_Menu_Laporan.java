package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Salon_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_salon_menu_laporan);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void LaporanJasaSalon(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Laporan.this, Laporan_Jasa_Salon_.class);
        startActivity(intent);
    }

    public void LaporanPelangganSalon(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Laporan.this, Laporan_Pelanggan_Salon_.class);
        startActivity(intent);
    }

    public void LaporanBookingSalon(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Laporan.this, Laporan_Booking_Salon_.class);
        startActivity(intent);
    }

    public void LaporanTransaksiSalon(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Laporan.this, Laporan_Transaksi_Salon_.class);
        startActivity(intent);
    }

    public void LaporanPendapatanSalon(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Laporan.this, Laporan_Pendapatan_Salon_.class);
        startActivity(intent);
    }
}