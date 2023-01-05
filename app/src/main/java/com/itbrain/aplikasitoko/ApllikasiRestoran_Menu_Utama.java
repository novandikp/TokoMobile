package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ApllikasiRestoran_Menu_Utama extends AppCompatActivity {

    TextView tvNama, tvAlamat;
    Database_Restoran db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apllikasi_restoran_menu_utama);
        tvNama = findViewById(R.id.tvNamaToko);
        tvAlamat = findViewById(R.id.tvAlamatToko);
    }

    protected void onResume() {
        super.onResume();
        load();
    }

    public void selectData() {
        String sql = "SELECT namatoko, alamat FROM tblidentitas";
        Cursor c = db.sq(sql);
        if (c != null) {
            if (c.moveToNext()) {
                tvNama.setText(c.getString(c.getColumnIndex("namatoko")));
                tvAlamat.setText(c.getString(c.getColumnIndex("alamat")));
            }
        }
    }

    public void load() {
        db = new Database_Restoran(this);
        selectData();
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(ApllikasiRestoran_Menu_Utama.this, AplikasiRestoran_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(ApllikasiRestoran_Menu_Utama.this, AplikasiRetoran_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(ApllikasiRestoran_Menu_Utama.this, Aplikasi_Restoran_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(ApllikasiRestoran_Menu_Utama.this, Aplikasi_Restoran_Menu_Utilitas.class);
        startActivity(intent);
    }
}