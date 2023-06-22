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

public class MenuCariBarangKredit extends AppCompatActivity {

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
        setContentView(R.layout.menu_penjualan_cari_barang_kredit);
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
        if (type.equals("barang")) {
            tabel = "tblbarang";
            adapter = new DataBarang(this, this, arrayList);
            recyclerView.setAdapter(adapter);
            getBarang("");

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
                if (tabel.equals("tblbarang")) {
                    getBarang(s.toString());
                }
            }
        });
    }

    private void getBarang(String cari) {
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKredit.select(tabel);
        } else {
            q = FQueryKredit.selectwhere(tabel) + FQueryKredit.sLike("barang", cari);
        }
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = FFunctionKredit.getString(c, "barang") + "__" + FFunctionKredit.getString(c, "stok") + "__" + FFunctionKredit.removeE(FFunctionKredit.getString(c, "hargajual")) + "__" + FFunctionKredit.getString(c, "idbarang");
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }
}


    class DataBarang extends RecyclerView.Adapter<DataBarang.ViewHolder> {
        private ArrayList<String> data;
        Context c;
        Activity activity;

        public DataBarang(Context a, Activity activity, ArrayList<String> kota) {
            this.data = kota;
            c = a;
            this.activity = activity;
        }

        @Override
        public DataBarang.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_penjualan_cari_barang_kredit, viewGroup, false);
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

                nama = view.findViewById(R.id.barang);
                telp = view.findViewById(R.id.stok);
                alamat = view.findViewById(R.id.harga);
                waddah = view.findViewById(R.id.wadah);

                waddah.setOnClickListener(v -> {
                    String tag = v.getTag().toString();
                    String key = activity.getIntent().getSerializableExtra("owner") == ActivityPenjualanKredit.class ? "idbarang_kredit" : "idbarang";
                    Class classToIntent = activity.getIntent().getSerializableExtra("owner") == ActivityPenjualanKredit.class ? ActivityPenjualanKredit.class : ActivityPenjualanTunaiKredit.class;
                    Intent i = new Intent(c, classToIntent);
                    FConfigKredit FConfigKredit = new FConfigKredit(c.getSharedPreferences("temp", Context.MODE_PRIVATE));
                    FConfigKredit.setCustom(key, tag);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    c.startActivity(i);
                });
            }
        }

        @Override
        public void onBindViewHolder(DataBarang.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.nama.setText(ModuleKredit.trim(row[0]));
            viewHolder.alamat.setText("Rp. " + row[2]);
            viewHolder.telp.setText("Sisa Stok : " + row[1]);
            viewHolder.waddah.setTag(row[3]);
        }
    }


