package com.itbrain.aplikasitoko.rentalmobil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;

public class MobilMenuLaporan_Mobil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan_mobil);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Kendaraan(View view) {
        Intent i = new Intent(this,LaporanKendaraan_Mobil.class);
        i.putExtra("type","kendaraan");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void Pelanggan(View view) {
        Intent i = new Intent(this,MenuLaporanPelanggan_Mobil.class);
        i.putExtra("type","pelanggan");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void Pegawai(View view) {
        Intent i = new Intent(this, MenuLaporanBagPegawai_Mobil.class);
        i.putExtra("type","pegawai");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void Rental(View view) {
        Intent i = new Intent(this, MenuLaporanBagRental_Mobil.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void Pendapatan(View view) {
        Intent i = new Intent(this, MenuLaporanBagPendapatan_Mobil.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}