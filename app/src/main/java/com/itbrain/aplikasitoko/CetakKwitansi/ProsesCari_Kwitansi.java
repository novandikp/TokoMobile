package com.itbrain.aplikasitoko.CetakKwitansi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.itbrain.aplikasitoko.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProsesCari_Kwitansi extends AppCompatActivity {

    String type;
    List<getterPelanggan> DaftarPelanggan;
    List<MenuJasa_Kwitansi.getterJasa> DaftarJasa;
    DatabaseCetakKwitansi db;
    AdapterListJasaCari adapterJasa;
    AdapterListPelangganCari adapterPelanggan;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prosescari_kwitansi);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.imageButton31);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseCetakKwitansi(this);
        type = getIntent().getStringExtra("cari");

        if (type.equals("pelanggan")) {
            getPelanggan("");
        } else if (type.equals("jasa")) {
            getJasa("");
        }

        final TextInputEditText edtCari = (TextInputEditText) findViewById(R.id.eCari);
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
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------------------//
    @SuppressLint("Range")
    public void getJasa(String keyword) {
        DaftarJasa = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recPenCari);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapterJasa = new AdapterListJasaCari(this, DaftarJasa);
        recyclerView.setAdapter(adapterJasa);
        String q;
        if (TextUtils.isEmpty(keyword)) {
            q = "SELECT * FROM tbljasa";
        } else {
            q = "SELECT * FROM tbljasa WHERE jasa LIKE '%" + keyword + "%' ORDER BY jasa";
        }

        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                DaftarJasa.add(new MenuJasa_Kwitansi.getterJasa(
                        c.getInt(c.getColumnIndex("idjasa")),
                        c.getString(c.getColumnIndex("jasa"))
                ));
            }
        }
        adapterJasa.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------------------------------------//
    @SuppressLint("Range")
    public void getPelanggan(String keyword) {
        DaftarPelanggan = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recPenCari);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapterPelanggan = new AdapterListPelangganCari(this, DaftarPelanggan);
        recyclerView.setAdapter(adapterPelanggan);
        String q;
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpelanggan WHERE idpelanggan>0";
        }else {
            q="SELECT * FROM tblpelanggan WHERE idpelanggan>0 AND (pelanggan LIKE '%"+keyword+"%' OR alamat LIKE '%"+keyword+"%' OR notelp LIKE '%"+keyword+"%') ORDER BY pelanggan";
        }
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                DaftarPelanggan.add(new getterPelanggan(
                        c.getInt(c.getColumnIndex("idpelanggan")),
                        c.getString(c.getColumnIndex("pelanggan")),
                        c.getString(c.getColumnIndex("alamat")),
                        c.getString(c.getColumnIndex("notelp"))
                ));
            }
        }
        adapterPelanggan.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------------------------------------//
    class AdapterListJasaCari extends RecyclerView.Adapter<AdapterListJasaCari.JasaCariViewHolder>{
        private Context ctxAdapter;
        private List<MenuJasa_Kwitansi.getterJasa> data;

        public AdapterListJasaCari(Context ctxAdapter, List<MenuJasa_Kwitansi.getterJasa> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }

        @NonNull
        @Override
        public JasaCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final MenuJasa_Kwitansi.getterJasa getter = data.get(i);
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view=inflater.inflate(R.layout.item_carijasa_kwitansi,viewGroup,false);
            return new JasaCariViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull JasaCariViewHolder holder, int i) {

            final MenuJasa_Kwitansi.getterJasa getter=data.get(i);


            holder.jasa.setText(getter.getJasa());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent terima = new Intent(ctxAdapter,MenuCetakTransaksi_Kwitansi.class);

                    terima.putExtra("idjasa",getter.getIdJasa());
                    terima.putExtra("jasa",getter.getJasa());
                    ((ProsesCari_Kwitansi)ctxAdapter).setResult(2,terima);
                    ((ProsesCari_Kwitansi)ctxAdapter).finish();

                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class JasaCariViewHolder extends RecyclerView.ViewHolder{
            private TextView jasa, kelompok, harga;
            public JasaCariViewHolder(@NonNull View itemView) {
                super(itemView);

                jasa = (TextView)itemView.findViewById(R.id.tAlam);

            }
        }
    }

    //--------------------------------------------------------------------------------------------------------//

    class AdapterListPelangganCari extends RecyclerView.Adapter<AdapterListPelangganCari.PelangganCariViewHolder> {
        private Context ctxAdapter;
        private List<getterPelanggan> data;

        public AdapterListPelangganCari(Context ctxAdapter, List<getterPelanggan> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }

        @NonNull
        @Override
        public PelangganCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.item_caripelanggan_kwitansi, viewGroup, false);
            return new PelangganCariViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
            final getterPelanggan getter = data.get(i);

            holder.nama.setText("Nama : "+getter.getPelanggan());
            holder.alamat.setText("Alamat : "+getter.getAlamat());
            holder.telp.setText("No. Telepon : "+getter.getTelp());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent terima = new Intent(ctxAdapter, MenuCetakTransaksi_Kwitansi.class);
                    terima.putExtra("idpelanggan", getter.getIdPelanggan());
                    terima.putExtra("pelanggan", getter.getPelanggan());
                    ((ProsesCari_Kwitansi) ctxAdapter).setResult(1, terima);
                    ((ProsesCari_Kwitansi) ctxAdapter).finish();
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
                nama = (TextView) itemView.findViewById(R.id.tNamas);
                alamat = (TextView) itemView.findViewById(R.id.tAlam);
                telp = (TextView) itemView.findViewById(R.id.tTelp);
            }
        }
    }

    public static String removeE(String value){
        double hasil = Double.parseDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return numberFormat(df.format(hasil)) ;
    }


    public static String numberFormat(String number){ // Rp. 1,000,000.00
        try{
            String hasil = "";
            String[] b = number.split("\\.") ;

            if(b.length == 1){
                String[] a = number.split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
            } else {
                String[] a = b[0].split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
                hasil+=","+b[1] ;
            }
            return  hasil ;
        }catch (Exception e){
            return  "" ;
        }
    }
}
