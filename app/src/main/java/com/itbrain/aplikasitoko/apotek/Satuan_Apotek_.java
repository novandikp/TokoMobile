package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Satuan_Apotek_ extends AppCompatActivity implements PemanggilMethod_apotek {
    DatabaseApotek db;
    ArrayList arrayList=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satuan_apotek_);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahSatuan);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Satuan_Apotek_.this, Form_Tambah_Satuan_Apotek_.class);
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
        protected void onResume() {
            getdata("");
            super.onResume();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId()==android.R.id.home){
                finish();
            }
            return super.onOptionsItemSelected(item);
        }



        public void getdata(String cari) {
            RecyclerView recyclerView;
            arrayList.clear();
            recyclerView = findViewById(R.id.recPel);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new Adapter_Satuan_Apotek(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            Cursor c ;
            if(TextUtils.isEmpty(cari)){
                c = db.sq(ModulApotek.select("tblsatuan")+ ModulApotek.sOrderASC("idsatuan"));
            } else {
                c = db.sq(ModulApotek.selectwhere("tblsatuan")+ ModulApotek.sLike("satuanbesar",cari)+" OR "+ ModulApotek.sLike("satuankecil",cari)+ ModulApotek.sOrderASC("idsatuan")) ;
            }

            while(c.moveToNext()){
                String satuanbesar = ModulApotek.getString(c,"satuanbesar") ;
                String satuankecil = ModulApotek.getString(c,"satuankecil") ;
                String nilai = ModulApotek.removeE(ModulApotek.getString(c,"nilai") );
                String idsatuan = ModulApotek.getString(c,"idsatuan") ;
                arrayList.add(idsatuan+"__"+satuanbesar+"__"+satuankecil+"__"+nilai);
            }

            adapter.notifyDataSetChanged();
        }

        public void tambah(View view) {

            Intent i = new Intent(this,Form_Tambah_Satuan_Apotek_.class);
            startActivity(i);

        }
    }

