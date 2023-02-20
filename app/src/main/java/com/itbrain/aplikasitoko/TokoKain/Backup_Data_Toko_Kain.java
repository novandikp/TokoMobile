package com.itbrain.aplikasitoko.TokoKain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class Backup_Data_Toko_Kain extends AppCompatActivity{


    String path,dirIn;
    SharedPreferences getPrefs;
    SharedPreferences.Editor editPref;
    String type;
    DatabaseTokoKain db;
    View v;
    final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_kain);

        ImageButton imageButton = findViewById(R.id.kembalicoy4);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            KumFunTokoKain.btnBack("Backup",getSupportActionBar());
            db=new DatabaseTokoKain(this);
            v=this.findViewById(android.R.id.content);
            getPrefs=getSharedPreferences("dir",MODE_PRIVATE);
            this.type = getIntent().getStringExtra("type");
            this.path = getPrefs.getString("dirBackup", getExternalFilesDir(null).getAbsolutePath()+"/"+ "/Backup/");
            this.dirIn="/data/data/com.itbrain.aplikasitoko/databases/";
            editPref=getPrefs.edit();

            File file = new File(path) ;
            if(!file.exists()){
                file.mkdirs() ;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.path = getPrefs.getString("dirBackup",getExternalFilesDir(null).getAbsolutePath()+"/" + "/Backup/");
        KumFunTokoKain.setText(v,R.id.viewPathBackup,path);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void browse(View view) {
//        showListItemDialog("Pilih Direktori", getExternalFilesDir(null).getAbsolutePath()+"/", FOLDER_ONLY_DIRECT_CHOICE_SELECTION,"PICK_FOLDER");
    }

    public void backup(View view) {
        String dbName = DatabaseTokoKain.NAMA_DATABASE ;
        String dbOut = DatabaseTokoKain.NAMA_DATABASE+KumFunTokoKain.getDate("HH.mm dd-MM-yyyy") ;

        if(KumFunTokoKain.copyFile(dirIn,path,dbName)){
            if(KumFunTokoKain.renameFile(path,dbName,dbOut)){
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    }

