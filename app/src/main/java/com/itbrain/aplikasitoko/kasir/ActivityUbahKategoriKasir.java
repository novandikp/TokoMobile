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

public class ActivityUbahKategoriKasir extends AppCompatActivity {

    FConfigKasir config, temp;
    DatabaseKasir db;
    View v;
    String id;
    String type;
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ubah);

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

                setContentView(R.layout.form_ubah_kategori_kasir_);
                //setTitle("Ubah Kategori");
                // Text Counter
                //limiter();
                setKategori();


        ImageButton imageButton = findViewById(R.id.KembaliUbahKate);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void limiter() {
        etkategori = (EditText) findViewById(R.id.tUbahKategorih);
        tvRpmkategorii = (TextView) findViewById(R.id.tvRpmkategori);
        textCounterKategori();
    }

    void textCounterKategori() {
        etkategori.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvRpmkategorii.setText(etkategori.length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setKategori() {
        Cursor c = db.sq("SELECT * FROM tblkategori WHERE idkategori= " + id);
        if (FFunctionKasir.getCount(c) > 0) {
            c.moveToNext();
            FFunctionKasir.setText(v, R.id.tUbahKategorih, FFunctionKasir.getString(c, "kategori"));
        }
    }

    public void Wubah2(View view) {
        tambahkategori();
    }

    private void tambahkategori() {
        if (!TextUtils.isEmpty(FFunctionKasir.getText(v, R.id.tUbahKategorih))) {
            if (db.exc(FQueryKasir.splitParam("UPDATE tblkategori SET kategori=? WHERE idkategori=?   ", new String[]{FFunctionKasir.getText(v, R.id.tUbahKategorih), id}))) {
                Toast.makeText(this, "Berhasil mengubah Kategori" , Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, Form_Tambah_Kategori_Kasir_.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal mengubah Kategori", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }



//    private void tambahkategori() {
//        String kategori = FFunctionKasir.getText(v, R.id.tUbahKategorih);
//        if (!TextUtils.isEmpty(kategori)) {
//            String[] p = {kategori, id};
//            String q = FQueryKasir.splitParam("UPDATE tblkategori SET kategori=? WHERE idkategori=?   ", p);
//            Toast.makeText(this, q, Toast.LENGTH_SHORT).show();
//            if (db.exc(q)) {
//                Toast.makeText(this, "Berhasil mengubah Pelanggan", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(this, Form_Tambah_Kategori_Kasir_.class);
//                i.putExtra("type", type);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//            } else {
//                Toast.makeText(this, "Gagal mengubah pelanggan", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
//        }
//    }
}