package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ItemReturnBarangTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_return_barang_toko_sepatu);
    }

    public void ReturnBarang(View view) {
        Intent intent = new Intent(ItemReturnBarangTokoSepatu.this, MenuTransaksiPenjualanTunaiPembayaranPosTokoKredit.class);
        startActivity(intent);
    }
}