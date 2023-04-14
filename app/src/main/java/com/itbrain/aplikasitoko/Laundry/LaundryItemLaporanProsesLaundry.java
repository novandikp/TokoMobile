package com.itbrain.aplikasitoko.Laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class LaundryItemLaporanProsesLaundry extends AppCompatActivity {
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
        setContentView(R.layout.laundry_laporan_proses);
        db = new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        ImageButton i = findViewById(R.id.kembali13);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tab = getIntent().getStringExtra("tab");
        final ConstraintLayout cTanggal = (ConstraintLayout) findViewById(R.id.cTanggal);
        final TextView tvPendapatan = (TextView) findViewById(R.id.tvPendapatan2);
//        button tgl
        String qFirstDate = "";
        String kolomTanggal = "";
        qFirstDate = QueryLaundry.select("tbllaundry");
        kolomTanggal = "tgllaundry";
        Cursor firstDate = db.sq(qFirstDate);
        dateawal = ModulLaundry.getDate("yyyyMMdd");
        if (firstDate.getCount() > 0) {
            firstDate.moveToFirst();
            dateawal = ModulLaundry.getString(firstDate, kolomTanggal);
        }
        datesampai = ModulLaundry.getDate("yyyyMMdd");
        ModulLaundry.setText(v, R.id.btnTglAwal, ModulLaundry.dateToNormal(dateawal));
        ModulLaundry.setText(v, R.id.btnTglSampai, ModulLaundry.getDate("dd/MM/yyyy"));
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
        getproseslaundry("", dateawal + "__" + datesampai);
        cTanggal.setVisibility(View.VISIBLE);
        tvPendapatan.setVisibility(View.VISIBLE);
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
                getproseslaundry(cari, dateawal + "__" + datesampai);

            }
        });
    }
    private void tglupdate(){
        String keyword = ModulLaundry.getText(v,R.id.edtCariii);
            getproseslaundry(keyword,dateawal+"__"+datesampai);
    }
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
    private String countJumlah(String value){
        try{
            Cursor c=db.sq(QueryLaundry.sSum("tbllaundrydetail","jumlahlaundry")+" WHERE idlaundry="+value);
            c.moveToFirst();
            return ModulLaundry.getStringFromColumn(c,0);
        }catch (Exception e){
            return "";
        }

    }
    private void getproseslaundry(String keyword,String date){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterLaporanProsesLaundry(this,arrayList);
        recyclerView.setAdapter(adapter);
        String[] tgl=date.split("__");
        String q;
        if (Integer.valueOf(tgl[0])<=Integer.valueOf(tgl[1])){
            if (keyword.isEmpty()){
                q= QueryLaundry.selectwhere("qproses")+ QueryLaundry.sBetween("tgllaundry",tgl[0],tgl[1])+" AND "+ QueryLaundry.sWhere("statuslaundry","Proses");
            }else {
                q= QueryLaundry.selectwhere("qproses")+"(("+ QueryLaundry.sBetween("tgllaundry",tgl[0],tgl[1])+") AND "+ QueryLaundry.sLike("pelanggan",keyword)+") AND "+ QueryLaundry.sWhere("statuslaundry","Proses");
            }
        }else {
            q= QueryLaundry.selectwhere("qproses")+ QueryLaundry.sBetween("tgllaundry",tgl[0],tgl[1])+" AND "+ QueryLaundry.sWhere("statuslaundry","Proses");
            Toast.makeText(this, "Masukkan tanggal dengan benar", Toast.LENGTH_SHORT).show();
        }
        Cursor pendapatan=db.sq(QueryLaundry.sSum("tbllaundry","total")+" WHERE statuslaundry='Proses' AND ("+ QueryLaundry.sBetween("tgllaundry",tgl[0],tgl[1])+")");
        pendapatan.moveToNext();
        Cursor c=db.sq(q);
        if (ModulLaundry.getCount(c)>0){
            ModulLaundry.setText(v,R.id.tvJumlahData2,"Jumlah Pesanan Diproses : "+ ModulLaundry.getCount(c));
            ModulLaundry.setText(v,R.id.tvPendapatan2,"Jumlah Pendapatan : Rp. "+ ModulLaundry.removeE(pendapatan.getString(0)));
            while (c.moveToNext()){
                String campur= ModulLaundry.getString(c,"faktur")+"__"+
                        ModulLaundry.getString(c,"pelanggan")+"__"+
                        ModulLaundry.getString(c,"tgllaundry")+"__"+
                        ModulLaundry.getString(c,"tglselesai")+"__"+
                        countJumlah(ModulLaundry.getString(c,"idlaundry"))+"__"+
                        ModulLaundry.getString(c,"total");
                arrayList.add(campur);
            }
        }else {
            ModulLaundry.setText(v,R.id.tvJumlahData2,"Jumlah Pesanan Diproses : 0");
            ModulLaundry.setText(v,R.id.tvPendapatan2,"Jumlah Pendapatan : Rp. 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void exportExcel(View view) {
        Intent i=new Intent(LaundryItemLaporanProsesLaundry.this,Export_Exel_Laundry_2.class);
        i.putExtra("tab","proseslaundry");
        startActivity(i);
    }
}
class AdapterLaporanProsesLaundry extends RecyclerView.Adapter<AdapterLaporanProsesLaundry.LaporanProsesLaundryViewHolder>{
    private Context ctx;
    private ArrayList<String> data;

    public AdapterLaporanProsesLaundry(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public LaporanProsesLaundryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_proses_laundry,viewGroup,false);
        return new LaporanProsesLaundryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanProsesLaundryViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.faktur.setText(row[0]+" - "+row[1]);
        holder.tanggal.setText(row[2]+" - "+row[3]);
        holder.pesanan.setText("Pesanan dibuat : "+row[4]);
        holder.harga.setText("Total : Rp. "+ ModulLaundry.removeE(row[5]));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LaporanProsesLaundryViewHolder extends RecyclerView.ViewHolder{
        private TextView faktur,tanggal,pesanan,harga;
        public LaporanProsesLaundryViewHolder(@NonNull View itemView) {
            super(itemView);
            faktur=(TextView)itemView.findViewById(R.id.tvFaktur);
            tanggal=(TextView)itemView.findViewById(R.id.tvTanggal);
            pesanan=(TextView)itemView.findViewById(R.id.tvPesanan);
            harga=(TextView)itemView.findViewById(R.id.tvHarga);
        }
    }
}
