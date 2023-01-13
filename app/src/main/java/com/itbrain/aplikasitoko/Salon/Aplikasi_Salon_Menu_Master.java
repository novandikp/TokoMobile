package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Salon_Menu_Master extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_salon_menu_master);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Master.this, Menu_Identitas_Salon_.class);
        startActivity(intent);
    }

    public void PindahJasa(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Master.this, Menu_Jasa_Salon_.class);
        startActivity(intent);
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Master.this, Menu_Pelanggan_Salon_.class);
        startActivity(intent);
    }
}