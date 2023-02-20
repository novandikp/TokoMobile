package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Tambah_Kategori_Kasir extends AppCompatActivity {

    String type;
    String Id;
    FConfigKasir configKasir, temp;
    DatabaseKasir db;
    View v;
    EditText etkategori, etnamabarang, ethargabeli, ethargajual, etsbarang, etnamapelanggan, etalamatpelanggan, ettelppelanggan;
    TextView tvRpmkategorii, tvRpmnamaBarang, tvRpmBeli, tvRpmJual, tvRpmStok, tvRpmpelanggan, tvRpmalamat, tvTelp;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayKategori = new ArrayList();
    ArrayList arrayId = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configKasir = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        temp = new FConfigKasir(getSharedPreferences("temp", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, configKasir);
        v = this.findViewById(android.R.id.content);

        setContentView(R.layout.form_tambah_kategori_kasir_);

        setText();

        ImageButton imageButton = findViewById(R.id.Kembali);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        try {
            type = getIntent().getStringExtra("type");
        } catch (Exception e) {
            type = "";
            limiter();
            finish();
        }
    }

    private void setText() {
        arrayKategori.add("Semua");
        //arrayId.add("0");
        //Spinner spinner = (Spinner) findViewById(R.id.wpencarian);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        Cursor c = db.sq(FQueryKasir.select("tblkategori"));
        if (FFunctionKasir.getCount(c) > 0) {
            while (c.moveToNext()) {
                arrayKategori.add(FFunctionKasir.getString(c, "kategori"));
                arrayId.add(FFunctionKasir.getString(c, "idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void limiter() {
        switch (type) {
            case "kategori":
                etkategori = (EditText) findViewById(R.id.tUbahKategorih);
                tvRpmkategorii = (TextView) findViewById(R.id.tvRpmkategori);
                textCounterKategori();
                break;
        }
    }

    void textCounterKategori(){
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

    public void Wubah2(View view) {
        simpan2();
    }


    public void kategori(View v) {
        String idkat = v.getTag().toString();
        Toast.makeText(this, idkat, Toast.LENGTH_SHORT).show();
    }

    private void simpan2() {
        if (!TextUtils.isEmpty(FFunctionKasir.getText(v, R.id.tKategorih))) {
            if (db.exc(FQueryKasir.splitParam("INSERT INTO tblkategori (kategori) values(?)", new String[]{FFunctionKasir.getText(v, R.id.tKategorih)}))) {
                Toast.makeText(this, "Berhasil menambah Kategori", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(this, Form_Tambah_Kategori_Kasir_.class);
                i.putExtra("type", type);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Toast.makeText(this, "Gagal menambah Kategori", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mohon Masukkan dengan Benar", Toast.LENGTH_SHORT).show();
        }

    }

}

