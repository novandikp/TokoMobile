package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Cari_Pelangan_Apotek extends AppCompatActivity {
    ModulApotek config,temp;
    DatabaseApotek db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cari_pelanggan_apotek);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahPelanggan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cari_Pelangan_Apotek.this, Form_Tambah_Pelangan_Apotek.class);
                startActivity(intent);
            }
        });
        db=new DatabaseApotek(this);
        v=this.findViewById(android.R.id.content);
        config=new ModulApotek(getSharedPreferences("config",this.MODE_PRIVATE));
        getdata(" ");
        final EditText eCari = (EditText) findViewById(R.id.eCari) ;
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString() ;
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
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcPelanggan) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Adapter_Pelanggan_Apotek(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("tblpelanggan") +"idpelanggan!=1"+" AND "+ ModulApotek.sLike("pelanggan",cari) + " ORDER BY pelanggan ASC";
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulApotek.getString(c,"idpelanggan")+"__"+ ModulApotek.getString(c,"pelanggan") + "__" + ModulApotek.getString(c,"alamat")+ "__" + ModulApotek.getString(c,"notelp");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }
    public void tambah(View view) {
        Intent i = new Intent(Cari_Pelangan_Apotek.this,Form_Tambah_Pelangan_Apotek.class);
        startActivity(i);
    }
}
