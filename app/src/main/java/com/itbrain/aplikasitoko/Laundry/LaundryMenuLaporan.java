package com.itbrain.aplikasitoko.Laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.databinding.ActivityLaporanPenjualanItemKasirBinding;

public class LaundryMenuLaporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenulaporan);
        ImageView i = findViewById(R.id.imageView27);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void jasa(View view){
        startActivity(new Intent(this, Laundry_Laporan_Jasa.class));
    }
    public void pegawai(View view){
        startActivity(new Intent(this,Laundry_Laporan_Pegawao.class));
    }
    public void pelanggan(View view){
        startActivity(new Intent(this, Laundry_Laporan_Pelangan.class));
    }
    public void laporan(View view){
        startActivity(new Intent(this, LaundryItemLaporanLaundry.class));
    }
    public void proses(View view){
        startActivity(new Intent(this, LaundryItemLaporanProsesLaundry.class));
    }
    public void pendapatan(View view){
        startActivity(new Intent(this, LaundryItemLaporanPendapatan.class));
    }
    public void hutang(View view){
        startActivity(new Intent(this, Laundry_Laporan_Hutang.class));
    }

    public void keuangan(View view){
        startActivity(new Intent(this, Laporan_Keuangan_Laundry.class));
    }
}