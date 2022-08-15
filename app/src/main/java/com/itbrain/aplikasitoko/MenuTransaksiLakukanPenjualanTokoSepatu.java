package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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