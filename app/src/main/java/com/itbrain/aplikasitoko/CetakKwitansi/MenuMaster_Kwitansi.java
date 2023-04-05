package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class MenuMaster_Kwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menumaster_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void cvIdentitas(View view) {
        Intent intent=new Intent(this, MenuIdentitas_Kwitansi.class);
        startActivity(intent);
    }

    public void cvJasa(View view) {
        Intent intent=new Intent(this, MenuJasa_Kwitansi.class);
        startActivity(intent);
    }

    public void cvPelanggan(View view) {
        Intent intent=new Intent(this, MenuPelanggan_Kwitansi.class);
        startActivity(intent);
    }
}