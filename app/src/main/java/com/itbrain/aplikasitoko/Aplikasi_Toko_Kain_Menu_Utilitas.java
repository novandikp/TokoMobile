package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.databinding.AplikasiTokoKainMenuUtilitasBinding;

public class Aplikasi_Toko_Kain_Menu_Utilitas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_toko_kain_menu_utilitas);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahBackup(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Utilitas.this, Backup_Data_Toko_Kain_.class);
        startActivity(intent);
    }

    public void PindahRestore(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Utilitas.this, Restore_Data_Toko_Kain_.class);
        startActivity(intent);
    }
}