package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Supplier_Apotek_ extends AppCompatActivity {
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayStat = new ArrayList();
    String type;
    int year, day, month;
    Calendar calendar;
    Spinner spinner;
    ModulApotek config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_supplier_apotek_);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        spinner = findViewById(R.id.setatus);
        config = new ModulApotek(getSharedPreferences("config", this.MODE_PRIVATE));
        setStatus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        type = getIntent().getStringExtra("type");
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getSupplier("");


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final EditText eCari = (EditText) findViewById(R.id.dicari);

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
                getSupplier(a);

            }

        });
    }
    private void setStatus() {
        arrayStat.clear();
        arrayStat.add("Semua");
        arrayStat.add("Tidak Berhutang");
        arrayStat.add("Berhutang");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayStat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }


    public String whereSupplier(String a) {

        int id = spinner.getSelectedItemPosition();
        if (id == 0) {
            return ModulApotek.selectwhere("tblsupplier") + " idsupplier!=1 AND " + ModulApotek.sLike("supplier", a) + " ORDER BY supplier ASC";
        } else if (id == 1) {
            return ModulApotek.selectwhere("tblsupplier") + " idsupplier!=1 AND utang =0  AND " + ModulApotek.sLike("supplier", a) + " ORDER BY supplier ASC";

        } else {
            return ModulApotek.selectwhere("tblsupplier") + " idsupplier!=1 AND utang >0  AND " + ModulApotek.sLike("supplier", a) + " ORDER BY supplier ASC";
        }
    }

        public void getSupplier (String cari){
            arrayList.clear();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recSuppliers);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            RecyclerView.Adapter adapter = new Adapter_Laporan_Pelanggan(this, arrayList);
            recyclerView.setAdapter(adapter);
            String q = whereSupplier(cari);
            Cursor c = db.sq(q);
            while (c.moveToNext()) {
                String campur = "supplier" + "__" + ModulApotek.getString(c, "supplier") + "__" + ModulApotek.getString(c, "alamatsupplier") + "__" + ModulApotek.getString(c, "nosupplier") + "__" + ModulApotek.getString(c, "utang");
                arrayList.add(campur);
            }

            adapter.notifyDataSetChanged();
        }
    public void export(View view) {
        Intent i = new Intent(this, Menu_Export_Exel_Apotek.class);
        i.putExtra("type", "supplier");
        startActivity(i);
    }
    }


