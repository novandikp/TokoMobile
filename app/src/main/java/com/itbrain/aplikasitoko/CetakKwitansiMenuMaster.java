package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CetakKwitansiMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cetakkwitansimenumaster);
    }

    public void Identitas(View view) {
        Intent intent = new Intent(CetakKwitansiMenuMaster.this, MenuIdentitasCetakKwitansi.class);
        startActivity(intent);
    }

    public void Jasa(View view) {
        Intent intent = new Intent(CetakKwitansiMenuMaster.this, MenuDaftarJasaCetakKwitansi.class);
        startActivity(intent);
    }

    public void Pelanggan(View view) {
        Intent intent = new Intent(CetakKwitansiMenuMaster.this, MenuDaftarPelangganCetakKwitansi.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( CetakKwitansiMenuMaster.this, CetakKwitansiMenuMasterUtama.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}