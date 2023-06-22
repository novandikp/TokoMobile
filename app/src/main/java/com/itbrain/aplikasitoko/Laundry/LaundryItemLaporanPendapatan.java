package com.itbrain.aplikasitoko.Laundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class LaundryItemLaporanPendapatan extends AppCompatActivity {
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
        setContentView(R.layout.laporan_pendapatan_laundry);
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
        final ConstraintLayout cTanggal = (ConstraintLayout) findViewById(R.id.cTanggal);


//        button tgl
        String qFirstDate = "";
        String kolomTanggal = "";
        qFirstDate = QueryLaundry.select("tbllaundry");
        kolomTanggal = "tgllaundry";
        Cursor firstDate = db.sq(qFirstDate);
        dateawal = ModulLaundry.getDate("yyyyMMdd");
        if (firstDate.getCount() > 0) {
            firstDate.moveToFirst();
            dateawal = ModulLaundry.getString(firstDate, kolomTanggal);
        }
        datesampai = ModulLaundry.getDate("yyyyMMdd");
        ModulLaundry.setText(v, R.id.btnTglAwal, ModulLaundry.dateToNormal(dateawal));
        ModulLaundry.setText(v, R.id.btnTglSampai, ModulLaundry.getDate("dd/MM/yyyy"));
        Button btnTglTerima = (Button) findViewById(R.id.btnTglAwal);
        btnTglTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
        Button btnTglSelesai = (Button) findViewById(R.id.btnTglSampai);
        btnTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
            }
        });

        getpendapatan("", dateawal + "__" + datesampai);
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
                cari = s.toString();
                getpendapatan(cari, dateawal + "__" + datesampai);
            }
        });
    }

    private void tglupdate() {
        String keyword = ModulLaundry.getText(v, R.id.edtCariii);
        getpendapatan(keyword, dateawal + "__" + datesampai);
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, dSelesai, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            dateawal = ModulLaundry.convertDate(ModulLaundry.setDatePickerNormal(thn, bln + 1, day));
            ModulLaundry.setText(v, R.id.btnTglAwal, ModulLaundry.setDatePickerNormal(thn, bln + 1, day));
            tglupdate();
        }
    };
    private DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            datesampai = ModulLaundry.convertDate(ModulLaundry.setDatePickerNormal(thn, bln + 1, day));
            ModulLaundry.setText(v, R.id.btnTglSampai, ModulLaundry.setDatePickerNormal(thn, bln + 1, day));
            tglupdate();
        }
    };

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

    private void getpendapatan(String keyword, String date) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recListLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterLaporanPendapatan(this, arrayList);
        recyclerView.setAdapter(adapter);
        String[] tgl = date.split("__");
        String qTunai = "";
        String qHutang = "";
        if (Integer.valueOf(tgl[0]) <= Integer.valueOf(tgl[1])) {
            if (keyword.isEmpty()) {
                qTunai = QueryLaundry.selectwhere("qbayar") + "(" + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]) + ") AND (" + QueryLaundry.sWhere("statuslaundry", "Selesai") + " AND bayar>0)";
                qHutang = QueryLaundry.selectwhere("qbayarhutang") + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]);
            } else {
                qTunai = QueryLaundry.selectwhere("qbayar") + "((" + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]) + ") AND " + QueryLaundry.sLike("pelanggan", keyword) + ") AND (" + QueryLaundry.sWhere("statuslaundry", "Selesai") + " AND bayar>0)";
                qHutang = QueryLaundry.selectwhere("qbayarhutang") + " (" + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]) + ") AND " + QueryLaundry.sLike("pelanggan", keyword);
            }
        } else {
            qTunai = QueryLaundry.selectwhere("qbayar") + "(" + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]) + ") AND (" + QueryLaundry.sWhere("statuslaundry", "Selesai") + " AND bayar>0)";
            qHutang = QueryLaundry.selectwhere("qbayarhutang") + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]);
            Toast.makeText(this, "Masukkan tanggal dengan benar", Toast.LENGTH_SHORT).show();
        }
        Cursor pendapatan1 = db.sq(QueryLaundry.sSum("tbllaundry", "total") + " WHERE statusbayar='Tunai' AND (" + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]) + ")");
        Cursor pendapatan2 = db.sq(QueryLaundry.sSum("tblbayarhutang", "bayarhutang") + " WHERE " + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]));
        Cursor pendapatan3 = db.sq(QueryLaundry.sSum("tbllaundry", "bayar") + " WHERE statusbayar='Hutang' AND (" + QueryLaundry.sBetween("tglbayar", tgl[0], tgl[1]) + ")");
        pendapatan1.moveToNext();
        pendapatan2.moveToNext();
        pendapatan3.moveToNext();
        Cursor cTunai = db.sq(qTunai);
        Cursor cHutang = db.sq(qHutang);
        if (cTunai.getCount() > 0 || cHutang.getCount() > 0) {
            ModulLaundry.setText(v, R.id.tvJumlahData2, "Jumlah Data Pendapatan : " + String.valueOf(cTunai.getCount() + cHutang.getCount()));
            ModulLaundry.setText(v, R.id.tvPendapatan2, "Jumlah Pendapatan : Rp. " + ModulLaundry.removeE(pendapatan1.getDouble(0) + pendapatan2.getDouble(0) + pendapatan3.getDouble(0)));
            while (cTunai.moveToNext()) {
                String campur = "bayar" + "__" +
                        ModulLaundry.getString(cTunai, "idlaundry") + "__" +
                        ModulLaundry.getString(cTunai, "pelanggan") + "__" +
                        ModulLaundry.getString(cTunai, "total") + "__" +
                        ModulLaundry.getString(cTunai, "bayar") + "__" +
                        ModulLaundry.getString(cTunai, "kembali") + "__" +
                        ModulLaundry.getString(cTunai, "tglbayar") + "__" +
                        ModulLaundry.getString(cTunai, "statusbayar");
                arrayList.add(campur);
            }
            while (cHutang.moveToNext()) {
                String campur = "bayarhutang" + "__" +
                        ModulLaundry.getString(cHutang, "idbayarhutang") + "__" +
                        ModulLaundry.getString(cHutang, "pelanggan") + "__" +
                        ModulLaundry.getString(cHutang, "hutang") + "__" +
                        ModulLaundry.getString(cHutang, "bayar") + "__" +
                        ModulLaundry.getString(cHutang, "kembali") + "__" +
                        ModulLaundry.getString(cHutang, "tglbayar");
                arrayList.add(campur);
            }
        } else {
            ModulLaundry.setText(v, R.id.tvJumlahData2, "Jumlah Data Pendapatan : 0");
            ModulLaundry.setText(v, R.id.tvPendapatan2, "Jumlah Pendapatan : Rp. 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void exportExcel(View view) {
        Intent i = new Intent(LaundryItemLaporanPendapatan.this, Export_Exel_Laundry_2.class);
        i.putExtra("tab", "pendapatan");
        startActivity(i);
    }

    class AdapterLaporanPendapatan extends RecyclerView.Adapter<AdapterLaporanPendapatan.LaporanPendapatanViewHolder> {
        private Context ctx;
        private ArrayList<String> data;

        public AdapterLaporanPendapatan(Context ctx, ArrayList<String> data) {
            this.ctx = ctx;
            this.data = data;
        }

        @NonNull
        @Override
        public LaporanPendapatanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_pendapatan_laundry, viewGroup, false);
            return new LaporanPendapatanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LaporanPendapatanViewHolder holder, int i) {
            String[] row = data.get(i).split("__");
            if (row[0].equals("bayar")) {
                holder.pelanggan.setText(row[7] + " - " + row[2]);
                holder.total.setText("Total : Rp. " + ModulLaundry.removeE(row[3]));
                holder.bayar.setText("Bayar : Rp. " + ModulLaundry.removeE(row[4]));
                if (row[7].equals("Tunai")) {
                    holder.kembali.setText("Kembali : Rp. " + ModulLaundry.removeE(row[5]));
                } else {
                    holder.kembali.setText("Kurang : Rp. " + ModulLaundry.removeE(row[5]));
                }
                holder.tanggal.setText(ModulLaundry.dateToNormal(row[6]));
            } else if (row[0].equals("bayarhutang")) {
                holder.pelanggan.setText("Bayar Hutang - " + row[2]);
                holder.total.setText("Total : Rp. " + ModulLaundry.removeE(row[3]));
                holder.bayar.setText("Bayar : Rp. " + ModulLaundry.removeE(row[4]));
                if (Double.valueOf(ModulLaundry.unNumberFormat(ModulLaundry.removeE(row[5]))) < 0) {
                    holder.kembali.setText("Kurang : Rp. " + ModulLaundry.removeE(row[5]));
                } else {
                    holder.kembali.setText("Kembali : Rp. " + ModulLaundry.removeE(row[5]));
                }
                holder.tanggal.setText(ModulLaundry.dateToNormal(row[6]));
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class LaporanPendapatanViewHolder extends RecyclerView.ViewHolder {
            TextView pelanggan, total, bayar, kembali, tanggal;

            public LaporanPendapatanViewHolder(@NonNull View itemView) {
                super(itemView);
                pelanggan = (TextView) itemView.findViewById(R.id.tvPelanggan);
                total = (TextView) itemView.findViewById(R.id.tvNominal);
                bayar = (TextView) itemView.findViewById(R.id.tvBayar);
                kembali = (TextView) itemView.findViewById(R.id.tvKembali);
                tanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
            }
        }
    }
}
