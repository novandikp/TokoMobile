package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Salon_Menu_Utama extends AppCompatActivity {

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    TextView ya,tidak;
    DatabaseSalon db;
    View view, dialogView;
    ConfigSalon config;
    CardView cv;
    boolean status;
    String proVersion = "salon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_salon_menu_utama);

        db = new DatabaseSalon(this);
        view = this.findViewById(android.R.id.content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = db.sq(QuerySalon.selectwhere("tblidentitas") + QuerySalon.sWhere("ididentitas", "1"));
        c.moveToNext();
        FunctionSalon.setText(view, R.id.textView478, FunctionSalon.upperCaseFirst(FunctionSalon.getString(c, "nama")));
        FunctionSalon.setText(view, R.id.textView479, FunctionSalon.getString(c, "alamat"));
    }

    public void PindahMaster(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Master.class);
        startActivity(intent);
    }

    public void PindahLaporan(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Laporan.class);
        startActivity(intent);
    }

    public void PindahTransaksi(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Transaksi.class);
        startActivity(intent);
    }

    public void PindahUtilitas(View view) {
        Intent intent = new Intent(Aplikasi_Salon_Menu_Utama.this, Aplikasi_Salon_Menu_Utillitas.class);
        startActivity(intent);
    }
    private void keluar(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.create();
        alert.setMessage("Apakah anda yakin ingin keluar?");
        alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        keluar();
    }
}