package com.itbrain.aplikasitoko.tokosepatu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Menu_Utama_Toko_Sepatu extends AppCompatActivity {
    static final Integer WRITE_EXST = 0x3;
    static final Integer PHONE_EXST = 0x2;
    DatabaseTokoSepatu db;

    View v;
    ModulTokoSepatu config,temp ;

    String PROUDUK_ID="com.komputerkit.tokosepatuplus.full";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama_sepatu);
        String[] PERMISSIONS ={
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Aplikasi_Menu_Utama_Toko_Sepatu.this,PERMISSIONS, WRITE_EXST);
        }
        String kode= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmG1ls2hwPhy9jxjxjeN5ztzvWFXfYbXD1fAY2l0wW9WEjGH3fdimmbLfFoUXhwsu2/H6tWeOvS5Aj5YmeL0og/G9pFIvK6DSvQuLYCTKtevI1zWhbnj5oeUL/uqGgmt4tLie2kt/TsmgrIrQQ3hVYJOM6CfdG8ztzAU9nMJ9v7mU0SdbO7nQ/17LUpat00Liw7xWluAGtHbIGDZWN/vgOtbPKYFPbGwLwJcBsVM8hFO03OgBk5TkJ0R7SQ9oiXkAabF0/Ma/VEl+6Tiyb0GD1mkQXO547RHaU8U1o0ov15U91bn7sEfjMOdo+f+dzaXgyTmqX7tLjyWeu2cqeGR/CwIDAQAB";

        db = new DatabaseTokoSepatu(this);
        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));
        v = this.findViewById(android.R.id.content);
        setText();
        videoTutor();
        String profile = config.getCustom("profil","");
        if (Splash_Activity_Sepatu.status){
            setPro();
        }
    }

    public void promo(View view) {
//        String appname ="com.ahlikasir.kasirportableonline";
//        try{
//            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id="+appname)));
//        }catch (Exception e){
//            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+appname)));
//        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://shop.kiosaplikasi.com/daftar")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText();
        String profile = config.getCustom("profil","");
        if (Splash_Activity_Sepatu.status){
            setPro();
        }

    }


    private void DialogBeli() {
        final String produk="com.komputerkit.tokosepatuplus.full";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_inapp_sepatu, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        CardView beli = dialogView.findViewById(R.id.beli);
        CardView tidak = dialogView.findViewById(R.id.cancel);
        CardView petunjuk = dialogView.findViewById(R.id.petunjuk);
        final AlertDialog dialogi = dialog.create();


        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogi.cancel();

            }
        });


        petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/ixy-dd2jfsc")));
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogi.cancel();
            }
        });

        dialogi.show();
    }



    private void videoTutor(){
        SharedPreferences sp = getSharedPreferences("MyPrefs", 0);
        if (sp.getBoolean("perta", true)) {
            sp.edit().putBoolean("perta", false).apply();
            DialogForm();
        }
    }

    private void DialogForm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_video,null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        TextView iya = dialogView.findViewById(R.id.iya);
        TextView tidak = dialogView.findViewById(R.id.tidak);

        final AlertDialog dialogi = dialog.create();


        iya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")) ;
                startActivity(i);
                dialogi.cancel();
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogi.cancel();
            }
        });

        dialogi.show();
    }

    private void setPro(){
        CardView cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.INVISIBLE);
    }


    public void master(View view) {
        Intent i = new Intent(Aplikasi_Menu_Utama_Toko_Sepatu.this,Aplikasi_Menu_Master_Toko_Sepatu.class);
        startActivity(i);
    }

    public void transaksi(View view) {
        Intent i = new Intent(Aplikasi_Menu_Utama_Toko_Sepatu.this, Aplikasi_Menu_Transaksi_Toko_Sepatu.class);
        startActivity(i);
    }


    public void laporan(View view) {
        Intent i = new Intent(Aplikasi_Menu_Utama_Toko_Sepatu.this, Aplikasi_Menu_Laporan_Toko_Sepatu.class);
        startActivity(i);
    }

    public void utilitas(View view) {
        Intent i = new Intent(Aplikasi_Menu_Utama_Toko_Sepatu.this, Aplikas_Menu_Utilitasi_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        startActivity(i);
    }

    public  void setText(){
        try{
            Cursor c=db.sq("SELECT * FROM tbltoko WHERE idtoko=1");
            c.moveToNext();
            ModulTokoSepatu.setText(v,R.id.header,ModulTokoSepatu.getString(c,"namatoko"));
            ModulTokoSepatu.setText(v,R.id.subheader,ModulTokoSepatu.getString(c,"alamattoko"));
        }catch (Exception e){

        }

    }

    public void petunjuk(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda ingin melihat video petunjuk? ");
        alertDialogBuilder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")) ;
                startActivity(i);
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


    public void beli(View view) {

        Intent i = new Intent(this, Aplikas_Menu_Utilitasi_Toko_Sepatu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
}
