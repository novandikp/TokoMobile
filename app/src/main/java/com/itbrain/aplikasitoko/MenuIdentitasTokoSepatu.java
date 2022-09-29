package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuIdentitasTokoSepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_identitas_toko_sepatu);
    }

    public void Kembali(View view) {
        Intent intent = new Intent( MenuIdentitasTokoSepatu.this, Aplikasi_Toko_Sepatu_Menu_Master.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}