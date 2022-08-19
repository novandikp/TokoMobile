package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Daftar_Hutang_Toko_Sepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_hutang_toko_sepatu);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( Daftar_Hutang_Toko_Sepatu.this, Aplikasi_Toko_Sepatu_Menu_Transaksi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}