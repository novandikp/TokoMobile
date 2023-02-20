package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class Backup_Data_Kasir_ extends AppCompatActivity {

    View v ;
    FConfigKasir config ;
    String dirOut, dirIn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_data_kasir_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config",this.MODE_PRIVATE)) ;

        dirIn = "/data/data/com.itbrain.aplikasitoko/databases/" ;

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(Build.VERSION.SDK_INT >= 29) {
            this.dirOut = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            FFunctionKasir.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FFunctionKasir.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }

        File file = new File(dirOut) ;
        if(!file.exists()){
            file.mkdirs() ;
        }
    }

    public void backupKasir(View v){
        String dbName = config.getDb() ;
        String dbOut = config.getDb()+FFunctionKasir.getDate("HH-mm dd-MM-yyyy") ;

        if(FFunctionKasir.copyFile(dirIn,dirOut,dbName)){
            if(FFunctionKasir.renameFile(dirOut,dbName,dbOut)){
//                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Backup DB");
                alert.setMessage("Backup Data tersimpan di folder Download");
                alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });
                alert.show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public void setText(){
        FFunctionKasir.setText(v,R.id.ePath,"Internal Storage/Download/");
    }

    public void browse(View view){
       // startActivity(new Intent(this, ActivityBrowse.class));
    }
}