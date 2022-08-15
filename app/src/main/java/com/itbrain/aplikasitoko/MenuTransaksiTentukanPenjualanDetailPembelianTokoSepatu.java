package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuTransaksiTentukanPenjualanDetailPembelianTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transaksi_tentukan_penjualan_detail_pembelian_toko_sepatu);
    }

    public void Tambah(View view) {
        Intent intent = new Intent(MenuTransaksiTentukanPenjualanDetailPembelianTokoSepatu.this, MenuTransaksiPilihBarangTokoSepatu.class);
        startActivity(intent);
    }

    public void LanjutkanPembayaran(View view) {
        Intent intent = new Intent(MenuTransaksiTentukanPenjualanDetailPembelianTokoSepatu.this, MenuTransaksiPilihBarangTokoSepatu.class);
        startActivity(intent);
    }
}