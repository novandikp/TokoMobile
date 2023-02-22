package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Form_Tambah_Pelanggan_Toko_Kasir_ extends AppCompatActivity {

    String type;
    String Id;
    FConfigKasir config, temp;
    DatabaseKasir db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayid = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_tambah_pelanggan_toko_kasir_);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        v = this.findViewById(android.R.id.content);


        ImageButton imageButton = findViewById(R.id.KembaliPelanggan);
        Button button = findViewById(R.id.simpanPelanggan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setText();
        //limiter();
    }

    private void setText() {
        arrayKategori.add("Semua");
//      arrayid.add("0");
        //Spinner spinner = (Spinner) findViewById(R.id.inputBarang);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKasir.select("tblkategori"));
        if (FFunctionKasir.getCount(c) > 0) {
            while (c.moveToNext()) {
                arrayList.add(FFunctionKasir.getString(c, "kategori"));
                arrayid.add(FFunctionKasir.getString(c, "idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void limiter() {
        switch (type) {
            case "pelanggan":
                etnamapelanggan = (EditText) findViewById(R.id.namaPelanggan);
                etalamatpelanggan = (EditText) findViewById(R.id.alamatPelanggan);
                ettelppelanggan = (EditText) findViewById(R.id.telpPelanggan);

                tvRpmpelanggan = (TextView) findViewById(R.id.tvNamaPelanggan);
                tvRpmalamat = (TextView) findViewById(R.id.tvAlamatPelanggan);
                tvTelp = (TextView) findViewById(R.id.tvTelpPelanggan);

                textCounterPelanggan();
                break;
        }
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

    public void simpanPelanggan(View view){
        tambahpelanggan();
    }

    private void tambahpelanggan() {
        String nama = FFunctionKasir.getText(v, R.id.namaPelanggan);
        String alamat = FFunctionKasir.getText(v, R.id.alamatPelanggan);
        String telp = FFunctionKasir.getText(v, R.id.telpPelanggan);
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(alamat) && !TextUtils.isEmpty(telp)) {
            String[] p = {nama, alamat, telp};
            String q = FQueryKasir.splitParam("INSERT INTO tblpelanggan (pelanggan,alamat,telp) values(?,?,?)", p);
            if (db.exc(q)) {
                Toast.makeText(this, "Berhasil menambah Pelanggan", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, Tambah_Pelanggan_Toko_Kasir_.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal menambah pelanggan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }
    }
}