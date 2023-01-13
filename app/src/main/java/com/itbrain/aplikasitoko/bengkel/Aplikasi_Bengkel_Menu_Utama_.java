package com.itbrain.aplikasitoko.bengkel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Bengkel_Menu_Utama_ extends AppCompatActivity {

    TextView tvNama, tvAlamat;
    Database_Bengkel_ db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_bengkel_menu_utama);

        tvNama = findViewById(R.id.tvNamaBengkel);
        tvAlamat = findViewById(R.id.tvAlamatBengkel);

    }

    protected void onResume() {
        super.onResume();

        load();
    }


    public void selectData() {
        String sql = "SELECT namatoko, alamattoko FROM tbltoko";

        Cursor cursor = db.sq(sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                tvNama.setText(cursor.getString(cursor.getColumnIndex("namatoko")));
                tvAlamat.setText(cursor.getString(cursor.getColumnIndex("alamattoko")));
            }
        }
    }

    public void load() {
        db = new Database_Bengkel_(this);

        selectData();
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Utama_.this, Aplikasi_Bengkel_Menu_Master_.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Utama_.this, AplikasiBengkel_MenuTransaksi.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Utama_.this, AplikasiBengkel_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_Bengkel_Menu_Utama_.this, AplikasiBengkel_Menu_Utilitas.class);
        startActivity(intent);
    }
}