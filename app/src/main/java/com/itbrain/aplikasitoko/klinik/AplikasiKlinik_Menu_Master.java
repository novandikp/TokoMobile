package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

public class AplikasiKlinik_Menu_Master extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_klinik_menu_master);

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

    public void PindahIdentitas(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Master.this, Identitas_Klinik.class);
        startActivity(intent);
    }

    public void PindahJasa(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Master.this, Tambah_Jasa_Klinik_.class);
        startActivity(intent);
    }

    public void PindahPasien(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Master.this, Tambah_Pasien_Klinik.class);
        startActivity(intent);
    }

    public void PindahDokter(View view) {
        Intent intent = new Intent(AplikasiKlinik_Menu_Master.this, Tambah_Dokter_Klinik_.class);
        startActivity(intent);
    }
}