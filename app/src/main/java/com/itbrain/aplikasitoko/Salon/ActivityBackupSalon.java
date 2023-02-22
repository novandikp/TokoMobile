package com.itbrain.aplikasitoko.Salon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.itbrain.aplikasitoko.R;

import java.io.File;

public class ActivityBackupSalon extends AppCompatActivity {

    Toolbar appbar;
    View v;
    ConfigSalon config;
    String dirOut, dirIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_salon);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        FunctionSalon.btnBack("Backup", getSupportActionBar());

        v = this.findViewById(android.R.id.content);
        config = new ConfigSalon(getSharedPreferences("config", 0));

        this.dirIn = "/data/data/com.itbrain.aplikasitoko/databases/";
        if(Build.VERSION.SDK_INT >= 29) {
            this.dirOut = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            FunctionSalon.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            FunctionSalon.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        File file = new File(this.dirOut);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void backup(View v) {
        String dbName = DatabaseSalon.nama_database;
        String dbOut = dbName + FunctionSalon.getDate("HH-mm dd-MM-yyyy");
        if (!FunctionSalon.copyFile(this.dirIn, this.dirOut, dbName).booleanValue()) {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        } else if (FunctionSalon.renameFile(this.dirOut, dbName, dbOut).booleanValue()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage((CharSequence) "Backup Data tersimpan di folder Download");
            alert.setPositiveButton((CharSequence) "ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        } else {
            Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText() {
        FunctionSalon.setText(this.v, R.id.ePath, "Internal Storage/Download/");

    }
}
