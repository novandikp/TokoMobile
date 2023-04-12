package com.itbrain.aplikasitoko.Laundry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

public class LaundryMenuUtamaMaster extends AppCompatActivity {
    static final Integer WRITE_EXST = 0x3 ;
    DatabaseLaundry db;
    View v;
    PrefLaundry prefLaundry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundry_utama);
        db = new DatabaseLaundry(this) ;
        v=this.findViewById(android.R.id.content);
        prefLaundry =new PrefLaundry(getSharedPreferences("id",MODE_PRIVATE));
        final SharedPreferences sp = getSharedPreferences("MyPrefs", 0);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.laundry_utama);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c=db.sq(Query.selectwhere("tblidentitas")+Query.sWhere("ididentitas","1"));
        c.moveToNext();
        ModulLaundry.setText(v,R.id.tvNamaToko, ModulLaundry.upperCaseFirst(ModulLaundry.getString(c,"namatoko")));
        ModulLaundry.setText(v,R.id.tvAlamatToko, ModulLaundry.getString(c,"alamattoko"));

    }

    public void promo(View view) {
//        String appname ="com.komputerkit.laundryonline";
//        try{
//            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id="+appname)));
//        }catch (Exception e){
//            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+appname)));
//        }

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://shop.kiosaplikasi.com/daftar")));
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(LaundryMenuUtamaMaster.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LaundryMenuUtamaMaster.this, permission)) {
                ActivityCompat.requestPermissions(LaundryMenuUtamaMaster.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(LaundryMenuUtamaMaster.this, new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah anda yakin ingin keluar?")
                .setNegativeButton("Batal",null)
                .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    public void clickViewMaster(View view) {
        startActivity(new Intent(this,LaundryMenuMaster.class));
    }

    public void clickViewTransaksi(View view) {
        startActivity(new Intent(this,LaundryMenuTransaksi.class));
    }

    public void clickViewLaporan(View view) {
        startActivity(new Intent(this,LaundryMenuLaporan.class));
    }

    public void clickViewUtilitas(View view) {
        startActivity(new Intent(this,MenuUtilitasLaundry.class));
    }
    }