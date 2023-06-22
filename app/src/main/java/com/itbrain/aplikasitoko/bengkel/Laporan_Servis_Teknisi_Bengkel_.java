package com.itbrain.aplikasitoko.bengkel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Servis_Teknisi_Bengkel_ extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    ArrayList arrayTeknisi = new ArrayList();
    ArrayList arrayId = new ArrayList();
    Database_Bengkel_ db;
    int year,day,month;
    Calendar calendar;
    View v;
    double total=0;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_servis_teknisi_bengkel_);
        ImageButton imageButton = findViewById(R.id.kembali32);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new Database_Bengkel_(this);
        v = this.findViewById(android.R.id.content);
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        String tgl   = ModulBengkel.getDate("dd/MM/yyyy") ;
        ModulBengkel.setText(v,R.id.tglawal6,tgl);
        ModulBengkel.setText(v,R.id.tglakhir6,tgl);
        type = getIntent().getStringExtra("type");
        if(type.equals("servisteknisi")) {
//            getSupportActionBar().setTitle("Laporan Pendapatan Teknisi");
//            ConstraintLayout cr = findViewById(R.id.eCari);
//            cr.setVisibility(View.GONE);
            setText();

            Spinner spinner = (Spinner) findViewById(R.id.sp_lp_pendapatan);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getPendapatanTeknisi();
                    ModulBengkel.setText(v, R.id.tTotal, "Rp." + ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        }
    public void getPendapatanTeknisi() {
        arrayList.clear();
        total=0;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView6) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPendapatan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = whereTeknisi();
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String harga = ModulBengkel.getString(c,"hargajual");
            double pendapatan=100-ModulBengkel.strToDouble(ModulBengkel.getString(c,"stok"));
            pendapatan=pendapatan/100;
            harga=ModulBengkel.doubleToStr(ModulBengkel.strToDouble(harga)*pendapatan);

            total+=ModulBengkel.strToDouble(harga);

            String campur = ModulBengkel.getString(c,"idkategori")+"__"+ModulBengkel.getString(c,"faktur") + "__" + ModulBengkel.getString(c,"pelanggan")+ "__" + ModulBengkel.getString(c,"nopol")+ "__" + ModulBengkel.getString(c,"barang")+"__" + harga+"__" + ModulBengkel.getString(c,"jumlah");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }
    public String whereTeknisi(){
        Spinner sTeknisi = findViewById(R.id.sp_lp_pendapatan);
        String idteknisi = ModulBengkel.intToStr(sTeknisi.getSelectedItemPosition()+1);
        idteknisi=ModulBengkel.selectwhere("qdetailjual")+" idteknisi="+idteknisi+" AND idkategori==1 AND "+ModulBengkel.sBetween("tglorder",ModulBengkel.getText(v,R.id.tglawal6),ModulBengkel.getText(v,R.id.tglakhir6));
        return idteknisi;
    }
    private void setText() {
        arrayTeknisi.clear();
        Spinner spinner = (Spinner) findViewById(R.id.sp_lp_pendapatan) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayTeknisi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(ModulBengkel.select("tblteknisi"));
        if(c.getCount() > 0){
            while(c.moveToNext()){
                arrayTeknisi.add(ModulBengkel.getString(c,"teknisi"));
            }
        }
        adapter.notifyDataSetChanged();
    }
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
            return new DatePickerDialog(this, date, year, month, day);
        }else if(id==2){
            return new DatePickerDialog(this, date1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v,R.id.tglawal6,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
            if (type.equals("servisteknisi")){
                getPendapatanTeknisi();
            }
            ModulBengkel.setText(v,R.id.tTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v,R.id.tglakhir6,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
             if (type.equals("servisteknisi")){
                getPendapatanTeknisi();
            }
            ModulBengkel.setText(v,R.id.tTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.doubleToStr(total)));
        }
    };

    public void tglawal(View view) {
        showDialog(1);
    }

    public void tglakhir(View view) {
        showDialog(2);
    }

    public void export(View view) {
        Intent i= new Intent(Laporan_Servis_Teknisi_Bengkel_.this,MenuExportExcelBengkel.class);
        i.putExtra("type",type);
        startActivity(i);
    }
}
