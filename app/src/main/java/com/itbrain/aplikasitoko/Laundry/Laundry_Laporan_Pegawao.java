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

import java.util.ArrayList;
import java.util.Calendar;

public class Laundry_Laporan_Pegawao extends AppCompatActivity {
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
        setContentView(R.layout.laundry_laporan_pegawao);
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



        getpegawai("");

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
              getpegawai(cari);
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

    private void getpegawai(String keyword) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListPegawai(this, arrayList, true);
        recyclerView.setAdapter(adapter);
        String q = "";
        if (keyword.isEmpty()) {
            q = QueryLaundry.selectwhere("tblpegawai") + "idpegawai>0";
        } else {
            q = QueryLaundry.selectwhere("tblpegawai") + QueryLaundry.sLike("pegawai", keyword) + " AND idpegawai>0";
        }
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c) > 0) {
            while (c.moveToNext()) {
                ModulLaundry.setText(v,R.id.jumlahdata,"Jumlah Pegawai : "+ ModulLaundry.intToStr(ModulLaundry.getCount(c))) ;

                String campur = ModulLaundry.getString(c, "idpegawai") + "__" +
                        ModulLaundry.getString(c, "pegawai") + "__" +
                        ModulLaundry.getString(c, "alamatpegawai") + "__" +
                        ModulLaundry.getString(c, "notelppegawai");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void exportExcel(View view) {
        Intent i=new Intent(Laundry_Laporan_Pegawao.this,Export_Exel_Laundry.class);
        i.putExtra("tab","pegawai");
        startActivity(i);
    }
}
