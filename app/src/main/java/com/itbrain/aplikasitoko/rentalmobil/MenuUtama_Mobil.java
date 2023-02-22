package com.itbrain.aplikasitoko.rentalmobil;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class MenuUtama_Mobil extends AppCompatActivity {

    DatabaseRentalMobil db;
    View v;
    public static boolean status;
    public static String produk ="rentalmobilpro";
    String deviceid;
    ModulRentalMobil config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama_mobil);

        db= new DatabaseRentalMobil(this);
        v=this.findViewById(android.R.id.content);
        setText();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        AlertDialog alertDialog ;
        alert.setMessage("Apakah anda yakin keluar dari aplikasi ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();

                        System.exit(0);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog=alert.create();
        alertDialog.setTitle("Keluar Aplikasi");
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText();
    }

    private void setText(){
        try {
            Cursor c = db.sq(ModulRentalMobil.selectwhere("tbltoko") + ModulRentalMobil.sWhere("idtoko", "1"));
            c.moveToNext();
            ModulRentalMobil.setText(v, R.id.textView478, ModulRentalMobil.getString(c, "namatoko"));
            ModulRentalMobil.setText(v, R.id.textView479, ModulRentalMobil.getString(c, "alamattoko"));
        }catch (Exception e){

        }
    }


    public void PindahMaster(View view) {
        Intent i = new Intent(this, MobilMenuMaster.class);
        startActivity(i);
    }

    public void PindahTransaksi(View view) {
        Intent i = new Intent(this, MenuTransaksi_Mobil.class);
        startActivity(i);
    }

    public void PindahLaporan(View view) {
        Intent i = new Intent(this, MobilMenuLaporan_Mobil.class);
        startActivity(i);
    }

    public void PindahUtilitas(View view) {
        Intent i = new Intent(this, MenuUtilitas_Mobil.class);
        startActivity(i);
    }
}