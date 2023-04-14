package com.itbrain.aplikasitoko.Laundry;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterJasaLaundry;
import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterPegawaiLaundry;
import com.itbrain.aplikasitoko.Laundry.dialog.DialogMasterPelangganLaundry;

import java.util.ArrayList;
import java.util.List;

public class ActivityTransaksiCari_Laundry extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseLaundry db;
    Spinner spinner;
    String keyword="",kategori="";
    String a;
    List<String> idKat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_cari_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);

        a=getIntent().getStringExtra("cari");
        if (a.equals("pegawai")){
            ModulLaundry.btnBack("Cari Pegawai",getSupportActionBar());
            getPegawai("");
        }else if (a.equals("pelanggan")){
            ModulLaundry.btnBack("Cari Pelanggan",getSupportActionBar());
            getPelanggan("");
        }else if (a.equals("jasa")){
            ModulLaundry.btnBack("Cari Jasa",getSupportActionBar());
            spinner = (Spinner)findViewById(R.id.spKatCari);
            getKategoriData();
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kategori=db.getIdKategori().get(parent.getSelectedItemPosition());
                    getJasa(keyword,kategori);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            getJasa(keyword,kategori);
        }

        final EditText edtCari = (EditText) findViewById(R.id.edtCari);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword=edtCari.getText().toString();
                if (a.equals("pegawai")){
                    getPegawai(keyword);
                }else if (a.equals("pelanggan")){
                    getPelanggan(keyword);
                }else if (a.equals("jasa")){
                    getJasa(keyword,kategori);
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
    private void getKategoriData(){
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,db.getKategori());
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(data);
    }
    //--------------------------------------------------------------------------------------------------------//
    public void getPegawai(String keyword){
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListPegawaiCari(this,arrayList);
        recyclerView.setAdapter(adapter);
        String q;
        if (keyword.isEmpty()){
            q="SELECT * FROM tblpegawai";
        }else {
            q="SELECT * FROM tblpegawai WHERE pegawai LIKE '%"+keyword+"%'"+ QueryLaundry.sOrderASC("pegawai");
        }
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idpegawai")+"__"+
                        ModulLaundry.getString(c,"pegawai")+"__"+
                        ModulLaundry.getString(c,"alamatpegawai")+"__"+
                        ModulLaundry.getString(c,"notelppegawai");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------------------------------------//
    public void getPelanggan(String keyword){
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListPelangganCari(this,arrayList);
        recyclerView.setAdapter(adapter);
        String q;
        if (keyword.isEmpty()){
            q="SELECT * FROM tblpelanggan";
        }else {
            q="SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%'"+ QueryLaundry.sOrderASC("pelanggan");
        }
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idpelanggan")+"__"+
                        ModulLaundry.getString(c,"pelanggan")+"__"+
                        ModulLaundry.getString(c,"alamat")+"__"+
                        ModulLaundry.getString(c,"notelp");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------------------------------------//
    public  void getJasa(String keyword,String kategori){
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListJasaCari(this,arrayList);
        recyclerView.setAdapter(adapter);
        String q;

        if (keyword.isEmpty()){
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa";
            }else {
                q="SELECT * FROM qjasa WHERE idkategori='"+kategori+"'";
            }
        }else {
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%'";
            }else {
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'";
            }
        }
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idjasa")+"__"+
                        ModulLaundry.getString(c,"idkategori")+"__"+
                        ModulLaundry.getString(c,"kategori")+"__"+
                        ModulLaundry.getString(c,"jasa")+"__"+
                        ModulLaundry.getString(c,"biaya")+"__"+
                        ModulLaundry.getString(c,"satuan");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambahdata(View view) {
        if (a.equals("pegawai")){
            DialogMasterPegawaiLaundry dialogMasterPegawaiLaundry =new DialogMasterPegawaiLaundry(this,true,null,false);
        }else if (a.equals("pelanggan")){
            DialogMasterPelangganLaundry dialogMasterPelangganLaundry =new DialogMasterPelangganLaundry(this, true, null,false);
        }else if (a.equals("jasa")){
            DialogMasterJasaLaundry dialogMasterJasaLaundry =new DialogMasterJasaLaundry(this,true,null,false);
        }
    }
    public void getList(){
        if (a.equals("pegawai")){
            getPegawai(keyword);
        }else if (a.equals("pelanggan")){
            getPelanggan(keyword);
        }else if (a.equals("jasa")) {
            getKategoriData();
            getJasa(keyword,kategori);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (a.equals("pegawai")){
            getPegawai(keyword);
        }else if (a.equals("pelanggan")){
            getPelanggan(keyword);
        }else if (a.equals("jasa")) {
            getKategoriData();
            getJasa(keyword,kategori);
        }
    }
}


