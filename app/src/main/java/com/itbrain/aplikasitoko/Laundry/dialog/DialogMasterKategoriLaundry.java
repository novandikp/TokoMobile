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
import com.itbrain.aplikasitoko.Laundry.Menu_Master_Kategori_Laundry;
import com.itbrain.aplikasitoko.Laundry.ModulLaundry;
import com.itbrain.aplikasitoko.Laundry.QueryLaundry;
import com.itbrain.aplikasitoko.R;

import org.apache.commons.lang3.StringUtils;

public class DialogMasterKategoriLaundry {
    Context context;
    View dialogView;
    DatabaseLaundry db;
    public DialogMasterKategoriLaundry(@NonNull final Context context, @NonNull final Boolean databaru, final String id) {
        this.context = context;
        db=new DatabaseLaundry(context);
        String saveButton="Perbarui";
        String title=saveButton+" Data Kategori";
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_master_kategori_laundry,null);
        if (databaru){
            title="Tambah Data Kategori";
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
            public void onShow(final DialogInterface d) {
                Button button=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] qBaru={ModulLaundry.getText(dialogView,R.id.edtKategori)};
                        String[] qUpdate={ModulLaundry.getText(dialogView,R.id.edtKategori),
                                id};
                        if (StringUtils.isAnyBlank(qBaru[0])){
                            Toast.makeText(context, "Isi semua form terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }else {
                            if (databaru){
                                String q= QueryLaundry.splitParam("INSERT INTO tblkategori (kategori) VALUES (?)",qBaru);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    ((Menu_Master_Kategori_Laundry)context).getList("");
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String q= QueryLaundry.splitParam("UPDATE tblkategori SET kategori=? WHERE idkategori=? ",qUpdate);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    ((Menu_Master_Kategori_Laundry)context).getList("");
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
        Cursor c = db.sq(QueryLaundry.selectwhere("tblkategori")+ QueryLaundry.sWhere("idkategori",id)) ;
        c.moveToFirst();
        ModulLaundry.setText(dialogView,R.id.edtKategori, ModulLaundry.getString(c,"kategori")) ;
    }
}
