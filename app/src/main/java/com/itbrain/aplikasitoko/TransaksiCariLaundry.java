package com.itbrain.aplikasitoko;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.DatabaseLaundry;
import com.itbrain.aplikasitoko.Model.JasaLaundry;
import com.itbrain.aplikasitoko.Model.Pegawai;
import com.itbrain.aplikasitoko.Model.PelangganLaundry;
import com.itbrain.aplikasitoko.Modul;
import com.itbrain.aplikasitoko.Query;
import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class TransaksiCariLaundry extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Pegawai> DaftarPegawai;
    List<PelangganLaundry> DaftarPelanggan;
    List<JasaLaundry> DaftarJasa;
    DatabaseLaundry db;
    AdapterListPegawaiCari adapterPegawai;
    AdapterListPelangganCari adapterPelanggan;
    AdapterListJasaCari adapterJasa;
    Spinner spinner;
    String keyword="",kategori="";
    String a;
    List<String> idKat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi_cari_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);

        a=getIntent().getStringExtra("cari");
        if (a.equals("pegawai")){
            Modul.btnBack("Cari Pegawai",getSupportActionBar());
            getPegawai("");
        }else if (a.equals("pelanggan")){
            Modul.btnBack("Cari Pelanggan",getSupportActionBar());
            getPelanggan("");
        }else if (a.equals("jasa")){
            Modul.btnBack("Cari Jasa",getSupportActionBar());
            spinner = (Spinner)findViewById(R.id.spKatCari);
            getKategoriData();
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kategori=db.getIdKategori().get(parent.getSelectedItemPosition());
                    getJasa(keyword,kategori);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            getJasa(keyword,kategori);
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
                if (a.equals("pegawai")){
                    getPegawai(keyword);
                }else if (a.equals("pelanggan")){
                    getPelanggan(keyword);
                }else if (a.equals("jasa")){
                    getJasa(keyword,kategori);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getKategoriData(){
        DatabaseLaundry db = new DatabaseLaundry(this);
        List<String> labels = db.getKategori();

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(data);
    }
    //--------------------------------------------------------------------------------------------------------//
    public void getPegawai(String keyword){
        DaftarPegawai = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterPegawai = new AdapterListPegawaiCari(this,DaftarPegawai);
        recyclerView.setAdapter(adapterPegawai);
        String q;
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpegawai";
        }else {
            q="SELECT * FROM tblpegawai WHERE pegawai LIKE '%"+keyword+"%'"+ Query.sOrderASC("pegawai");
        }
        Cursor c=db.sq(q);
        if (Modul.getCount(c)>0){
            while(c.moveToNext()){
                DaftarPegawai.add(new Pegawai(
                        Modul.getInt(c,"idpegawai"),
                        Modul.getString(c,"pegawai"),
                        Modul.getString(c,"alamatpegawai"),
                        Modul.getString(c,"notelppegawai")
                ));
            }
        }
        adapterPegawai.notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------------------------------------//
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
            q="SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%'"+Query.sOrderASC("pelanggan");
        }

        Cursor c = db.sq(q);
        if (Modul.getCount(c)>0){
            while(c.moveToNext()){
                DaftarPelanggan.add(new PelangganLaundry(
                        Modul.getInt(c,"idpelanggan"),
                        Modul.getString(c,"pelanggan"),
                        Modul.getString(c,"alamat"),
                        Modul.getString(c,"notelp")
                ));
            }
        }

        adapterPelanggan.notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------------------------------------//
    public  void getJasa(String keyword,String kategori){
        DaftarJasa = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterJasa = new AdapterListJasaCari(this,DaftarJasa);
        recyclerView.setAdapter(adapterJasa);
        String q;

        if (TextUtils.isEmpty(keyword)){
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa";
            }else {
                q="SELECT * FROM qjasa WHERE idkategori='"+kategori+"'";
            }
        }else {
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%'";
            }else {
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'";
            }
        }
        Cursor c=db.sq(q);
        if (Modul.getCount(c)>0){
            while(c.moveToNext()){
                DaftarJasa.add(new JasaLaundry(
                        Modul.getInt(c,"idjasa"),
                        Modul.getInt(c,"idkategori"),
                        Modul.getString(c,"kategori"),
                        Modul.getString(c,"jasa"),
                        Modul.removeE(Modul.getString(c,"biaya")),
                        Modul.getString(c,"satuan")
                ));
            }
        }
        adapterJasa.notifyDataSetChanged();
    }

    public void tambahdata(View view) {
        if (a.equals("pegawai")){
            startActivity(new Intent(this,MenuDaftarPegawaiLaundry.class));
        }else if (a.equals("pelanggan")){
            startActivity(new Intent(this,MenuDaftarPelangganLaundry.class));
        }else if (a.equals("jasa")){
            startActivity(new Intent(this,MenuDaftarJasaLaundry.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (a.equals("pegawai")){
            getPegawai(keyword);
        }else if (a.equals("pelanggan")){
            getPelanggan(keyword);
        }else if (a.equals("jasa")) {
            getKategoriData();
            getJasa(keyword,kategori);
        }
    }
}
class AdapterListPegawaiCari extends RecyclerView.Adapter<AdapterListPegawaiCari.PegawaiCariViewHolder>{
    private Context ctxAdapter;
    private List<Pegawai> data;

    public AdapterListPegawaiCari(Context ctxAdapter, List<Pegawai> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }
    @NonNull
    @Override
    public PegawaiCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.laundryitemdaftarpegawai,viewGroup,false);
        return new PegawaiCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PegawaiCariViewHolder holder, int i) {
        final Pegawai getter = data.get(i);

        holder.nama.setText(getter.getPegawai());
        holder.alamat.setText(getter.getAlamatpegawai());
        holder.telp.setText(getter.getNotelppegawai());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
                terima.putExtra("idpegawai",getter.getIdpegawai());
                terima.putExtra("namapegawai",getter.getPegawai());
                ((MenuTerimaLaundry)ctxAdapter).setResult(1000,terima);
                ((MenuTerimaLaundry)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PegawaiCariViewHolder extends RecyclerView.ViewHolder{
        private TextView nama,alamat,telp;
        public PegawaiCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView) itemView.findViewById(R.id.namaPegawai);
            alamat=(TextView) itemView.findViewById(R.id.alamatPegawai);
            telp=(TextView) itemView.findViewById(R.id.notelpPegawai);
        }
    }
}
class AdapterListPelangganCari extends RecyclerView.Adapter<AdapterListPelangganCari.PelangganCariViewHolder>{
    private Context ctxAdapter;
    private List<PelangganLaundry> data;

    public AdapterListPelangganCari(Context ctxAdapter, List<PelangganLaundry> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public PelangganCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final PelangganLaundry getter=data.get(i);
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view=inflater.inflate(R.layout.laundryitemdaftarpelanggan,viewGroup,false);
        return new PelangganCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
        final PelangganLaundry getter=data.get(i);

        holder.nama.setText(getter.getPelanggan());
        holder.alamat.setText(getter.getAlamatpelanggan());
        holder.telp.setText(getter.getNotelppelanggan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,.class);
                terima.putExtra("idpelanggan",getter.getIdpelanggan());
                terima.putExtra("namapelanggan",getter.getPelanggan());
                ((TransaksiCariLaundry)ctxAdapter).setResult(2000,terima);
                ((TransaksiCariLaundry)ctxAdapter).finish();
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
            nama= (TextView)itemView.findViewById(R.id.txtNamaPelanggan);
            alamat= (TextView)itemView.findViewById(R.id.txtAlamatPelanggan);
            telp= (TextView)itemView.findViewById(R.id.txtNomerPelanggan);
        }
    }
}
class AdapterListJasaCari extends RecyclerView.Adapter<AdapterListJasaCari.JasaCariViewHolder>{
    private Context ctxAdapter;
    private List<JasaLaundry> data;

    public AdapterListJasaCari(Context ctxAdapter, List<JasaLaundry> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public JasaCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final JasaLaundry getter=data.get(i);
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view=inflater.inflate(R.layout.laundryitemdaftarjasa,viewGroup,false);
        return new JasaCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JasaCariViewHolder holder, int i) {
        final JasaLaundry getter=data.get(i);
        holder.nama.setText(getter.getJasa());
        holder.biaya.setText("Rp."+getter.getSatuan());
        if(getter.getSatuan().equals("pc")){
            holder.satuan.setText("/Pcs");
        }else if (getter.getSatuan().equals("kg")){
            holder.satuan.setText("/Kg");
        } else if (getter.getSatuan().equals("m2")) {
            holder.satuan.setText("/M²");
        }
        holder.kategori.setText(getter.getIdkategori());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
                terima.putExtra("idjasa",getter.getIdJasa());
                terima.putExtra("idkategori",getter.get());
                terima.putExtra("namajasa",getter.getJasa());
                terima.putExtra("kategorijasa",getter.getKategori());
                terima.putExtra("biayajasa",getter.getBiaya());
                terima.putExtra("satuanjasa",getter.getSatuan());
                ((ActivityTransaksiCari)ctxAdapter).setResult(3000,terima);
                ((ActivityTransaksiCari)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class JasaCariViewHolder extends RecyclerView.ViewHolder{
        TextView nama,biaya,satuan,kategori;
        public JasaCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView)itemView.findViewById(R.id.tvNamaJasa);
            biaya=(TextView)itemView.findViewById(R.id.tvBiayaJasa);
            satuan=(TextView)itemView.findViewById(R.id.tvJenisSatuan);
            kategori=(TextView)itemView.findViewById(R.id.tvKategoriJasa);
        }
    }
}
