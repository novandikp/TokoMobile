package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuTransaksiPenjualanTunaiPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transaksi_penjualan_tunai_pos_toko_kredit);
    }

    public void Penjualan(View view) {
        Intent intent = new Intent(MenuTransaksiPenjualanTunaiPosTokoKredit.this, MenuTransaksiPenjualanTunaiPembayaranPosTokoKredit.class);
        startActivity(intent);
    }
}