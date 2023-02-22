package com.itbrain.aplikasitoko.kasir;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class ActivityPenjualanCariKasir extends AppCompatActivity {

    String type;
    String tabel = "";
    View v;
    FConfigKasir config;
    DatabaseKasir db;
    RecyclerView.Adapter adapter;
    ArrayList arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_cari_kasir);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recCari);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton imageButton = findViewById(R.id.kembali2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        try{
            type = getIntent().getStringExtra("type");
        }catch (Exception e){
            type="";
            finish();
        }
        if(type.equals("barang")){
            tabel = "tblbarang" ;

            adapter = new DataBarang(this, arrayList) ;
            recyclerView.setAdapter(adapter);
            getBarang("") ;
        } else if (type.equals("pelanggan")){
            tabel = "tblpelanggan" ;

            adapter = new DataPelanggan(this,arrayList) ;
            recyclerView.setAdapter(adapter);
            getPelanngan("") ;
        }


        final EditText eCari = (EditText) findViewById(R.id.cari);
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
                if (tabel.equals("tblpelanggan")) {
                    getPelanngan(s.toString());
                } else {
                    getBarang(s.toString());
                }
            }
        });
    }


    private void getPelanngan(String cari) {
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKasir.select(tabel) + " EXCEPT " + FQueryKasir.selectwhere(tabel) + FQueryKasir.sWhere("idpelanggan", "1");
        } else {
            q = FQueryKasir.selectwhere(tabel) + FQueryKasir.sLike("pelanggan", cari) + " EXCEPT " + FQueryKasir.selectwhere(tabel) + FQueryKasir.sWhere("idpelanggan", "1");
        }
        Cursor c = db.sq(q);
        if (FFunctionKasir.getCount(c) > 0) {
            while (c.moveToNext()) {
                String campur = FFunctionKasir.getString(c, "pelanggan") + "__" + FFunctionKasir.getString(c, "alamat") + "__" + FFunctionKasir.getString(c, "telp") + "__" + FFunctionKasir.getString(c, "idpelanggan");
                arrayList.add(campur);
            }
        } else {

        }
        adapter.notifyDataSetChanged();
    }

    private void getBarang(String cari) {
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKasir.select(tabel);
        } else {
            q = FQueryKasir.selectwhere(tabel) + FQueryKasir.sLike("barang", cari);
        }
        Cursor c = db.sq(q);
        if (FFunctionKasir.getCount(c) > 0) {
            while (c.moveToNext()) {
                String campur = FFunctionKasir.getString(c, "barang") + "__" + FFunctionKasir.getString(c, "stok") + "__" + FFunctionKasir.removeE(FFunctionKasir.getString(c, "hargajual")) + "__" + FFunctionKasir.getString(c, "idbarang");
                arrayList.add(campur);
            }
        } else {

        }
        adapter.notifyDataSetChanged();
    }

    class DataPelanggan extends RecyclerView.Adapter<DataPelanggan.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public DataPelanggan(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public DataPelanggan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_penjualan_cari_pelanggan, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nama, telp, alamat;
            private LinearLayout waddah;

            public ViewHolder(View view) {
                super(view);

                nama = (TextView) view.findViewById(R.id.nama);
                telp = (TextView) view.findViewById(R.id.telp);
                alamat = (TextView) view.findViewById(R.id.alamat);
                waddah = (LinearLayout) view.findViewById(R.id.parent);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = v.getTag().toString();
                        Intent i = new Intent(c, Form_Penjualan_Kasir_.class);
                        FConfigKasir fConfig = new FConfigKasir(c.getSharedPreferences("temp", c.MODE_PRIVATE));
                        fConfig.setCustom("idpelanggan", tag);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        c.startActivity(i);
                    }
                });
            }
        }

        @Override
        public void onBindViewHolder(DataPelanggan.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.nama.setText(row[0]);
            viewHolder.telp.setText(row[2]);
            viewHolder.alamat.setText(row[1]);
            viewHolder.waddah.setTag(row[3]);
        }
    }

    class DataBarang extends RecyclerView.Adapter<DataBarang.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public DataBarang(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public DataBarang.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_penjualan_cari_barang, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nama, telp, alamat;
            private LinearLayout waddah;

            public ViewHolder(View view) {
                super(view);

                nama = (TextView) view.findViewById(R.id.barang);
                telp = (TextView) view.findViewById(R.id.stok);
                alamat = (TextView) view.findViewById(R.id.harga);
                waddah = (LinearLayout) view.findViewById(R.id.parent);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = v.getTag().toString();
                        Intent i = new Intent(c, Form_Penjualan_Kasir_.class);
                        FConfigKasir fConfig = new FConfigKasir(c.getSharedPreferences("temp", c.MODE_PRIVATE));
                        fConfig.setCustom("idbarang", tag);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        c.startActivity(i);
                    }
                });
            }
        }

        @Override
        public void onBindViewHolder(DataBarang.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.nama.setText(row[0]);
            viewHolder.alamat.setText("Rp. " + row[2]);
            viewHolder.telp.setText("Sisa Stok : " + row[1]);
            viewHolder.waddah.setTag(row[3]);
        }
    }
}




