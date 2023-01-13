package com.itbrain.aplikasitoko.bengkel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.MenuLaporanDua;
import com.itbrain.aplikasitoko.MenuLaporanSatu;
import com.itbrain.aplikasitoko.MenuLaporanTiga;
import com.itbrain.aplikasitoko.R;

public class AplikasiBengkel_Menu_Laporan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_bengkel_menu_laporan);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
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

    public void LaporanBayar(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Bayar_Bengkel_.class);
//        intent.putExtra("type", "bayar");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanDua.class);
        i.putExtra("type", "bayar");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void LaporanPelanggan(View view) {
//        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Pelanggan_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanSatu.class);
        i.putExtra("type", "pelanggan");
        startActivity(i);
    }

    public void LaporanTeknisi(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Teknisi_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanSatu.class);
        i.putExtra("type", "teknisi");
        startActivity(i);
    }

    public void LaporanPenjualan(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Penjualan_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanDua.class);
        i.putExtra("type", "jual");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void LaporanServis(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Servis_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanDua.class);
        i.putExtra("type", "servis");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void LaporanPendapatan(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Pendapatan_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanTiga.class);
        i.putExtra("type", "pendapatan");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void LaporanServisTeknisi(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Servis_Teknisi_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanTiga.class);
        i.putExtra("type", "servisteknisi");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void LaporanLaba(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Laba_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanTiga.class);
        i.putExtra("type", "laba");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    public void LaporanHutang(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Laporan.this, Laporan_Hutang_Bengkel_.class);
//        startActivity(intent);
        Intent i = new Intent(AplikasiBengkel_Menu_Laporan.this, MenuLaporanDua.class);
        i.putExtra("type", "hutang");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}