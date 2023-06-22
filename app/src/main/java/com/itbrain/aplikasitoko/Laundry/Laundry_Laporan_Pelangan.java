package com.itbrain.aplikasitoko.Laundry;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.kasir.FFunctionKasir;

import java.util.ArrayList;
import java.util.Calendar;

public class Laundry_Laporan_Pelangan extends AppCompatActivity {
    DatabaseLaundry db;
    View v;

    int year, month, day;
    Calendar calendar;
    String tab;
    String dateawal, datesampai, dateFirst, cari = "";
    Integer keuanganOpsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundry_laporan_pelanggan);
        db = new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tab = getIntent().getStringExtra("tab");
        ImageButton i = findViewById(R.id.kembali13);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        getpelanggan("");

        final EditText edtCari = (EditText) findViewById(R.id.edtCariii);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cari = s.toString();
                getpelanggan(cari);
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

    private void getpelanggan(String keyword) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListPelanggan(this, arrayList, true);
        recyclerView.setAdapter(adapter);
        String q = "";
        if (keyword.isEmpty()) {
            q = QueryLaundry.selectwhere("tblpelanggan") + "idpelanggan>0";
        } else {
            q = QueryLaundry.selectwhere("tblpelanggan") + QueryLaundry.sLike("pelanggan", keyword) + " AND idpelanggan>0";
        }
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c) > 0) {
            while (c.moveToNext()) {
                String campur = ModulLaundry.getString(c, "idpelanggan") + "__" +
                        ModulLaundry.getString(c, "pelanggan") + "__" +
                        ModulLaundry.getString(c, "alamat") + "__" +
                        ModulLaundry.getString(c, "notelp");
                arrayList.add(campur);
            }
        }
        if(FFunctionKasir.getCount(c) > 0){
            FFunctionKasir.setText(v,R.id.jumlahdata,"Jumlah Pelanggan : "+FFunctionKasir.intToStr(FFunctionKasir.getCount(c))) ;
            while(c.moveToNext()){
                String campur =  FFunctionKasir.getString(c,"idpelanggan")+"__"+FFunctionKasir.getString(c,"pelanggan") + "__" + FFunctionKasir.getString(c,"alamat")+ "__" + FFunctionKasir.getString(c,"notelp");
                arrayList.add(campur);
            }
        } else {
            FFunctionKasir.setText(v,R.id.jumlahdata,"Jumlah Pelanggan : 0") ;
        }
        adapter.notifyDataSetChanged();
    }
    public void exportExcel(View view) {
        Intent i=new Intent(Laundry_Laporan_Pelangan.this,Export_Exel_Laundry.class);
        i.putExtra("tab","pelanggan");
        startActivity(i);
    }
}
