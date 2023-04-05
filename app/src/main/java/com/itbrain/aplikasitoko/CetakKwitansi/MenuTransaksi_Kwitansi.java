package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class MenuTransaksi_Kwitansi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menutransaksi_kwitansi);


        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void cvCetakKwitansi(View view) {
        Intent intent=new Intent(this, MenuCetakTransaksi_Kwitansi.class);
        startActivity(intent);
    }

    public void cvCetakUlang(View view) {
        Intent intent=new Intent(this, CetakUlang_Kwitansi.class);
        startActivity(intent);
    }
}