package com.itbrain.aplikasitoko.Laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;

public class LaundryMenuTransaksi extends AppCompatActivity {
    SharedPreferences getPrefs,pref2,pref3;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenutransaksi);
        ImageView i = findViewById(R.id.imageView24);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pref2 = getSharedPreferences("id", MODE_PRIVATE);
        edit = pref2.edit();
    }

        public void clickTerima(View view) {
            Intent intent = new Intent(LaundryMenuTransaksi.this, ActivityTransaksiTerimaLaundry.class);
            intent.putExtra("status","terima");
            startActivity(intent);
        }

        public void clickProses(View view) {
            Intent intent = new Intent(LaundryMenuTransaksi.this, ActivityTransaksiProsesLaundry.class);
            startActivity(intent);        }

        public void clickHutang(View view){
        Intent i = new Intent(LaundryMenuTransaksi.this, ActivityTransaksiHutangLaundry.class);
        startActivity(i);
        }
        public void keuangan(View view){
        Intent intent = new Intent(LaundryMenuTransaksi.this, ActivityTransaksiKeuanganLaundry.class);
       startActivity(intent);
        }
    }