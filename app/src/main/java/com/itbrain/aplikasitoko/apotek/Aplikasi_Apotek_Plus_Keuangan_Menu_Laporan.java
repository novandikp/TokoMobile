package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_apotek_plus_keuagnan_menu_laporan);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Pelanggan_Apotek_.class);
        startActivity(intent);
    }

    public void PindahSupplier(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Supplier_Apotek_.class);
        startActivity(intent);
    }

    public void PindahObat(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Obat_Apotek_.class);
        startActivity(intent);
    }

    public void PindahPengeluaran(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Pengeluaran_Apotek_.class);
        startActivity(intent);
    }

    public void PindahPenjualan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Penjualan_Apotek_.class);
        startActivity(intent);
    }

    public void PindahKeuangan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Keuangan_Apotek_.class);
        startActivity(intent);
    }

    public void PindahPendapatan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Pendapatan_Apotek_.class);
        startActivity(intent);
    }

    public void PindahPembayaranHutang(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Pembayaran_Hutang_Apotek_.class);
        startActivity(intent);
    }

    public void PindahPembayaranPiutang(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Piutang_Apotek_.class);
        startActivity(intent);
    }

    public void PindahObatExpired(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.this, Laporan_Obat_Expired_Apotek_.class);
        startActivity(intent);
    }
}