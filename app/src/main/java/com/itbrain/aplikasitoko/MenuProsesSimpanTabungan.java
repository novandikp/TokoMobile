package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuProsesSimpanTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_proses_simpan_tabungan);
    }

    public void CariDataSimpanan(View view) {
        Intent intent = new Intent(MenuProsesSimpanTabungan.this, MenuCariDataSimpananTabugan.class);
        startActivity(intent);
    }

    public void Pemasukan(View view) {
        Intent intent = new Intent(MenuProsesSimpanTabungan.this, MenuPemasukanTabungan.class);
        startActivity(intent);
    }
}