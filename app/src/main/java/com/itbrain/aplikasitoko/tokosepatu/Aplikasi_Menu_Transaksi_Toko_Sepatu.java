package com.itbrain.aplikasitoko.tokosepatu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Menu_Transaksi_Toko_Sepatu extends AppCompatActivity  {
    ModulTokoSepatu config,temp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_transaksi_sepatu);
        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String kode= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmG1ls2hwPhy9jxjxjeN5ztzvWFXfYbXD1fAY2l0wW9WEjGH3fdimmbLfFoUXhwsu2/H6tWeOvS5Aj5YmeL0og/G9pFIvK6DSvQuLYCTKtevI1zWhbnj5oeUL/uqGgmt4tLie2kt/TsmgrIrQQ3hVYJOM6CfdG8ztzAU9nMJ9v7mU0SdbO7nQ/17LUpat00Liw7xWluAGtHbIGDZWN/vgOtbPKYFPbGwLwJcBsVM8hFO03OgBk5TkJ0R7SQ9oiXkAabF0/Ma/VEl+6Tiyb0GD1mkQXO547RHaU8U1o0ov15U91bn7sEfjMOdo+f+dzaXgyTmqX7tLjyWeu2cqeGR/CwIDAQAB";
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Aplikasi_Menu_Transaksi_Toko_Sepatu.this,Aplikasi_Menu_Utama_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void transaksi(View view) {
        Intent i = new Intent(Aplikasi_Menu_Transaksi_Toko_Sepatu.this,Daftar_Hutang_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void penjualan(View view) {
        String freetran = config.getCustom("transaksi","1");
        String profile = config.getCustom("profil","");
        if (Splash_Activity_Sepatu.status){
            Intent i = new Intent(Aplikasi_Menu_Transaksi_Toko_Sepatu.this,Menu_Penjualan_Barang_Transaksi_Sepatu.class);
            i.putExtra("type","kat");
            startActivity(i);

        }else{
            if(ModulTokoSepatu.strToInt(freetran)>5){
                Toast.makeText(this, "Limit telah terlampui", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, Aplikas_Menu_Utilitasi_Toko_Sepatu.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(Aplikasi_Menu_Transaksi_Toko_Sepatu.this,Menu_Penjualan_Barang_Transaksi_Sepatu.class);
                i.putExtra("type","kat");
                startActivity(i);
            }
        }
    }

    private void DialogBeli() {
        final String produk="com.komputerkit.tokosepatuplus.full";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_inapp_sepatu, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        CardView beli = dialogView.findViewById(R.id.beli);
        CardView tidak = dialogView.findViewById(R.id.cancel);
        CardView petunjuk = dialogView.findViewById(R.id.petunjuk);
        final AlertDialog dialogi = dialog.create();




        petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/ixy-dd2jfsc")));
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogi.cancel();
            }
        });

        dialogi.show();
    }
    public void retur(View view) {
        Intent i = new Intent(Aplikasi_Menu_Transaksi_Toko_Sepatu.this,Menu_Daftar_Retur_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    //

    public void pemasukan(View view) {
        Intent i = new Intent(Aplikasi_Menu_Transaksi_Toko_Sepatu.this,Menu_Pemasukan_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void pengeluaran(View view) {
        Intent i = new Intent(Aplikasi_Menu_Transaksi_Toko_Sepatu.this,Menu_Pengeluaran_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}