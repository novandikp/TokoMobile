package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuLaporanPenjualanPerJenisPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_penjualan_per_jenis_pos_toko_kredit);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanPenjualanPerJenisPosTokoKredit.this, MenuLaporanExcelPosTokoKredit.class);
        startActivity(intent);
    }
}