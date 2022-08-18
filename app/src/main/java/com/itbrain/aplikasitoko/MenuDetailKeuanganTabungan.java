package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDetailKeuanganTabungan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menudetailkeuangantabungan);
    }

    public void Pemasukan(View view) {
        Intent intent = new Intent(MenuDetailKeuanganTabungan.this, MenuPemasukanTabungan.class);
        startActivity(intent);
    }

    public void Pengeluaran(View view) {
        Intent intent = new Intent(MenuDetailKeuanganTabungan.this, MenuPengeluaranTabungan.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuDetailKeuanganTabungan.this, TabunganMenuTransaksi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}