package com.itbrain.aplikasitoko.tokosepatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Menu_Laporan_Toko_Sepatu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan_sepatu);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Menu Laporan");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void pelanggan(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Sepatu.class);
        i.putExtra("type","lappelanggan");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void barang(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Sepatu.class);
        i.putExtra("type","lapbarang");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void lapel(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Sepatu.class);
        i.putExtra("type","pelanggan");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void lapbarang(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_Per_Barang_Sepatu.class);
        i.putExtra("type","barang");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void lapkate(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Sepatu.class);
        i.putExtra("type","kategori");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void lapuk(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Sepatu.class);
        i.putExtra("type","ukuran");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void pendapatan(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Kedua_Sepatu.class);
        i.putExtra("type","pendapatan");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void hutang(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Sepatu.class);
        i.putExtra("type","hutang");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void dethutang(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Kedua_Sepatu.class);
        i.putExtra("type","dethutang");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void retur(View view) {
        Intent i = new Intent(Aplikasi_Menu_Laporan_Toko_Sepatu.this,Menu_Laporan_List_Kedua_Sepatu.class);
        i.putExtra("type","retur");
        startActivity(i);
    }



    public void keuangan(View view) {
        Intent i = new Intent(this,Menu_Dompet_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }
}
