package com.itbrain.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Identitas_Restoran extends AppCompatActivity {

    EditText namatoko, alamattoko, nomortoko, caption1toko, caption2toko, caption3toko, pjk;
    Database_Restoran db;
    View v;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identitas_restoran);
        v = this.findViewById(android.R.id.content);
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        db = new Database_Restoran(this);
        namatoko = findViewById(R.id.edtNamaToko);
        alamattoko = findViewById(R.id.edtAlamatToko);
        nomortoko = findViewById(R.id.edtNomorTlpn);
        caption1toko = findViewById(R.id.edtCaption1);
        caption2toko = findViewById(R.id.edtCaption2);
        caption3toko= findViewById(R.id.edtCaption3);
        pjk = findViewById(R.id.edtPpn);

        String cCode="+"+String.valueOf(ModulRestoran.getCurrentCountryCode(this));
        ModulRestoran.setText(v,R.id.countryCode,cCode);
//        Cursor c = db.sq(Query.select("tblidentitas")) ;
//        if(c.moveToFirst()){
//            selectData(); ;
//        }

        load();
//        String eCode = "+" + String.valueOf(ModulRestoran.getCurrentCountryCode(this));
//        ModulRestoran.setText(v,R.id.countryCode,eCode);
//        Cursor c = db.sq(Query.select("tblidentitas"));
//        if (c.moveToFirst()) {
//            setText();
//        }

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void selectData() {
        String sql = "SELECT * FROM tblidentitas WHERE id = 1";
        Cursor c = db.sq(sql);
        if (c != null) {
            if (c.moveToNext()) {
                namatoko.setText(c.getString(c.getColumnIndex("namatoko")));
                nomortoko.setText(c.getString(c.getColumnIndex("notelp")));
                alamattoko.setText(c.getString(c.getColumnIndex("alamat")));
                caption1toko.setText(c.getString(c.getColumnIndex("caption1")));
                caption2toko.setText(c.getString(c.getColumnIndex("caption2")));
                caption3toko.setText(c.getString(c.getColumnIndex("caption3")));
                pjk.setText(c.getString(c.getColumnIndex("pajak")));
            }
        }
    }

    public void clear(View view) {
        ModulRestoran.setText(v, R.id.edtNamaToko, null);
        ModulRestoran.setText(v, R.id.edtAlamatToko, null);
        ModulRestoran.setText(v, R.id.edtNomorTlpn, null);
        ModulRestoran.setText(v, R.id.edtCaption1, null);
        ModulRestoran.setText(v, R.id.edtCaption2, null);
        ModulRestoran.setText(v, R.id.edtCaption3, null);

    }

    public void load() {
        db = new Database_Restoran(this);

        selectData();
    }

    public void simpan(View view) {
        String nama = namatoko.getText().toString();
        String nomor = nomortoko.getText().toString();
        String alamat = alamattoko.getText().toString();
        String caption1 = caption1toko.getText().toString();
        String caption2 = caption2toko.getText().toString();
        String caption3 = caption3toko.getText().toString();
        String pajak = pjk.getText().toString();
        Cursor c = db.sq(Query.select("tblidentitas"));
        String[] p = {"1",
                nama,
                nomor,
                alamat,
                caption1,
                caption2,
                caption3,
                pajak
        };
        String[] p1 = {nama,
                nomor,
                alamat,
                caption1,
                caption2,
                caption3,
                pajak,
                "1"
        };
        
        if (nama.isEmpty()||alamat.isEmpty()||nomor.isEmpty()||caption1.isEmpty()||caption2.isEmpty()||caption3.isEmpty()||pajak.isEmpty()) {
            Toast.makeText(this, "Isi Semua Form Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        } else {
//            String sql = "UPDATE tblidentitas SET namatoko = '"+nama+"', notelp = '"+nomor+"', alamat = '"+alamat+"', caption1 = '"+caption1+"', caption2 = '"+caption2+"', caption3 = '"+caption3+", pajak = '"+pajak+"' WHERE id = 1 ";
//            db.exc(sql);
//            Toast.makeText(this, "Simpan Identitas", Toast.LENGTH_SHORT).show();
//            finish();

            String q = "";
            if (c.getCount() == 1) {
                q = Query.splitParam("UPDATE tblidentitas SET namatoko=? ,notelp=? ,alamat=? ,caption1=? ,caption2=? ,caption3=?, pajak=? WHERE id=?   ", p1);
            } else {
                q = Query.splitParam("INSERT INTO tblidentitas VALUES(?,?,?,?,?,?,?,?)", p);
            }
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void infoPpn(View view) {
        Locale currnentLocale = Locale.getDefault();
        String url = "https://wikipedia.org/wiki/Value-added_tax";
        if (currnentLocale.getCountry().equals("ID")) {
            url = "https://id.wikipedia.org/wiki/Pajak_pertambahan_nilai";
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}