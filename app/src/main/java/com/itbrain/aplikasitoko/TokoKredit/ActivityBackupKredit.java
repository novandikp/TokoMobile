package com.itbrain.aplikasitoko.TokoKredit;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class ActivityBackupKredit extends AppCompatActivity {

    View v;
    FConfigKredit config;
    String dirOut, dirIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));

        dirIn = "/data/data/com.itbrain.aplikasitoko/databases/";
        dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";

        if(Build.VERSION.SDK_INT >= 29) {
            this.dirOut = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            FFunctionKredit.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FFunctionKredit.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }

        File file = new File(dirOut);
        if (!file.exists()) {
            file.mkdirs();
        }

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void backup(View v) {
        String dbName = config.getDb();
        String dbOut = config.getDb() + FFunctionKredit.getDate("HH-mm dd-MM-yyyy");

        if (FFunctionKredit.copyFile(dirIn, dirOut, dbName)) {
            if (FFunctionKredit.renameFile(dirOut, dbName, dbOut)) {
//                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Backup DB");
                alert.setMessage("Backup Data tersimpan di folder Download");
                alert.setPositiveButton("ok", (dialog, which) -> {
                    dialog.dismiss();
                });
                alert.show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public void setText() {
        FFunctionKredit.setText(v, R.id.ePath, "Internal Storage/Download/");
    }

//    public void browse(View view) {
//        startActivity(new Intent(this, ActivityBrowse.class));
//    }
}