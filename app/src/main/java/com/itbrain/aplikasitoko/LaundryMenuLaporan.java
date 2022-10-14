package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LaundryMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenulaporan);
        Modul.btnBack("Menu Transaksi",getSupportActionBar());
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent (LaundryMenuLaporan.this, LaundryMenuUtamaMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return true;
    }

    public void LaporanJasa(String value){
        Intent i=new Intent(this,MenuLaporanJasaLaundry.class);
        i.putExtra("tab",value);
        startActivity(i);
    }

    public void LaporanPegawai(String value){
        Intent i=new Intent(this,MenuLaporanPegawaiLaundry.class);
        i.putExtra("tab",value);
        startActivity(i);
    }

    public void LaporanPelanggan(String value){
        Intent i=new Intent(this,MenuLaporanPelangganLaundry.class);
        i.putExtra("tab",value);
        startActivity(i);
    }

    public void LaporanLaundry(String value){
        Intent i=new Intent(this,MenulaporanLaundry.class);
        i.putExtra("tab",value);
        startActivity(i);
    }

    public void LaporanJasa(View view) {
        LaporanJasa("jasa");
    }

    public void LaporanPegawai(View view) {
        LaporanPegawai("pegawai");
    }

    public void LaporanPelanggan(View view) {
        LaporanPelanggan("pelanggan");
    }

    public void LaporanLaundry(View view) {
        LaporanLaundry("laundry");
    }

    public void LaporanProsesLaundry(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanProsesLaundry.class);
        startActivity(intent);
    }
    public void LaporanPendapatan(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanPendapatanLaundry.class);
        startActivity(intent);
    }
    public void LaporanHutang(View view) {
        Intent intent = new Intent(LaundryMenuLaporan.this, MenuLaporanHutangLaundry.class);
        startActivity(intent);
    }
}