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

import com.itbrain.aplikasitoko.Form_Tambah_Supplier;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Cari_Supplier_Apotek extends AppCompatActivity implements PemanggilMethod_apotek {
    ModulApotek config,temp;
    DatabaseApotek db ;
    View v ;
    ArrayList arrayList = new ArrayList() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cari_supplier_apotek);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahSupplier);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cari_Supplier_Apotek.this, Form_Tambah_Supplier.class);
                startActivity(intent);
            }
        });
            config = new ModulApotek(getSharedPreferences("config",this.MODE_PRIVATE));
            temp = new ModulApotek(getSharedPreferences("temp",this.MODE_PRIVATE));
            db = new DatabaseApotek(this) ;
            v = this.findViewById(android.R.id.content);

            getdata("");
            final EditText eCari = (EditText) findViewById(R.id.etCari) ;
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
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcSupp) ;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new Adapter_Supplier_Apotek(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            String q = ModulApotek.selectwhere("tblsupplier") +"idsupplier!=1"+" AND " + ModulApotek.sLike("supplier",cari) + " ORDER BY supplier ASC";
            Cursor c = db.sq(q) ;
            while(c.moveToNext()){
                String campur = ModulApotek.getString(c,"idsupplier")+"__"+ ModulApotek.getString(c,"supplier") + "__" + ModulApotek.getString(c,"alamatsupplier")+ "__" + ModulApotek.getString(c,"nosupplier");
                arrayList.add(campur);
            }

            adapter.notifyDataSetChanged();
        }




        public void tambah(View view) {
            Intent i = new Intent(this,Form_Tambah_Supplier.class);
            startActivity(i);
        }
    }
