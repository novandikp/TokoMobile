package com.itbrain.aplikasitoko.tabungan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;
//import com.itbrain.aplikasitabunganplus.Kunci.Aktivasi;
//import com.itbrain.aplikasitoko.Kunci.LisensiBaru;
//import com.itbrain.aplikasitoko.Purchase.IAPHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MenuUtilitasTabungan<SkuDetails> extends AppCompatActivity {
    View v ;
    DatabaseTabungan db;
    ImageView ivStatus;
    TextView tvStatus;
    PrefTabungan config;
    String fitur="";
    boolean status;
    String belisku =PrefTabungan.productID;
    private List<String> skuList = Arrays.asList(PrefTabungan.productID,PrefTabungan.produkid2);
    HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();


    boolean premium=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utilitas_tabungan);
        v=this.findViewById(android.R.id.content);
        ModulTabungan.btnBack(R.string.title_utilitas,getSupportActionBar());

        ImageButton imageButton = findViewById(R.id.kembaliUtilitasTabungan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        PackageInfo pinfo;
        String versionName="";
        try {
            pinfo=getPackageManager().getPackageInfo(getPackageName(),0);
            versionName=pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ModulTabungan.setText(v, R.id.tvVersionName,"V "+versionName);
        config=new PrefTabungan(getSharedPreferences("id",MODE_PRIVATE));
        db= new DatabaseTabungan(this);
        ivStatus=(ImageView)findViewById(R.id.iv1);
        tvStatus=(TextView)findViewById(R.id.tv1);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void DialogBeli() {
    //    startActivity(new Intent(this, Aktivasi.class));
    }




    private void billingPurchase(){
        DialogBeli();
    }

    public void beli(View view) {
        if (status){

        }else{

            billingPurchase();
        }
    }

    public void petunjuk(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")));
    }

//    public void backup(View view) {
//        if (status){
//            startActivity(new Intent(this,MenuUtilitasBackupTabungan.class));
//        }else{
//            fitur="backup";
//            billingPurchase();
//        }
//
//    }

    public void backup (View view) {
        Intent intent = new Intent(MenuUtilitasTabungan.this, MenuUtilitasBackupTabungan.class);
        startActivity(intent);
    }

    public void restore(View view) {

//        if (status){
            startActivity(new Intent(this,MenuUtilitasRestoreTabungan.class));
//        }else{
//            fitur="restore";
//            billingPurchase();
//        }

    }

    public void reset(View view) {
        reset3();
//        if (premium){
//
//        }else {
//            billingPurchase();
//            Toast.makeText(this, R.string.fiturpremium, Toast.LENGTH_SHORT).show();
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
                        if (ModulTabungan.deleteFile("data/data/com.itbrain.aplikasitoko/databases/"+PrefTabungan.db)){
                            db.cektbl() ;
                            Intent i=new Intent(MenuUtilitasTabungan.this,MainActivityTabungan.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
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
