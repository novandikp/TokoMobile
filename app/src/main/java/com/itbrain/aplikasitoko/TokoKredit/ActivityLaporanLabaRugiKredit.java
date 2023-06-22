package com.itbrain.aplikasitoko.TokoKredit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityLaporanLabaRugiKredit extends AppCompatActivity {

    String type;
    View v;
    FConfigKredit config;
    FKoneksiKredit db;
    ArrayList arrayList = new ArrayList();
    String dari, ke;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_laba_rugi_kredit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        // Calendar
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("labarugi");

        setText();
        getLabaRugi("");
        submit();

        ImageButton imageButton = findViewById(R.id.imageView59);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

                        getLabaRugi(a);

                }
        });

        setText();
        getLabaRugi("");

    }

    @Override
    protected void onResume(){
        super.onResume();
        getLabaRugi("");

    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcelKredit.class);
        i.putExtra("type", type);
        startActivity(i);
    }

    public void setText() {
        dari = FFunctionKredit.setDatePicker(year, month, 1);
        ke = FFunctionKredit.setDatePicker(year, month, day);
        String dari = FFunctionKredit.setDatePickerNormal(year, month + 1, 1);
        String ke = FFunctionKredit.setDatePickerNormal(year, month + 1, day);

        FFunctionKredit.setText(v, R.id.eDari, dari);
        FFunctionKredit.setText(v, R.id.eKe, ke);
    }

    public void getPendapatan(String cari) {
        arrayList.clear();
        String q = "SELECT * FROM qbayar WHERE (pelanggan LIKE '%" + cari + "%' OR fakturbayar LIKE '%" + cari + "%') AND tglbayar BETWEEN '" + dari + "' AND '" + ke + "'";

        RecyclerView recyclerView = findViewById(R.id.recUtang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPendapatan(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            double total = 0;
            double back = 0;
            double pay = 0;
            while (c.moveToNext()) {
                String nama = FFunctionKredit.getString(c, "pelanggan");
                String jumlah = FFunctionKredit.getString(c, "jumlahbayar");
                String bayar = FFunctionKredit.getString(c, "bayar");
                String kembali = FFunctionKredit.getString(c, "kembali");
                String tgl = FFunctionKredit.getString(c, "tglbayar");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");

                String campur = FFunctionKredit.getCampur(nama + "\n" + ModuleKredit.getDateString(tgl, "dd MMM yyyy"), "Total   : Rp. " + FFunctionKredit.removeE(jumlah),
                        "Bayar   : Rp. " + FFunctionKredit.removeE(bayar),
                        "Kembali : Rp. " + FFunctionKredit.removeE(kembali), faktur);
                arrayList.add(campur);
                total += FFunctionKredit.strToDouble(jumlah);
                back += FFunctionKredit.strToDouble(kembali);
                pay += FFunctionKredit.strToDouble(bayar);
            }
            FFunctionKredit.setText(v, R.id.tCaption, "Pendapatan\t: Rp. " + FFunctionKredit.removeE(total));
            FFunctionKredit.setText(v, R.id.tValue, "Kembali\t\t\t: Rp. " + FFunctionKredit.removeE(back));
            FFunctionKredit.setText(v, R.id.tValue2, "Pembayaran\t: Rp. " + FFunctionKredit.removeE(pay));
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : " + String.valueOf(c.getCount()));
        } else {
            FFunctionKredit.setText(v, R.id.tCaption, "Pendapatan\t: Rp. 0");
            FFunctionKredit.setText(v, R.id.tValue, "Kembali\t\t\t: Rp. 0");
            FFunctionKredit.setText(v, R.id.tValue2, "Pembayaran\t: Rp. 0");
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void getLabaRugi(String cari) {
        arrayList.clear();
        String q = "SELECT * FROM qpenjualan WHERE (barang LIKE '%" + cari + "%' OR fakturbayar LIKE '%" + cari + "%') AND tgljual BETWEEN '" + dari + "' AND '" + ke + "'";

        RecyclerView recyclerView = findViewById(R.id.recUtang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLabaRugi(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            double total = 0;
            while (c.moveToNext()) {
                String barang = FFunctionKredit.getString(c, "barang");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String jumlah = FFunctionKredit.getString(c, "labarugi");
                String jumbarang = FFunctionKredit.getString(c, "jumlahjual");
                String tgl = FFunctionKredit.getString(c, "tgljual");
                double laba = FFunctionKredit.strToDouble(jumbarang) * FFunctionKredit.strToDouble(jumlah);

                String campur = FFunctionKredit.getCampur(barang, jumbarang + " x Rp. " + FFunctionKredit.removeE(jumlah), "Laba : Rp. " + FFunctionKredit.removeE(laba), faktur + "\n" + ModuleKredit.getDateString(tgl, "dd MMM yyyy"), "tidak");
                arrayList.add(campur);
                total += laba;
            }
            FFunctionKredit.setText(v, R.id.tCaption, "Jumlah Laba :");
            FFunctionKredit.setText(v, R.id.tValue, "Rp. " + FFunctionKredit.removeE(total));
            FFunctionKredit.setText(v, R.id.tValue2, "");
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : " + String.valueOf(c.getCount()));
        } else {
            FFunctionKredit.setText(v, R.id.tCaption, "Jumlah Laba :");
            FFunctionKredit.setText(v, R.id.tValue, "Rp. 0");
            FFunctionKredit.setText(v, R.id.tValue2, "");
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void getHutang(String cari) {
        arrayList.clear();
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKredit.selectwhere("qbayar") + FQueryKredit.sWhere("flagbayar", "0") + " AND " + FQueryKredit.sBetween("tglbayar", dari, ke);
        } else {
            q = FQueryKredit.selectwhere("qbayar") + FQueryKredit.sWhere("flagbayar", "0") + " AND " + FQueryKredit.sLike("pelanggan", cari) + " AND " + FQueryKredit.sBetween("tglbayar", dari, ke);
        }

        RecyclerView recyclerView = findViewById(R.id.recUtang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPendapatan(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            double total = 0;
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : " + String.valueOf(c.getCount()));
            while (c.moveToNext()) {
                String nama = FFunctionKredit.getString(c, "pelanggan");
                String tgl = FFunctionKredit.getString(c, "tglbayar");
                String jumlah = FFunctionKredit.getString(c, "kembali");

                String campur = FFunctionKredit.getCampur(nama, "Hutang : Rp. " + FFunctionKredit.removeE(jumlah), FFunctionKredit.dateToNormal(tgl));
                arrayList.add(campur);
                total += FFunctionKredit.strToDouble(jumlah);
            }
            FFunctionKredit.setText(v, R.id.tCaption, "Jumlah Hutang :");
            FFunctionKredit.setText(v, R.id.tValue, "Rp. " + FFunctionKredit.removeE(total));
            FFunctionKredit.setText(v, R.id.tValue2, "");
        } else {
            FFunctionKredit.setText(v, R.id.tCaption, "Jumlah Hutang :");
            FFunctionKredit.setText(v, R.id.tValue, "Rp. 0");
            FFunctionKredit.setText(v, R.id.tValue2, "");
            FFunctionKredit.setText(v, R.id.tJumlahData, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void dateDari(View view) {
        setDate(1);
    }

    public void dateKe(View view) {
        setDate(2);
    }

    public void submit() {
        String a = FFunctionKredit.getText(v, R.id.eCari);
                getLabaRugi(a);
    }

    public void open(final String faktur) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus transaksi ini?");
        alertDialogBuilder.setPositiveButton("Iya", (arg0, arg1) -> {
            //yes
            if (!faktur.equals("tidak")) {
                Cursor c = db.sq(FQueryKredit.selectwhere("tblpenjualan") + FQueryKredit.sWhere("fakturbayar", faktur));
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        String id = FFunctionKredit.getString(c, "idpenjualan");
                        db.exc("DELETE FROM tblpenjualan WHERE idpenjualan='" + id + "'");
                    }
                }
                db.exc("DELETE FROM tblbayar WHERE fakturbayar='" + faktur + "'");
                Toast.makeText(ActivityLaporanLabaRugiKredit.this, "Hapus Berhasil", Toast.LENGTH_SHORT).show();
                getPendapatan("");
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eDari, FFunctionKredit.setDatePickerNormal(thn, bln + 1, 1));
            dari = FFunctionKredit.setDatePicker(thn, bln + 1, 1);
            Log.i("info", "dari" + dari);
            submit();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eKe, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
            ke = FFunctionKredit.setDatePicker(thn, bln + 1, day);
            Log.i("info", "ke : " + ke);
            submit();
        }
    };
    //end date time picker

    public void delete(View view) {
        String faktur = view.getTag().toString();
        open(faktur);
    }
}

class AdapterLabaRugi extends RecyclerView.Adapter<AdapterLabaRugi.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    AdapterLabaRugi(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_transaksi_labarugi_kredit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barang, hitung, laba, faktur;
        ConstraintLayout hapus;

        public ViewHolder(View view) {
            super(view);

            hitung = view.findViewById(R.id.tHitung);
            barang = view.findViewById(R.id.tBarang);
            laba = view.findViewById(R.id.tLaba);
            faktur = view.findViewById(R.id.tFaktur);
            hapus = view.findViewById(R.id.wHapus);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.barang.setText(row[0]);
        viewHolder.hitung.setText(row[1]);
        viewHolder.laba.setText(row[2]);
        viewHolder.faktur.setText(row[3]);
        if (row[4].equals("tidak")) {
            viewHolder.hapus.setVisibility(View.GONE);
        } else {
            viewHolder.hapus.setTag(row[4]);
        }
    }
}
