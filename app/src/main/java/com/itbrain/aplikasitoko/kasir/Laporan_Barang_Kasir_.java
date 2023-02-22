package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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

public class Laporan_Barang_Kasir_ extends AppCompatActivity {

    View v;
    FConfigKasir config;
    DatabaseKasir db;
    ArrayList arrayList = new ArrayList();
    RecyclerView.Adapter adapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_barang_kasir_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);

        type = "barang";

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvLaporanBarang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        setTitle("Laporan Barang");
        adapter = new AdapterBarang(this, arrayList);
        recyclerView.setAdapter(adapter);
        getBarang("");

        final EditText eCari = (EditText) findViewById(R.id.eCariLaporanBarang);
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
                String a = eCari.getText().toString();
                getBarang(a);
            }
        });
    }

    public void exportLaporanBarang(View view){
        Intent i = new Intent(this, ActivityExportExcelKasir.class) ;
        i.putExtra("type",type) ;
        startActivity(i);
    }

    public void getBarang(String a) {
        String hasil = "";
        String tabel = "tblbarang";
        if (TextUtils.isEmpty(a)) {
            hasil = FQueryKasir.select(tabel) + FQueryKasir.sOrderASC("barang");
        } else {
            hasil = FQueryKasir.selectwhere(tabel) + FQueryKasir.sLike("barang", a) + FQueryKasir.sOrderASC("barang");
        }
        Cursor c = db.sq(hasil);
        if (FFunctionKasir.getCount(c) > 0) {
            FFunctionKasir.setText(v, R.id.terserah, "Jumlah Data : " + FFunctionKasir.intToStr(FFunctionKasir.getCount(c)));
            while (c.moveToNext()) {
                String barang = FFunctionKasir.getString(c, "barang");
                String hargabeli = FFunctionKasir.getString(c, "hargabeli");
                String hargajual = FFunctionKasir.getString(c, "hargajual");
                String stok = FFunctionKasir.getString(c, "stok");

                String campur = barang + "__" + FFunctionKasir.removeE(hargabeli) + "__" + "Rp. " + FFunctionKasir.removeE(hargajual) + "__" + stok;
                arrayList.add(campur);
            }
        } else {
            FFunctionKasir.setText(v, R.id.terserah, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }


    class AdapterBarang extends RecyclerView.Adapter<AdapterBarang.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterBarang(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public AdapterBarang.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_barang_kasir, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView barang, beli, jual, stok;

            public ViewHolder(View view) {
                super(view);

                barang = (TextView) view.findViewById(R.id.tBarang);
                jual = (TextView) view.findViewById(R.id.tjual);
                beli = (TextView) view.findViewById(R.id.tHitung);
                stok = (TextView) view.findViewById(R.id.tStok);
            }
        }

        @Override
        public void onBindViewHolder(AdapterBarang.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.barang.setText(row[0]);
            viewHolder.beli.setText("Harga Beli : " + row[1]);
            viewHolder.jual.setText("Harga Jual : " + row[2]);
            viewHolder.stok.setText("Stok : " + row[3]);
        }
    }
}