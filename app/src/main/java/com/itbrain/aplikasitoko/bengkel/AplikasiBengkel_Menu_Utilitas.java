package com.itbrain.aplikasitoko.bengkel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.Backup_Data;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Restore_Data;

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
            Intent i = new Intent(AplikasiBengkel_Menu_Utilitas.this, Backup_Data_bengkel_.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

    }

    public void Restore(View view) {
        Intent intent = new Intent(AplikasiBengkel_Menu_Utilitas.this, Restore_Data_Bengkel.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void DialogForm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_video, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        TextView iya = dialogView.findViewById(R.id.iya);
        TextView tidak = dialogView.findViewById(R.id.tidak);

        final AlertDialog dialogi = dialog.create();


        iya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe"));
                startActivity(i);
                dialogi.cancel();
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogi.cancel();
            }
        });

        dialogi.show();
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

    public void CaraPenggunaan(View view) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLfTB96jbjODxxZ-cyh1YHeUxabnpZ_aHe")));
    }
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