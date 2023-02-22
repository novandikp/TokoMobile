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

public class Aplikasi_Toko_Kain_Menu_Master_Toko_Kain extends AppCompatActivity {
    CardView cvIdentitas,cvKategori,cvKain,cvPelanggan;
    Animation bottomtotop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_toko_kain_menu_master);
        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            bottomtotop = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top_toko_kain);
            cvIdentitas = findViewById(R.id.cvIdentitas);
            cvKategori = findViewById(R.id.cvKategori);
            cvKain = findViewById(R.id.cvBarang);
            cvPelanggan = findViewById(R.id.cvPelanggan);
            cvIdentitas.setAnimation(bottomtotop);
            cvKategori.setAnimation(bottomtotop);
            cvKain.setAnimation(bottomtotop);
            cvPelanggan.setAnimation(bottomtotop);
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

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master_Toko_Kain.this, Identitas_Toko_Kain.class);
        startActivity(intent);
    }

    public void PindahKategori(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master_Toko_Kain.this, Kategori_Toko_Kain_.class);
        startActivity(intent);
    }

    public void PindahKain(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master_Toko_Kain.this, TambahKain_Toko_Kain.class);
        startActivity(intent);
    }

    public void PindahPelanggan(View view) {
        Intent intent = new Intent(Aplikasi_Toko_Kain_Menu_Master_Toko_Kain.this, Pelanggan_Toko_Kain_.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
//        Intent i = new Intent(this,MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
        finish();
    }
}