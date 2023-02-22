package com.itbrain.aplikasitoko.klinik;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;



import com.itbrain.aplikasitoko.MainActivity;
import com.itbrain.aplikasitoko.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AplikasiKlinik_Menu_Utilitas extends AppCompatActivity {
    ModulKlinik config;
    String produkid = "klinikdokterpro";
    //    String produkid ="android.test.purchased";
    String produkid2 = "com.itbrain.aplikasitoko.full";
    String belisku =produkid;
    private List<String> skuList = Arrays.asList(produkid,produkid2);

    boolean stat;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_klinik_menu_utilitas);
        v=this.findViewById(android.R.id.content);
        config = new ModulKlinik(getSharedPreferences("config",this.MODE_PRIVATE));
        setText();

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void backup(View view){

            Intent i = new Intent(AplikasiKlinik_Menu_Utilitas.this,Backup_Data_Klinik_.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);



    }

    public void restore(View view){
            Intent i = new Intent(AplikasiKlinik_Menu_Utilitas.this, Restore_Data_Klinik_.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);



    }

    private void setText(){
        if(true){
           ModulKlinik.setText(v,R.id.textView8,"Terima kasih");
           ModulKlinik.setText(v,R.id.textView9,"telah membeli aplikasi kami");
        }else{
           ModulKlinik.setText(v,R.id.textView8,"Tingkatkan Versi");
           ModulKlinik.setText(v,R.id.textView9,"Tingkatkan Versi untuk fasilitas lengkap");
        }

    }

    public void reset(View view){
        final String  dirIn="/data/data/com.itbrain.aplikasitoko/databases/";;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hilangkan segala data dan aplikasi akan restart");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertDialog.Builder ale = new AlertDialog.Builder(AplikasiKlinik_Menu_Utilitas.this);
                        ale.setMessage("Apakah anda yakin untuk mereset data ?");
                        ale.setPositiveButton("Iya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (ModulKlinik.deleteFile(dirIn + DatabaseKlinik.nama)) {
                                           ModulKlinik.showToast(AplikasiKlinik_Menu_Utilitas.this, "Reset Berhasil, Aplikasi terestart");
                                            finishAffinity();
                                            System.exit(0);

                                        } else {
                                           ModulKlinik.showToast(AplikasiKlinik_Menu_Utilitas.this, "Reset Gagal");
                                        }
                                    }
                                });

                        ale.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = ale.create();
                        alertDialog.show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Reset Data");
        alertDialog.show();
    }

    public void petunjuk(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

}