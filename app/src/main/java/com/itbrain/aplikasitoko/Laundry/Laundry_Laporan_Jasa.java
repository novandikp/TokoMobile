package com.itbrain.aplikasitoko.Laundry;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Laundry_Laporan_Jasa extends AppCompatActivity {
    DatabaseLaundry db;
    View v;

    int year, month, day;
    Calendar calendar;
    String tab;
    String dateawal, datesampai, dateFirst, cari = "";
    Integer keuanganOpsi;
    String[] kat = {"0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemlaporanjasa);
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

        final Spinner sp = (Spinner) findViewById(R.id.spinner6);
        final String[] kat = {"0"};

        getjasa("", kat[0]);
        List<String> labels = db.getKategori();
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(data);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kat[0] = db.getIdKategori().get(parent.getSelectedItemPosition());
                getjasa("", kat[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                getjasa(cari, kat[0]);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getjasa("", kat[0]);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getjasa(String keyword, String idkategori) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListJasa(this, arrayList, true);
        recyclerView.setAdapter(adapter);
        String q = "";
        if (keyword.isEmpty()) {
            if (idkategori.equals("0")) {
                q = QueryLaundry.select("qjasa");
            } else {
                q = QueryLaundry.selectwhere("qjasa") + QueryLaundry.sWhere("idkategori", idkategori);
            }
        } else {
            if (idkategori.equals("0")) {
                q = QueryLaundry.selectwhere("qjasa") + QueryLaundry.sLike("jasa", keyword);
            } else {
                q = QueryLaundry.selectwhere("qjasa") + QueryLaundry.sLike("jasa", keyword) + " AND " + QueryLaundry.sWhere("idkategori", idkategori);
            }
    }
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c) > 0) {
            while (c.moveToNext()) {
                ModulLaundry.setText(v, R.id.jumlahdata, "Jumlah Jasa : " + ModulLaundry.intToStr(ModulLaundry.getCount(c)));
                String campur = ModulLaundry.getString(c, "idjasa") + "__" +
                        ModulLaundry.getString(c, "idkategori") + "__" +
                        ModulLaundry.getString(c, "kategori") + "__" +
                        ModulLaundry.getString(c, "jasa") + "__" +
                        ModulLaundry.getString(c, "biaya") + "__" +
                        ModulLaundry.getString(c, "satuan");
                arrayList.add(campur);
            }
        } else {
            ModulLaundry.setText(v, R.id.jumlahdata, "Jumlah Jasa : 0");
        }
        adapter.notifyDataSetChanged();
    }
    public void exportExcel(View view) {
        Intent i=new Intent(Laundry_Laporan_Jasa.this,Export_Exel_Laundry.class);
        i.putExtra("tab","jasa");
        startActivity(i);
    }

}
