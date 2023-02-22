package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_TokoKain_Menu_Utama_Toko_Kain extends AppCompatActivity {
    DatabaseTokoKain db;
    View v;

    TextView etNamautama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_menu_utama_toko_kain);

        db = new DatabaseTokoKain(this) ;
        db.cektbl() ;
        v=this.findViewById(android.R.id.content);
        etNamautama = findViewById(R.id.tvNamaUtama);

    }
    @Override
    protected void onResume() {
        super.onResume();
        Cursor c=db.sq(FQueryTokoKain.selectwhere("tblidentitas")+FQueryTokoKain.sWhere("id","1"));
        c.moveToNext();
        etNamautama.setText(KumFunTokoKain.upperCaseFirst(KumFunTokoKain.getString(c,"namatoko")));
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_TokoKain_Menu_Utama_Toko_Kain.this, Aplikasi_Toko_Kain_Menu_Utilitas_Toko_Kain.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent i = new Intent(Aplikasi_TokoKain_Menu_Utama_Toko_Kain.this, Transaksi_Toko_Kain.class);
        i.putExtra("status","terima");
        startActivity(i);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(Aplikasi_TokoKain_Menu_Utama_Toko_Kain.this, Aplikasi_Toko_Kain_Menu_Laporan_Toko_Kain.class);
        startActivity(intent);
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_TokoKain_Menu_Utama_Toko_Kain.this, Aplikasi_Toko_Kain_Menu_Master_Toko_Kain.class);
        startActivity(intent);
    }
}