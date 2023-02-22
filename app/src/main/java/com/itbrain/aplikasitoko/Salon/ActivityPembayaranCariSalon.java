package com.itbrain.aplikasitoko.Salon;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityPembayaranCariSalon extends AppCompatActivity {

    Toolbar appbar;
    String type;
    DatabaseSalon db;
    List<Menu_Jasa_Salon_.getterJasa> DaftarJasa;
    List<Menu_Pelanggan_Salon_.getterPelanggan> DaftarPelanggan;
    List<ActivityJanji.getterJanji> DaftarJanji;
    AdapterListJasaCari adapterJasa;
    AdapterListPelangganCari adapterPelanggan;
    AdapterListJanjiCari adapterJanji;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_cari_salon);

        db = new DatabaseSalon(this);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        type = getIntent().getStringExtra("cari");
        String title = "judul";

        if (type.equals("pelanggan")) {
            title = "Cari Pelanggan";
            FunctionSalon.btnBack("Cari Pelanggan",getSupportActionBar());
            getPelanggan("");
        } else if (type.equals("jasa")) {
            title = "Cari Jasa";
            FunctionSalon.btnBack("Cari Jasa",getSupportActionBar());
            getJasa("");
        } else if (type.equals("janji")) {
            title = "Cari Booking";
            FunctionSalon.btnBack("Cari Booking", getSupportActionBar());
            getJanji("");
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
                String keyword = edtCari.getText().toString();
                if (type.equals("pelanggan")){
                    getPelanggan(keyword);
                } else if (type.equals("jasa")){
                    getJasa(keyword);
                } else if (type.equals("janji")){
                    getJanji(keyword);
                }
            }
        });

        FunctionSalon.btnBack(title, getSupportActionBar());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------------------//

    @SuppressLint("Range")
    public void getJasa(String keyword){
        DaftarJasa = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterJasa = new AdapterListJasaCari(this, DaftarJasa);
        recyclerView.setAdapter(adapterJasa);
        String q;
        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM tbljasa";
        } else {
            q = "SELECT * FROM tbljasa WHERE jasa LIKE '%"+keyword+"%' ORDER BY jasa";
        }

        Cursor c = db.sq(q);
        if (FunctionSalon.getCount(c) > 0){
            while (c.moveToNext()){
                DaftarJasa.add(new Menu_Jasa_Salon_.getterJasa(
                        c.getInt(c.getColumnIndex("idjasa")),
                        c.getString(c.getColumnIndex("jasa")),
                        c.getInt(c.getColumnIndex("harga"))
                ));
            }
        }
        adapterJasa.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------------------------------------//

    @SuppressLint("Range")
    public void getPelanggan(String keyword) {
        DaftarPelanggan = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterPelanggan = new AdapterListPelangganCari(this, DaftarPelanggan);
        recyclerView.setAdapter(adapterPelanggan);
        String q;
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpelanggan";
        }else {
            q="SELECT * FROM tblpelanggan WHERE (pelanggan LIKE '%"+keyword+"%' OR alamatpel LIKE '%"+keyword+"%' OR telppel LIKE '%"+keyword+"%') ORDER BY pelanggan";
        }
        Cursor c = db.sq(q);
        if (FunctionSalon.getCount(c) > 0) {
            while (c.moveToNext()) {
                DaftarPelanggan.add(new Menu_Pelanggan_Salon_.getterPelanggan(
                        c.getInt(c.getColumnIndex("idpelanggan")),
                        c.getString(c.getColumnIndex("pelanggan")),
                        c.getString(c.getColumnIndex("alamatpel")),
                        c.getString(c.getColumnIndex("telppel"))
                ));
            }
        }
        adapterPelanggan.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------------------------------------//

    @SuppressLint("Range")
    public void getJanji(String keyword){
        DaftarJanji = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recListCari);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterJanji = new AdapterListJanjiCari(this, DaftarJanji);
        recyclerView.setAdapter(adapterJanji);
        String q;
        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM qjanji WHERE status=1";
        } else {
            q = "SELECT * FROM qjanji WHERE status=1 AND (pelanggan LIKE '%" + keyword + "%' OR telppel LIKE '%" + keyword + "%') ORDER BY pelanggan";
        }

        Cursor c = db.sq(q);
        if (FunctionSalon.getCount(c) > 0){
            while (c.moveToNext()){
                DaftarJanji.add(new ActivityJanji.getterJanji(
                        c.getInt(c.getColumnIndex("idjanji")),
                        c.getInt(c.getColumnIndex("idpelanggan")),
                        c.getString(c.getColumnIndex("pelanggan")),
                        c.getString(c.getColumnIndex("tgljanji")),
                        c.getString(c.getColumnIndex("jamjanji")),
                        c.getString(c.getColumnIndex("status")),
                        c.getString(c.getColumnIndex("telppel"))
                ));
            }
        }
        adapterJanji.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------------------------------------//

    class AdapterListJasaCari extends RecyclerView.Adapter<AdapterListJasaCari.JasaCariViewHolder> {
        private Context ctxAdapter;
        private List<Menu_Jasa_Salon_.getterJasa> data;

        public AdapterListJasaCari(Context ctxAdapter, List<Menu_Jasa_Salon_.getterJasa> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }

        @NonNull
        @Override
        public JasaCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.list_pembayaran_cari_jasa_salon, viewGroup, false);
            return new JasaCariViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull JasaCariViewHolder holder, int i) {
            final Menu_Jasa_Salon_.getterJasa getter = data.get(i);

            holder.jasa.setText("Jasa : "+getter.getJasa());
            holder.harga.setText("Harga : Rp. "+FunctionSalon.removeE(getter.getHarga()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent terima = new Intent(ctxAdapter, Form_Pembayaran_Langsung_Salon_.class);
                    terima.putExtra("idjasa", getter.getIdjasa());
                    terima.putExtra("jasa", getter.getJasa());
                    terima.putExtra("harga", getter.getJasa());
                    ((ActivityPembayaranCariSalon) ctxAdapter).setResult(2000, terima);
                    ((ActivityPembayaranCariSalon) ctxAdapter).finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class JasaCariViewHolder extends RecyclerView.ViewHolder{
            private TextView jasa,harga;
            public JasaCariViewHolder(@NonNull View itemView) {
                super(itemView);
                jasa=(TextView) itemView.findViewById(R.id.tvNamaPelanggan);
                harga=(TextView) itemView.findViewById(R.id.tvHargaJasa);
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------//

    class AdapterListPelangganCari extends RecyclerView.Adapter<AdapterListPelangganCari.PelangganCariViewHolder> {
        private Context ctxAdapter;
        private List<Menu_Pelanggan_Salon_.getterPelanggan> data;

        public AdapterListPelangganCari(Context ctxAdapter, List<Menu_Pelanggan_Salon_.getterPelanggan> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }

        @NonNull
        @Override
        public PelangganCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.list_pembayaran_cari_pelanggan_salon, viewGroup, false);
            return new PelangganCariViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
            final Menu_Pelanggan_Salon_.getterPelanggan getter = data.get(i);

            holder.nama.setText("Nama : "+getter.getPelanggan());
            holder.alamat.setText("Alamat : "+getter.getAlamat());
            holder.telp.setText("No. Telepon : "+getter.getNoTelp());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent terima = new Intent(ctxAdapter, Form_Pembayaran_Langsung_Salon_.class);
                    terima.putExtra("idpelanggan", getter.getIdPelanggan());
                    terima.putExtra("pelanggan", getter.getPelanggan());
                    ((ActivityPembayaranCariSalon) ctxAdapter).setResult(1000, terima);
                    ((ActivityPembayaranCariSalon) ctxAdapter).finish();
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
                nama=(TextView) itemView.findViewById(R.id.namapelanggan);
                alamat=(TextView) itemView.findViewById(R.id.alamat);
                telp=(TextView) itemView.findViewById(R.id.telp);
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------//

    class AdapterListJanjiCari extends RecyclerView.Adapter<AdapterListJanjiCari.JanjiCariViewHolder> {
        private Context ctxAdapter;
        private List<ActivityJanji.getterJanji> data;

        public AdapterListJanjiCari(Context ctxAdapter, List<ActivityJanji.getterJanji> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }

        @NonNull
        @Override
        public JanjiCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.list_pembayaran_cari_janji_salon, viewGroup, false);
            return new JanjiCariViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull JanjiCariViewHolder holder, int i) {
            final ActivityJanji.getterJanji getter = data.get(i);

            holder.pelanggan.setText("Nama : "+getter.getPel());
            holder.notelp.setText("No. Telepon : "+getter.getTelp());
            holder.tglJanji.setText("Tanggal Booking : "+FunctionSalon.dateToNormal(getter.getTgljanji()));
            holder.jamJanji.setText("Jam Booking : "+FunctionSalon.timeToNormal(getter.getJamjanji()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent terima = new Intent(ctxAdapter, Form_Pembayaran_Langsung_Salon_.class);
                    terima.putExtra("idjanji", getter.getIdjanji());
                    terima.putExtra("idpelanggan", getter.getIdpelanggan());
                    terima.putExtra("pelanggan", getter.getPel());
                    terima.putExtra("tgljanji", getter.getTgljanji());
                    ((ActivityPembayaranCariSalon) ctxAdapter).setResult(3000, terima);
                    ((ActivityPembayaranCariSalon) ctxAdapter).finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class JanjiCariViewHolder extends RecyclerView.ViewHolder{
            private TextView pelanggan, tglJanji, jamJanji, notelp;
            public JanjiCariViewHolder(@NonNull View itemView) {
                super(itemView);
                pelanggan = (TextView) itemView.findViewById(R.id.tvNamaPelanggan);
                notelp = (TextView) itemView.findViewById(R.id.tvNoTelp);
                tglJanji = (TextView) itemView.findViewById(R.id.tvTanggalJanji);
                jamJanji = (TextView) itemView.findViewById(R.id.tvJamJanji);
            }
        }
    }
}
