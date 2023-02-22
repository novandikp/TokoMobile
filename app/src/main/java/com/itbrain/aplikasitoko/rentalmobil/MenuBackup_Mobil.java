package com.itbrain.aplikasitoko.rentalmobil;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class MenuBackup_Mobil extends AppCompatActivity {
    ModulRentalMobil config,temp;
    DatabaseRentalMobil db;
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
        setContentView(R.layout.backup_data_mobil);

        this.config = new ModulRentalMobil(getSharedPreferences("config", 0));
        this.temp = new ModulRentalMobil(getSharedPreferences("temp", 0));
        this.db = new DatabaseRentalMobil(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            ModulRentalMobil.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulRentalMobil.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        this.dirIn="/data/data/com.itbrain.aplikasitoko/databases/";

        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        File file = new File(path) ;
        if(!file.exists()){
            file.mkdirs() ;
        }

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
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
        String dbName = DatabaseRentalMobil.nama ;
        String dbOut = DatabaseRentalMobil.nama+ModulRentalMobil.getDate("HH:mm dd-MM-yyyy") ;

        if(ModulRentalMobil.copyFile(dirIn,path,dbName)){
            if(ModulRentalMobil.renameFile(path,dbName,dbOut)){
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}
