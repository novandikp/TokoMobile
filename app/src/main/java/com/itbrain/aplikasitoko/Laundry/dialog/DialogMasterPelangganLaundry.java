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

import com.itbrain.aplikasitoko.Laundry.Cari_Pelanggan_Laundry;
import com.itbrain.aplikasitoko.Laundry.DatabaseLaundry;
import com.itbrain.aplikasitoko.Laundry.MenuPelangganLaundry;
import com.itbrain.aplikasitoko.Laundry.ModulLaundry;
import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

import org.apache.commons.lang3.StringUtils;

public class DialogMasterPelangganLaundry {
    Context context;
    View dialogView;
    DatabaseLaundry db;
    Boolean fromMaster;
    public DialogMasterPelangganLaundry(@NonNull final Context context, @NonNull final Boolean databaru, final String id, final boolean fromMaster) {
        this.context = context;
        this.fromMaster=fromMaster;
        db=new DatabaseLaundry(context);
        String saveButton="Perbarui";
        String title=saveButton+" Data Pelanggan";
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_master_pelanggan_laundry,null);
        if (databaru){
            title="Tambah Data Pelanggan";
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
                        String[] qBaru={ModulLaundry.getText(dialogView,R.id.edtNamaPelanggan),
                                ModulLaundry.getText(dialogView,R.id.edtAlamatPelanggan),
                                ModulLaundry.getText(dialogView,R.id.edtTeleponPelanggan)};
                        String[] qUpdate={ModulLaundry.getText(dialogView,R.id.edtNamaPelanggan),
                                ModulLaundry.getText(dialogView,R.id.edtAlamatPelanggan),
                                ModulLaundry.getText(dialogView,R.id.edtTeleponPelanggan),
                                id};
                        if (StringUtils.isAnyBlank(qBaru[0],qBaru[1],qBaru[2])){
                            Toast.makeText(context, "Isi semua form terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }else {
                            if (databaru){
                                String q= Query.splitParam("INSERT INTO tblpelanggan (pelanggan,alamat,notelp) VALUES (?,?,?)",qBaru);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    if (fromMaster){
                                        ((MenuPelangganLaundry)context).getList("");
                                    }else {
                                        ((Cari_Pelanggan_Laundry)context).getList();
                                    }
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String q=Query.splitParam("UPDATE tblpelanggan SET pelanggan=? , alamat=? , notelp=? WHERE idpelanggan=? ",qUpdate);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    if (fromMaster){
                                        ((MenuPelangganLaundry)context).getList("");
                                    }else {
                                        ((Cari_Pelanggan_Laundry)context).getList();
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
        Cursor c = db.sq(Query.selectwhere("tblpelanggan")+Query.sWhere("idpelanggan",id)) ;
        c.moveToFirst();
        ModulLaundry.setText(dialogView,R.id.edtNamaPelanggan, ModulLaundry.getString(c,"pelanggan")) ;
        ModulLaundry.setText(dialogView,R.id.edtAlamatPelanggan, ModulLaundry.getString(c,"alamat")) ;
        ModulLaundry.setText(dialogView,R.id.edtTeleponPelanggan, ModulLaundry.getString(c,"notelp")) ;
    }
}
