package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MobilMenuUtamaMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilmenuutamamaster);
    }

    public void MobilMenuMaster(View view) {
        Intent intent = new Intent(MobilMenuUtamaMaster.this, MobilMenuMaster.class);
        startActivity(intent);
    }

    public void MenuTransaksi(View view) {
        Intent intent = new Intent(MobilMenuUtamaMaster.this, MobilMenuTransaksi.class);
        startActivity(intent);
    }

    public void MenuLaporan(View view) {
        Intent intent = new Intent(MobilMenuUtamaMaster.this, MobilMenuLaporan.class);
        startActivity(intent);
    }

    public void MenuUtilitas(View view) {
        Intent intent = new Intent(MobilMenuUtamaMaster.this, MobilMenuUtilitas.class);
        startActivity(intent);
    }
}