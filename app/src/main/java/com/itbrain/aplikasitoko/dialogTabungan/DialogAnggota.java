package com.itbrain.aplikasitoko.dialogTabungan;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.tabungan.DatabaseTabungan;
import com.itbrain.aplikasitoko.tabungan.MenuAnggotaTabungan;
import com.itbrain.aplikasitoko.tabungan.MenuTransaksiCariTabungan;
import com.itbrain.aplikasitoko.tabungan.ModulTabungan;
import com.itbrain.aplikasitoko.tabungan.QueryTabungan;
//import com.komputerkit.aplikasitabunganplus.Database;
//import com.komputerkit.aplikasitabunganplus.MenuMasterList;
//import com.komputerkit.aplikasitabunganplus.MenuTransaksiCari;
//import com.komputerkit.aplikasitabunganplus.Modul;
//import com.komputerkit.aplikasitabunganplus.Query;
//import com.komputerkit.aplikasitabunganplus.R;

import org.apache.commons.lang3.StringUtils;

public class DialogAnggota {
    Context context;
    DatabaseTabungan db;
    View dialogView;

    public DialogAnggota(final Context context, @NonNull final Boolean databaru, final String id , final boolean fromActivityTambah) {
        this.context = context;
        db=new DatabaseTabungan(context);
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_tambah_anggota_tabungan,null);
        String saveButton=context.getResources().getString(R.string.perbarui);
        String title=saveButton+" "+context.getResources().getString(R.string.dataanggota);

        if (databaru){
            title=context.getResources().getString(R.string.tambah)+" "+context.getResources().getString(R.string.dataanggota);
            saveButton=context.getResources().getString(R.string.tambah);
        }else {
            setText(id);
        }

        AlertDialog dialog=new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle(title)
                .setCancelable(false)
                .setIcon(R.drawable.ic_edit_tabungan)
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
                        String nama= ModulTabungan.getText(dialogView,R.id.edtNamaPelanggan);
                        String alamat=ModulTabungan.getText(dialogView,R.id.edtAlamatPelanggan);
                        String notelp=ModulTabungan.getText(dialogView,R.id.edtNoTelpPelanggan);

                        String[] pBaru={
                                nama,
                                alamat,
                                notelp
                        };
                        String[] pUpdate={
                                nama,
                                alamat,
                                notelp,
                                id
                        };
                        if (StringUtils.isAnyBlank(nama,notelp,alamat)){
                            Toast.makeText(context, context.getResources().getString(R.string.formnull), Toast.LENGTH_SHORT).show();
                        }else {
                            if (databaru){
                                String q= QueryTabungan.splitParam("INSERT INTO tblanggota (namaanggota,alamatanggota,notelpanggota) VALUES (?,?,?)",pBaru);
                                if (db.exc(q)){
                                    Toast.makeText(context, context.getResources().getString(R.string.successAdd), Toast.LENGTH_SHORT).show();
                                    if (fromActivityTambah){
                                        ((MenuAnggotaTabungan)context).getList();
                                    }else {
                                        ((MenuTransaksiCariTabungan)context).getList();
                                    }
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, context.getResources().getString(R.string.gagal), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String q=QueryTabungan.splitParam("UPDATE tblanggota SET namaanggota=? , alamatanggota=? , notelpanggota=? WHERE idanggota=? ",pUpdate);
                                if (db.exc(q)){
                                    Toast.makeText(context, context.getResources().getString(R.string.successUpdate), Toast.LENGTH_SHORT).show();
                                    if (fromActivityTambah){
                                        ((MenuAnggotaTabungan)context).getList();
                                    }else {
                                        ((MenuTransaksiCariTabungan)context).getList();
                                    }
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
        Cursor c = db.sq(QueryTabungan.selectwhere("tblanggota")+QueryTabungan.sWhere("idanggota",id)) ;
        c.moveToFirst();
        ModulTabungan.setText(dialogView,R.id.edtNamaPelanggan,ModulTabungan.getString(c,"namaanggota")) ;
        ModulTabungan.setText(dialogView,R.id.edtAlamatPelanggan,ModulTabungan.getString(c,"alamatanggota")) ;
        ModulTabungan.setText(dialogView,R.id.edtNoTelpPelanggan,"0"+ModulTabungan.getString(c,"notelpanggota")) ;
    }
}
