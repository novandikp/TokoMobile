package com.itbrain.aplikasitoko.TokoKredit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PosTokoKreditMenuUtama extends AppCompatActivity {


    FKoneksiKredit db;
    FConfigKredit config;
    CekInAppKredit cekInApp;
    View v;
    String posKreditFull = "com.itbrain.aplikasitoko.full";
    //    String posKreditFull = "android.test.purchased";
    String produkid2 = "com.itbrain.aplikasitoko.full";
    String belisku =posKreditFull;
    private List<String> skuList = Arrays.asList(posKreditFull,produkid2);


    TextView tvToko, tvDeskripsi;
    Class activityToIntent;
    String deviceid;
    boolean status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postokokreditmenuutama);

        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        db.cektbl();
        v = this.findViewById(android.R.id.content);

        setText();
        init();
        openDialogPetunjuk();
    }

    @Override
    protected void onResume() {
        super.onResume();
    //    updateIdentitas();
          setText();
          init();
    }

    public void setText() {
        Cursor c = db.sq("SELECT * FROM tblidentitas WHERE id=1");
        c.moveToNext();
        FFunctionKredit.setText(v, R.id.tvToko, FFunctionKredit.getString(c, "nama"));
        FFunctionKredit.setText(v, R.id.tvDeskripsi, FFunctionKredit.getString(c, "alamat"));
        FFunctionKredit.setText(v, R.id.noTelp, FFunctionKredit.getString(c, "telp"));
    }
    
//    void updateIdentitas() {
//        Cursor c = db.sq("SELECT * FROM tblidentitas");
//        c.moveToFirst();
//        String nama = FFunctionKredit.getString(c,"nama");
//
//        tvToko.setText(nama);
//        tvDeskripsi.setText(FFunctionKredit.getString(c,"alamat") + " - " + FFunctionKredit.getString(c,"telp"));
//
//        if (nama.split("\\s+").length > 2)
//            tvToko.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
//        if (nama.split("\\s+").length > 3)
//            tvToko.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
//    }

    void openDialogPetunjuk() {
        DialogPetunjukKredit dialogPetunjuk = new DialogPetunjukKredit();
        dialogPetunjuk.show(getSupportFragmentManager(), "");
    }

    void init() {

        db = new FKoneksiKredit(this, config);
        cekInApp = new CekInAppKredit(this);

        tvToko = findViewById(R.id.tvToko);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
    }

    public void guide(View view) {
        openDialogPetunjuk();
    }
    

    public void master(View view) {
            ModuleKredit.goToActivity(this, PosTokoKreditMenuMaster.class);
        }
//        ModuleKredit.goToActivity(this, ActivityMaster.class);


   public void transaksi(View view) {
            ModuleKredit.goToActivity(this, ActivityTransaksiKredit.class);
        }
//        ModuleKredit.goToActivity(this, ActivityTransaksi.class);


    public void laporan(View view) {
            ModuleKredit.goToActivity(this, ActivityLaporanMenuKredit.class);
        }
//        ModuleKredit.goToActivity(this, ActivityLaporan.class);


    public void utilitas(View view) {
        ModuleKredit.goToActivity(this, ActivityUtilitasKredit.class);
    }

    public void beliPro(View view) {
        Toast.makeText(this, "Silahkan restart aplikasi", Toast.LENGTH_SHORT).show();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }



}