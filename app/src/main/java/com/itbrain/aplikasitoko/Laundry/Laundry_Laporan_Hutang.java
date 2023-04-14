package com.itbrain.aplikasitoko.Laundry;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laundry_Laporan_Hutang extends AppCompatActivity {
    DatabaseLaundry db;
    View v;

    int year, month, day;
    Calendar calendar;
    String tab;
    String dateawal, datesampai, dateFirst, cari = "";
    Integer keuanganOpsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundry_laporan_hutang);
        db = new DatabaseLaundry(this);
        v = this.findViewById(android.R.id.content);
        ImageButton i = findViewById(R.id.kembali13);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String title = "judul";
        tab = getIntent().getStringExtra("tab");

        final TextView tvPendapatan = (TextView) findViewById(R.id.tvPendapatan2);
//        spinner kategori

//        button tgl
        gethutang("");
        tvPendapatan.setVisibility(View.VISIBLE);
        final EditText edtCari = (EditText) findViewById(R.id.edtCariii);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cari = edtCari.getText().toString();

                gethutang(cari);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String countJumlah(String value) {
        try {
            Cursor c = db.sq(QueryLaundry.sSum("tbllaundrydetail", "jumlahlaundry") + " WHERE idlaundry=" + value);
            c.moveToFirst();
            return ModulLaundry.getStringFromColumn(c, 0);
        } catch (Exception e) {
            return "";
        }

    }

    private void gethutang(String keyword) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterLaporanHutang(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = "";
        if (keyword.isEmpty()) {
            q = QueryLaundry.selectwhere("tblpelanggan") + " hutang>0";
        } else {
            q = QueryLaundry.selectwhere("tblpelanggan") + " hutang>0 AND " + QueryLaundry.sLike("pelanggan", keyword);
        }
        Cursor hutang = db.sq(QueryLaundry.sSum("tblpelanggan", "hutang"));
        hutang.moveToNext();
        Cursor c = db.sq(q);
        if (ModulLaundry.getCount(c) > 0) {
            ModulLaundry.setText(v, R.id.tvJumlahData2, "Jumlah Pelanggan Hutang : " + ModulLaundry.getCount(c));
            ModulLaundry.setText(v, R.id.tvPendapatan2, "Total Hutang : Rp. " + ModulLaundry.removeE(hutang.getString(0)));
            while (c.moveToNext()) {
                String campur = ModulLaundry.getString(c, "idpelanggan") + "__" +
                        ModulLaundry.getString(c, "pelanggan") + "__" +
                        ModulLaundry.getString(c, "alamat") + "__" +
                        ModulLaundry.getString(c, "notelp") + "__" +
                        ModulLaundry.getString(c, "hutang");
                arrayList.add(campur);
            }
        } else {
            ModulLaundry.setText(v, R.id.tvJumlahData2, "Jumlah Pelanggan Hutang : 0");
            ModulLaundry.setText(v, R.id.tvPendapatan2, "Total Hutang : Rp. 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void exportExcel(View view) {
        Intent i = new Intent(this, Export_Exel_Laundry.class);
        i.putExtra("tab", "Hutang");
        startActivity(i);

    }

    class AdapterLaporanHutang extends RecyclerView.Adapter<AdapterLaporanHutang.LaporanHutangViewHolder> {
        private Context ctx;
        private ArrayList<String> data;

        public AdapterLaporanHutang(Context ctx, ArrayList<String> data) {
            this.ctx = ctx;
            this.data = data;
        }

        @NonNull
        @Override
        public LaporanHutangViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_hutang_laundry, viewGroup, false);
            return new LaporanHutangViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LaporanHutangViewHolder holder, int i) {
            String[] row = data.get(i).split("__");
            holder.pelanggan.setText(row[1]);
            holder.alamat.setText(row[2]);
            holder.notelp.setText(row[3]);
            holder.hutang.setText("Rp. " + ModulLaundry.numberFormat(row[4]));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class LaporanHutangViewHolder extends RecyclerView.ViewHolder {
            TextView pelanggan, alamat, notelp, hutang;

            public LaporanHutangViewHolder(@NonNull View itemView) {
                super(itemView);
                pelanggan = (TextView) itemView.findViewById(R.id.tvPelanggan);
                alamat = (TextView) itemView.findViewById(R.id.tvAlamat);
                notelp = (TextView) itemView.findViewById(R.id.tvNoTelp);
                hutang = (TextView) itemView.findViewById(R.id.tvHutang);
            }
        }

    }
}

