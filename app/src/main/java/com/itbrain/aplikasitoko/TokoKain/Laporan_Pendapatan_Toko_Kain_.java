package com.itbrain.aplikasitoko.TokoKain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Pendapatan_Toko_Kain_ extends AppCompatActivity {



    DatabaseTokoKain db;
    View v;

    int year, month, day;
    Calendar calendar ;
    String tab;
    String dateawal,datesampai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_tanggal_kain);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ImageButton imageButton = findViewById(R.id.kembalitod);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            db=new DatabaseTokoKain(this);
            v=this.findViewById(android.R.id.content);

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            String tgl = KumFunTokoKain.getDate("dd/MM/yyyy");
            KumFunTokoKain.setText(v, R.id.btnTglAwal,tgl);
            KumFunTokoKain.setText(v, R.id.btnTglSampai,tgl);


            String title="judul";
            tab=getIntent().getStringExtra("tab");
//        button tgl
            Cursor firstDate=db.sq(FQueryTokoKain.select("tblorder"));
            firstDate.moveToFirst();
            if(firstDate.getCount() == 0){
                dateawal=KumFunTokoKain.getDate("yyyyMMdd");
                KumFunTokoKain.setText(v,R.id.btnTglAwal,KumFunTokoKain.getDate("dd/MM/yyyy"));
            }else{
                dateawal=KumFunTokoKain.getString(firstDate,"tglorder");
                KumFunTokoKain.setText(v,R.id.btnTglAwal,KumFunTokoKain.dateToNormal(dateawal));
            }
            datesampai=KumFunTokoKain.getDate("yyyyMMdd");
            KumFunTokoKain.setText(v,R.id.btnTglSampai,KumFunTokoKain.getDate("dd/MM/yyyy"));

            Button btnTglTerima = (Button)findViewById(R.id.btnTglAwal);
            btnTglTerima.setText(KumFunTokoKain.getDate("dd/MM/yyyy"));
            btnTglTerima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate(1);

                }
            });
            Button btnTglSelesai = (Button)findViewById(R.id.btnTglSampai);
            btnTglSelesai.setText(KumFunTokoKain.getDate("dd/MM/yyyy"));
            btnTglSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate(2);
                }
            });

            if (tab.equals("order")) {
                title="Laporan Order";
                getorder("",dateawal+"__"+datesampai);
            } else if (tab.equals("pendapatan")) {
                title="Laporan Pendapatan";
                getpendapatan("",dateawal+"__"+datesampai);
                Toast.makeText(this, "halo", Toast.LENGTH_SHORT).show();
            }
            final EditText edtCari = (EditText)findViewById(R.id.edtCari);
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
                    if (tab.equals("order")) {
                        getorder(keyword,dateawal+"__"+datesampai);
                    } else if (tab.equals("pendapatan")) {
                        getpendapatan(keyword,dateawal+"__"+datesampai);
                    }
                }
            });

            KumFunTokoKain.btnBack(title,getSupportActionBar());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void tglupdate(){
        EditText etCari;

        etCari = findViewById(R.id.edtCari);
        String keyword = etCari.getText().toString();
        if (tab.equals("order")) {
            getorder(keyword,dateawal+"__"+datesampai);
        } else if (tab.equals("pendapatan")) {
            getpendapatan(keyword,dateawal+"__"+datesampai);
        }
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
            return new DatePickerDialog(this, dTerima, year, month, day);
        }else if (id==2){
            return new DatePickerDialog(this, dSelesai, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            dateawal=KumFunTokoKain.convertDate(KumFunTokoKain.setDatePickerNormal(thn,bln+1,day));
            Button eTgla;
            eTgla = findViewById(R.id.btnTglAwal);
            eTgla.setText(KumFunTokoKain.setDatePickerNormal(thn,bln+1,day)) ;
            tglupdate();
        }
    };
    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            datesampai=KumFunTokoKain.convertDate(KumFunTokoKain.setDatePickerNormal(thn,bln+1,day));
            Button eTglS;
            eTglS = findViewById(R.id.btnTglSampai);
            eTglS.setText(KumFunTokoKain.setDatePickerNormal(thn,bln+1,day)) ;
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
    private void getorder(String keyword,String date){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterLaporanOrder(this,arrayList);
        recyclerView.setAdapter(adapter);
        String[] tgl=date.split("__");
        String q;
        if (Integer.valueOf(tgl[0]) <= Integer.valueOf(tgl[1])){
            if (keyword.isEmpty()){
                q=FQueryTokoKain.selectwhere("qorder")+FQueryTokoKain.sBetween("tglorder",tgl[0],tgl[1]);
            }else {
                q=FQueryTokoKain.selectwhere("qorder")+"("+FQueryTokoKain.sBetween("tglorder",tgl[0],tgl[1])+") AND "+FQueryTokoKain.sLike("namapelanggan",keyword);
            }
        }else {
            q=FQueryTokoKain.selectwhere("qorder")+FQueryTokoKain.sBetween("tglorder",tgl[0],tgl[1]);
            Toast.makeText(this, "Masukkan tanggal dengan benar", Toast.LENGTH_SHORT).show();
        }
        Cursor pendapatan=db.sq(FQueryTokoKain.sSum("qorder","hargaakhir")+" WHERE "+FQueryTokoKain.sBetween("tglorder",tgl[0],tgl[1]));
        pendapatan.moveToNext();
        Cursor c=db.sq(q);
        if (c.getCount()>0){
            KumFunTokoKain.setText(v,R.id.tvJumlahData,"Jumlah Pesanan Diterima : "+c.getCount());
            KumFunTokoKain.setText(v,R.id.tvPendapatan,"Jumlah Pendapatan : Rp. "+KumFunTokoKain.removeE(pendapatan.getString(0)));
            while (c.moveToNext()){
                String campur=KumFunTokoKain.getString(c,"faktur")+"__"+
                        KumFunTokoKain.getString(c,"namapelanggan")+"__"+
                        KumFunTokoKain.getString(c,"tglorder")+"__"+
                        KumFunTokoKain.getString(c,"kategori")+"__"+
                        KumFunTokoKain.getString(c,"kain")+"__"+
                        KumFunTokoKain.getString(c,"biaya")+"__"+
                        KumFunTokoKain.getString(c,"panjang")+"__"+
                        KumFunTokoKain.getString(c,"lebar")+"__"+
                        KumFunTokoKain.getString(c,"hargaakhir")+"__"+
                        KumFunTokoKain.getString(c,"jumlah");
                arrayList.add(campur);
            }
        }else {
            KumFunTokoKain.setText(v,R.id.tvJumlahData,"Jumlah Pesanan Diterima : 0");
            KumFunTokoKain.setText(v,R.id.tvPendapatan,"Jumlah Pendapatan : Rp. 0");
        }
        adapter.notifyDataSetChanged();
    }


    private void getpendapatan(String keyword,String date){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterLaporanPendapatan(this,arrayList);
        recyclerView.setAdapter(adapter);
        String[] tgl=date.split("__");
        String qTunai = "";
        if (Integer.valueOf(tgl[0])<=Integer.valueOf(tgl[1])){
            if (keyword.isEmpty()){
                qTunai=FQueryTokoKain.selectwhere("qbayar")+"("+FQueryTokoKain.sBetween("tglbayar",tgl[0],tgl[1])+") AND bayar>0";
            }else {
                qTunai=FQueryTokoKain.selectwhere("qbayar")+"(("+FQueryTokoKain.sBetween("tglbayar",tgl[0],tgl[1])+") AND "+FQueryTokoKain.sLike("namapelanggan",keyword)+")  AND bayar>0";
            }
        }else {
            qTunai=FQueryTokoKain.selectwhere("qbayar")+"("+FQueryTokoKain.sBetween("tglbayar",tgl[0],tgl[1])+") AND bayar>0";
            Toast.makeText(this, "Masukkan tanggal dengan benar", Toast.LENGTH_SHORT).show();
        }
        Cursor pendapatan1=db.sq(FQueryTokoKain.sSum("tblorder","total")+" WHERE ("+FQueryTokoKain.sBetween("tglbayar",tgl[0],tgl[1])+")");
        pendapatan1.moveToNext();
        Log.d("sqlerror", "getpendapatan: "+qTunai);
        Cursor cTunai=db.sq(qTunai);
        if (cTunai.getCount()>0){
            KumFunTokoKain.setText(v,R.id.tvJumlahData,"Jumlah Data Pendapatan : "+String.valueOf(cTunai.getCount()));
            KumFunTokoKain.setText(v,R.id.tvPendapatan,"Jumlah Pendapatan : Rp. "+KumFunTokoKain.removeE(pendapatan1.getDouble(0)));
            while (cTunai.moveToNext()){
                String campur="bayar"+"__"+
                        KumFunTokoKain.getString(cTunai,"idorder")+"__"+
                        KumFunTokoKain.getString(cTunai,"namapelanggan")+"__"+
                        KumFunTokoKain.getString(cTunai,"total")+"__"+
                        KumFunTokoKain.getString(cTunai,"bayar")+"__"+
                        KumFunTokoKain.getString(cTunai,"kembali")+"__"+
                        KumFunTokoKain.getString(cTunai,"tglbayar");
                arrayList.add(campur);
            }
        }else {
            Toast.makeText(this, "tidak ada", Toast.LENGTH_SHORT).show();
            KumFunTokoKain.setText(v,R.id.tvJumlahData,"Jumlah Data Pendapatan : 0");
            KumFunTokoKain.setText(v,R.id.tvPendapatan,"Jumlah Pendapatan : Rp. 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void exportExcel(View view) {
        String tanggal = dateawal+"__"+datesampai;
        Intent i=new Intent(Laporan_Pendapatan_Toko_Kain_.this, ActivityExportExcel_Toko_Kain.class);
        i.putExtra("tab",tab);
        i.putExtra("tgl",tanggal);
        startActivity(i);
    }

    public void tgl1(View view) {
        showDialog(1);
    }

    public void tgl2(View view) {
        showDialog(2);
    }
}
class AdapterLaporanOrder extends RecyclerView.Adapter<AdapterLaporanOrder.LaporanOrderViewHolder>{
    private Context ctx;
    private ArrayList<String> data;

    public AdapterLaporanOrder(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public LaporanOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_order_kain,viewGroup,false);
        return new LaporanOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanOrderViewHolder holder, int i) {
        final String[] row=data.get(i).split("__");
        holder.faktur.setText(row[0]+" - "+row[1]);
        holder.tanggal.setText(KumFunTokoKain.dateToNormal(row[2]));
        holder.kain.setText(row[3]+" - "+row[4]);
        holder.harga.setText("Rp. "+KumFunTokoKain.removeE(row[5])+" x (Panjang "+row[6].replace(".",",")+" x Lebar " + row[7]+" x Jumlah "+ row[9]+")");
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ctx, Activity_Bayar_Cetak_Laporan_Toko_Kain.class);
                i.putExtra("faktur",row[0]);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LaporanOrderViewHolder extends RecyclerView.ViewHolder{
        private TextView faktur,tanggal,kain,harga;
        private ImageView print;
        public LaporanOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            faktur=(TextView)itemView.findViewById(R.id.tvFaktur);
            tanggal=(TextView)itemView.findViewById(R.id.tvTanggal);
            kain=(TextView)itemView.findViewById(R.id.tvKain);
            harga=(TextView)itemView.findViewById(R.id.tvHarga);
            print=(ImageView)itemView.findViewById(R.id.ibtnPrint);
        }
    }
}
class AdapterLaporanPendapatan extends RecyclerView.Adapter<AdapterLaporanPendapatan.LaporanPendapatanViewHolder> {
    private Context ctx;
    private ArrayList<String> data;

    public AdapterLaporanPendapatan(Context ctx, ArrayList<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public LaporanPendapatanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_pendapatan_kain, viewGroup, false);
        return new LaporanPendapatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanPendapatanViewHolder holder, int i) {
        String[] row = data.get(i).split("__");

        holder.pelanggan.setText(row[2]);
        holder.total.setText("Total : Rp. " + KumFunTokoKain.removeE(row[3]));
        holder.bayar.setText("Bayar : Rp. " + KumFunTokoKain.removeE(row[4]));
        holder.kembali.setText("Kembali : Rp. " + KumFunTokoKain.removeE(row[5]));
        holder.tanggal.setText(KumFunTokoKain.dateToNormal(row[6]));

        holder.tanggal.setText(KumFunTokoKain.dateToNormal(row[6]));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LaporanPendapatanViewHolder extends RecyclerView.ViewHolder {
        TextView pelanggan, total, bayar, kembali, tanggal;

        public LaporanPendapatanViewHolder(@NonNull View itemView) {
            super(itemView);
            pelanggan = (TextView) itemView.findViewById(R.id.tvPelanggan);
            total = (TextView) itemView.findViewById(R.id.tvNominal);
            bayar = (TextView) itemView.findViewById(R.id.tvBayar);
            kembali = (TextView) itemView.findViewById(R.id.tvKembali);
            tanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
        }
    }
}
