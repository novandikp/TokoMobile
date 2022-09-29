package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuLaporanTagihanPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_tagihan_pos_toko_kredit);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanTagihanPosTokoKredit.this, MenuLaporanExcelPosTokoKredit.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent(MenuLaporanTagihanPosTokoKredit.this, PosTokoKreditMenuLaporan.class);
        startActivity(intent);
    }
}