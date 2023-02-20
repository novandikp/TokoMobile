package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;

public class Aplikasi_Toko_Kain_Menu_Utilitas_Toko_Kain extends AppCompatActivity {

    View v ;
    boolean bBackup = false;
    boolean bRestore= false;
    boolean bReset = false;
    SharedPreferences getPrefs,pref2,pref3;
    SharedPreferences.Editor edit;
    String kondisi,deviceid;
    DatabaseTokoKain db;
    String android_id="",infoTransaction="",prefstats="",prefid="",status="";
    ImageView ivStatus;
    String fitur;
    CardView cvBackup,cvRestore,cvReset,cvBeliVersi;
    Animation toptobottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_kain);
//        getSupportActionBar().setElevation(0);
        ImageButton imageButton = findViewById(R.id.kembalicoy2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            v=this.findViewById(android.R.id.content);
            PackageInfo pinfo;
            String versionName="";
            try {
                pinfo=getPackageManager().getPackageInfo(getPackageName(),0);
                versionName=pinfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            KumFunTokoKain.setText(v,R.id.tvVersi,"V "+versionName);

            db=new DatabaseTokoKain(this);
            getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            bBackup = getPrefs.getBoolean("inBackup",false) ;
            bRestore = getPrefs.getBoolean("inRestore",false) ;
            bReset = getPrefs.getBoolean("inReset",false) ;
            deviceid = KumFunTokoKain.getDecrypt(getPrefs.getString("deviceid","")) ;
            pref2=getSharedPreferences("id",MODE_PRIVATE);
            edit=pref2.edit();
            pref3=getSharedPreferences("dataVersion",MODE_PRIVATE);


            toptobottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom_toko_kain);
            cvBackup = findViewById(R.id.cardView);
            cvRestore = findViewById(R.id.cardView1);
            cvReset = findViewById(R.id.cardView2);
            cvBackup.setAnimation(toptobottom);
            cvReset.setAnimation(toptobottom);
            cvRestore.setAnimation(toptobottom);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reset(View view){
        deviceid = KumFunTokoKain.getDecrypt(getPrefs.getString(KumFunTokoKain.getEncrypt("deviceid"),"")) ;
        bReset = getPrefs.getBoolean("inReset",false) ;
        if(!bReset && !KumFunTokoKain.getDeviceID(getContentResolver()).equals(deviceid)){
            reset3() ;
        } else {
            kondisi = "reset" ;
        }
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
                        KumFunTokoKain.deleteFile("data/data/com.itbrain.aplikasitoko/databases/"+"dbtokokain") ;
                        db.cektbl() ;
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


    public void cvRestore(View view) {
        startActivity(new Intent(this,Restore_Data_Toko_Kain_.class));
    }

    public void cvBackup(View view) {
        startActivity(new Intent(this, Backup_Data_Toko_Kain.class));
    }
}