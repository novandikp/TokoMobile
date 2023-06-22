package com.itbrain.aplikasitoko.Laundry;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Keuangan_Laundry extends AppCompatActivity {
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
        setContentView(R.layout.laundryitemlaporankeuangan);
        db = new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        ImageButton i = findViewById(R.id.kembali13);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tab = getIntent().getStringExtra("tab");
        String qFirstDate = "";
        String kolomTanggal = "";
        qFirstDate= QueryLaundry.select("tblkeuangan");
        kolomTanggal="tgltransaksi";
        Cursor firstDate = db.sq(qFirstDate);
        dateawal = ModulLaundry.getDate("yyyyMMdd");
        if (firstDate.getCount() > 0) {
            firstDate.moveToFirst();
            dateawal = ModulLaundry.getString(firstDate, kolomTanggal);
        }
        datesampai = ModulLaundry.getDate("yyyyMMdd");
        ModulLaundry.setText(v, R.id.btnTglAwal, ModulLaundry.dateToNormal(dateawal));
        ModulLaundry.setText(v, R.id.btnTglSampai, ModulLaundry.getDate("dd/MM/yyyy"));
        final ConstraintLayout cTanggal = (ConstraintLayout) findViewById(R.id.cTanggal);
        final TextView tvPendapatan = (TextView) findViewById(R.id.tvPendapatan3);
        final Spinner sp = (Spinner) findViewById(R.id.spinner6);
        String[] opsiKeuangan={"Semua","Pemasukan","Pengeluaran"};
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opsiKeuangan);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(data);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                keuanganOpsi=position;
                getKeuangan(cari,keuanganOpsi,dateawal+"__"+datesampai);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btnTglTerima = (Button) findViewById(R.id.btnTglAwal);
        btnTglTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
        Button btnTglSelesai = (Button) findViewById(R.id.btnTglSampai);
        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
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
                cari = edtCari.getText().toString();
                getKeuangan(cari, keuanganOpsi, dateawal + "__" + datesampai);
            }
        });
    }
    private void tglupdate(){
        String keyword = ModulLaundry.getText(v,R.id.edtCariii);
        getKeuangan(keyword, keuanganOpsi, dateawal + "__" + datesampai);        }
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        }else if (id==2){
            return new DatePickerDialog(this, dSelesai, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            dateawal= ModulLaundry.convertDate(ModulLaundry.setDatePickerNormal(thn,bln+1,day));
            ModulLaundry.setText(v, R.id.btnTglAwal, ModulLaundry.setDatePickerNormal(thn,bln+1,day)) ;
            tglupdate();
        }
    };
    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            datesampai= ModulLaundry.convertDate(ModulLaundry.setDatePickerNormal(thn,bln+1,day));
            ModulLaundry.setText(v, R.id.btnTglSampai, ModulLaundry.setDatePickerNormal(thn,bln+1,day)) ;
            tglupdate();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getKeuangan(String keyword,Integer kategori,String tgl){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListKeuangan(this,arrayList,true);
        recyclerView.setAdapter(adapter);
        String[] tanggal=tgl.split("__");
        String q="";
        if (keyword.isEmpty()){
            if (kategori==0){
                q= QueryLaundry.select("tblkeuangan");
            }else if (kategori==1){
                q= QueryLaundry.selectwhere("tblkeuangan")+"masuk>0 AND ("+ QueryLaundry.sBetween("tgltransaksi",tanggal[0],tanggal[1])+")";
            }else {
                q= QueryLaundry.selectwhere("tblkeuangan")+"keluar>0 AND ("+ QueryLaundry.sBetween("tgltransaksi",tanggal[0],tanggal[1])+")";
            }
        }else {
            if (kategori==0){
                q= QueryLaundry.selectwhere("tblkeuangan")+ QueryLaundry.sLike("faktur",keyword);
            }else if (kategori==1){
                q= QueryLaundry.selectwhere("tblkeuangan")+"masuk>0 AND ("+ QueryLaundry.sBetween("tgltransaksi",tanggal[0],tanggal[1])+") AND "+ QueryLaundry.sLike("faktur",keyword);
            }else {
                q= QueryLaundry.selectwhere("tblkeuangan")+"keluar>0 AND ("+ QueryLaundry.sBetween("tgltransaksi",tanggal[0],tanggal[1])+") AND "+ QueryLaundry.sLike("faktur",keyword);
            }
        }
        Cursor c=db.sq(q);
        Cursor cSaldo=db.sq(QueryLaundry.select("tblkeuangan"));
        cSaldo.moveToLast();
        if (cSaldo.getCount()>0){
            ModulLaundry.setText(v,R.id.tvPendapatan3,"Saldo : "+ ModulLaundry.removeE(ModulLaundry.getString(cSaldo,"saldo")));
        }else {
            ModulLaundry.setText(v,R.id.tvPendapatan3,"Saldo : 0");
        }
        if (ModulLaundry.getCount(c)>0){
            ModulLaundry.setText(v,R.id.jumlahdata,"Jumlah Data : "+String.valueOf(ModulLaundry.getCount(c)));
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"idtransaksi")+"__"+
                        ModulLaundry.getString(c,"tgltransaksi")+"__"+
                        ModulLaundry.getString(c,"faktur")+"__"+
                        ModulLaundry.getString(c,"keterangantransaksi")+"__"+
                        ModulLaundry.getString(c,"masuk")+"__"+
                        ModulLaundry.getString(c,"keluar")+"__"+
                        ModulLaundry.getString(c,"saldo");
                arrayList.add(campur);
            }
        }else {
            ModulLaundry.setText(v,R.id.jumlahdata,"Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }
    public void exportExcel(View view) {
        Intent i=new Intent(this,Export_Exel_Laundry_2.class);
        i.putExtra("tab","keuangan");
        startActivity(i);
    }
}
