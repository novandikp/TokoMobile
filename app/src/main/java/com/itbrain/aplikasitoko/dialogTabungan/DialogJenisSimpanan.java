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

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;
import com.itbrain.aplikasitoko.tabungan.DatabaseTabungan;
import com.itbrain.aplikasitoko.tabungan.MenuJenisTabungan;
import com.itbrain.aplikasitoko.tabungan.ModulTabungan;
import com.itbrain.aplikasitoko.tabungan.QueryTabungan;
//import com.komputerkit.aplikasitabunganplus.Database;
//import com.komputerkit.aplikasitabunganplus.MenuMasterList;
//import com.komputerkit.aplikasitabunganplus.Modul;
//import com.komputerkit.aplikasitabunganplus.Query;
//import com.komputerkit.aplikasitabunganplus.R;
//import com.komputerkit.aplikasitabunganplus.util.NumberTextWatcher;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class DialogJenisSimpanan {
    Context context;
    DatabaseTabungan db;
    View dialogView;

    public DialogJenisSimpanan(final Context context, @NonNull final Boolean databaru, final String id , final boolean fromActivityTambah) {
        this.context = context;
        db=new DatabaseTabungan(context);
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_jenis_simpanan_tabungan,null);
        String saveButton=context.getResources().getString(R.string.perbarui);
        String title=saveButton+" "+context.getResources().getString(R.string.jenissimpanan);
        if (databaru){
            title=context.getResources().getString(R.string.tambah)+" "+context.getResources().getString(R.string.jenissimpanan);
            saveButton=context.getResources().getString(R.string.tambah);
        }else {
            setText(id);
        }
        final TextInputEditText edtBunga=dialogView.findViewById(R.id.edtBunga);
        edtBunga.addTextChangedListener(new NumberTextWatcher(edtBunga,new Locale("in","ID"),8));
        edtBunga.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (Double.valueOf(ModulTabungan.parseDF(edtBunga.getText().toString()))<0){
                    edtBunga.setText("0");
                }
            }
        });

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
                        String nama= ModulTabungan.getText(dialogView,R.id.edtNamaJenis);
                        String bunga=ModulTabungan.parseDF(ModulTabungan.getText(dialogView,R.id.edtBunga));

                        String[] pBaru={
                                nama,
                                bunga
                        };
                        String[] pUpdate={
                                nama,
                                bunga,
                                id
                        };
                        if (StringUtils.isAnyBlank(nama,bunga)){
                            Toast.makeText(context, context.getResources().getString(R.string.formnull), Toast.LENGTH_SHORT).show();
                        }else {
                            if (databaru){
                                String q= QueryTabungan.splitParam("INSERT INTO tbljenissimpanan (jenis,bunga) VALUES (?,?)",pBaru);
                                if (db.exc(q)){
                                    Toast.makeText(context, context.getResources().getString(R.string.successAdd), Toast.LENGTH_SHORT).show();
                                    if (fromActivityTambah){
                                        ((MenuJenisTabungan)context).getList();
                                    }else {

                                    }
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, context.getResources().getString(R.string.gagal), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String q=QueryTabungan.splitParam("UPDATE tbljenissimpanan SET jenis=? , bunga=? WHERE idjenis=? ",pUpdate);
                                if (db.exc(q)){
                                    Toast.makeText(context, context.getResources().getString(R.string.successUpdate), Toast.LENGTH_SHORT).show();
                                    if (fromActivityTambah){
                                        ((MenuJenisTabungan)context).getList();
                                    }else {

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
        Cursor c = db.sq(QueryTabungan.selectwhere("tbljenissimpanan")+QueryTabungan.sWhere("idjenis",id)) ;
        c.moveToFirst();
        ModulTabungan.setText(dialogView,R.id.edtNamaJenis,ModulTabungan.getString(c,"jenis")) ;
        ModulTabungan.setText(dialogView,R.id.edtBunga,ModulTabungan.numFormat(ModulTabungan.getString(c,"bunga"))) ;
    }
}
