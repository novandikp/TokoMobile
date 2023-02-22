package com.itbrain.aplikasitoko.rentalmobil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

public class MobilMenuBayar extends AppCompatActivity {
    DatabaseRentalMobil db;
    View v;
    String idrental;
    ModulRentalMobil config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bayar_mobil);

        config = new ModulRentalMobil(getSharedPreferences("config",MODE_PRIVATE));
        db = new DatabaseRentalMobil(this);
        v = this.findViewById(android.R.id.content);
        idrental = getIntent().getStringExtra("idrental");
        setText();
        final TextInputEditText eBayar = findViewById(R.id.eBayar);
        eBayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double total = ModulRentalMobil.strToDouble(ModulRentalMobil.unNumberFormat(ModulRentalMobil.getText(v,R.id.eTotal)));
                double bayar = ModulRentalMobil.strToDouble(eBayar.getText().toString());
                ModulRentalMobil.setText(v,R.id.eKembali,ModulRentalMobil.removeE(bayar-total));

            }
        });

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setText(){
        Cursor c = db.sq(ModulRentalMobil.selectwhere("view_rental")+ModulRentalMobil.sWhere("idrental",idrental));
        c.moveToNext();
        double total, dp;
        total = ModulRentalMobil.strToDouble(ModulRentalMobil.getString(c,"total"));
        dp = ModulRentalMobil.strToDouble(ModulRentalMobil.getString(c,"dp"));
        ModulRentalMobil.setText(v,R.id.tFaktur,"Faktur : "+ModulRentalMobil.getString(c,"faktur"));
        ModulRentalMobil.setText(v,R.id.tPelanggan,"Pelanggan : "+ModulRentalMobil.getString(c,"pelanggan"));
        ModulRentalMobil.setText(v,R.id.tTanggal,"Tanggal Rental : "+ModulRentalMobil.getString(c,"tglrental"));
        ModulRentalMobil.setText(v,R.id.tTotal,"Total Rental : "+ModulRentalMobil.removeE(total));
        ModulRentalMobil.setText(v,R.id.tDP,"Uang Muka : "+ModulRentalMobil.removeE(dp));
        ModulRentalMobil.setText(v,R.id.eTotal,ModulRentalMobil.removeE(total-dp));
        ModulRentalMobil.setText(v,R.id.eKembali,"-"+ModulRentalMobil.removeE(total-dp));


    }

    private void print(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.judul_cetak)).setMessage(getString(R.string.subjudul_cetak)).
                setPositiveButton(getString(R.string.iya), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent i = new Intent(MobilMenuBayar.this, MenuCetak2_Mobil.class);
                i.putExtra("idorder",idrental);
                i.putExtra("type","bayar");
                startActivity(i);
            }
        }).setNegativeButton(getString(R.string.tidak), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent i = new Intent(MobilMenuBayar.this, MenuTransaksi_Mobil.class);
                startActivity(i);
            }
        }).show();
    }

    private void simpan(){
        String bayar = ModulRentalMobil.getText(v,R.id.eBayar);
        String kembali = ModulRentalMobil.unNumberFormat(ModulRentalMobil.getText(v,R.id.eKembali));
        if (ModulRentalMobil.strToDouble(kembali)<0){
            ModulRentalMobil.showToast(this,"Uang Masih Belum cukup");
        }else{
            String isi [] = {bayar,kembali,"3",idrental};
            String q = ModulRentalMobil.splitParam("UPDATE tblrental SET bayar=?,kembali=?,flagrental=? WHERE idrental=?   ",isi);
            if (db.exc(q)){
                print();
            }

        }
    }

    public void bayar(View view) {
        simpan();
    }
}

