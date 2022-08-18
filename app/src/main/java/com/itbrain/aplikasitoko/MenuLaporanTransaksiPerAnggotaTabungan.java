package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanTransaksiPerAnggotaTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporantransaksiperanggotatabungan);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanTransaksiPerAnggotaTabungan.this, MenuLaporanExportExcelTabungan.class);
        startActivity(intent);
    }

    public void CariDataSimpanan(View view) {
        Intent intent = new Intent(MenuLaporanTransaksiPerAnggotaTabungan.this, MenuCariDataSimpananTabugan.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuLaporanTransaksiPerAnggotaTabungan.this, TabunganMenuLaporan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}