package com.itbrain.aplikasitoko.tokosepatu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Menu_Master_Toko_Sepatu extends AppCompatActivity {
    ModulTokoSepatu config,temp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_master_toko_sepatu);
        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Master");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }






    public void kategori(View view) {
        Intent i = new Intent(Aplikasi_Menu_Master_Toko_Sepatu.this,Menu_Kategori_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void identitas(View view) {
        Intent i = new Intent(Aplikasi_Menu_Master_Toko_Sepatu.this,Menu_Identitas_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public void barang(View view) {
        Intent i = new Intent(Aplikasi_Menu_Master_Toko_Sepatu.this,Menu_Barang_Master_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);

    }

    public void pelanggan(View view) {
        Intent i = new Intent(Aplikasi_Menu_Master_Toko_Sepatu.this,Menu_Pelanggan_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }


//    public void keuangan(View view) {
//        Intent i = new Intent(Aplikasi_Menu_Master_Toko_Sepatu.this,MenuDompet.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
//        startActivity(i);
//    }
}
