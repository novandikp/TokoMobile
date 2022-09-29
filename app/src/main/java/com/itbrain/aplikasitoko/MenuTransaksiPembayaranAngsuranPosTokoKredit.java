package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuTransaksiPembayaranAngsuranPosTokoKredit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_transaksi_pembayaran_angsuran_pos_toko_kredit);
    }

    public void Informasi(View view) {
        Intent intent = new Intent(MenuTransaksiPembayaranAngsuranPosTokoKredit.this, MenuTransaksiPembayaranAngsuranKonfirmasiPosTokoKredit.class);
        startActivity(intent);
    }
}