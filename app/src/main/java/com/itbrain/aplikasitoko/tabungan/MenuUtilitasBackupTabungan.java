package com.itbrain.aplikasitoko.tabungan;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.github.isabsent.filepicker.SimpleFilePickerDialog;
import com.itbrain.aplikasitoko.R;

import java.io.File;



public class MenuUtilitasBackupTabungan extends AppCompatActivity /* implements SimpleFilePickerDialog.InteractionListenerString */{
    String path, dirIn;
    SharedPreferences getPrefs;
    SharedPreferences.Editor editPref;
    String type;
    DatabaseTabungan db;
    View v;
    final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utilitas_backup_tabungan);
//        ModulTabungan.btnBack("Backup", getSupportActionBar());
        db = new DatabaseTabungan(this);
        v = this.findViewById(android.R.id.content);
        getPrefs = getSharedPreferences("dir", MODE_PRIVATE);
        this.type = getIntent().getStringExtra("type");

        ImageButton imageButton = findViewById(R.id.kembaliBackupTabungan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= 29) {
//            this.path = this.getExternalFilesDir("backup").toString()+"/";
//            String codename= this.getPackageName();
//            Modul.setText(v,R.id.viewPathBackup,"Internal Storage/Android/data/"+codename+"/files/backup/");
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulTabungan.setText(v, R.id.viewPathBackup, "Internal Storage/Download");
            //only api 21 above
        } else {
            this.path = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ModulTabungan.setText(v, R.id.viewPathBackup, "Internal Storage/Download");
            //only api 21 down
        }
        this.dirIn = "/data/data/com.itbrain.aplikasitoko/databases/";
        editPref = getPrefs.edit();

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
        String dbName = PrefTabungan.db;
        String dbOut = PrefTabungan.db + ModulTabungan.getDate("HH.mm dd-MM-yyyy");

        if (ModulTabungan.copyFile(dirIn, path, dbName)) {
            if (ModulTabungan.renameFile(path, dbName, dbOut)) {
                Toast.makeText(this, R.string.successBackup, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.failedBackup, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.failedBackup, Toast.LENGTH_SHORT).show();
        }
    }

    public void browse(View view) {

    }

//    @Override
//    public void showListItemDialog(String title, String folderPath, SimpleFilePickerDialog.CompositeMode mode, String dialogTag) {
//        SimpleFilePickerDialog.build(folderPath,mode)
//                .title(title)
//                .neut(getResources().getString(R.string.batal))
//                .neg(getResources().getString(R.string.buka))
//                .pos(getResources().getString(R.string.pilihfolder))
//                .show(this,dialogTag);
//    }
//
//    @Override
//    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
//        switch (dialogTag){
//            case "PICK_FOLDER":
//                if (extras.containsKey(SimpleFilePickerDialog.SELECTED_SINGLE_PATH)){
//                    String selectedSinglePath = extras.getString(SimpleFilePickerDialog.SELECTED_SINGLE_PATH);
//                    if(!selectedSinglePath.isEmpty()){
//                        editPref.putString("dirBackup",selectedSinglePath+"/");
//                        editPref.apply();
//                        this.path = getPrefs.getString("dirBackup",Environment.getExternalStorageDirectory().toString() + "/KomputerKit/Aplikasi Tabungan Plus Keuangan/");
//                        ModulTabungan.setText(v,R.id.viewPathBackup,path);
//                    }
//                }
//                break;
//        }
//        return false;
//    }
}


