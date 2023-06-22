package com.itbrain.aplikasitoko.TokoKredit;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class ActivityLaporanBarangKredit extends AppCompatActivity {

    View v;
    FConfigKredit config;
    FKoneksiKredit db;
    ArrayList arrayList = new ArrayList();
    RecyclerView.Adapter adapter;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_master_barang_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        type = getIntent().getStringExtra("type");

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final RecyclerView recyclerView = findViewById(R.id.recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        if (type.equals("barang")) {
            setTitle("Laporan Barang");
            adapter = new AdapterBarang(this, arrayList);
            recyclerView.setAdapter(adapter);
            getBarang("");
        }

        final EditText eCari = findViewById(R.id.eCari);
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
                if (type.equals("barang")) {
                    getBarang(a);
                }
            }
        });

    }

    public void getBarang(String a) {
        String hasil = "";
        String tabel = "tblbarang";
        if (TextUtils.isEmpty(a)) {
            hasil = FQueryKredit.select(tabel) + FQueryKredit.sOrderASC("barang");
        } else {
            hasil = FQueryKredit.selectwhere(tabel) + FQueryKredit.sLike("barang", a) + FQueryKredit.sOrderASC("barang");
        }
        Cursor c = db.sq(hasil);
        if (c.getCount() > 0) {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String barang = FFunctionKredit.getString(c, "barang");
                String hargabeli = FFunctionKredit.getString(c, "hargabeli");
                String hargajual = FFunctionKredit.getString(c, "hargajual");
                String stok = FFunctionKredit.getString(c, "stok");

                String campur = barang + "__" + FFunctionKredit.removeE(hargabeli) + "__" + "Rp. " + FFunctionKredit.removeE(hargajual) + "__" + stok;
                arrayList.add(campur);
            }
        } else {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcelKredit.class);
        i.putExtra("type", type);
        startActivity(i);
    }
}

class AdapterBarang extends RecyclerView.Adapter<AdapterBarang.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterBarang(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_barang_kredit, viewGroup, false);
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

            barang = view.findViewById(R.id.tBarang);
            jual = view.findViewById(R.id.tjual);
            beli = view.findViewById(R.id.tHitung);
            stok = view.findViewById(R.id.tStok);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[0]);
        viewHolder.beli.setText("Harga Beli : " + row[1]);
        viewHolder.jual.setText("Harga Jual : " + row[2]);
        viewHolder.stok.setText("Stok : " + row[3]);
    }
}


