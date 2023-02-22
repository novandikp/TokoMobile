package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.itbrain.aplikasitoko.R;

public class AplikasiKlinikDokter_Menu_Utama extends AppCompatActivity {
        DatabaseKlinik db;
        View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_klinik_menu_utama);
        db= new DatabaseKlinik(this);
        v=this.findViewById(android.R.id.content);
        setText();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setText();
    }

    private void setText(){
        try {
            Cursor c = db.sq(ModulKlinik.selectwhere("tbltoko") + ModulKlinik.sWhere("idtoko", "1"));
            c.moveToNext();
            ModulKlinik.setText(v, R.id.tvCom, ModulKlinik.getString(c, "namatoko"));
            ModulKlinik.setText(v, R.id.tvAlamat, ModulKlinik.getString(c, "alamattoko"));
        }catch (Exception e){

        }
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(AplikasiKlinikDokter_Menu_Utama.this, AplikasiKlinik_Menu_Utilitas.class);
        startActivity(intent);
    }



}