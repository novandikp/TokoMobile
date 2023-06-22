package com.itbrain.aplikasitoko.TokoKredit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;
import com.itbrain.aplikasitoko.TokoKredit.ModuleKredit;
import com.itbrain.aplikasitoko.TokoKredit.ModelKeuanganKredit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityLaporanKeuanganKredit extends AppCompatActivity {

    FConfigKredit config;
    FKoneksiKredit conn;
    List<ModelKeuanganKredit> daftarLaporanKeuangan;
    RecyclerView reDaftarLaporanKeuangan;
    EditText eCari;
    TextView tvJumlah, tvTotalMasuk, tvTotalKeluar, tvSaldo, tCaption, tCaption3;
    AdapterLaporanKeuangan adapterLaporanKeuangan;
    View v;
    String dari, ke;
    Spinner spTipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_keuangan_kredit);

        init();
        getDaftarLaporanKeuangan("");

        // Listen to textchange event
        textChangedListenerManager();
        setDisplayedDate();
        handleSpinnerChange();

        ImageView imageView = findViewById(R.id.imageView28);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void init() {
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        conn = new FKoneksiKredit(this, config);
        daftarLaporanKeuangan = new ArrayList<>();
        reDaftarLaporanKeuangan = findViewById(R.id.reDaftarKeuangan);
        eCari = findViewById(R.id.eCari);
        tvJumlah = findViewById(R.id.tvJumlah);
        tvTotalMasuk = findViewById(R.id.tvTotalMasuk);
        tvTotalKeluar = findViewById(R.id.tvTotalKeluar);
        tCaption = findViewById(R.id.tCaption);
        tCaption3 = findViewById(R.id.tCaption3);
        tvSaldo = findViewById(R.id.tvSaldo);
        v = findViewById(android.R.id.content);
        spTipe = findViewById(R.id.spTipe);
        // RecyclerView Stuff
        adapterLaporanKeuangan = new AdapterLaporanKeuangan(this, daftarLaporanKeuangan);
        reDaftarLaporanKeuangan.setLayoutManager(new LinearLayoutManager(this));
        reDaftarLaporanKeuangan.setHasFixedSize(true);
        reDaftarLaporanKeuangan.setAdapter(adapterLaporanKeuangan);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    void textChangedListenerManager() {
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getDaftarLaporanKeuangan(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDaftarLaporanKeuangan("");
    }

    void handleSpinnerChange() {
        spTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDaftarLaporanKeuangan(eCari.getText().toString());
                switch (position) {
                    case 0:
                        tvTotalMasuk.setVisibility(View.VISIBLE);
                        tCaption.setVisibility(View.VISIBLE);

                        tvTotalKeluar.setVisibility(View.VISIBLE);
                        tCaption3.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        tvTotalMasuk.setVisibility(View.VISIBLE);
                        tCaption.setVisibility(View.VISIBLE);

                        tvTotalKeluar.setVisibility(View.GONE);
                        tCaption3.setVisibility(View.GONE);
                        break;
                    case 2:
                        tvTotalMasuk.setVisibility(View.GONE);
                        tCaption.setVisibility(View.GONE);

                        tvTotalKeluar.setVisibility(View.VISIBLE);
                        tCaption3.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void setDisplayedDate() {
        int year, month;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;

        dari = FFunctionKredit.getStandardDate(year, month, 1);
        ke = FFunctionKredit.getStandardDate(year, month, c.get(Calendar.DAY_OF_MONTH));
        String dari = FFunctionKredit.getDateWithSlash(year, month, 1);
        String ke = FFunctionKredit.getDateWithSlash(year, month, c.get(Calendar.DAY_OF_MONTH));

        FFunctionKredit.setText(v, R.id.eDari, dari);
        FFunctionKredit.setText(v, R.id.eKe, ke);
    }

    @SuppressLint("Range")
    void getDaftarLaporanKeuangan(String cari) {
        String whereClause = "";
        switch (spTipe.getSelectedItemPosition()) {
            case 0:
                whereClause = "1=1";
                break;
            case 1:
                whereClause = "keluar = 0";
                break;
            case 2:
                whereClause = "masuk = 0";
                break;
        }

        daftarLaporanKeuangan.clear();
        String sql = "SELECT * FROM tblkeuangan WHERE (keterangan LIKE '%" + cari + "%' OR faktur LIKE '%" + cari + "%') AND (tglkeuangan BETWEEN '" + dari + "' AND '" + ke + "') AND " + whereClause + " ORDER BY no_transaksi DESC";
        Cursor res = conn.db.rawQuery(sql, null);
        if (res.getCount() > 0) {
            double totalMasuk = 0, totalKeluar = 0, saldo = 0;
            tvJumlah.setText("" + res.getCount());
            while (res.moveToNext()) {
                try {
                    daftarLaporanKeuangan.add(new ModelKeuanganKredit(
                            res.getString(res.getColumnIndex("keterangan")),
                            res.getString(res.getColumnIndex("faktur")),
                            res.getString(res.getColumnIndex("tglkeuangan")),
                            res.getDouble(res.getColumnIndex("masuk")),
                            res.getDouble(res.getColumnIndex("keluar")),
                            res.getInt(res.getColumnIndex("no_transaksi"))
                    ));
                    totalMasuk += res.getDouble(res.getColumnIndex("masuk"));
                    totalKeluar += res.getDouble(res.getColumnIndex("keluar"));
                } catch (Exception e) {
                    Log.e("err", e.getMessage());
                }
            }
            tvTotalMasuk.setText(ModuleKredit.numFormat(totalMasuk));
            tvTotalKeluar.setText(ModuleKredit.numFormat(totalKeluar));
        } else {
            tvJumlah.setText("0");
            tvTotalMasuk.setText("0");
            tvTotalKeluar.setText("0");
        }
        tvSaldo.setText(ModuleKredit.numFormat(getSaldo()));
        adapterLaporanKeuangan.notifyDataSetChanged();
    }

    @SuppressLint("Range")
    double getSaldo() {
        Cursor c = conn.sq("SELECT * FROM tblkeuangan ORDER BY no_transaksi DESC");
        if (c.getCount() > 0) {
            c.moveToFirst();
            return c.getDouble(c.getColumnIndex("saldo"));
        } else {
            return 0;
        }
    }

    @SuppressLint("Range")
    int getLastNoTransaksi() {
        Cursor c = conn.sq("SELECT * FROM tblkeuangan ORDER BY no_transaksi DESC");
        if (c.getCount() > 0) {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex("no_transaksi"));
        } else {
            return 0;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        int year, month;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, 1);
        } else if (id == 2) {
            return new DatePickerDialog(this, edit2, year, month, c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eDari, FFunctionKredit.getDateWithSlash(thn, bln + 1, day));
            dari = FFunctionKredit.getStandardDate(thn, bln + 1, 1);
            getDaftarLaporanKeuangan(eCari.getText().toString());
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eKe, FFunctionKredit.getDateWithSlash(thn, bln + 1, day));
            ke = FFunctionKredit.getStandardDate(thn, bln + 1, day);
            getDaftarLaporanKeuangan(eCari.getText().toString());
        }
    };

    public void dateDari(View view) {
        showDialog(1);
    }

    public void dateKe(View view) {
        showDialog(2);
    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcelKredit.class);
        i.putExtra("type", "keuangan");
        startActivity(i);
    }

    public void hapusKeuangan(View view) {
        ModuleKredit.yesNoDialog(this, "Peringatan", "Yakin ingin menghapus catatan keuangan ?", (dialog, which) -> {
            String sql = "DELETE FROM tblkeuangan WHERE no_transaksi = '" + view.getTag().toString() + "'";
            if (conn.exc(sql)) {
                ModuleKredit.info(this, "Berhasil hapus catatan keuangan");
                getDaftarLaporanKeuangan(eCari.getText().toString());
            }
        }, (dialog, which) -> dialog.dismiss());
    }
}


class AdapterLaporanKeuangan extends RecyclerView.Adapter<AdapterLaporanKeuangan.DaftarLaporanKeuanganViewHolder> {

    private Context ctx;
    private List<ModelKeuanganKredit> daftarLaporanKeuangan;

    AdapterLaporanKeuangan(Context ctx, List<ModelKeuanganKredit> daftarLaporanKeuangan) {
        this.ctx = ctx;
        this.daftarLaporanKeuangan = daftarLaporanKeuangan;
    }

    @Override
    public DaftarLaporanKeuanganViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.list_item_keuangan_kredit, parent, false);
        return new DaftarLaporanKeuanganViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DaftarLaporanKeuanganViewHolder holder, int position) {
        ModelKeuanganKredit row = daftarLaporanKeuangan.get(position);
        holder.tvFaktur.setText(row.getFaktur());
        holder.tvKet.setText(row.getKet());
        holder.tvMasuk.setText(ModuleKredit.numFormat(row.getMasuk()));
        holder.tvKeluar.setText(ModuleKredit.numFormat(row.getKeluar()));
        holder.tvTgl.setText(ModuleKredit.getDateString(row.getTgl(), "dd MMM yyyy"));
        holder.bHapus.setTag(row.getNoTransaksi());
        if (row.getNoTransaksi() != ((ActivityLaporanKeuanganKredit) ctx).getLastNoTransaksi()) {
            holder.bHapus.setVisibility(View.INVISIBLE);
        } else {
            holder.bHapus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return daftarLaporanKeuangan.size();
    }

    class DaftarLaporanKeuanganViewHolder extends RecyclerView.ViewHolder {

        TextView tvFaktur, tvKet, tvMasuk, tvKeluar, tvTgl;
        ImageView bHapus;

        DaftarLaporanKeuanganViewHolder(View itemView) {
            super(itemView);
            tvFaktur = itemView.findViewById(R.id.tvFaktur);
            tvKet = itemView.findViewById(R.id.tvKet);
            tvMasuk = itemView.findViewById(R.id.tvMasuk);
            tvKeluar = itemView.findViewById(R.id.tvKeluar);
            tvTgl = itemView.findViewById(R.id.tvTgl);
            bHapus = itemView.findViewById(R.id.bHapus);
        }

    }

}


