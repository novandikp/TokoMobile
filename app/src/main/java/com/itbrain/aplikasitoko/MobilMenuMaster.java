package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MobilMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilmenumaster);
    }

    public void MenuIdentitas(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, MenuIdentitasMobil.class);
        startActivity(intent);
    }

    public void MenuMerk(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, DaftarMerkMobil.class);
        startActivity(intent);
    }

    public void MenuKendaraan(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, DaftarKendaraanMobil.class);
        startActivity(intent);
    }

    public void MenuPelanggan(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, DaftarPelangganMobil.class);
        startActivity(intent);
    }

    public void MenuPegawai(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, DaftarPegawaiMobil.class);
        startActivity(intent);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MobilMenuMaster.this, MobilMenuUtamaMaster.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}