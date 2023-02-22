package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Toko_Kain_Menu_Laporan_Toko_Kain extends AppCompatActivity {

    CardView cvLaporanPelanggan,cvLaporanBarang,cvLaporanPendapatan,cvLaporanOrder;
    Animation righttoleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kain);
      //  getSupportActionBar().setElevation(0);
        ImageButton imageButton = findViewById(R.id.kembaliLap);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            righttoleft = AnimationUtils.loadAnimation(this,R.anim.right_to_left_toko_kain);
            cvLaporanPelanggan = findViewById(R.id.cvLaporanPelanggan);
            cvLaporanBarang = findViewById(R.id.cvLaporanBarang);
            cvLaporanPendapatan = findViewById(R.id.cvLaporanPendapatan);
            cvLaporanOrder = findViewById(R.id.cvLaporanOrder);
            cvLaporanPelanggan.setAnimation(righttoleft);
            cvLaporanBarang.setAnimation(righttoleft);
            cvLaporanPendapatan.setAnimation(righttoleft);
            cvLaporanOrder.setAnimation(righttoleft);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void iLaporan(String value){
        Intent i=new Intent(this,Laporan_Pelanggan_Toko_Kain.class);
        i.putExtra("tab",value);
        startActivity(i);
    }

    private void iLaporanTanggal(String value){
        Intent i=new Intent(this,Laporan_Pendapatan_Toko_Kain_.class);
        i.putExtra("tab",value);
        startActivity(i);
    }
    private void iLaporanbarang(String value){
        Intent i=new Intent(this, Laporan_Barang_Toko_Kain.class);
        i.putExtra("tab",value);
        startActivity(i);
    }

    private void iLaporanorder(String value){
        Intent i=new Intent(this,Laporan_Order_Toko_Kain.class);
        i.putExtra("tab",value);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
//        Intent i = new Intent(this,MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
        finish();
    }

    public void cvLaporanPendapatan(View view) {
        iLaporanTanggal("pendapatan");
    }

    public void cvLaporanBarang(View view) {
        iLaporanbarang("kain");
    }

    public void cvLaporanPelanggan(View view) {
        iLaporan("pelanggan");
    }

    public void cvLaporanOrder(View view) {
        iLaporanorder("order");
    }
}