package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuLaporanKreditPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_kredit_pos_toko_kredit);
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanKreditPosTokoKredit.this, MenuLaporanExcelPosTokoKredit.class);
        startActivity(intent);
    }
}