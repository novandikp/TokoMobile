package com.itbrain.aplikasitoko.TokoKredit;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuCariPelangganKredit extends AppCompatActivity {


    String type;
    String tabel = "";
    View v;
    FConfigKredit config;
    FKoneksiKredit db;
    RecyclerView.Adapter adapter;
    ArrayList arrayList = new ArrayList();
    Serializable owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_cari_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);

        RecyclerView recyclerView = findViewById(R.id.recCari);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        owner = getIntent().getSerializableExtra("owner");

        type = getIntent().getStringExtra("type");
        if (type.equals("pelanggan")) {
            tabel = "tblpelanggan";
            adapter = new DataPelanggan(this, this, arrayList);
            recyclerView.setAdapter(adapter);
            getPelanngan("");
        }


        final EditText eCari = findViewById(R.id.cari);
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
                }
            }
        });
    }

    private void getPelanngan(String cari) {
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKredit.select(tabel) + " EXCEPT " + FQueryKredit.selectwhere(tabel) + FQueryKredit.sWhere("idpelanggan", "1");
        } else {
            q = FQueryKredit.selectwhere(tabel) + FQueryKredit.sLike("pelanggan", cari) + " EXCEPT " + FQueryKredit.selectwhere(tabel) + FQueryKredit.sWhere("idpelanggan", "1");
        }
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = FFunctionKredit.getString(c, "pelanggan") + "__" + FFunctionKredit.getString(c, "alamat") + "__" + FFunctionKredit.getString(c, "telp") + "__" + FFunctionKredit.getString(c, "idpelanggan");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

}


    class DataPelanggan extends RecyclerView.Adapter<DataPelanggan.ViewHolder> {
        private ArrayList<String> data;
        Context c;
        Activity activity;

        public DataPelanggan(Context a, Activity activity, ArrayList<String> kota) {
            this.data = kota;
            c = a;
            this.activity = activity;
        }

        @Override
        public DataPelanggan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_penjualan_cari_pelanggan_kredit, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nama, telp, alamat;
            private CardView waddah;

            public ViewHolder(View view) {
                super(view);

                nama = view.findViewById(R.id.nama);
                telp = view.findViewById(R.id.telp);
                alamat = view.findViewById(R.id.alamat);
                waddah = view.findViewById(R.id.wadah);

                waddah.setOnClickListener(v -> {
                    String key = activity.getIntent().getSerializableExtra("owner") == ActivityPenjualanKredit.class ? "idpelanggan_kredit" : "idpelanggan";
                    Class classToIntent = activity.getIntent().getSerializableExtra("owner") == ActivityPenjualanKredit.class ? ActivityPenjualanKredit.class : ActivityPenjualanTunaiKredit.class;
                    String tag = v.getTag().toString();
                    Intent i = new Intent(c, classToIntent);
                    FConfigKredit FConfigKredit = new FConfigKredit(c.getSharedPreferences("temp", Context.MODE_PRIVATE));
                    FConfigKredit.setCustom(key, tag);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    c.startActivity(i);
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




