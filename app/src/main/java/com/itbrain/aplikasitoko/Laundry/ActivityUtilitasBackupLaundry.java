package com.itbrain.aplikasitoko.Laundry;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class ActivityUtilitasBackupLaundry extends AppCompatActivity {
    String path, dirIn;
    SharedPreferences getPrefs;
    SharedPreferences.Editor editPref;
    String type;
    DatabaseLaundry db;
    View v;
    final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_laundry);
        db = new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        getPrefs = getSharedPreferences("dir", MODE_PRIVATE);
        ImageButton i = findViewById(R.id.kembali20);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.type = getIntent().getStringExtra("type");
        this.path = getPrefs.getString("dirBackup", Environment.getExternalStorageDirectory().toString() + "/KomputerKit/Laundry & Keuangan/");
        this.dirIn = "/data/data/com.itbrain.aplikasitoko/databases//";
        editPref = getPrefs.edit();
        if (Build.VERSION.SDK_INT >= 29) {
            this.path = this.getExternalFilesDir("backup").toString() + "/";
            String codename = this.getPackageName();
            ModulLaundry.setText(v, R.id.viewPathBackup, "Internal Storage/Android/data/" + codename + "/files/backup/");
            //only api 21 above
        } else {
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulLaundry.setText(v, R.id.viewPathBackup, "Internal Storage/Download");
            //only api 21 down
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backup(View view) {
        String dbName = PrefLaundry.db;
        String dbOut = PrefLaundry.db + ModulLaundry.getDate("HH.mm dd-MM-yyyy");

        if (ModulLaundry.copyFile(dirIn, path, dbName)) {
            if (ModulLaundry.renameFile(path, dbName, dbOut)) {
                Toast.makeText(this, "Backup Data Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }
}