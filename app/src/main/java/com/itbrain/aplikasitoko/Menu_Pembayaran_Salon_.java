package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Menu_Pembayaran_Salon_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pembayaran_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahPembayaranBooking(View view) {
        Intent intent = new Intent(Menu_Pembayaran_Salon_.this, Form_Pembayaran_Booking_Salon_.class);
        startActivity(intent);
    }

    public void PinddahPembayaranLangsung(View view) {
        Intent intent = new Intent(Menu_Pembayaran_Salon_.this, Form_Pembayaran_Langsung_Salon_.class);
        startActivity(intent);
    }
}