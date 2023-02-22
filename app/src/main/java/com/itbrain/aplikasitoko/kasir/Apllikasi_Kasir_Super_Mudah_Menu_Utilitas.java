package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

public class Apllikasi_Kasir_Super_Mudah_Menu_Utilitas extends AppCompatActivity {

    static final Integer WRITE_EXST = 0x3;
    DatabaseKasir db;
    FConfigKasir config;
    View v;
    SharedPreferences getPrefs;
    CekInAppKasir cekInApp;
    String produk = "com.komputerkit.pklsupermudah.full";
    Integer kondisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apllikasi_kasir_super_mudah_menu_utilitas);
        //getSupportActionBar().setElevation(0);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        db.cektbl();
        v = this.findViewById(android.R.id.content);



        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        cekInApp = new CekInAppKasir(this);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void openDialogPetunjuk() {
        DialogPetunjukKasir dialogPetunjuk = new DialogPetunjukKasir();
        dialogPetunjuk.show(getSupportFragmentManager(), "");
    }

    public void petunjukKasir(View v) {
        openDialogPetunjuk();
    }

    public void BackupData(View view) {
        Intent intent = new Intent(Apllikasi_Kasir_Super_Mudah_Menu_Utilitas.this, Backup_Data_Kasir_.class);
        startActivity(intent);
    }

    public void RestoreData(View view) {
        Intent intent = new Intent(Apllikasi_Kasir_Super_Mudah_Menu_Utilitas.this, Restore_Data_Kasir_.class);
        startActivity(intent);
    }

    public void resetKasir(View view){
        reset3();
    }

    public void reset3() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin Akan Reset Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        reset2();
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void reset2() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Benarkah Anda akan Reset Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Batal",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertDialogBuilder.setNegativeButton("Lanjutkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset1();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void reset1() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Reset akan menghilangkan atau menghapus semua data dalam Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        FFunctionKasir.deleteFile("data/data/com.itbrain.aplikasitoko/databases/" + FConfigKasir.getDb());
                        try {
                            SharedPreferences.Editor e = getPrefs.edit();
                            e.putBoolean("tambahkolom", true);
                            e.apply();
                            FFunctionKasir.deleteFile("data/data/com.itbrain.aplikasitoko/shared_prefs/temp.xml");
                        } catch (Exception e) {

                        }
                        db.cektbl();
                        Toast.makeText(Apllikasi_Kasir_Super_Mudah_Menu_Utilitas.this, "Reset Data Berhasil, Silahkan Restart Aplikasi agar sempurna dalam reset", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}