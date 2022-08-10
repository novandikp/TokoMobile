package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PosTokoKreditMenuTransaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos_toko_kredit_menu_transaksi);
    }

    public void PenjualanTunai(View view) {
        Intent intent = new Intent(PosTokoKreditMenuTransaksi.this, MenuTransaksiPenjualanTunaiPosTokoKredit.class);
        startActivity(intent);
    }

    public void PenjualanKredit(View view) {
        Intent intent = new Intent(PosTokoKreditMenuTransaksi.this, MenuTransaksiPenjualanKreditPosTokoKredit.class);
        startActivity(intent);
    }

    public void PembayaranAngsuran(View view) {
        Intent intent = new Intent(PosTokoKreditMenuTransaksi.this, MenuTransaksiPembayaranAngsuranPosTokoKredit.class);
        startActivity(intent);
    }

    public void Return(View view) {
        Intent intent = new Intent(PosTokoKreditMenuTransaksi.this, MenuTransaksiReturnBarangPosTokoKredit.class);
        startActivity(intent);
    }
}