package com.itbrain.aplikasitoko;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class DialogPelanggan {
    Database_Restoran db;
    View dialogView;
    public DialogPelanggan(@NonNull final Context context, final Boolean databaru, final String id) {
        db=new Database_Restoran(context);
        dialogView=LayoutInflater.from(context).inflate(R.layout.activity_dialog_pelanggan,null);
        String saveButton=context.getResources().getString(R.string.perbarui);
        String title=saveButton+" "+context.getResources().getString(R.string.datapelanggan);
        if (databaru){
            title=context.getResources().getString(R.string.tambah)+" "+context.getResources().getString(R.string.datapelanggan);
            saveButton=context.getResources().getString(R.string.tambah);
        }else {
            setText(id);
        }
        AlertDialog dialog=new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle(title)
                .setCancelable(false)
                .setIcon(R.drawable.ic_baseline_remove_24)
                .setPositiveButton(saveButton, null)
                .setNegativeButton(context.getResources().getString(R.string.batal), null)
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button=((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nama= ModulRestoran.getText(dialogView,R.id.edtNamaPelanggan);
                        String notelp=ModulRestoran.getText(dialogView,R.id.edtNoTelpPelanggan);
                        String alamat=ModulRestoran.getText(dialogView,R.id.edtAlamatPelanggan);
                        String[] pBaru={
                                nama,
                                notelp,
                                alamat
                        };
                        String[] pUpdate={
                                nama,
                                alamat,
                                notelp,
                                id
                        };
                        if (nama.isEmpty() || alamat.isEmpty() || notelp.isEmpty()){
                            Toast.makeText(context, context.getResources().getString(R.string.formnull), Toast.LENGTH_SHORT).show();
                        }else {
                            if (databaru){
                                String q=Query.splitParam("INSERT INTO tblpelanggan (namapelanggan,notelppelanggan,alamatpelanggan) VALUES (?,?,?)",pBaru);
                                if (db.exc(q)){
                                    Toast.makeText(context, context.getResources().getString(R.string.successAdd), Toast.LENGTH_SHORT).show();
//                                    ((Pelanggan_Restoran_)context).getList();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, context.getResources().getString(R.string.gagal), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String q=Query.splitParam("UPDATE tblpelanggan SET namapelanggan=? , alamatpelanggan=? , notelppelanggan=? WHERE idpelanggan=? ",pUpdate);
                                if (db.exc(q)){
                                    Toast.makeText(context, context.getResources().getString(R.string.successUpdate), Toast.LENGTH_SHORT).show();
//                                    ((Pelanggan_Restoran_)context).getList();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, context.getResources().getString(R.string.gagal), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }
    private void setText(String id){
        Cursor c = db.sq(Query.selectwhere("tblpelanggan")+Query.sWhere("idpelanggan",id)) ;
        c.moveToFirst();
        ModulRestoran.setText(dialogView,R.id.edtNamaPelanggan,ModulRestoran.getString(c,"namapelanggan")) ;
        ModulRestoran.setText(dialogView,R.id.edtNoTelpPelanggan,"0"+ModulRestoran.getString(c,"notelppelanggan")) ;
        ModulRestoran.setText(dialogView,R.id.edtAlamatPelanggan,ModulRestoran.getString(c,"alamatpelanggan")) ;
    }
}
