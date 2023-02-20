package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Apotek_Plus_Keuangan_Menu_Utama extends AppCompatActivity {
    DatabaseApotek db;
    View v;
    public static boolean status;
    String deviceid;
    String produkid = "com.komputerkit.aplikasiapotekpluskeuangan.full";
    //    String produkid = "android.test.purchased";
    String produkid2 = "com.komputerkit.aplikasiapotekpluskeuangannew.full";
    String belisku =produkid;


    ModulApotek config;
    public static String lisence="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhg6GvLr9Qa2ZJ9GLK/3cjDPQmKNWKoosTN2gwI2kjVS48d17k7x+QJtgbE0NyVJYXk2UoqdnEo3UVBYgNNpFzlaNSiWNwDun39TvU3qiI8IJdxqgZRAWuljPu37KajdYsCFsVTj+ANLlJWfY34wnul2LYQvAnjZyR7BOdH22c3nxn3BL31+YVx7W4eiSjgqX43bRiYGh9tGvTvSkyJ9P5v8efFsOHCWnW095H7NrR5Cg/lGO5/7gwB4KSgx48qaUdNJPFWaPeTK0qorLnbiR4hceYH9tjO2+09RsILBFDPKkRcyGr0EGj8CnrszaOE6ZI+/ETu69i6heAgLk64G2+QIDAQAB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_apotek_plus_keuangan_menu_utama);
        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
setText();

    }
        @Override
        protected void onResume() {
            super.onResume();
            setText();
        }

    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Menu_Keuangan_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Keuangan_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Keuangan_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.this, Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas.class);
        startActivity(intent);
    }
    private void setText(){
        Cursor c= db.sq(ModulApotek.selectwhere("tbltoko")+ ModulApotek.sWhere("idtoko","1"));
        c.moveToNext();
        ModulApotek.setText(v,R.id.textView480, ModulApotek.getString(c,"namatoko"));
        ModulApotek.setText(v,R.id.textView481, ModulApotek.getString(c,"alamattoko"));
    }
}