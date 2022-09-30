package com.itbrain.aplikasitoko;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransaksiCariLaundry extends AppCompatActivity {

    RecyclerView recyclerView;
    List<getterPegawai> DaftarPegawai;
    List<getterPelanggan> DaftarPelanggan;
    List<getterJasa> DaftarJasa;
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
            q="SELECT * FROM tblpegawai where idpegawai !=0 ";
        }else {
            q="SELECT * FROM tblpegawai WHERE pegawai LIKE '%"+keyword+"%'"+Query.sOrderASC("pegawai");
        }
        Cursor c=db.sq(q);
        if (Modul.getCount(c)>0){
            while(c.moveToNext()){
                DaftarPegawai.add(new getterPegawai(
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
            q="SELECT * FROM tblpelanggan where idpelanggan !=0";
        }else {
            q="SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%'"+Query.sOrderASC("pelanggan");
        }

        Cursor c = db.sq(q);
        if (Modul.getCount(c)>0){
            while(c.moveToNext()){
                DaftarPelanggan.add(new getterPelanggan(
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
                DaftarJasa.add(new getterJasa(
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
        Cursor kategori  = db.sq(Query.select("tblkategori"));
        if (a.equals("pegawai")){
            startActivity(new Intent(this,MenuPegawaiLaundry.class));
        }else if (a.equals("pelanggan")){
            startActivity(new Intent(this,MenuPelangganLaundry.class));
        }else if (Modul.getCount(kategori) == 0 ){
            Toast.makeText(this, "Mohon Mengisi Kategori Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }else if (a.equals("jasa")){
            startActivity(new Intent(this,MenuUbahJasaLaundry.class));
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
    private List<getterPegawai> data;

    public AdapterListPegawaiCari(Context ctxAdapter, List<getterPegawai> data) {
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
        final getterPegawai getter = data.get(i);
        holder.nama.setText(getter.getNamaPegawai());
        holder.alamat.setText(getter.getAlamatPegawai());
        holder.telp.setText(getter.getNotelpPegawai());
        holder.optMuncul.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
                terima.putExtra("idpegawai",getter.getIdPegawai());
                terima.putExtra("namapegawai",getter.getNamaPegawai());
                terima.putExtra("alamatpegawai",getter.getAlamatPegawai());
                terima.putExtra("notelppegawai",getter.getNotelpPegawai());
                ((TransaksiCariLaundry)ctxAdapter).setResult(1000,terima);
                ((TransaksiCariLaundry)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PegawaiCariViewHolder extends RecyclerView.ViewHolder{
        private TextView nama,alamat,telp;
        ImageView optMuncul;
        public PegawaiCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView) itemView.findViewById(R.id.namaPegawai);
            alamat=(TextView) itemView.findViewById(R.id.alamatPegawai);
            telp=(TextView) itemView.findViewById(R.id.notelpPegawai);
            optMuncul = itemView.findViewById(R.id.optMuncul);
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
        View view=inflater.inflate(R.layout.laundryitemdaftarpelanggan,viewGroup,false);
        return new PelangganCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
        final getterPelanggan getter=data.get(i);
        holder.nama.setText(getter.getNamaPelanggan());
        holder.alamat.setText(getter.getAlamatPelanggan());
        holder.telp.setText(getter.getNotelpPelanggan());
        holder.hutang.setVisibility(View.GONE);
        holder.optMuncul.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
                terima.putExtra("idpelanggan",getter.getIdPelanggan());
                terima.putExtra("namapelanggan",getter.getNamaPelanggan());
                terima.putExtra("alamatpelanggan",getter.getAlamatPelanggan());
                terima.putExtra("notelppelanggan",getter.getNotelpPelanggan());
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
        private TextView nama,alamat,telp,hutang;
        ImageView optMuncul;
        public PelangganCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama= (TextView)itemView.findViewById(R.id.txtNamaPelanggan);
            alamat= (TextView)itemView.findViewById(R.id.txtAlamatPelanggan);
            telp= (TextView)itemView.findViewById(R.id.txtNomerPelanggan);
            hutang = itemView.findViewById(R.id.txtHutang);
            optMuncul = itemView.findViewById(R.id.optMuncul);
        }
    }
}
class AdapterListJasaCari extends RecyclerView.Adapter<AdapterListJasaCari.JasaCariViewHolder>{
    private Context ctxAdapter;
    private List<getterJasa> data;

    public AdapterListJasaCari(Context ctxAdapter, List<getterJasa> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public JasaCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final getterJasa getter=data.get(i);
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view=inflater.inflate(R.layout.laundryitemdaftarjasa,viewGroup,false);
        return new JasaCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JasaCariViewHolder holder, int i) {
        final getterJasa getter=data.get(i);
        holder.nama.setText(getter.getJasa());
        holder.biaya.setText("Rp."+getter.getBiaya());
        holder.optMuncul.setVisibility(View.GONE);
        if(getter.getSatuan().equals("pc")){
            holder.satuan.setText("/Pcs");
        }else if (getter.getSatuan().equals("kg")){
            holder.satuan.setText("/Kg");
        } else if (getter.getSatuan().equals("m2")) {
            holder.satuan.setText("/M²");
        }
        holder.kategori.setText(getter.getKategori());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter,MenuTerimaLaundry.class);
                terima.putExtra("idjasa",getter.getIdJasa());
                terima.putExtra("idkategori",getter.getIdKategori());
                terima.putExtra("namajasa",getter.getJasa());
                terima.putExtra("kategorijasa",getter.getKategori());
                terima.putExtra("biayajasa",getter.getBiaya());
                terima.putExtra("satuanjasa",getter.getSatuan());
                ((TransaksiCariLaundry)ctxAdapter).setResult(3000,terima);
                ((TransaksiCariLaundry)ctxAdapter).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class JasaCariViewHolder extends RecyclerView.ViewHolder{
        TextView nama,biaya,satuan,kategori;
        ImageView optMuncul;
        public JasaCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=(TextView)itemView.findViewById(R.id.edtDaftarKategori);
            biaya=(TextView)itemView.findViewById(R.id.edtSatuan);
            satuan=(TextView)itemView.findViewById(R.id.edtjenisSatuan);
            kategori=(TextView)itemView.findViewById(R.id.tvKategoriJasa);
            optMuncul = itemView.findViewById(R.id.optMuncul);
        }
    }
}

