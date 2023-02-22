package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Salon_Menu_Utillitas extends AppCompatActivity {

    Toolbar appbar;
    static final Integer WRITE_EXST = 0x3;
    DatabaseSalon db;
    ConfigSalon config;
    View v;
    boolean bBackup = false;
    boolean bRestore = false;
    boolean bReset = false;
    SharedPreferences getPrefs;

    String deviceid;
    String productID = "salon", produkid2 = "com.itbrain.aplikasitoko.full";

    String belisku = productID;

    boolean status = false;
    ConstraintLayout pro;

    TextView desc4, sub4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_salon_menu_utillitas);

        appbar = (Toolbar) findViewById(R.id.toolbar68);
//        setSupportActionBar(appbar);
//        getSupportActionBar().setElevation(0);
//        Function.btnBack("Utilitas",getSupportActionBar());

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config = new ConfigSalon(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseSalon(this);
        db.cektbl();
        v = this.findViewById(android.R.id.content);

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        bBackup = getPrefs.getBoolean("inBackup", false);
        bRestore = getPrefs.getBoolean("inRestore", false);
        bReset = getPrefs.getBoolean("inReset", false);
//        deviceid = Function.getDecrypt(getPrefs.getString("deviceid","")) ;
//        iapHelper = new IAPHelper(this,this,skuList);
        //pro = (ConstraintLayout)findViewById(R.id.pro);
        desc4 = (TextView) findViewById(R.id.tvidentitas);
        sub4 = (TextView) findViewById(R.id.tvidentitasmobil);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset(View view) {
        reset2();
    }

    public void backup(View v) {
//        if (status) {
            Intent i = new Intent(this, ActivityBackupSalon.class);
            startActivity(i);
        //}
    }

    public void restore(View v) {
//        if (status) {
            Intent i = new Intent(this, ActivityRestoreSalon.class);
            startActivity(i);
        }
    //}

    public void reset2() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda Yakin Ingin Mereset Aplikasi Ini? ");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        reset1();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void reset1() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Reset akan menghapus semua data dalam Aplikasi ini");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        FunctionSalon.deleteFile("data/data/com.itbrain.aplikasitoko/databases/" + "db_salon");
                        db.cektbl();
//                        Intent i=new Intent(ActivityUtilitas.this,ActivitySplash.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
                        Toast.makeText(Aplikasi_Salon_Menu_Utillitas.this, "Reset Data Berhasil, Aplikasi Terestart", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

//    private void DialogBeli() {
//        startActivity(new Intent(this, Aktivasi.class));
//    }
    public void petunjuk(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.video_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        TextView ya = dialogView.findViewById(R.id.ya);
        TextView tidak = dialogView.findViewById(R.id.tidak);

        final AlertDialog dial = dialog.create();

        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")) ;
                startActivity(i);
                dial.cancel();
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial.cancel();
            }
        });

        dial.show();
    }
}