package com.itbrain.aplikasitoko.Laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;

public class LaundryMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundrymenumaster);
        ImageView i = findViewById(R.id.imageView26);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void clickIdentitas(View view) {
        startActivity(new Intent(this,MenuIdentitasLaundry.class));
    }

    public void clickPegawai(View view) {
        startActivity(new Intent(this,Menu_Master_Pegawai_Laundry.class));
    }

    public void clickPelanggan(View view) {
        startActivity(new Intent(this,MenuPelangganLaundry.class));
    }

    public void clickKategori(View view) {
        startActivity(new Intent(this,Menu_Master_Kategori_Laundry.class));
    }

    public void clickJasa(View view) {
        startActivity(new Intent(this, Menu_Master_Jasa_Laundry.class));
    }
}