package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Aplikasi_Toko_Sepatu_Menu_Transaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_toko_sepatu_menu_transaksi);
    }

    public void LakukanPenjualan(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Transaksi.this, MenuTransaksiLakukanPenjualanTokoSepatu.class);
        startActivity(intent);
    }

    public void BayarHutang(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Transaksi.this, Daftar_Hutang_Toko_Sepatu.class);
        startActivity(intent);
    }

    public void ReturnBarang(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Sepatu_Menu_Transaksi.this, MenuReturnBarangTokoSepatu.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( Aplikasi_Toko_Sepatu_Menu_Transaksi.this, Aplikasi_Toko_Sepatu_Menu_Utama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}