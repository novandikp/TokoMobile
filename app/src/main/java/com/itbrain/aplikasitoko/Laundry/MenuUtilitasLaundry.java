package com.itbrain.aplikasitoko.Laundry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;

import java.util.Arrays;
import java.util.List;

public class MenuUtilitasLaundry extends AppCompatActivity {
    boolean bBackup = false;
    boolean bRestore= false;
    boolean bReset = false;
    View v;
    SharedPreferences getPrefs,pref2,pref3;
    SharedPreferences.Editor edit;
    String kondisi,deviceid;
    DatabaseLaundry db;
    String android_id="",infoTransaction="",prefstats="",prefid="",status="";
    ImageView ivStatus;
    String fitur;
    String belisku = PrefLaundry.productID;
    private List<String> skuList = Arrays.asList(PrefLaundry.productID, PrefLaundry.produkid2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuutilitaslaundry);
        ImageView i = findViewById(R.id.imageView13);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        v = this.findViewById(android.R.id.content);
        PackageInfo pinfo;
        String versionName = "";
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ModulLaundry.setText(v, R.id.tvVersionName, "V " + versionName);

        db = new DatabaseLaundry(this);
        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        bBackup = getPrefs.getBoolean("inBackup", false);
        bRestore = getPrefs.getBoolean("inRestore", false);
        bReset = getPrefs.getBoolean("inReset", false);
        pref2 = getSharedPreferences("id", MODE_PRIVATE);
        edit = pref2.edit();
        pref3 = getSharedPreferences("dataVersion", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void petunjuk(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")));
    }
    public void backup(View view) {
        boolean free=false;
        SharedPreferences pref2=getSharedPreferences("id",MODE_PRIVATE);


        startActivity(new Intent(this, ActivityUtilitasBackupLaundry.class));

    }
    public void restore(View view) {
        boolean free=false;
        SharedPreferences pref2=getSharedPreferences("id",MODE_PRIVATE);

            startActivity(new Intent(this, ActivityUtilitasRestoreLaundry.class));
        }


    public void reset(View view){
        reset3() ;
//        deviceid = Modul.getDecrypt(getPrefs.getString(Modul.getEncrypt("deviceid"),"")) ;
//        bReset = getPrefs.getBoolean("inReset",false) ;
//        if(!bReset && !Modul.getDeviceID(getContentResolver()).equals(deviceid)){
//
//        } else {
//            kondisi = "reset" ;
//        }
    }
    public void reset3(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin Akan Reset Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        reset2();
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void reset2(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Benarkah Anda akan Reset Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Batal",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertDialogBuilder.setNegativeButton("Lanjutkan",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset1();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void reset1(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Reset akan menghilangkan atau menghapus semua data dalam Aplikasi ini ? ");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ModulLaundry.deleteFile("data/data/com.itbrain.aplikasitoko/databases/"+ PrefLaundry.db) ;
                        db.cektbl() ;
                        Intent i=new Intent(MenuUtilitasLaundry.this,LaundryMenuUtamaMaster.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}