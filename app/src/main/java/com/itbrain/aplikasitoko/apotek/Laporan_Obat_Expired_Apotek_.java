package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Obat_Expired_Apotek_ extends AppCompatActivity {
    int year, day, month;
    Calendar calendar;
    ArrayList arrayList = new ArrayList();
    DatabaseApotek db;
    View v;
    String type;
    ModulApotek config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_obat_expired_apotek_);

        db = new DatabaseApotek(this);
        config = new ModulApotek(getSharedPreferences("config", this.MODE_PRIVATE));
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String tgl = ModulApotek.getDate("dd/MM/yyyy");
        ModulApotek.setText(v, R.id.tgl1, tgl);
        ModulApotek.setText(v, R.id.tgl2, tgl);
        getExpired("");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        type = getIntent().getStringExtra("type");
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final EditText eCari = (EditText) findViewById(R.id.dicari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString();
                arrayList.clear();

                getExpired(a);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getExpired(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recObatExp) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Adapter_Laporan_Bat_Apotek(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("qbarang") + ModulApotek.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String idbarang = ModulApotek.getString(c,"idbarang");
            String hargasatu="", hargadua="", satuankecil="" , satuanbesar="",barang;
            satuanbesar= ModulApotek.getString(c,"satuanbesar");
            satuankecil= ModulApotek.getString(c,"satuankecil");
            Cursor b = db.sq(ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbarang",idbarang)+" ORDER BY idbelidetail");
            if (b.getCount()>0){
                b.moveToLast();
                String idorederbeli = ModulApotek.getString(b,"idbelidetail");
                Cursor d = db.sq(ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbelidetail",idorederbeli)+" AND "+ ModulApotek.sBetween("expired", ModulApotek.getDate("dd/MM/yyyy"), ModulApotek.getExpiredEnd(ModulApotek.strToInt(config.getCustom("expired","30")))));
                Log.d("sqlexpired", "getExpired: "+ ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idbelidetail",idorederbeli)+" AND "+ ModulApotek.sBetween("expired", ModulApotek.getDate("dd/MM/yyyy"), ModulApotek.getExpiredEnd(ModulApotek.strToInt(config.getCustom("expired","30")))));
                if (d.getCount()>0){
                    while (d.moveToNext()){
                        barang = ModulApotek.getString(b,"barang");
                        hargasatu= ModulApotek.getString(d,"harga_jual_satu");
                        hargadua= ModulApotek.getString(d,"harga_jual_dua");
                        String campur = ModulApotek.getString(d,"idbelidetail")+"__"+ ModulApotek.getString(d,"barang")+"__" + ModulApotek.getString(d,"stok")+ "__" + hargasatu+ "__" + hargadua+ "__" + satuankecil+ "__" + satuanbesar+"__"+ ModulApotek.getString(d,"nilai")+"__"+ ModulApotek.getString(d,"expired");
                        arrayList.add(campur);
                    }
                }





            }

            adapter.notifyDataSetChanged();
        }}

    public void openDialog(final String id){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_expired,null);
        dialog.setView(dialogView);

//        dialog.setCancelable(false);
        final AlertDialog alertDialog = dialog.create();

        Cursor c = db.sq("select * from tblbelidetail where idbelidetail ="+id);
        c.moveToNext();
        final TextInputEditText tgl = dialogView.findViewById(R.id.tgl);
        tgl.setText(ModulApotek.getString(c,"expired"));

        ImageView calendar = dialogView.findViewById(R.id.calendar);

        Button update = dialogView.findViewById(R.id.update);
        Button cancel = dialogView.findViewById(R.id.cancel);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog datepicker = new DatePickerDialog(Laporan_Obat_Expired_Apotek_.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tgl.setText(ModulApotek.setDatePickerNormal(year,month+1,dayOfMonth));


                    }
                },year,month,day);
                datepicker.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tgl1 = tgl.getText().toString();
                String [] isi={tgl1,id};
                if (db.exc(ModulApotek.splitParam("UPDATE tblbelidetail SET expired=? WHERE idbelidetail=?   ",isi))){
                    ModulApotek.showToast(Laporan_Obat_Expired_Apotek_.this,"Berhasil diupdate");
                    getExpired("");
                }else{
                    ModulApotek.showToast(Laporan_Obat_Expired_Apotek_.this,"Gagal diupdate");
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }
    public void export(View view) {
        Intent i = new Intent(this, Menu_Export_Exel_Apotek.class);
        i.putExtra("type", "expired");
        startActivity(i);
    }


}
