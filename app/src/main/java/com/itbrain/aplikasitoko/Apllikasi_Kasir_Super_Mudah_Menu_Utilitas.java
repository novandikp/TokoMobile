package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Apllikasi_Kasir_Super_Mudah_Menu_Utilitas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apllikasi_kasir_super_mudah_menu_utilitas);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void BackupData(View view) {
        Intent intent = new Intent(Apllikasi_Kasir_Super_Mudah_Menu_Utilitas.this, Backup_Data_Kasir_.class);
        startActivity(intent);
    }

    public void RestoreData(View view) {
        Intent intent = new Intent(Apllikasi_Kasir_Super_Mudah_Menu_Utilitas.this, Restore_Data_Kasir_.class);
        startActivity(intent);
    }
}