package com.itbrain.aplikasitoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;

public class AplikasiBengkel_Menu_Utilitas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_bengkel_menu_utilitas);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void Backup(View view) {
//        Intent intent = new Intent(AplikasiBengkel_Menu_Utilitas.this, Backup_Data.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
            Intent i = new Intent(AplikasiBengkel_Menu_Utilitas.this,Backup_Data.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

    }

    public void Restore(View view) {
        Intent intent = new Intent(AplikasiBengkel_Menu_Utilitas.this, Restore_Data.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void CaraPenggunaan(View view) {
    }

    public void Reset(View view) {
        final String  dirIn="/data/data/com.itbrain.aplikasitoko/databases/";;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hilangkan segala data dan aplikasi akan restart");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertDialog.Builder ale = new AlertDialog.Builder(AplikasiBengkel_Menu_Utilitas.this);
                        ale.setMessage("Apakah anda yakin untuk mereset data ?");
                        ale.setPositiveButton("Iya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        if (ModulBengkel.deleteFile(dirIn + Database_Bengkel_.nama)) {
                                            ModulBengkel.showToast(AplikasiBengkel_Menu_Utilitas.this, "Reset Berhasil, Aplikasi terestart");
                                            finishAffinity();
                                            System.exit(0);

                                        } else {
                                            ModulBengkel.showToast(AplikasiBengkel_Menu_Utilitas.this, "Reset Gagal");
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

//    public void petunjuk(View view){
//        DialogForm();
//    }
//
//    @Override
//    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        MenuUtama.status=true;
//        status=true;
//        setText();
//        ModulBengkel.showToast(this,"Pembelian Berhasil");
//        config.setCustom(ModulBengkel.getEncrypt("iddevice"),ModulBengkel.getEncrypt(Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID))+"posbengkel");
//
//    }
}