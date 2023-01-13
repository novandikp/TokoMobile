package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Salon_Menu_Transaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_salon_menu_transaksi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahBooking(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Transaksi.this, Menu_Booking_Salon_.class);
        startActivity(intent);
    }

    public void PindahPembayaran(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Transaksi.this, Menu_Pembayaran_Salon_.class);
        startActivity(intent);
    }
}