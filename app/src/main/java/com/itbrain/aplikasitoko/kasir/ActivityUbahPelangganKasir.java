package com.itbrain.aplikasitoko.kasir;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itbrain.aplikasitoko.R;

public class ActivityUbahPelangganKasir extends AppCompatActivity {

    String type;
    FConfigKasir config, temp;
    DatabaseKasir db;
    View v;
    String id;
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_ubah);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        v = this.findViewById(android.R.id.content);


        try {
            type = getIntent().getStringExtra("type");
        } catch (Exception e) {
            type = "";
            finish();
        }
        id = getIntent().getStringExtra("id");


        setContentView(R.layout.form_ubah_pelanggan_toko_kasir_);
        //setTitle("Ubah Pelanggan");
        // Text Counter

        //limiter();
        setPelanggan();

        ImageButton imageButton = findViewById(R.id.KembaliPelanggan);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void limiter() {
        etnamapelanggan = (EditText) findViewById(R.id.namaPelanggan);
        etalamatpelanggan = (EditText) findViewById(R.id.alamatPelanggan);
        ettelppelanggan = (EditText) findViewById(R.id.telpPelanggan);

        tvRpmpelanggan = (TextView) findViewById(R.id.namaPelanggan);
        tvRpmalamat = (TextView) findViewById(R.id.alamatPelanggan);
        tvTelp = (TextView) findViewById(R.id.telpPelanggan);

        textCounterPelanggan();
    }

    void textCounterPelanggan() {
        etnamapelanggan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvRpmpelanggan.setText(etnamapelanggan.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etalamatpelanggan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvRpmalamat.setText(etalamatpelanggan.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ettelppelanggan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTelp.setText(ettelppelanggan.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setPelanggan() {
        Cursor c = db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan=" + id);
        if (FFunctionKasir.getCount(c) > 0) {
            c.moveToNext();
            FFunctionKasir.setText(v, R.id.namaPelanggan, FFunctionKasir.getString(c, "pelanggan"));
            FFunctionKasir.setText(v, R.id.alamatPelanggan, FFunctionKasir.getString(c, "alamat"));
            FFunctionKasir.setText(v, R.id.telpPelanggan, FFunctionKasir.getString(c, "telp"));
        }
    }

    public void simpanPelanggan(View view){
                tambahpelanggan();
    }

    private void tambahpelanggan() {
        String nama = FFunctionKasir.getText(v, R.id.namaPelanggan);
        String alamat = FFunctionKasir.getText(v, R.id.alamatPelanggan);
        String telp = FFunctionKasir.getText(v, R.id.telpPelanggan);
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(telp)) {
            String[] p = {nama, alamat, telp, id};
            String q = FQueryKasir.splitParam("UPDATE tblpelanggan SET pelanggan=?,alamat=?,telp=? WHERE idpelanggan=?   ", p);
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil mengubah Pelanggan", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, Tambah_Pelanggan_Toko_Kasir_.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal mengubah pelanggan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }
}
