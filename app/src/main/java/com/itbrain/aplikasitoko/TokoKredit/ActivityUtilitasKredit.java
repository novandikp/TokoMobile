package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKredit.ModuleKredit;

public class ActivityUtilitasKredit extends AppCompatActivity {

    FConfigKredit config;
    FKoneksiKredit db;
    SharedPreferences getPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilitas_kredit);

        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        getPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void tentang(View view) {
        ModuleKredit.goToActivity(this, ActivityTentangKredit.class);
    }

    public void resetData(View view) {
        reset3();
    }

    public void restore(View view) {
        ModuleKredit.goToActivity(this, ActivityRestoreKredit.class);
    }

    public void backup(View view) {
        ModuleKredit.goToActivity(this, ActivityBackupKredit.class);
    }

    public void reset3() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin Akan Reset Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Iya", (arg0, arg1) -> reset2());
        alertDialogBuilder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void reset2() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Benarkah Anda akan Reset Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Batal", (arg0, arg1) -> arg0.dismiss());

        alertDialogBuilder.setNegativeButton("Lanjutkan", (dialog, which) -> reset1());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void reset1() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Reset akan menghilangkan atau menghapus semua data dalam Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Reset", (arg0, arg1) -> {
            FFunctionKredit.deleteFile("data/data/com.itbrain.aplikasitoko/databases/" + config.getDb());
            try {
                SharedPreferences.Editor e = getPrefs.edit();
                e.putBoolean("tambahkolom", true);
                e.apply();
                FFunctionKredit.deleteFile("data/data/com.itbrain.aplikasitoko/shared_prefs/temp.xml");
            } catch (Exception e) {
                Log.e("err", e.getMessage());
            }
            db.cektbl();
            Toast.makeText(this, "Reset Data Berhasil, Silahkan Restart Aplikasi agar sempurna dalam reset", Toast.LENGTH_SHORT).show();
        });
        alertDialogBuilder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
