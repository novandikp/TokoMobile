package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
        Intent intent = new Intent(MenuTransaksiTentukanPenjualanDetailPembelianTokoSepatu.this, MenuTransaksiLanjutkanPembayaranTokoSepatu.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuTransaksiTentukanPenjualanDetailPembelianTokoSepatu.this, MenuTransaksiPilihBarangTokoSepatu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}