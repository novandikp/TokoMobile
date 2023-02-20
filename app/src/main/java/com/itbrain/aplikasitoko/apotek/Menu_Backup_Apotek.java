package com.itbrain.aplikasitoko.apotek;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class Menu_Backup_Apotek extends AppCompatActivity {
    ModulApotek config,temp;
    DatabaseApotek db;
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
        setContentView(R.layout.menu_backup_apotek);
        this.config = new ModulApotek(getSharedPreferences("config", 0));
        this.temp = new ModulApotek(getSharedPreferences("temp", 0));
        this.db = new DatabaseApotek(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            ModulApotek.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulApotek.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        this.dirIn="/data/data/com.itbrain.aplikasitoko/databases/";

        this.getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        File file = new File(path) ;
        if(!file.exists()){
            file.mkdirs() ;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void backup(View view) {
        String dbName = DatabaseApotek.nama ;
        String dbOut = DatabaseApotek.nama+ ModulApotek.getDate("HH:mm dd-MM-yyyy") ;

        if(ModulApotek.copyFile(dirIn,path,dbName)){
            if(ModulApotek.renameFile(path,dbName,dbOut)){
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}
