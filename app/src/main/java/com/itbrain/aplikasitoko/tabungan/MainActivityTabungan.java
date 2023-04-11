package com.itbrain.aplikasitoko.tabungan;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itbrain.aplikasitoko.R;
//import com.itbrain.aplikasitoko.Kunci.LisensiBaru;

import java.util.Calendar;

public class MainActivityTabungan extends AppCompatActivity{
    static final Integer WRITE_EXST = 0x3 ;
    DatabaseTabungan db;
    View v;
    boolean premium;
    CardView cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabungan);
//        getSupportActionBar().hide();
        cv = findViewById(R.id.cardView);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
        v=this.findViewById(android.R.id.content);
        db=new DatabaseTabungan(this);

        final SharedPreferences sp = getSharedPreferences("MyPrefs", 0);
        if (!premium) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setView(LayoutInflater.from(this).inflate(R.layout.dialog_youtube_tabungan,null));
            builder.setNegativeButton("Nanti",null);
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String url = "https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            sp.edit().putBoolean("first", false).apply();
            builder.show();
        }

        Calendar dateNow=Calendar.getInstance();
        Cursor c=db.sq(QueryTabungan.select("qsimpanan"));
        while (c.moveToNext()){
            Cursor cSaldo=db.sq(QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sWhere("idsimpanan",ModulTabungan.getString(c,"idsimpanan"))+QueryTabungan.sOrderASC("notransaksi"));
            cSaldo.moveToLast();
            Cursor cBunga=db.sq(QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sWhere("idsimpanan",ModulTabungan.getString(c,"idsimpanan"))+" AND "+QueryTabungan.sWhere("kode","B")+QueryTabungan.sOrderASC("notransaksi"));
            if (cBunga.getCount()>0){
                cBunga.moveToLast();
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(ModulTabungan.dateToDate(ModulTabungan.getString(cBunga,"tgltransaksi")));
                Integer diff=ModulTabungan.getDifferenceDays(calendar.getTime(),dateNow.getTime()).intValue();
                Double saldo=ModulTabungan.getDouble(cSaldo,"saldo");
                int notransaksi=ModulTabungan.getInt(cBunga,"notransaksi");
                if (diff>29){
                    Double times=Math.floor(diff.doubleValue()/30);
                    for (int i=times.intValue();i>0;i--){
                        calendar.add(Calendar.DAY_OF_MONTH,30);
                        Double bungaPerBln=((ModulTabungan.getDouble(c,"bunga")/100)*saldo);
                        saldo=saldo+bungaPerBln;
                        notransaksi++;
                        String[] qBaru={
                                ModulTabungan.getString(cBunga,"idsimpanan"),
                                String.valueOf(notransaksi),
                                ModulTabungan.getDate2(calendar,"yyyyMMdd"),
                                String.valueOf(bungaPerBln),
                                "0",
                                String.valueOf(saldo),
                                "B"
                        };
                        db.exc(QueryTabungan.splitParam("INSERT INTO tbltransaksi (idsimpanan,notransaksi,tgltransaksi,masuk,keluar,saldo,kode) " +
                                "VALUES (?,?,?,?,?,?,?)",qBaru));
                    }
                }
            }else {
                Cursor cTabungan=db.sq(QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sWhere("idsimpanan",ModulTabungan.getString(c,"idsimpanan"))+" AND "+QueryTabungan.sWhere("kode","T")+QueryTabungan.sOrderASC("notransaksi"));
                if (cTabungan.getCount()>0){
                    cTabungan.moveToLast();
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(ModulTabungan.dateToDate(ModulTabungan.getString(cTabungan,"tgltransaksi")));
                    Integer diff=ModulTabungan.getDifferenceDays(calendar.getTime(),dateNow.getTime()).intValue();
                    Double saldo=ModulTabungan.getDouble(cSaldo,"saldo");
                    int notransaksi=ModulTabungan.getInt(cTabungan,"notransaksi");
                    if (diff>29){
                        Double times=Math.floor(diff.doubleValue()/30);
                        for (int i=times.intValue();i>0;i--){
                            calendar.add(Calendar.DAY_OF_MONTH,30);
                            Double bungaPerBln=((ModulTabungan.getDouble(c,"bunga")/100)*saldo);
                            saldo=saldo+bungaPerBln;
                            notransaksi++;
                            String[] qBaru={
                                    ModulTabungan.getString(cTabungan,"idsimpanan"),
                                    String.valueOf(notransaksi),
                                    ModulTabungan.getDate2(calendar,"yyyyMMdd"),
                                    String.valueOf(bungaPerBln),
                                    "0",
                                    String.valueOf(saldo),
                                    "B"
                            };
                            db.exc(QueryTabungan.splitParam("INSERT INTO tbltransaksi (idsimpanan,notransaksi,tgltransaksi,masuk,keluar,saldo,kode) " +
                                    "VALUES (?,?,?,?,?,?,?)",qBaru));
                        }
                    }
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c=db.sq(QueryTabungan.selectwhere("tblidentitas")+QueryTabungan.sWhere("id","1"));
        c.moveToFirst();
        ModulTabungan.setText(v,R.id.tvNamaToko,ModulTabungan.upperCaseFirst(ModulTabungan.getString(c,"namatoko")));
        ModulTabungan.setText(v,R.id.tvAlamatToko,ModulTabungan.getString(c,"alamat"));

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
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivityTabungan.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityTabungan.this, permission)) {
                ActivityCompat.requestPermissions(MainActivityTabungan.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(MainActivityTabungan.this, new String[]{permission}, requestCode);
            }
        }
    }

    public void menuMaster(View view) {
        startActivity(new Intent(this,MenuMasterTabungan.class));
    }

    public void menuTransaksi(View view) {
        startActivity(new Intent(this,MenuTransaksiTabungan.class));
    }

    public void menuLaporan(View view) {
        startActivity(new Intent(this,MenuLaporanTabungan.class));
    }

    public void menuUtilitas(View view) {
        startActivity(new Intent(this,MenuUtilitasTabungan.class));
    }

//    @Override
//    public void onProductPurchased(String productId, TransactionDetails details) {
//
//    }
//
//    @Override
//    public void onPurchaseHistoryRestored() {
//
//    }
//
//    @Override
//    public void onBillingError(int errorCode, Throwable error) {
//
//    }
//
//    @Override
//    public void onBillingInitialized() {
//
//    }
//
    public void beli(View view) {
        startActivity(new Intent(this,MenuUtilitasTabungan.class));
    }
}
