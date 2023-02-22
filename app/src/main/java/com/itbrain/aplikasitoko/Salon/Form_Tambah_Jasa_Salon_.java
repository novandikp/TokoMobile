package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

import java.util.function.Function;

public class Form_Tambah_Jasa_Salon_ extends AppCompatActivity {

    Button bSimpan;
    TextInputEditText edtNamaJasa, edtHarga;
    Integer idJasa;
    SharedPreferences getPrefs;
    String namaJasa, harga, deviceid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_jasa_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DatabaseSalon db = new DatabaseSalon(this);
        getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        deviceid = (getPrefs.getString("deviceid", ""));

        bSimpan = (Button) findViewById(R.id.btnSimpan);
        edtNamaJasa = (TextInputEditText) findViewById(R.id.edtNamaJasa);
        edtHarga = (TextInputEditText) findViewById(R.id.edtHarga);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            //Insert
            idJasa = null;
        } else {
            idJasa = extra.getInt("idjasa");
            edtNamaJasa.setText(extra.getString("jasa"));
            edtHarga.setText(extra.getString("harga"));
        }

        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaJasa = edtNamaJasa.getText().toString();
                harga = edtHarga.getText().toString();

                if (namaJasa.equals("") || harga.equals("") || harga.equals("0")) {
                    Toast.makeText(Form_Tambah_Jasa_Salon_.this, "Masukan Data Dengan Benar", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseSalon db = new DatabaseSalon(Form_Tambah_Jasa_Salon_.this);
                    if (idJasa == (null)) {
                        if (db.insertJasa(namaJasa, harga)) {
                            Toast.makeText(Form_Tambah_Jasa_Salon_.this, "Tambah Jasa berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Form_Tambah_Jasa_Salon_.this, "Tambah data gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (db.updateJasa(idJasa, namaJasa, harga)) {
                            Toast.makeText(Form_Tambah_Jasa_Salon_.this, "Berhasil memperbarui data", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Form_Tambah_Jasa_Salon_.this, "Gagal memperbaharui data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void simpan(View view) {
        Intent intent = new Intent(this,Menu_Jasa_Salon_.class);
        startActivity(intent);

    }
}