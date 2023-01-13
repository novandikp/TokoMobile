package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_apotek_plus_keuangan_menu_transaksi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahPembelian(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.this, Menu_Pembelian_Apotek_.class);
        startActivity(intent);
    }

    public void PindahPenjualan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.this, Menu_Penjualan_Apotek.class);
        startActivity(intent);
    }

    public void PindahPiutang(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.this, Menu_Pembayaran_Piutang_Apotek.class);
        startActivity(intent);
    }

    public void PindahHutang(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.this, Menu_Pembayaran_Hutang_Apotek.class);
        startActivity(intent);
    }

    public void PindahPemasukan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.this, Menu_Pemasukan_Apotek.class);
        startActivity(intent);
    }

    public void PindahPengeluaran(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.this, Menu_Pengeluaran_Apotek.class);
        startActivity(intent);
    }
}