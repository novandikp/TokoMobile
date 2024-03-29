package com.itbrain.aplikasitoko.bengkel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.MenuPenjualan;
import com.itbrain.aplikasitoko.MenuPenjualanJasa;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.bengkel.Menu_Pembayaran_Hutang_Bengkel_;

public class AplikasiBengkel_MenuTransaksi extends AppCompatActivity {
    public static boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_bengkel_menu_transaksi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void JualOnderdil(View view) {
        Intent intent = new Intent(AplikasiBengkel_MenuTransaksi.this, MenuPenjualanBengkel.class);
        startActivity(intent);
    }

    public void Servis(View view) {
        Intent intent = new Intent(AplikasiBengkel_MenuTransaksi.this, Menu_Servis_Bengkel_.class);
       intent.putExtra("type","transaksi");
        startActivity(intent);
    }

    public void BayarTambah(View view) {
        Intent intent = new Intent(AplikasiBengkel_MenuTransaksi.this, Menu_Bayar_Bengkel_.class);
        startActivity(intent);
    }

    public void BayarHutang(View view) {
        Intent intent = new Intent(AplikasiBengkel_MenuTransaksi.this, Menu_Pembayaran_Hutang_Bengkel_.class);
        startActivity(intent);
    }
}