package com.itbrain.aplikasitoko.tokosepatu;

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
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.tokosepatu.DatabaseTokoSepatu;
import com.itbrain.aplikasitoko.tokosepatu.ModulTokoSepatu;

import java.io.File;

public class Menu_Backup_Tokosepatu extends AppCompatActivity {

    String path,dirIn;
    SharedPreferences getPrefs;
    SharedPreferences.Editor editPref;
    String type;
    DatabaseTokoSepatu db;
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
            db=new DatabaseTokoSepatu(this);
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
        ModulTokoSepatu.setText(v,R.id.viewPathBackup,path);
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
        String dbName = DatabaseTokoSepatu.nama ;
        String dbOut = DatabaseTokoSepatu.nama+ModulTokoSepatu.getDate("HH.mm dd-MM-yyyy") ;

        if(ModulTokoSepatu.copyFile(dirIn,path,dbName)){
            if(ModulTokoSepatu.renameFile(path,dbName,dbOut)){
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

}

