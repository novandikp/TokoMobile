package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TabunganMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabunganmenumaster);
    }

    public void MenuIdentitas(View view) {
        Intent intent = new Intent(TabunganMenuMaster.this, MenuIdentitasTabungan.class);
        startActivity(intent);
    }

    public void MenuAnggota(View view) {
        Intent intent = new Intent(TabunganMenuMaster.this, MenuDaftarAnggotaTabungan.class);
        startActivity(intent);
    }

    public void MenuJenisSimpanan(View view) {
        Intent intent = new Intent(TabunganMenuMaster.this, MenuDaftarJenisSimpananTabungan.class);
        startActivity(intent);
    }
}