package com.itbrain.aplikasitoko.restoran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TambahMeja;

public class AplikasiRestoran_Menu_Master extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_restoran_menu_master);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(AplikasiRestoran_Menu_Master.this, Identitas_Restoran.class);
        startActivity(intent);
    }

    public void PelangganRestoran(View view) {
        Intent intent = new Intent(AplikasiRestoran_Menu_Master.this, Pelanggan_Restoran_.class);
        startActivity(intent);
    }

    public void MejaRestoran(View view) {
        Intent intent = new Intent(AplikasiRestoran_Menu_Master.this, TambahMeja.class);
        startActivity(intent);
    }

    public void KategoriRestoran(View view) {
        Intent intent = new Intent(AplikasiRestoran_Menu_Master.this, Tambah_Kategori_Menu_Restoran_.class);
        startActivity(intent);
    }

    public void IsiMenuRestoran(View view) {
        Intent intent = new Intent(AplikasiRestoran_Menu_Master.this, Tambah_Isi_Menu_Restoran_.class);
        startActivity(intent);
    }
}