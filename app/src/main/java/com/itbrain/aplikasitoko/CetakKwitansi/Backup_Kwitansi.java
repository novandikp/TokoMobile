package com.itbrain.aplikasitoko.CetakKwitansi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Backup_Kwitansi extends AppCompatActivity {

    String dirOut, dirIn;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_kwitansi);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        v = this.findViewById(android.R.id.content);
        this.dirIn = "data/data/com.itbrain.aplikasitoko/databases/";
        this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
        if(Build.VERSION.SDK_INT >= 29) {
            this.dirOut = this.getExternalFilesDir("backup").toString()+"/";
            String codename= this.getPackageName();
            ConfigKwitansi.setText(v,R.id.ePath,"Internal Storage/Android/data/"+codename+"/files/backup/");
            //only api 21 above
        }else{
            this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
            ConfigKwitansi.setText(v,R.id.ePath,"Internal Storage/Download");
            //only api 21 down
        }
        File file = new File(this.dirOut);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backup(View v){
        String dbName = DatabaseCetakKwitansi.nama_database;
        String dbOut = dbName + getDate("HH-mm dd-MM-yyyy");
        if (!copyFile(this.dirIn, this.dirOut, dbName).booleanValue()) {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        } else if (renameFile(this.dirOut, dbName, dbOut).booleanValue()) {
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

    public void setText(){
        EditText path = (EditText) findViewById(R.id.ePath);
        path.setText("Internal Storage/Download/");
    }

    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }

    public static Boolean copyFile(String pIn, String pOut, String name){
        try{
            File file = new File(pOut);
            if (!file.exists()) {
                file.mkdirs();
            }
            InputStream in = new FileInputStream(pIn + name);
            OutputStream out = new FileOutputStream(pOut + name);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer);
                if (read != -1) {
                    out.write(buffer, 0, read);
                } else {
                    in.close();
                    out.flush();
                    out.close();
                    return Boolean.valueOf(true);
                }
            }
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }

    }

    public static Boolean renameFile(String path, String namaLama, String namaBaru){
        try {
            new File(path + namaLama).renameTo(new File(path + namaBaru));
            return Boolean.valueOf(true);
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }

    }
}
