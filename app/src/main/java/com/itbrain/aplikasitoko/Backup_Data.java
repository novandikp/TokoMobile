package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;

import java.io.File;

public class Backup_Data extends AppCompatActivity {
    ModulBengkel config,temp;
    Database_Bengkel_ db;
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
        setContentView(R.layout.backup_data);

        this.config = new ModulBengkel(getSharedPreferences("config", 0));
        this.temp = new ModulBengkel(getSharedPreferences("temp", 0));
        this.db = new Database_Bengkel_(this);
        this.v = findViewById(android.R.id.content);
        this.type = getIntent().getStringExtra("type");
        if(Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            ModulBengkel.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulBengkel.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        this.dirIn="/data/data/com.komputerkit.aplikasibengkel/databases/";

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
        String dbName = Database_Bengkel_.nama ;
        String dbOut = Database_Bengkel_.nama+ModulBengkel.getDate("HH:mm dd-MM-yyyy") ;

        if(ModulBengkel.copyFile(dirIn,path,dbName)){
            if(ModulBengkel.renameFile(path,dbName,dbOut)){
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}
