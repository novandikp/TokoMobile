package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

public class Form_Tambah_Pelanggan_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    Button btnSimpan;
    TextInputEditText edtNamaPelanggan, edtAlamat, edtNoTelp;
    String namapelanggan, alamat;
    Integer idPelanggan;
    String notelp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_pelanggan_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        appbar = (Toolbar) findViewById(R.id.toolbar68);

        btnSimpan = (Button) findViewById(R.id.button80);
        edtNamaPelanggan = (TextInputEditText) findViewById(R.id.tNama);
        edtAlamat = (TextInputEditText) findViewById(R.id.tAlamat);
        edtNoTelp = (TextInputEditText) findViewById(R.id.tTelp);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            //Insert
            idPelanggan = null;
        } else {
            idPelanggan = extra.getInt("idpelanggan");
            edtNamaPelanggan.setText(extra.getString("pelanggan"));
            edtAlamat.setText(extra.getString("alamatpel"));
            edtNoTelp.setText(extra.getString("telppel"));
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namapelanggan = edtNamaPelanggan.getText().toString();
                alamat = edtAlamat.getText().toString();
                notelp = edtNoTelp.getText().toString();

                if (namapelanggan.equals("") || alamat.equals("") || notelp.equals("")) {
                    Toast.makeText(Form_Tambah_Pelanggan_Salon_.this, "Masukan Data Dengan Benar", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseSalon db = new DatabaseSalon(Form_Tambah_Pelanggan_Salon_.this);
                    if (idPelanggan == null) {
                        if (db.insertPelanggan(namapelanggan, alamat, notelp)) {
                            Toast.makeText(Form_Tambah_Pelanggan_Salon_.this, "Tambah Pelanggan berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Form_Tambah_Pelanggan_Salon_.this, "Tambah data gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (db.updatePelanggan(idPelanggan, namapelanggan, alamat, notelp)) {
                            Toast.makeText(Form_Tambah_Pelanggan_Salon_.this, "Berhasil memperbarui data", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Form_Tambah_Pelanggan_Salon_.this, "Gagal memperbaharui data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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
}