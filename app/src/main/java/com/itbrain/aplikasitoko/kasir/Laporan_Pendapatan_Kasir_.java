package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Pendapatan_Kasir_ extends AppCompatActivity {

    String type;
    View v;
    FConfigKasir config;
    DatabaseKasir db;
    ArrayList arrayList = new ArrayList();
    String dari, ke;
    Calendar calendar;
    int year, month, day;

    String deviceid;
    SharedPreferences getPrefs;
    boolean bHapus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_pendapatan_kasir_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");

        type = "pendapatan";


        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final EditText eCari = (EditText) findViewById(R.id.lapPen);
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
                getPendapatan(a);
            }
        });

        setText();
        setTitle("Laporan Pendapatan");
        getPendapatan("");
    }

    public void exportLaporanPendapatan(View view){
        Intent i = new Intent(this, ActivityExportExcelKasir.class) ;
        i.putExtra("type",type) ;
        startActivity(i);
    }

    public void setText() {
        dari = FFunctionKasir.setDatePicker(year, month + 1, day);
        ke = FFunctionKasir.setDatePicker(year, month + 1, day);
        String now = FFunctionKasir.setDatePickerNormal(year, month + 1, day);
        FFunctionKasir.setText(v, R.id.mumu, now);
        FFunctionKasir.setText(v, R.id.abu, now);
    }

    public void getPendapatan(String cari) {
        arrayList.clear();
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKasir.selectwhere("qbayar") + FQueryKasir.sBetween("tglbayar", dari, ke);
        } else {
            q = FQueryKasir.selectwhere("qbayar") + FQueryKasir.sLike("pelanggan", cari) + " AND " + FQueryKasir.sBetween("tglbayar", dari, ke);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvLaporanPendapatan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLabaRugi(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (FFunctionKasir.getCount(c) > 0) {
            double total = 0;
            double back = 0;
            double pay = 0;
            while (c.moveToNext()) {
                String nama = FFunctionKasir.getString(c, "pelanggan");
                String jumlah = FFunctionKasir.getString(c, "jumlahbayar");
                String bayar = FFunctionKasir.getString(c, "bayar");
                String kembali = FFunctionKasir.getString(c, "kembali");
                String tgl = FFunctionKasir.getString(c, "tglbayar");
                String faktur = FFunctionKasir.getString(c, "fakturbayar");

                String campur = FFunctionKasir.getCampur(nama + "\n" + FFunctionKasir.dateToNormal(tgl), "Total   : Rp. " + FFunctionKasir.removeE(jumlah),
                        "Bayar   : Rp. " + FFunctionKasir.removeE(bayar),
                        "Kembali : Rp. " + FFunctionKasir.removeE(kembali), faktur);
                arrayList.add(campur);
                total += FFunctionKasir.strToDouble(jumlah);
                back += FFunctionKasir.strToDouble(kembali);
                pay += FFunctionKasir.strToDouble(bayar);
            }
            FFunctionKasir.setText(v, R.id.penda, "Pendapatan \t: Rp. " + FFunctionKasir.removeE(total));
            FFunctionKasir.setText(v, R.id.bali, "Kembali \t\t: Rp.  " + FFunctionKasir.removeE(back));
            FFunctionKasir.setText(v, R.id.bayarr, "Pembayaran \t: Rp.  " + FFunctionKasir.removeE(pay));
            FFunctionKasir.setText(v, R.id.juda, "Jumlah Data : " + String.valueOf(FFunctionKasir.getCount(c)));
        } else {
            FFunctionKasir.setText(v, R.id.penda, "Pendapatan : Rp. 0");
            FFunctionKasir.setText(v, R.id.bali, "Kembali    : Rp. 0");
            FFunctionKasir.setText(v, R.id.bayarr, "Pembayaran : Rp. 0");
            FFunctionKasir.setText(v, R.id.juda, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void abu(View view) {
        setDate(1);
    }

    public void mumu(View view) {
        setDate(2);
    }

    public void submit() {
        String a = FFunctionKasir.getText(v, R.id.lapPen);
        getPendapatan(a);
    }

    public void open(final String faktur) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin akan menghapus transaksi ini?");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        if (!faktur.equals("tidak")) {
                            Cursor c = db.sq(FQueryKasir.selectwhere("tblpenjualan") + FQueryKasir.sWhere("fakturbayar", faktur));
                            if (FFunctionKasir.getCount(c) > 0) {
                                while (c.moveToNext()) {
                                    String id = FFunctionKasir.getString(c, "idpenjualan");
                                    db.exc("DELETE FROM tblpenjualan WHERE idpenjualan='" + id + "'");
                                }
                            }
                            db.exc("DELETE FROM tblbayar WHERE fakturbayar='" + faktur + "'");
                            Toast.makeText(Laporan_Pendapatan_Kasir_.this, "Hapus Berhasil", Toast.LENGTH_SHORT).show();
                            getPendapatan("");
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
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
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKasir.setText(v, R.id.abu, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
            dari = FFunctionKasir.setDatePicker(thn, bln + 1, day);
            submit();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKasir.setText(v, R.id.mumu, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
            ke = FFunctionKasir.setDatePicker(thn, bln + 1, day);
            submit();
        }
    };
    //end date time picker

    public void delete(View view) {
        String faktur = view.getTag().toString();
        open(faktur);
    }


//    class AdapterPendapatan extends RecyclerView.Adapter<AdapterPendapatan.ViewHolder> {
//        private ArrayList<String> data;
//        Context c;
//
//        public AdapterPendapatan(Context a, ArrayList<String> kota) {
//            this.data = kota;
//            c = a;
//        }
//
//        @Override
//        public AdapterPendapatan.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_transaksi_kasir_pendapatan, viewGroup, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public int getItemCount() {
//            return data.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            private TextView faktur, nma, jumlah;
//
//            public ViewHolder(View view) {
//                super(view);
//
//                nma = (TextView) view.findViewById(R.id.tHitung);
//                faktur = (TextView) view.findViewById(R.id.tBarang);
//                jumlah = (TextView) view.findViewById(R.id.tNama);
//            }
//        }
//
//        @Override
//        public void onBindViewHolder(AdapterPendapatan.ViewHolder viewHolder, int i) {
//            String[] row = data.get(i).split("__");
//
//            viewHolder.jumlah.setText(row[2]);
//            viewHolder.nma.setText(row[1]);
//            viewHolder.faktur.setText(row[0]);
//        }
//    }
//

    class AdapterLabaRugi extends RecyclerView.Adapter<AdapterLabaRugi.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterLabaRugi(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public AdapterLabaRugi.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_transaksi_kasir_labarugi, viewGroup, false);
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

                hitung = (TextView) view.findViewById(R.id.tHitung);
                barang = (TextView) view.findViewById(R.id.tBarang);
                laba = (TextView) view.findViewById(R.id.tLaba);
                faktur = (TextView) view.findViewById(R.id.tFaktur);
                hapus = (ConstraintLayout) view.findViewById(R.id.wHapus);
            }
        }

        @Override
        public void onBindViewHolder(AdapterLabaRugi.ViewHolder viewHolder, int i) {
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
}

