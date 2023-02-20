package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;

import java.io.File;
import java.util.Calendar;

public class MenuExportExcel extends AppCompatActivity {

    String Opsi;

    ModulBengkel config,temp;
    Database_Bengkel_ db;
    String deviceid;
    SharedPreferences getPrefs;
    String[] header;
    String idAkun;
    private String inputFile;
    String nama;
    Boolean needDate = Boolean.valueOf(true);
    String path;

    String rincian;
    int row = 0;

    String tglAkhir;
    String tglAwal;

    Calendar calendar ;
    int year,month, day ;
    String ttglAwal,ttglAkhir;

    String type;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_export_excel_kasir);

        getWindow().setSoftInputMode(3);
        this.config = new ModulBengkel(getSharedPreferences("config", 0));
        this.temp = new ModulBengkel(getSharedPreferences("temp", 0));
        this.db = new Database_Bengkel_(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";

        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        try {
            new File(Environment.getExternalStorageDirectory() + "/kasirsupermudah").mkdirs();
        } catch (Exception e) {
        }

        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("Laporan").toString()+"/";
            String codename= this.getPackageName();
            ModulBengkel.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/Laporan/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulBengkel.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
//        setTanggal();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        ModulBengkel.setText(v,R.id.ePath,"Internal Storage/Download/");
    }

    public void export(View view) {
    }

    public void tglAkhir(View view) {
    }

    public void tglAwal(View view) {
    }
}