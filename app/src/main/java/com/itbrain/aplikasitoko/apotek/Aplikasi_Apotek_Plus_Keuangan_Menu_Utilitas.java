package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.Arrays;
import java.util.List;

public class Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas extends AppCompatActivity {
    ModulApotek config;
    String produkid = "com.komputerkit.aplikasiapotekpluskeuangan.full";
    String produkid2 = "com.komputerkit.aplikasiapotekpluskeuangannew.full";
    String belisku = produkid;
    private List<String> skuList = Arrays.asList(produkid, produkid2);
    boolean stat;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikasi_apotek_plus_keuangan_menu_utilitas);
        v = this.findViewById(android.R.id.content);
        config = new ModulApotek(getSharedPreferences("config", this.MODE_PRIVATE));
        setText();
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void DialogForm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_video, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        TextView iya = dialogView.findViewById(R.id.iya);
        TextView tidak = dialogView.findViewById(R.id.tidak);

        final AlertDialog dialogi = dialog.create();


        iya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/x8zFL-0rBAw"));
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




        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            if (item.getItemId() == android.R.id.home) {
                finish();
            }
            return super.onOptionsItemSelected(item);
        }


        public void backup (View view){
                Intent i = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas.this, Menu_Backup_Apotek.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


        }

        public void restore (View view){
                Intent i = new Intent(Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas.this, Menu_Restore_Apotek.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
        }

        private void setText () {
            if (Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.status) {
                ModulApotek.setText(v, R.id.textView8, "Terima kasih");
                ModulApotek.setText(v, R.id.textView9, "telah membeli aplikasi kami");
            } else {
                ModulApotek.setText(v, R.id.textView8, "Tingkatkan Versi");
                ModulApotek.setText(v, R.id.textView9, "Tingkatkan Versi untuk fasilitas lengkap");
            }

        }

        public void reset (View view){
            final String dirIn = "/data/data/com.itbrain.aplikasitoko/databases/";
            ;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Hilangkan segala data dan aplikasi akan restart");
            alertDialogBuilder.setPositiveButton("Reset",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            AlertDialog.Builder ale = new AlertDialog.Builder(Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas.this);
                            ale.setMessage("Apakah anda yakin untuk mereset data ?");
                            ale.setPositiveButton("Iya",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            if (ModulApotek.deleteFile(dirIn + DatabaseApotek.nama)) {
                                                ModulApotek.showToast(Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas.this, "Reset Berhasil, Aplikasi terestart");
                                                finishAffinity();
                                                System.exit(0);

                                            } else {
                                                ModulApotek.showToast(Aplikasi_Apotek_Plus_Keuangan_Menu_Utilitas.this, "Reset Gagal");
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

        public void petunjuk (View view){
            DialogForm();
        }

    }
