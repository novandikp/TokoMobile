package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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

    public void Kembali(View view) {
        Intent intent = new Intent( MenuProsesSimpanTabungan.this, TabunganMenuTransaksi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}