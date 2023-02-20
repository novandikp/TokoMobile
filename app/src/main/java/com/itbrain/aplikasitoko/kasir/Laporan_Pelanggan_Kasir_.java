package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class Laporan_Pelanggan_Kasir_ extends AppCompatActivity {

    View v;
    FConfigKasir config;
    DatabaseKasir db;
    ArrayList arrayList = new ArrayList();
    RecyclerView.Adapter adapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_pelanggan_kasir_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);

        type ="pelanggan";

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvLaporanPelanggan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        setTitle("Laporan Pelanggan");
        adapter = new AdapterPelanggan(this, arrayList);
        recyclerView.setAdapter(adapter);
        getPelanggan("");

        final EditText eCari = (EditText) findViewById(R.id.eCariLaporanPelanggan);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                String a = eCari.getText().toString() ;
                    getPelanggan(a);
                }
        });
    }

    public void getPelanggan(String a){
        // + " EXCEPT SELECT * FROM tblpelanggan WHERE idpelanggan=1"
        String hasil = "" ;
        if(TextUtils.isEmpty(a)){
            hasil = FQueryKasir.select("tblpelanggan") +" EXCEPT SELECT * FROM tblpelanggan WHERE idpelanggan=1 "+FQueryKasir.sOrderASC("pelanggan") ;
        } else {
            hasil = FQueryKasir.selectwhere("tblpelanggan") +FQueryKasir.sLike("pelanggan",a) + " EXCEPT SELECT * FROM tblpelanggan WHERE idpelanggan=1" +FQueryKasir.sOrderASC("pelanggan");
        }
        Cursor c = db.sq(hasil) ;
        if(FFunctionKasir.getCount(c) > 0){
            FFunctionKasir.setText(v,R.id.jede,"Jumlah Data : "+FFunctionKasir.intToStr(FFunctionKasir.getCount(c))) ;
            while(c.moveToNext()){
                String nama = FFunctionKasir.getString(c,"pelanggan");
                String telp = FFunctionKasir.getString(c,"telp");
                String alamat = FFunctionKasir.getString(c,"alamat");

                String campur = nama +"__"+alamat+"__"+telp ;
                arrayList.add(campur);
            }
        } else {
            FFunctionKasir.setText(v,R.id.jede,"Jumlah Data : ") ;
        }
        adapter.notifyDataSetChanged();
    }

    public void exportLaporanPelanggan(View view){
        Intent i = new Intent(this, ActivityExportExcelKasir.class) ;
        i.putExtra("type",type) ;
        startActivity(i);
    }

    class AdapterPelanggan extends RecyclerView.Adapter<AdapterPelanggan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterPelanggan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public AdapterPelanggan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_pelanggan_kasir, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView faktur, nma, jumlah;

            public ViewHolder(View view) {
                super(view);

                nma = (TextView) view.findViewById(R.id.tBarang);
                faktur = (TextView) view.findViewById(R.id.tHitung);
                jumlah = (TextView) view.findViewById(R.id.tTelp);
            }
        }

        @Override
        public void onBindViewHolder(AdapterPelanggan.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.jumlah.setText(row[2]);
            viewHolder.nma.setText(row[0]);
            viewHolder.faktur.setText(row[1]);
        }
    }
}

