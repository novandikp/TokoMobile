package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class MenuLaporan_Kwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporan_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void laporanJasa(View view) {
        Intent intent=new Intent(this,LaporanJasa_Kwitansi.class);
        intent.putExtra("type","jasa") ;
        startActivity(intent);
    }

    public void LaporanPelanggan(View view) {
        Intent intent=new Intent(this,LaporanPelanggan_Kwitansi.class);
        intent.putExtra("type","pelanggan") ;
        startActivity(intent);
    }

    public void LaporanTransaksi(View view) {
        Intent intent=new Intent(this, LaporanTransaksi_Kwitansi.class);
        intent.putExtra("type","transaksi") ;
        startActivity(intent);
    }
}