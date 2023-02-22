package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Kategori_Apotek_ extends AppCompatActivity implements PemanggilMethod_apotek {
    Context a;
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new  ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategori_apotek_);


        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahKategori);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Kategori_Apotek_.this, Form_Tambah_kategori_Apotek_.class);
                startActivity(intent);
            }
        });
        db = new DatabaseApotek(this);
        getdata("");

        final EditText eCari = (EditText) findViewById(R.id.eCari);
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
                getdata(a);

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

            @Override
            protected void onResume() {
                getdata("");
                super.onResume();
            }


            public void getdata(String cari) {
                RecyclerView recyclerView;
                arrayList.clear();
                recyclerView = findViewById(R.id.rcKategori);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setHasFixedSize(true);
                RecyclerView.Adapter adapter = new Adapter_Kategori_Apotek(this, arrayList);
                recyclerView.setAdapter(adapter);
                Cursor c;
                if (TextUtils.isEmpty(cari)) {
                    c = db.sq(ModulApotek.select("tblkategori") + ModulApotek.sOrderASC("kategori"));
                } else {
                    c = db.sq(ModulApotek.selectwhere("tblkategori") + ModulApotek.sLike("kategori", cari) + ModulApotek.sOrderASC("kategori"));
                }

                while (c.moveToNext()) {
                    String kategori = ModulApotek.getString(c, "kategori");
                    String idkategori = ModulApotek.getString(c, "idkategori");
                    arrayList.add(idkategori + "__" + kategori);
                }

                adapter.notifyDataSetChanged();
            }

            public void tambahKategori(View view) {

                Intent i = new Intent(Kategori_Apotek_.this, Form_Tambah_kategori_Apotek_.class);
                startActivity(i);

            }
        }