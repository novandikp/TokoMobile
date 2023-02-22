package com.itbrain.aplikasitoko.Salon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class Laporan_Transaksi_Salon_ extends AppCompatActivity {

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
        setContentView(R.layout.laporan_transaksi_salon_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        appbar = (Toolbar) findViewById(R.id.toolbar68);
//        setSupportActionBar(appbar);
//        String title = "judul";

        v = this.findViewById(android.R.id.content);
        config = new ConfigSalon(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseSalon(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
//                if (type.equals("janji")) {
                arrayList.clear();
                loadList2(eCari.getText().toString());
            }
            //}
        });

        List<String> categories = new ArrayList<>();
//        if (type.equals("janji")) {
            //title = ("Laporan Booking");
            loadList2("");
            categories = new ArrayList<String>();
            categories.add("Semua");
            categories.add("Booking");
            categories.add("Langsung");
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
                    loadList2(FunctionSalon.getText(v,R.id.eCari));
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

    public void loadList2(String cari) {
        String item = FunctionSalon.getSpinnerItem(v, R.id.Spinner);
        String tipe = "";
        String q = "";
        if (item.equals("Semua")) {
            q = QuerySalon.selectwhere("qorderdetail") + QuerySalon.sLike("(jasa", cari)+ " OR " +QuerySalon.sLike("pelanggan", cari)+ ") AND " +QuerySalon.sBetween("tglorder", dari, ke) + QuerySalon.sOrderASC("tglorder");
        } else if (item.equals("Booking")) {
            tipe = "1";
            q = QuerySalon.selectwhere("qorderdetail") + QuerySalon.sWhere("tipe", tipe)+ " AND (" +QuerySalon.sLike("jasa", cari)+ " OR " +QuerySalon.sLike("pelanggan", cari)+ ") AND " +QuerySalon.sBetween("tglorder", dari, ke) + QuerySalon.sOrderASC("tglorder");
        } else if (item.equals("Langsung")) {
            tipe = "0";
            q = QuerySalon.selectwhere("qorderdetail") + QuerySalon.sWhere("tipe", tipe)+ " AND (" +QuerySalon.sLike("jasa", cari)+ " OR " +QuerySalon.sLike("pelanggan", cari)+ ") AND " +QuerySalon.sBetween("tglorder", dari, ke) + QuerySalon.sOrderASC("tglorder");
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanTransaksiSalon(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (FunctionSalon.getCount(c) > 0) {
            double jum = 0;
            FunctionSalon.setText(v, R.id.tjumlah, "Jumlah Data : " + FunctionSalon.intToStr(FunctionSalon.getCount(c)));
            while (c.moveToNext()) {
                String faktur = FunctionSalon.getString(c, "faktur");
                String idorderdetail = FunctionSalon.getString(c, "idorderdetail");
                String tglorder = FunctionSalon.getString(c, "tglorder");
                String pelanggan = FunctionSalon.getString(c, "pelanggan");
                String jasa = FunctionSalon.getString(c, "jasa");
                String jumlah = FunctionSalon.getString(c, "jumlah");
                String hargajual = FunctionSalon.getString(c, "hargajual");
                String tot = FunctionSalon.getString(c, "total");

                String campur = faktur + "__" + idorderdetail + "__" + FunctionSalon.dateToNormal(tglorder) + "__" + pelanggan + "__" + jasa + "__" + jumlah + "__" + FunctionSalon.removeE(hargajual) + "__" + FunctionSalon.removeE(tot);
                arrayList.add(campur);
                jum += FunctionSalon.strToDouble(tot);
            }
            FunctionSalon.setText(v, R.id.tValue1, "Total Penjualan :");
            FunctionSalon.setText(v, R.id.tValue2, "Rp. " + FunctionSalon.removeE(jum));
            FunctionSalon.setText(v, R.id.tValue3, "");
            FunctionSalon.setText(v, R.id.tjumlah2, "");
        } else {
            FunctionSalon.setText(v, R.id.tValue1, "Total Penjualan :");
            FunctionSalon.setText(v, R.id.tValue2, "Rp. 0");
            FunctionSalon.setText(v, R.id.tValue3, "");
            FunctionSalon.setText(v, R.id.tjumlah, "Jumlah Data : 0");
            FunctionSalon.setText(v, R.id.tjumlah2, "");
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view){
        Intent i = new Intent(this, ActivityExportExcelSalon.class);
        i.putExtra("type","transaksi") ;
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if(type.equals("transaksi")){
            loadList2("");
        //}
    }

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    public void filter(){
        //if(type.equals("transaksi")){
            loadList2(FunctionSalon.getText(v,R.id.eCari));
        //}
    }

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

class AdapterLaporanTransaksiSalon extends RecyclerView.Adapter<AdapterLaporanTransaksiSalon.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterLaporanTransaksiSalon(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_transaksi_salon, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView faktur, tanggal, nama, item;
        ConstraintLayout print;

        public ViewHolder(View view) {
            super(view);

            faktur = (TextView) view.findViewById(R.id.teFaktur);
            tanggal = (TextView) view.findViewById(R.id.teTanggal);
            nama = (TextView) view.findViewById(R.id.teNama);
            item = (TextView) view.findViewById(R.id.teItem);
            print = (ConstraintLayout) view.findViewById(R.id.wPrinter);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final String[] row = data.get(i).split("__");

        viewHolder.faktur.setText(row[0]);
        viewHolder.tanggal.setText(row[2]);
        viewHolder.nama.setText("Nama : "+row[3]);
        viewHolder.item.setText(row[4]+" : "+row[5]+" x "+row[6]+" = "+row[7]);
        viewHolder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, MenuCetakSalon.class);
                intent.putExtra("faktur", row[0]);
                intent.putExtra("type", "laporan");
                c.startActivity(intent);
            }
        });
    }
}