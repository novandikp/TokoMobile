package com.itbrain.aplikasitoko.Laundry.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.itbrain.aplikasitoko.Laundry.DatabaseLaundry;
import com.itbrain.aplikasitoko.Laundry.ActivityTransaksiCari_Laundry;
import com.itbrain.aplikasitoko.Laundry.Menu_Master_Jasa_Laundry;
import com.itbrain.aplikasitoko.Laundry.ModulLaundry;
import com.itbrain.aplikasitoko.Laundry.QueryLaundry;
import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Util.NumberTextWatcher;


import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

public class DialogMasterJasaLaundry {
    Context context;
    View dialogView;
    DatabaseLaundry db;
    Spinner spKategori,spSatuan;
    ArrayAdapter<String> data;
    List<String> kategori,idkategori;
    Boolean fromMaster;
    public DialogMasterJasaLaundry(@NonNull final Context context, @NonNull final Boolean databaru, final String id, final boolean fromMaster) {
        this.fromMaster=fromMaster;
        this.context = context;
        db=new DatabaseLaundry(context);
        kategori=db.getKategori();
        idkategori=db.getIdKategori();
        if (db.getKategori().get(0).equals("Semua Kategori")){
            kategori.remove(0);
            idkategori.remove(0);
        }
        String saveButton="Perbarui";
        String title=saveButton+" Data Jasa";
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_master_jasa_laundry,null);
        spKategori=dialogView.findViewById(R.id.spKategori);
        spSatuan=dialogView.findViewById(R.id.spSatuan);
        data = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,kategori);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(data);
        if (databaru){
            title="Tambah Data Jasa";
            saveButton="Tambah";
        }else {
            setText(id);
        }
        EditText edtBiaya=dialogView.findViewById(R.id.edtBiaya);
        edtBiaya.addTextChangedListener(new NumberTextWatcher(edtBiaya,new Locale("in","ID"),2));
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
                        String Satuan = "";
                        if (spSatuan.getSelectedItemPosition()==0){
                            Satuan = "pc";
                        }else if (spSatuan.getSelectedItemPosition()==1){
                            Satuan = "kg";
                        }else if (spSatuan.getSelectedItemPosition()==2){
                            Satuan = "m2";
                        }
                        String[] qBaru={idkategori.get(spKategori.getSelectedItemPosition()),
                                ModulLaundry.getText(dialogView,R.id.edtNamaJasa),
                                ModulLaundry.unNumberFormat(ModulLaundry.getText(dialogView,R.id.edtBiaya)),
                                Satuan};
                        String[] qUpdate={idkategori.get(spKategori.getSelectedItemPosition()),
                                ModulLaundry.getText(dialogView,R.id.edtNamaJasa),
                                ModulLaundry.unNumberFormat(ModulLaundry.getText(dialogView,R.id.edtBiaya)),
                                Satuan,
                                id};
                        if (StringUtils.isAnyBlank(qBaru[0],qBaru[1],qBaru[2])|| ModulLaundry.strToDouble(qBaru[2])<=0||qBaru[0]=="0"){
                            Toast.makeText(context, "Isi semua form terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }else {
                            if (databaru){
                                String q= QueryLaundry.splitParam("INSERT INTO tbljasa (idkategori,jasa,biaya,satuan) VALUES (?,?,?,?)",qBaru);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    if (fromMaster){
                                        ((Menu_Master_Jasa_Laundry)context).getList("");
                                    }else {
                                        ((ActivityTransaksiCari_Laundry)context).getList();
                                    }
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String q= QueryLaundry.splitParam("UPDATE tbljasa SET idkategori=? , jasa=? , biaya=?, satuan=? WHERE idjasa=? ",qUpdate);
                                if (db.exc(q)){
                                    Toast.makeText(context, "Sukses", Toast.LENGTH_SHORT).show();
                                    if (fromMaster){
                                        ((Menu_Master_Jasa_Laundry)context).getList("");
                                    }else {
                                        ((ActivityTransaksiCari_Laundry)context).getList();
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
        Cursor c = db.sq(QueryLaundry.selectwhere("tbljasa")+ QueryLaundry.sWhere("idjasa",id)) ;
        c.moveToFirst();
        spKategori.setSelection(db.getKategoriPosition(ModulLaundry.getString(c,"idkategori")));
        ModulLaundry.setText(dialogView,R.id.edtNamaJasa, ModulLaundry.getString(c,"jasa")) ;
        ModulLaundry.setText(dialogView,R.id.edtBiaya, ModulLaundry.removeE(ModulLaundry.getString(c,"biaya"))) ;
        if (ModulLaundry.getString(c,"satuan").equals("pc")){
            spSatuan.setSelection(0);
        }else if (ModulLaundry.getString(c,"satuan").equals("kg")){
            spSatuan.setSelection(1);
        }else if (ModulLaundry.getString(c,"satuan").equals("m2")){
            spSatuan.setSelection(2);
        }
    }
}
