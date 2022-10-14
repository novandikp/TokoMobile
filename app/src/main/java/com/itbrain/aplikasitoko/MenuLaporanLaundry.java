package com.itbrain.aplikasitoko;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MenulaporanLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;

    int year, month, day;
    Calendar calendar ;
    String tab;
    String dateawal,datesampai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);
        v=this.findViewById(android.R.id.content);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String title="judul";
        tab=getIntent().getStringExtra("tab");
        final ConstraintLayout konstretTanggal=(ConstraintLayout)findViewById(R.id.konstretTanggal);
        final TextView txtPendapatan=(TextView)findViewById(R.id.txtPendapatan);
        Cursor firstDate=db.sq(Query.select("tbllaundry"));
        dateawal=Modul.getDate("yyyyMMdd");
        if (firstDate.getCount()>0) {
            firstDate.moveToFirst();
            dateawal=Modul.getString(firstDate,"tgllaundry");
        }
        datesampai=Modul.getDate("yyyyMMdd");
        Modul.setText(v,R.id.btnTanggalAwal,Modul.dateToNormal(dateawal));
        Modul.setText(v,R.id.btnTanggalAkhir,Modul.getDate("dd/MM/yyyy"));
        Button btnTglTerima = (Button)findViewById(R.id.btnTanggalAwal);
        btnTglTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
        Button btnTglSelesai = (Button)findViewById(R.id.btnTanggalAkhir);
        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
            }
        });

        if (tab.equals("laundry")) {
            title="Laporan Laundry";
            getlaundry("",dateawal+"__"+datesampai);
            konstretTanggal.setVisibility(View.VISIBLE);
            txtPendapatan.setVisibility(View.VISIBLE);
        }
        final EditText edtCari = (EditText)findViewById(R.id.edtPencarian);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = edtCari.getText().toString();
                if (tab.equals("laundry")) {
                    getproseslaundry(keyword,dateawal+"__"+datesampai);
                }
            }
        });

        Modul.btnBack(title,getSupportActionBar());
    }
    private void tglupdate(){
        String keyword = Modul.getText(v,R.id.edtPencarian);
        if (tab.equals("laundry")) {
            getlaundry(keyword,dateawal+"__"+datesampai);
        }
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
            dateawal=Modul.convertDate(Modul.setDatePickerNormal(thn,bln+1,day));
            Modul.setText(v, R.id.btnTanggalAwal, Modul.setDatePickerNormal(thn,bln+1,day)) ;
            tglupdate();
        }
    };
    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            datesampai=Modul.convertDate(Modul.setDatePickerNormal(thn,bln+1,day));
            Modul.setText(v, R.id.btnTanggalAkhir, Modul.setDatePickerNormal(thn,bln+1,day)) ;
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
            Cursor c=db.sq(Query.sSum("tbllaundrydetail","jumlahlaundry")+" WHERE idlaundry="+value);
            c.moveToFirst();
            return c.getString(0);
        }catch (Exception e){
            return "0";
        }

    }

    private void getlaundry(String keyword,String date){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterLaporanLaundry(this,arrayList);
        recyclerView.setAdapter(adapter);
        String[] tgl=date.split("__");
        String q;
        if (Integer.valueOf(tgl[0])<=Integer.valueOf(tgl[1])){
            if (keyword.isEmpty()){
                q=Query.selectwhere("qlaundry")+Query.sBetween("tgllaundry",tgl[0],tgl[1]);
            }else {
                q=Query.selectwhere("qlaundry")+"("+Query.sBetween("tgllaundry",tgl[0],tgl[1])+") AND "+Query.sLike("pelanggan",keyword);
            }
        }else {
            q=Query.selectwhere("qlaundry")+Query.sBetween("tgllaundry",tgl[0],tgl[1]);
            Toast.makeText(this, "Masukkan tanggal dengan benar", Toast.LENGTH_SHORT).show();
        }
        Cursor pendapatan=db.sq(Query.sSum("qlaundry","biayalaundry")+" WHERE "+Query.sBetween("tgllaundry",tgl[0],tgl[1]));
        pendapatan.moveToNext();
        Cursor c=db.sq(q);
        if (Modul.getCount(c)>0){
            Modul.setText(v,R.id.txtJumlahData,"Jumlah Pesanan Diterima : "+Modul.getCount(c));
            Modul.setText(v,R.id.txtPendapatan,"Jumlah Pendapatan : Rp. "+Modul.removeE(pendapatan.getString(0)));
            while (c.moveToNext()){
                String campur=Modul.getString(c,"faktur")+"__"+
                        Modul.getString(c,"pelanggan")+"__"+
                        Modul.getString(c,"tgllaundry")+"__"+
                        Modul.getString(c,"kategori")+"__"+
                        Modul.getString(c,"jasa")+"__"+
                        Modul.getString(c,"biaya")+"__"+
                        Modul.getString(c,"jumlahlaundry")+"__"+
                        Modul.getString(c,"biayalaundry")+"__"+
                        Modul.getString(c,"satuan");
                arrayList.add(campur);
            }
        }else {
            Modul.setText(v,R.id.txtJumlahData,"Jumlah Pesanan Diterima : 0");
            Modul.setText(v,R.id.txtPendapatan,"Jumlah Pendapatan : Rp. 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void Excel(View view) {
        Intent i=new Intent(MenulaporanLaundry.this,MenuCetaklaundry.class);
        i.putExtra("tab",tab);
        startActivity(i);
    }
}
class AdapterLaporanLaundry extends RecyclerView.Adapter<AdapterLaporanLaundry.LaporanLaundryViewHolder>{
    private Context ctx;
    private ArrayList<String> data;

    public AdapterLaporanLaundry(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public LaporanLaundryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_laundry,viewGroup,false);
        return new LaporanLaundryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanLaundryViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.faktur.setText(row[0]+" - "+row[1]);
        holder.tanggal.setText(Modul.dateToNormal(row[2]));
        holder.jasa.setText(row[3]+" - "+row[4]);
        String satuan="";
        if(row[8].equals("pc")){
            satuan="Pcs";
        }else if (row[8].equals("kg")){
            satuan="Kg";
        }else if (row[8].equals("m2")){
            satuan="M²";
        }
        holder.harga.setText("Rp. "+Modul.removeE(row[5])+" x "+row[6].replace(".",",")+" "+satuan+" = "+Modul.removeE(row[7]));
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ctx,ActivityTransaksiCetak.class);
                i.putExtra("faktur",row[0]);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LaporanLaundryViewHolder extends RecyclerView.ViewHolder{
        private TextView faktur,tanggal,jasa,harga;
        private ImageView print;
        public LaporanLaundryViewHolder(@NonNull View itemView) {
            super(itemView);
            faktur=(TextView)itemView.findViewById(R.id.tvFaktur);
            tanggal=(TextView)itemView.findViewById(R.id.tvTanggal);
            jasa=(TextView)itemView.findViewById(R.id.tvJasa);
            harga=(TextView)itemView.findViewById(R.id.tvHarga);
            print=(ImageView)itemView.findViewById(R.id.ibtnPrint);
        }
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
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_proses_laundry,viewGroup,false);
        return new LaporanProsesLaundryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanProsesLaundryViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.faktur.setText(row[0]+" - "+row[1]);
        holder.tanggal.setText(row[2]+" - "+row[3]);
        holder.pesanan.setText("Pesanan dibuat : "+row[4]);
        holder.harga.setText("Total : Rp. "+Modul.removeE(row[5]));
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
class AdapterLaporanPendapatan extends RecyclerView.Adapter<AdapterLaporanPendapatan.LaporanPendapatanViewHolder>{
    private Context ctx;
    private ArrayList<String> data;

    public AdapterLaporanPendapatan(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public LaporanPendapatanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_pendapatan,viewGroup,false);
        return new LaporanPendapatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanPendapatanViewHolder holder, int i) {
        String[] row=data.get(i).split("__");
        if (row[0].equals("bayar")){
            holder.pelanggan.setText(row[7]+" - "+row[2]);
            holder.total.setText("Total : Rp. "+Modul.removeE(row[3]));
            holder.bayar.setText("Bayar : Rp. "+Modul.removeE(row[4]));
            if (row[7].equals("Tunai")){
                holder.kembali.setText("Kembali : Rp. "+Modul.removeE(row[5]));
            }else {
                holder.kembali.setText("Kurang : Rp. "+Modul.removeE(row[5]));
            }
            holder.tanggal.setText(Modul.dateToNormal(row[6]));
        }else if (row[0].equals("bayarhutang")){
            holder.pelanggan.setText("Bayar Hutang - "+row[2]);
            holder.total.setText("Total : Rp. "+Modul.removeE(row[3]));
            holder.bayar.setText("Bayar : Rp. "+Modul.removeE(row[4]));
            if (Double.valueOf(Modul.unNumberFormat(Modul.removeE(row[5])))<0){
                holder.kembali.setText("Kurang : Rp. "+Modul.removeE(row[5]));
            }else {
                holder.kembali.setText("Kembali : Rp. "+Modul.removeE(row[5]));
            }
            holder.tanggal.setText(Modul.dateToNormal(row[6]));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LaporanPendapatanViewHolder extends RecyclerView.ViewHolder{
        TextView pelanggan,total,bayar,kembali,tanggal;
        public LaporanPendapatanViewHolder(@NonNull View itemView) {
            super(itemView);
            pelanggan=(TextView)itemView.findViewById(R.id.tvPelanggan);
            total=(TextView)itemView.findViewById(R.id.tvNominal);
            bayar=(TextView)itemView.findViewById(R.id.tvBayar);
            kembali=(TextView)itemView.findViewById(R.id.tvKembali);
            tanggal=(TextView)itemView.findViewById(R.id.tvTanggal);
        }
    }
}
class AdapterLaporanHutang extends RecyclerView.Adapter<AdapterLaporanHutang.LaporanHutangViewHolder>{
    private Context ctx;
    private ArrayList<String> data;

    public AdapterLaporanHutang(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public LaporanHutangViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_hutang,viewGroup,false);
        return new LaporanHutangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanHutangViewHolder holder, int i) {
        String[] row=data.get(i).split("__");
        holder.pelanggan.setText(row[1]);
        holder.alamat.setText(row[2]);
        holder.notelp.setText(row[3]);
        holder.hutang.setText("Rp. "+Modul.removeE(row[4]));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LaporanHutangViewHolder extends RecyclerView.ViewHolder{
        TextView pelanggan,alamat,notelp,hutang;
        public LaporanHutangViewHolder(@NonNull View itemView) {
            super(itemView);
            pelanggan=(TextView)itemView.findViewById(R.id.tvPelanggan);
            alamat=(TextView)itemView.findViewById(R.id.tvAlamat);
            notelp=(TextView)itemView.findViewById(R.id.tvNoTelp);
            hutang=(TextView)itemView.findViewById(R.id.tvHutang);
        }
    }
}