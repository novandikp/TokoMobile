package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        Intent intent = new Intent(MobilMenuMaster.this, MobilMenuMerk.class);
        startActivity(intent);
    }

    public void MenuKendaraan(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, MenuKendaraanMobil.class);
        startActivity(intent);
    }

    public void MenuPelanggan(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, MenuPelangganMobil.class);
        startActivity(intent);
    }

    public void MenuPegawai(View view) {
        Intent intent = new Intent(MobilMenuMaster.this, MobilMenuPegawai.class);
        startActivity(intent);
    }
}