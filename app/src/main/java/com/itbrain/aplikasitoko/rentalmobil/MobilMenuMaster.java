package com.itbrain.aplikasitoko.rentalmobil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;

public class MobilMenuMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menumaster_mobil);

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

    public void Identitas(View view) {
        Intent i = new Intent(this, MenuIdentitas_Mobil.class);
        i.putExtra("type","identitas");
        startActivity(i);
    }

    public void Merk(View view) {
        Intent i = new Intent(this, ItemMerk_Mobil.class);
        i.putExtra("type","merk");
        startActivity(i);
    }

    public void Kendaraan(View view) {
        Intent i = new Intent(this, Item_Kendaraan_Mobil.class);
        i.putExtra("type","kendaraan");
        startActivity(i);
    }

    public void Pelanggan(View view) {
        Intent i = new Intent(this, ItemPelangganMobil.class);
        i.putExtra("type","pelanggan");
        startActivity(i);
    }

    public void Pegawai(View view) {
        Intent i = new Intent(this, ItemPegawai_Mobil.class);
        i.putExtra("type","pegawai");
        startActivity(i);
    }
}