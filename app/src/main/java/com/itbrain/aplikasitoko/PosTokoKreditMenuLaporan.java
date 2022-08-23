package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PosTokoKreditMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos_toko_kredit_menu_laporan);
    }

    public void LaporanPelanggan(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanPelangganPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanBarang(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanBarangPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanPenjualanPerPelanggan(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanPenjualanPerPelangganPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanPenjualanPerKategori(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanPenjualanPerKategoriPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanPenjualanPerJenis(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanPenjualanPerJenisPosTokoKredit.class);
        startActivity(intent);

    }    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanPenjualanPendapatanPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanReturn(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanReturnPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanLabaRugi(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanLabaRugiPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanKredit(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanKreditPosTokoKredit.class);
        startActivity(intent);
    }

    public void LaporanTagihan(View view) {
        Intent intent = new Intent(PosTokoKreditMenuLaporan.this, MenuLaporanTagihanPosTokoKredit.class);
        startActivity(intent);
    }
}