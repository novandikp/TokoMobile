package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuTransaksiLanjutkanPembayaranTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transaksi_lanjutkan_pembayaran_toko_sepatu);
    }

    public void Pembayaran(View view) {
        Intent intent = new Intent(MenuTransaksiLanjutkanPembayaranTokoSepatu.this, MenuTransaksiPenjualanTunaiPembayaranPosTokoKredit.class);
        startActivity(intent);
    }
}