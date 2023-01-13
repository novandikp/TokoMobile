package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class AplikasiKlinik_Menu_Utilitas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_klinik_menu_utilitas);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahRestore(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Utilitas.this, Restore_Data_Klinik_.class);
        startActivity(intent);
    }

    public void PindahBackup(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Utilitas.this, Backup_Data_Klinik_.class);
        startActivity(intent);
    }
}