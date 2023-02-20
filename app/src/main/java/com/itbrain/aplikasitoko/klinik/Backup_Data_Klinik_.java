package com.itbrain.aplikasitoko.klinik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class Backup_Data_Klinik_ extends AppCompatActivity {
    ModulKlinik config,temp;
    DatabaseKlinik db;
    String deviceid;
    SharedPreferences getPrefs;
    String[] header;
    String idAkun;
    private String inputFile;
    String nama;
    Boolean needDate = Boolean.valueOf(true);
    String path,dirIn;
    String rincian;
    int row = 0;

    String type;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_data_klinik_);

        this.config = new ModulKlinik(getSharedPreferences("config", 0));
        this.temp = new ModulKlinik(getSharedPreferences("temp", 0));
        this.db = new DatabaseKlinik(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            ModulKlinik.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulKlinik.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        this.dirIn="/data/data/com.itbrain.aplikasitoko/databases/";

        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        File file = new File(path) ;
        if(!file.exists()){
            file.mkdirs() ;
        }

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void backup(View view) {
        String dbName = DatabaseKlinik.nama ;
        String dbOut = DatabaseKlinik.nama+ModulKlinik.getDate("HH:mm dd-MM-yyyy") ;

        if(ModulKlinik.copyFile(dirIn,path,dbName)){
            if(ModulKlinik.renameFile(path,dbName,dbOut)){
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}

