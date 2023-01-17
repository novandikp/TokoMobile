package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class FormTambahKain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_kain);

        ImageButton imageButton = findViewById(R.id.Kembali);
        ImageButton imageButton1 = findViewById(R.id.TambahKategori);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormTambahKain.this, Form_Tambah_Kategori_Kain_.class);
                startActivity(intent);
            }
        });
    }
}