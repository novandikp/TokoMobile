package com.itbrain.aplikasitoko.tokosepatu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Aplikas_Menu_Utilitasi_Toko_Sepatu extends AppCompatActivity {

    View v;
    ModulTokoSepatu config,temp ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utilitas_sepatu);


        config = new ModulTokoSepatu(getSharedPreferences("config", this.MODE_PRIVATE));
        v = this.findViewById(android.R.id.content);
        String profile = config.getCustom("profil","");
        if (Splash_Activity_Sepatu.status){
            setText();
        }
    }

    public void setText(){
        ModulTokoSepatu.setText(v,R.id.textView8,"Terima Kasih");
        ModulTokoSepatu.setText(v,R.id.textView9,"Telah Membeli Aplikasi Kami");
    }





    public void backup(View view) {

        String profile = config.getCustom("profil","");

            Intent i = new Intent(Aplikas_Menu_Utilitasi_Toko_Sepatu.this,Menu_Backup_Tokosepatu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);



        }



    public void restore(View view) {

        String profile = config.getCustom("profil","");

            Intent i = new Intent(Aplikas_Menu_Utilitasi_Toko_Sepatu.this,Menu_Restrore_Toko_Sepatu.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);



    }

    @Override
    protected void onResume() {
        super.onResume();
        String profile = config.getCustom("profil","");

            setText();

    }

    public void reset(View view) {

        String profile = config.getCustom("profil","");

            reset();


    }

    public void reset(){
        final String  dirIn = "data/data/com.itbrain.aplikasitoko/databases/";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hilangkan segala data dan aplikasi akan restart");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertDialog.Builder ale = new AlertDialog.Builder(Aplikas_Menu_Utilitasi_Toko_Sepatu.this);
                        ale.setMessage("Apakah anda yakin untuk mereset data ?");
                        ale.setPositiveButton("Iya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (ModulTokoSepatu.deleteFile(dirIn + DatabaseTokoSepatu.nama)) {
                                            ModulTokoSepatu.showToast(Aplikas_Menu_Utilitasi_Toko_Sepatu.this, "Reset Berhasil, Aplikasi terestart");
//                            finishAffinity();
//a
//                            System.exit(0);
                                            Intent i = new Intent(Aplikas_Menu_Utilitasi_Toko_Sepatu.this, Splash_Activity_Sepatu.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);

                                        } else {
                                            ModulTokoSepatu.showToast(Aplikas_Menu_Utilitasi_Toko_Sepatu.this, "Reset Gagal");
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


    public void petunjuk(View view) {
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
