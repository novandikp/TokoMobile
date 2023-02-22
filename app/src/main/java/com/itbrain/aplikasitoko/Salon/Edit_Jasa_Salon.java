package com.itbrain.aplikasitoko.Salon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

public class Edit_Jasa_Salon extends AppCompatActivity {

    Button bSimpan;
    TextInputEditText edtNamaJasa, edtHarga;
    Integer idJasa;
    SharedPreferences getPrefs;
    String namaJasa, harga, deviceid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_jasa_salon);

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
                    Toast.makeText(Edit_Jasa_Salon.this, "Masukan Data Dengan Benar", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseSalon db = new DatabaseSalon(Edit_Jasa_Salon.this);
                    if (idJasa == (null)) {
                        if (db.insertJasa(namaJasa, harga)) {
                            Toast.makeText(Edit_Jasa_Salon.this, "Tambah Jasa berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Edit_Jasa_Salon.this, "Tambah data gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (db.updateJasa(idJasa, namaJasa, harga)) {
                            Toast.makeText(Edit_Jasa_Salon.this, "Berhasil memperbarui data", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Edit_Jasa_Salon.this, "Gagal memperbaharui data", Toast.LENGTH_SHORT).show();
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