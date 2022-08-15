package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuTransaksiPilihBarangTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transaksi_pilih_barang_toko_sepatu);
    }

    public void BeliAja(View view) {
        Intent intent = new Intent(MenuTransaksiPilihBarangTokoSepatu.this, MenuTransaksiTentukanPenjualanDetailPembelianTokoSepatu.class);
        startActivity(intent);
    }
}