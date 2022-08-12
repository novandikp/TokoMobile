package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MenuDaftarJasaCetakKwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_jasa_cetak_kwitansi);
        ImageView Kembali = (ImageView) findViewById(R.id.Kembali);
        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action;
                Intent intent = new Intent(MenuDaftarJasaCetakKwitansi.this,CetakKwitansiMenuMaster.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void TambahJasa(View view) {
        Intent intent = new Intent(MenuDaftarJasaCetakKwitansi.this, MenuTambahJasaAntrianPortable.class);
        startActivity(intent);
    }
}