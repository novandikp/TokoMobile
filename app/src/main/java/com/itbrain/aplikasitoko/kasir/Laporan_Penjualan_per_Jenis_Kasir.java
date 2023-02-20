package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Laporan_Penjualan_per_Jenis_Kasir extends AppCompatActivity {

    View v;
    FConfigKasir config;
    DatabaseKasir db;
    ArrayList arrayList = new ArrayList();
    String dari, ke, total, type;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_penjualan_per_jenis_kasir);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        nyelect();

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");

        type = "laporanpenjualan";


        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText eCari = (EditText) findViewById(R.id.eCariLaporanPerJenis);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                loadList2(eCari.getText().toString());
            }
        });

        setText();
        List<String> categories = new ArrayList<>();
        loadList2("");
        categories = new ArrayList<String>();
        categories.add("Semua");
        categories.add("Kulakan");
        categories.add("Titipan");

        Spinner spinner = (Spinner) findViewById(R.id.spinnerPerJenis);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                submit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void printAll(View v) {
        String faktur = v.getTag().toString() ;
        Intent i = new Intent(this, ActivityCetak2Kasir.class) ;
        i.putExtra("fakturbayar",faktur) ;
        startActivity(i);
    }

    public void exportLaporanPerJenis(View view){
        Intent i = new Intent(this, ActivityExportExcelKasir.class) ;
        i.putExtra("type",type) ;
        startActivity(i);
    }

    public void nyelect() {
//        Spinner spinner = (Spinner)findViewById(R.id.spinner);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selected = String.valueOf(parent.getItemAtPosition(position));
//                if(selected == "semua"){
//                    loadList4("");
//                }else{
//                    itemnyelect(selected);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    public void setText() {
        dari = FFunctionKasir.setDatePicker(year, month + 1, day);
        ke = FFunctionKasir.setDatePicker(year, month + 1, day);
        String now = FFunctionKasir.setDatePickerNormal(year, month + 1, day);
        FFunctionKasir.setText(v, R.id.eKePerNis, now);
        FFunctionKasir.setText(v, R.id.eDariPerNis, now);
    }

    public void loadList2(String cari) {
        String item = FFunctionKasir.getSpinnerItem(v, R.id.spinnerPerJenis);
        String bayar = "";
        String q = "";
        if (item.equals("Semua")) {
            q = FQueryKasir.selectwhere("qpenjualan") + FQueryKasir.sLike("barang", cari) + " AND " + FQueryKasir.sBetween("tgljual", dari, ke) + FQueryKasir.sOrderASC("tgljual");
        } else if (item.equals("Kulakan")) {
            bayar = "0";
            q = FQueryKasir.selectwhere("qpenjualan") + FQueryKasir.sWhere("titipan", bayar) + " AND " + FQueryKasir.sLike("barang", cari) + " AND " + FQueryKasir.sBetween("tgljual", dari, ke) + FQueryKasir.sOrderASC("tgljual");
        } else if (item.equals("Titipan")) {
            bayar = "1";
            q = FQueryKasir.selectwhere("qpenjualan") + FQueryKasir.sWhere("titipan", bayar) + " AND " + FQueryKasir.sLike("barang", cari) + " AND " + FQueryKasir.sBetween("tgljual", dari, ke) + FQueryKasir.sOrderASC("tgljual");
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvPerJenis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanPenjualan(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (FFunctionKasir.getCount(c) > 0) {
            double jum = 0;
            FFunctionKasir.setText(v, R.id.ba, "Jumlah Data : " + FFunctionKasir.intToStr(FFunctionKasir.getCount(c)));
            while (c.moveToNext()) {
                String pelanggan = FFunctionKasir.getString(c, "pelanggan");
                String kembali = FFunctionKasir.getString(c, "barang");
                String faktur = FFunctionKasir.getString(c, "fakturbayar");
                String jumlah = FFunctionKasir.getString(c, "jumlahjual");
                String harga = FFunctionKasir.getString(c, "hargajual:1");
                double total = FFunctionKasir.strToDouble(harga) * FFunctionKasir.strToDouble(jumlah);

                String campur = faktur + "__" + pelanggan + "__" + kembali + "__" + jumlah + " x " + FFunctionKasir.removeE(harga) + " = " + FFunctionKasir.removeE(total) + "__" + FFunctionKasir.dateToNormal(FFunctionKasir.getString(c, "tgljual"));
                arrayList.add(campur);
                jum += total;
            }
            String a = FFunctionKasir.removeE(jum);
            FFunctionKasir.setText(v, R.id.cot, "Rp. " + a);
        } else {
            FFunctionKasir.setText(v, R.id.cot, "Rp. 0");
            FFunctionKasir.setText(v, R.id.ba, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void eDariPerNis(View view) {
        setDate(1);
    }

    public void eKePerNis(View view) {
        setDate(2);
    }

    public void submit() {
        try {
            loadList2(FFunctionKasir.getText(v, R.id.eCariLaporanPerJenis));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void export(View view) {
//        Intent i = new Intent(this, ActivityExportExcel.class);
//        i.putExtra("type","laporanpenjualan") ;
//        startActivity(i);
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKasir.setText(v, R.id.eDariPerNis, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
            dari = FFunctionKasir.setDatePicker(thn, bln + 1, day);
            submit();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKasir.setText(v, R.id.eKePerNis, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
            ke = FFunctionKasir.setDatePicker(thn, bln + 1, day);
            submit();
        }
    };
    //end date time picker
}