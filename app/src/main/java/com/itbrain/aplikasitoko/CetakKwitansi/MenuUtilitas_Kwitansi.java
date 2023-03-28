package com.itbrain.aplikasitoko.CetakKwitansi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itbrain.aplikasitoko.Main_App;
import com.itbrain.aplikasitoko.R;

import java.io.File;

public class MenuUtilitas_Kwitansi extends AppCompatActivity {

    DatabaseCetakKwitansi db;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuutilitas_kwitansi);

        ImageButton imageButton = findViewById(R.id.imageButton31);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backup(View view) {
        Intent intent=new Intent(this, Backup_Kwitansi.class);
        startActivity(intent);
        }

    public void restore(View view) {
        Intent intent=new Intent(this, Restore_Kwitansi.class);
        startActivity(intent);
    }

    public void reset(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Reset akan menghapus semua data dalam Aplikasi ini");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteFile("data/data/com.itbrain.aplikasitoko/databases/" + "db_receipt") ;
                        db.cektbl() ;
                        Intent i=new Intent(MenuUtilitas_Kwitansi.this, Main_App.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(MenuUtilitas_Kwitansi.this, "Reset Data Berhasil, Aplikasi Terestart", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void penggunaan(View view) {
        AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda ingin melihat video petunjuk? ");
        alertDialogBuilder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")) ;
                startActivity(i);
            }
        });

        alertDialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean deleteFile(String path){
        try {
            new File(path).delete();
            return Boolean.valueOf(true);
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }
    }
    }