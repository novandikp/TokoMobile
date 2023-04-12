package com.itbrain.aplikasitoko.Laundry.dialog;

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

import com.itbrain.aplikasitoko.Laundry.DatabaseLaundry;
import com.itbrain.aplikasitoko.Laundry.Menu_Master_Pegawai_Laundry;
import com.itbrain.aplikasitoko.Laundry.ModulLaundry;
import com.itbrain.aplikasitoko.Laundry.QueryLaundry;
import com.itbrain.aplikasitoko.Laundry.Transaksi_Cari_Pegawai;
import com.itbrain.aplikasitoko.R;

import org.apache.commons.lang3.StringUtils;

public class DialogMasterPegawaiLaundry {
    Context context;
    DatabaseLaundry db;
    View dialogView;
    Boolean fromMaster;
    public DialogMasterPegawaiLaundry(@NonNull final Context context, @NonNull final Boolean databaru, final String id, final boolean fromMaster) {
        this.context = context;
        this.fromMaster=fromMaster;
        db=new DatabaseLaundry(context);
        String saveButton="Perbarui";
        String title=saveButton+" Data Pegawai";
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_master_pegawai_laundry,null);
        if (databaru){
            title="Tambah Data Pegawai";
            saveButton="Tambah";
        }else {
            setText(id);
        }
        final AlertDialog dialog=new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setTitle(title)
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_edit)
                        .setPositiveButton(saveButton, null)
                        .setNegativeButton("Batal", null)
                        .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button button=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] qBaru={ModulLaundry.getText(dialogView,R.id.edtNamaPegawai),
                                        ModulLaundry.getText(dialogView,R.id.edtAlamatPegawai),
                                        ModulLaundry.getText(dialogView,R.id.edtTeleponPegawai)};
                        String[] qUpdate={ModulLaundry.getText(dialogView,R.id.edtNamaPegawai),
                                        ModulLaundry.getText(dialogView,R.id.edtAlamatPegawai),
                                        ModulLaundry.getText(dialogView,R.id.edtTeleponPegawai),
                                        id};
                        if (StringUtils.isAnyBlank(qBaru[0],qBaru[1],qBaru[2])){
                            Toast.makeText(context, "Isi semua form terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }else {
                            if (databaru){
                                String q= QueryLaundry.splitParam("INSERT INTO tblpegawai (pegawai,alamatpegawai,notelppegawai) VALUES (?,?,?)",qBaru);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    if (fromMaster){
                                        ((Menu_Master_Pegawai_Laundry)context).getList("");
                                    }else {
                                        ((Transaksi_Cari_Pegawai)context).getList();
                                    }
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String q= QueryLaundry.splitParam("UPDATE tblpegawai SET pegawai=? , alamatpegawai=? , notelppegawai=? WHERE idpegawai=? ",qUpdate);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    if (fromMaster){
                                        try{
                                            ((Menu_Master_Pegawai_Laundry)context).getList("");
                                        }catch (Exception e){

                                        }

                                    }else {
                                        try{
                                            ((Transaksi_Cari_Pegawai)context).getList();
                                        }catch (Exception e){

                                        }

                                    }
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
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
        Cursor c = db.sq(QueryLaundry.selectwhere("tblpegawai")+ QueryLaundry.sWhere("idpegawai",id)) ;
        c.moveToFirst();
        ModulLaundry.setText(dialogView,R.id.edtNamaPegawai, ModulLaundry.getString(c,"pegawai")) ;
        ModulLaundry.setText(dialogView,R.id.edtAlamatPegawai, ModulLaundry.getString(c,"alamatpegawai")) ;
        ModulLaundry.setText(dialogView,R.id.edtTeleponPegawai, ModulLaundry.getString(c,"notelppegawai")) ;
    }
}
