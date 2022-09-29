package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuTransaksiLakukanPenjualanTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transaksi_lakukan_penjualan_toko_sepatu);
    }

    public void PilihBarang(View view) {
        Intent intent = new Intent(MenuTransaksiLakukanPenjualanTokoSepatu.this, MenuTransaksiPilihBarangTokoSepatu.class);
        startActivity(intent);
    }
}