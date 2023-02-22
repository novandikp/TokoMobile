package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Kasir_Super_Mudah_Menu_Transaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_kasir_super_mudah_menu_transaksi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahPenjualan(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Transaksi.this, Form_Penjualan_Kasir_.class);
        intent.putExtra("type","faktur");
        startActivity(intent);

    }

    public void BayarHutang(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Transaksi.this, Menu_Bayar_Hutang_Kasir.class);
        startActivity(intent);
    }

    public void PindahReturn(View view) {
        Intent intent = new Intent(Aplikasi_Kasir_Super_Mudah_Menu_Transaksi.this, Menu_Return_Kasir_.class);
        startActivity(intent);
    }
}