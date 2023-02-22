package com.itbrain.aplikasitoko.TokoKain;

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_Penjualan_Cari_Toko_Kain extends AppCompatActivity {

    RecyclerView recyclerView;
    List<getterPelanggan> DaftarPelanggan;
    List<getterKain> DaftarKain;
    DatabaseTokoKain db;
    AdapterListPelangganCari adapterPelanggan;
    AdapterListKainCari adapterKain;
    Spinner spinner;
    String keyword="",kategori="";
    String a;
    List<String> idKat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_cari_kain);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {
            db=new DatabaseTokoKain(this);

            a=getIntent().getStringExtra("cari");
            if (a.equals("pelanggan")){
//                KumFunTokoKain.btnBack("Cari Pelanggan",getSupportActionBar());
                getPelanggan("");
            }else if (a.equals("kain")){
//                KumFunTokoKain.btnBack("Cari kain",getSupportActionBar());
                spinner = (Spinner)findViewById(R.id.spKatCari);
                getKategoriData();
                spinner.setVisibility(View.VISIBLE);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        kategori=db.getIdKategori().get(parent.getSelectedItemPosition());
                        getKain(keyword,kategori);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                getKain(keyword,kategori);
            }
            final EditText edtCari = (EditText) findViewById(R.id.edtCari);
            edtCari.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    keyword=edtCari.getText().toString();
                    if (a.equals("pelanggan")){
                        getPelanggan(keyword);
                    }else if (a.equals("kain")){
                        getKain(keyword,kategori);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getKategoriData(){
        DatabaseTokoKain db = new DatabaseTokoKain(this);
        List<String> labels = db.getKategori();

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(data);
    }

    public void getPelanggan(String keyword){
        DaftarPelanggan = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterPelanggan = new AdapterListPelangganCari(this,DaftarPelanggan);
        recyclerView.setAdapter(adapterPelanggan);

        String q;
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpelanggan";
        }else {
            q="SELECT * FROM tblpelanggan WHERE namapelanggan LIKE '%"+keyword+"%'"+FQueryTokoKain.sOrderASC("namapelanggan");
        }

        Cursor c = db.sq(q);
        if (c.getCount()>0){
            while(c.moveToNext()){
                DaftarPelanggan.add(new getterPelanggan(
                        c.getInt(c.getColumnIndex("idpelanggan")),
                        c.getString(c.getColumnIndex("namapelanggan")),
                        c.getString(c.getColumnIndex("alamatpelanggan")),
                        c.getString(c.getColumnIndex("telppelanggan"))
                ));
            }
        }

        adapterPelanggan.notifyDataSetChanged();
    }
    public  void getKain(String keyword,String kategori){
        DaftarKain = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterKain = new AdapterListKainCari(this,DaftarKain);
        recyclerView.setAdapter(adapterKain);
        String q;

        if (TextUtils.isEmpty(keyword)){
            if (kategori.equals("0")){
                q="SELECT * FROM qkain";
            }else {
                q="SELECT * FROM qkain WHERE idkategori='"+kategori+"'";
            }
        }else {
            if (kategori.equals("0")){
                q="SELECT * FROM qkain WHERE kain LIKE '%"+keyword+"%'";
            }else {
                q="SELECT * FROM qkain WHERE kain LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'";
            }
        }
        Cursor c=db.sq(q);
        if (c.getCount()>0){
            while(c.moveToNext()){
                DaftarKain.add(new getterKain(
                        c.getInt(c.getColumnIndex("idkain")),
                        c.getInt(c.getColumnIndex("idkategori")),
                        c.getString(c.getColumnIndex("kategori")),
                        c.getString(c.getColumnIndex("kain")),
                        KumFunTokoKain.removeE(c.getString(c.getColumnIndex("biaya")))
                ));
            }
        }
        adapterKain.notifyDataSetChanged();
    }
    public void tambahdata(View view) {
       if (a.equals("pelanggan")){
            startActivity(new Intent(this,Form_Tambah_Pelanggan_Toko_Kain_.class));
        }else if (a.equals("kain")){
            startActivity(new Intent(this, FormTambah_Toko_Kain.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
         if (a.equals("pelanggan")){
            getPelanggan(keyword);
        }else if (a.equals("kain")) {
            getKategoriData();
            getKain(keyword,kategori);
        }
    }
}
class AdapterListPelangganCari extends RecyclerView.Adapter<AdapterListPelangganCari.PelangganCariViewHolder>{
    private Context ctxAdapter;
    private List<getterPelanggan> data;

    public AdapterListPelangganCari(Context ctxAdapter, List<getterPelanggan> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public PelangganCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final getterPelanggan getter=data.get(i);
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view=inflater.inflate(R.layout.list_transaksi_cari_pelanggan_kain,viewGroup,false);
        return new PelangganCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
        final getterPelanggan getter=data.get(i);

        holder.nama.setText(getter.getNamaPelanggan());
        holder.alamat.setText(getter.getAlamatPelanggan());
        holder.telp.setText(getter.getNotelpPelanggan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,Transaksi_Toko_Kain.class);
                terima.putExtra("idpelanggan",getter.getIdPelanggan());
                terima.putExtra("namapelanggan",getter.getNamaPelanggan());
                ((Activity_Penjualan_Cari_Toko_Kain)ctxAdapter).setResult(1000,terima);
                ((Activity_Penjualan_Cari_Toko_Kain)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PelangganCariViewHolder extends RecyclerView.ViewHolder{
        private TextView nama,alamat,telp;
        public PelangganCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama= (TextView)itemView.findViewById(R.id.tvNamaPelanggan);
            alamat= (TextView)itemView.findViewById(R.id.tvAlamatPelanggan);
            telp= (TextView)itemView.findViewById(R.id.tvTelpPelanggan);
        }
    }
}
class AdapterListKainCari extends RecyclerView.Adapter<AdapterListKainCari.KainCariViewHolder>{
    private Context ctxAdapter;
    private List<getterKain> data;

    public AdapterListKainCari(Context ctxAdapter, List<getterKain> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public KainCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final getterKain getter=data.get(i);
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view=inflater.inflate(R.layout.list_transaksi_cari_toko_kain,viewGroup,false);
        return new KainCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KainCariViewHolder holder, int i) {
        final getterKain getter=data.get(i);
        holder.nama.setText(getter.getKain());
        holder.biaya.setText("Rp."+getter.getBiaya());
        holder.kategori.setText(getter.getKategori());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,Transaksi_Toko_Kain.class);
                terima.putExtra("idkain",getter.getIdKain());
                terima.putExtra("idkategori",getter.getIdKategori());
                terima.putExtra("namakain",getter.getKain());
                terima.putExtra("kategorikain",getter.getKategori());
                terima.putExtra("biayakain",getter.getBiaya());
                ((Activity_Penjualan_Cari_Toko_Kain)ctxAdapter).setResult(2000,terima);
                ((Activity_Penjualan_Cari_Toko_Kain)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class KainCariViewHolder extends RecyclerView.ViewHolder{
        TextView nama,biaya,kategori;
        public KainCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView)itemView.findViewById(R.id.tvNamaKain);
            biaya=(TextView)itemView.findViewById(R.id.tvBiayaKain);
            kategori=(TextView)itemView.findViewById(R.id.tvKategoriKain);
        }
    }
}

