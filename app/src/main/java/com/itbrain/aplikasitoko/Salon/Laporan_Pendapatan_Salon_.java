package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Laporan_Pendapatan_Salon_ extends AppCompatActivity {

    Toolbar appbar;
    View v;
    ConfigSalon config;
    DatabaseSalon db;
    ArrayList arrayList = new ArrayList();
    String dari="", ke="", type;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_pendapatan_salon_);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        appbar = (Toolbar) findViewById(R.id.toolbar68);

        v = this.findViewById(android.R.id.content);
        config = new ConfigSalon(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseSalon(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");

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
//                if (type.equals("pendapatan")) {
                arrayList.clear();
                loadList(eCari.getText().toString());
            }
            //}
        });

        List<String> categories = new ArrayList<>();
        //if (type.equals("pendapatan")) {
            v.findViewById(R.id.Spinner).setVisibility(View.GONE);
            //title = ("Laporan Pendapatan");
            loadList("");
            categories = new ArrayList<String>();
            categories.add("Semua");
        //}

        Spinner spinner = (Spinner) findViewById(R.id.Spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        //FunctionSalon.btnBack(title, getSupportActionBar());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 //if(type.equals("transaksi")){
                    loadList(FunctionSalon.getText(v,R.id.eCari));
                }
            //}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        setText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setText(){
        dari = FunctionSalon.setDatePicker(year,month+1,day) ;
        ke = FunctionSalon.setDatePicker(year,month+1,day) ;
        String now = FunctionSalon.setDatePickerNormal(year,month+1,day) ;
        FunctionSalon.setText(v,R.id.eKe,now) ;
        FunctionSalon.setText(v,R.id.eDari,now) ;
    }

    public void loadList(String cari) {
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = QuerySalon.selectwhere("qorder") + "total>0 AND " + QuerySalon.sBetween("tglorder", dari, ke) + " LIMIT 30";
        } else {
            q = QuerySalon.selectwhere("qorder") + "total>0 AND " + QuerySalon.sLike("pelanggan", cari) + " AND " + QuerySalon.sBetween("tglorder", dari, ke);
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanPendapatanSalon(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (FunctionSalon.getCount(c) > 0) {
            double total = 0;
            double back = 0;
            double pay = 0;
            while (c.moveToNext()) {
                String faktur = FunctionSalon.getString(c, "faktur");
                String tgl = FunctionSalon.getString(c, "tglorder");
                String nama = FunctionSalon.getString(c, "pelanggan");
                String tot = FunctionSalon.getString(c, "total");
                String bayar = FunctionSalon.getString(c, "bayar");
                String kembali = FunctionSalon.getString(c, "kembali");

                String campur = faktur+"__"+FunctionSalon.dateToNormal(tgl)+"__"+nama+"__"+FunctionSalon.removeE(tot)+"__"+FunctionSalon.removeE(bayar)+"__"+FunctionSalon.removeE(kembali);

                arrayList.add(campur);
                total += FunctionSalon.strToDouble(tot);
                back += FunctionSalon.strToDouble(kembali);
                pay += FunctionSalon.strToDouble(bayar);
            }
            FunctionSalon.setText(v, R.id.tValue1, "Pendapatan : Rp." + FunctionSalon.removeE(total));
            FunctionSalon.setText(v, R.id.tValue2, "Pembayaran : Rp." + FunctionSalon.removeE(pay));
            FunctionSalon.setText(v, R.id.tValue3, "Kembali : Rp." + FunctionSalon.removeE(back));
            FunctionSalon.setText(v, R.id.tjumlah, "Jumlah Data : " + String.valueOf(FunctionSalon.getCount(c)));
            FunctionSalon.setText(v, R.id.tjumlah2, "");
        } else {
            FunctionSalon.setText(v, R.id.tValue1, "Pendapatan : Rp. 0");
            FunctionSalon.setText(v, R.id.tValue2, "Kembali    : Rp. 0");
            FunctionSalon.setText(v, R.id.tValue3, "Pembayaran : Rp. 0");
            FunctionSalon.setText(v, R.id.tjumlah, "Jumlah Data : 0");
            FunctionSalon.setText(v, R.id.tjumlah2, "");
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view){
        Intent i = new Intent(this, ActivityExportExcelSalon.class);
        i.putExtra("type","pendapatan") ;
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if(type.equals("pendapatan")){
            loadList("");
        }
    //}

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    public void filter(){
        //if(type.equals("pendapatan")){
            loadList(FunctionSalon.getText(v,R.id.eCari));
        }
    //}

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if(id == 2){
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FunctionSalon.setText(v, R.id.eDari, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
            dari = FunctionSalon.setDatePicker(thn,bln+1,day) ;
            filter();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FunctionSalon.setText(v, R.id.eKe, FunctionSalon.setDatePickerNormal(thn,bln+1,day)) ;
            ke = FunctionSalon.setDatePicker(thn,bln+1,day) ;
            filter();
        }
    };
    //end date time picker
}

class AdapterLaporanPendapatanSalon extends RecyclerView.Adapter<AdapterLaporanPendapatanSalon.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLaporanPendapatanSalon(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_pendapatan_salon, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur, tanggal, nama, pendapatan;

        public ViewHolder(View view) {
            super(view);

            faktur = (TextView) view.findViewById(R.id.tFaktur);
            tanggal = (TextView) view.findViewById(R.id.tTanggal);
            nama = (TextView) view.findViewById(R.id.tNama);
            pendapatan = (TextView) view.findViewById(R.id.tPendapatan);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.faktur.setText(row[0]);
        viewHolder.tanggal.setText(row[1]);
        viewHolder.nama.setText("Nama : "+row[2]);
        viewHolder.pendapatan.setText("Total : "+row[3]+"\n"+"Bayar : "+row[4]+"\n"+"Kembali : "+row[5]);
    }
}