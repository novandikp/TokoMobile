package com.itbrain.aplikasitoko.TokoKain;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Laporan_Barang_Toko_Kain extends AppCompatActivity{

        DatabaseTokoKain db;
        View v;

        int year, month, day;
        Calendar calendar ;
        String tab, tnjumlahdata;

        TextView etjumlah;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_laporan_barang_kain);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            ImageButton imageButton = findViewById(R.id.kembalicoi);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            try {
                db=new DatabaseTokoKain(this);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                v=this.findViewById(android.R.id.content);


                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                String title="judul";
                tab=getIntent().getStringExtra("tab");
                etjumlah = findViewById(R.id.tvJumlahData);

//        spinner kategori
                final Spinner sp=(Spinner)findViewById(R.id.spKat);
                final String[] kat = {"0"};
                List<String> labels = db.getKategori();
                ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
                data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(data);

                if (tab.equals("kain")){
                    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            kat[0] =db.getIdKategori().get(parent.getSelectedItemPosition());
                            getkain("",kat[0]);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    title="Laporan Kain";
                    getkain("",kat[0]);
                }  else if (tab.equals("pelanggan")) {
                    title="Laporan Pelanggan";
                    getpelanggan("");
                    sp.setVisibility(View.INVISIBLE);
                }
                final EditText edtCari = (EditText)findViewById(R.id.edtCari);
                edtCari.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String keyword = edtCari.getText().toString();
                        if (tab.equals("kain")){
                            getkain(keyword,kat[0]);
                        } else if (tab.equals("pelanggan")) {
                            getpelanggan(keyword);
                        }
                    }
                });

//            KumFunTokoKain.btnBack(title,getSupportActionBar());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void getkain(String keyword,String kategori){
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recListLaporan);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            ArrayList DaftarKain=new ArrayList();
            RecyclerView.Adapter adapter = new AdapterListKain(this,DaftarKain,Boolean.FALSE);
            recyclerView.setAdapter(adapter);
            String q;

            if (TextUtils.isEmpty(keyword)){
                if (kategori.equals("0")){
                    q="SELECT * FROM qkain";
                }else {
                    q="SELECT * FROM qkain WHERE idkategori='"+kategori+"'";
                }
            }else {
                if (kategori.equals("0")){
                    q="SELECT * FROM qkain WHERE kain LIKE '%"+keyword+"%'";
                }else {
                    q="SELECT * FROM qkain WHERE kain LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'";
                }
            }
            Cursor c=db.sq(q);
            if (c.getCount()>0){
                KumFunTokoKain.setText(v,R.id.tvJumlahData,"Jumlah Kain : "+String.valueOf(c.getCount()));
                while(c.moveToNext()){
                    DaftarKain.add(new getterKain(
                            c.getInt(c.getColumnIndex("idkain")),
                            c.getInt(c.getColumnIndex("idkategori")),
                            c.getString(c.getColumnIndex("kategori")),
                            c.getString(c.getColumnIndex("kain")),
                            c.getString(c.getColumnIndex("biaya"))
                    ));
                }
            }else {
                KumFunTokoKain.setText(v,R.id.tvJumlahData,"Jumlah Kain : 0");
            }
            adapter.notifyDataSetChanged();
        }
        private void getpelanggan(String keyword){
            ArrayList DaftarPelanggan = new ArrayList();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recListLaporan);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            RecyclerView.Adapter adapter = new AdapterListPelanggan(this,DaftarPelanggan,Boolean.FALSE);
            recyclerView.setAdapter(adapter);

            String q;
            if (TextUtils.isEmpty(keyword)){
                q="SELECT * FROM tblpelanggan WHERE idpelanggan>0";
            }else {
                q="SELECT * FROM tblpelanggan WHERE namapelanggan LIKE '%"+keyword+"%' AND idpelanggan>0 "+FQueryTokoKain.sOrderASC("pelanggan");
            }

            Cursor c = db.sq(q);
            if (c.getCount()>0){
                etjumlah.setText("Jumlah Pelanggan Terdaftar : "+String.valueOf(c.getCount()));
                while(c.moveToNext()){
                    DaftarPelanggan.add(new getterPelanggan(
                            c.getInt(c.getColumnIndex("idpelanggan")),
                            c.getString(c.getColumnIndex("namapelanggan")),
                            c.getString(c.getColumnIndex("alamatpelanggan")),
                            c.getString(c.getColumnIndex("telppelanggan"))
                    ));
                }
            }else {
                etjumlah.setText("Jumlah Pelanggan Terdaftar : 0");
            }

            adapter.notifyDataSetChanged();

        }


        public void exportExcel(View view) {
            Intent i=new Intent(Laporan_Barang_Toko_Kain.this, ActivityExportExcel_Toko_Kain.class);
            i.putExtra("tab",tab);
            startActivity(i);
        }

    }


