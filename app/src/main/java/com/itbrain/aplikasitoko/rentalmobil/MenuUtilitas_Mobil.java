package com.itbrain.aplikasitoko.rentalmobil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.itbrain.aplikasitoko.R;

import java.util.Arrays;
import java.util.List;

public class MenuUtilitas_Mobil extends AppCompatActivity {


    ModulRentalMobil config;
    String produkid = "rentalmobilpro";
    //String produkid="android.test.purchased";
    String produkid2 = "com.itbrain.aplikasitoko.rentalmobil";
    String belisku = produkid;
    private List<String> skuList = Arrays.asList(produkid, produkid2);
    boolean stat;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuutilitas_mobil);

        v = this.findViewById(android.R.id.content);
        config = new ModulRentalMobil(getSharedPreferences("config", this.MODE_PRIVATE));

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void versi(View view) {
        {
            ModulRentalMobil.showToast(this, "Terima Kasih");
        }

    }

    public void bekap(View view) {
        {
            Intent i = new Intent(MenuUtilitas_Mobil.this, MenuBackup_Mobil.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }


    }

    public void restor(View view) {
         {
            Intent i = new Intent(MenuUtilitas_Mobil.this, MenuRestore_Mobil.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }


    public void resetdata(View view) {
        final String dirIn = "/data/data/com.itbrain.aplikasitoko/databases/";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hilangkan segala data dan aplikasi akan restart");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertDialog.Builder ale = new AlertDialog.Builder(MenuUtilitas_Mobil.this);
                        ale.setMessage("Apakah anda yakin untuk mereset data ?");
                        ale.setPositiveButton("Iya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (ModulRentalMobil.deleteFile(dirIn + DatabaseRentalMobil.nama)) {
                                            ModulRentalMobil.showToast(MenuUtilitas_Mobil.this, "Reset Berhasil, Aplikasi terestart");
                                            finishAffinity();
                                            System.exit(0);

                                        } else {
                                            ModulRentalMobil.showToast(MenuUtilitas_Mobil.this, "Reset Gagal");
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

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe"));
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