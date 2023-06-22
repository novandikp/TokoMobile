package com.itbrain.aplikasitoko.TokoKredit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityLaporanPerJenisKredit extends AppCompatActivity {

    View v;
    FConfigKredit config;
    FKoneksiKredit db;
    String dari, ke, total, type;
    Calendar calendar;
    int year, month, day;
    List<String> daftarLaporanPenjualan;
    AdapterLaporanPerJenis AdapterLaporanPerJenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_per_jenis_kredit);

        nyelect();
        daftarLaporanPenjualan = new ArrayList<>();
        init();

        setText();


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
                switch (type) {
                    case "perpelanggan":
                        loadList(eCari.getText().toString());
                        break;
                    case "perjenis":
                        loadList2(eCari.getText().toString());
                        break;
                    case "perbarang":
                        loadList3(eCari.getText().toString());
                        break;
                    case "perkategori":
                        loadList4(eCari.getText().toString());
                        break;
                }
            }
        });

        setText();
        List<String> categories = new ArrayList<>();
        switch (type) {
            case "perpelanggan":
                loadList("");
                categories = new ArrayList<>();
                categories.add("Semua");
                categories.add("Hutang");
                categories.add("Tunai");
                break;
            case "perjenis":
                loadList2("");
                categories = new ArrayList<>();
                categories.add("Semua");
                categories.add("Kulakan");
                categories.add("Titipan");
                break;
            case "perbarang":
                loadList3("");
                categories = new ArrayList<>();
                categories.add("Semua");
                categories.add("Hutang");
                categories.add("Tunai");
                break;
            case "perkategori":
                loadList4("");
                categories = new ArrayList<>();
                categories.add("Semua");
                Cursor c = db.sq(FQueryKredit.select("tblkategori"));
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        categories.add(FFunctionKredit.getString(c, "kategori"));
                    }
                }
                break;
        }

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                submit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadList2("");

    }
    
    void init() {
        v = this.findViewById(android.R.id.content);
        config = new FConfigKredit(getSharedPreferences("config", MODE_PRIVATE));
        db = new FKoneksiKredit(this, config);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        type = getIntent().getStringExtra("type");
        // RecyclerView Stufff
        daftarLaporanPenjualan = new ArrayList<>();
        AdapterLaporanPerJenis = new AdapterLaporanPerJenis(this, daftarLaporanPenjualan);
        RecyclerView recUtang = findViewById(R.id.recUtang);
        recUtang.setAdapter(AdapterLaporanPerJenis);
        recUtang.setLayoutManager(new LinearLayoutManager(this));
        recUtang.setHasFixedSize(true);
    }

    public void print(View v) {
        String faktur = v.getTag().toString();
        Intent i = new Intent(this, ActivityCetak2Kredit.class);
        i.putExtra("fakturbayar", faktur);
        startActivity(i);
    }

    public void itemnyelect(String selected) {
        String q = "";
        String bayar = "";
        q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("flagbayar", bayar) + " AND " + FQueryKredit.sWhere("kategori", selected);
        daftarLaporanPenjualan.clear();
        Cursor c = db.sq(q);

        RecyclerView recyclerView = findViewById(R.id.recUtang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterLaporanPerJenis(this, daftarLaporanPenjualan);
        recyclerView.setAdapter(adapter);
        if (c.getCount() > 0) {
            double jum = 0;
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String kembali = FFunctionKredit.getString(c, "barang");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String jumlah = FFunctionKredit.getString(c, "jumlahjual");
                String harga = FFunctionKredit.getString(c, "hargajual:1");
                double total = FFunctionKredit.strToDouble(harga) * FFunctionKredit.strToDouble(jumlah);

                String campur = faktur + "__" + pelanggan + "__" + kembali + "__" + jumlah + " x " + FFunctionKredit.removeE(harga) + " = " + FFunctionKredit.removeE(total) + "__" + FFunctionKredit.dateToNormal(FFunctionKredit.getString(c, "tgljual"));
                daftarLaporanPenjualan.add(campur);
                jum += total;
            }
            String a = FFunctionKredit.removeE(jum);
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. " + a);
        } else {
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. 0");
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void nyelect() {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = String.valueOf(parent.getItemAtPosition(position));
                if(selected == "semua"){
                    loadList4("");
                }else{
                    itemnyelect(selected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setText() {
        dari = FFunctionKredit.setDatePicker(year, month, 1);
        ke = FFunctionKredit.setDatePicker(year, month, day);
        String dari = FFunctionKredit.setDatePickerNormal(year, month + 1, 1);
        String ke = FFunctionKredit.setDatePickerNormal(year, month + 1, day);

        FFunctionKredit.setText(v, R.id.eDari, dari);
        FFunctionKredit.setText(v, R.id.eKe, ke);
    }

    public void loadList(String cari) {
        String item = FFunctionKredit.getSpinnerItem(this, R.id.spinner);
        String bayar = "";
        String q = "";
        switch (item) {
            case "Semua":
                q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sLike("pelanggan", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
                break;
            case "Hutang":
                bayar = "0";
                q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("flagbayar", bayar) + " AND " + FQueryKredit.sLike("pelanggan", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
                break;
            case "Tunai":
                bayar = "1";
                q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("flagbayar", bayar) + " AND " + FQueryKredit.sLike("pelanggan", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
                break;
        }
        daftarLaporanPenjualan.clear();

        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            double jum = 0;
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String kembali = FFunctionKredit.getString(c, "barang");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String jumlah = FFunctionKredit.getString(c, "jumlahjual");
                String harga = FFunctionKredit.getString(c, "hargajual:1");
                double total = FFunctionKredit.strToDouble(harga) * FFunctionKredit.strToDouble(jumlah);

                String campur = faktur + "__" + pelanggan + "__" + kembali + "__" + jumlah + " x " + FFunctionKredit.removeE(harga) + " = " + FFunctionKredit.removeE(total) + "__" + FFunctionKredit.dateToNormal(FFunctionKredit.getString(c, "tgljual"));
                daftarLaporanPenjualan.add(campur);
                jum += total;
            }
            String a = FFunctionKredit.removeE(jum);
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. " + a);
        } else {
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. 0");
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        AdapterLaporanPerJenis.notifyDataSetChanged();
    }

    public void loadList2(String cari) {
        String item = FFunctionKredit.getSpinnerItem(this, R.id.spinner);
        String bayar = "";
        String q = "";
        if (item.equals("Semua")) {
            q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
        } else if (item.equals("Kulakan")) {
            bayar = "0";
            q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("titipan", bayar) + " AND " + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
        } else if (item.equals("Titipan")) {
            bayar = "1";
            q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("titipan", bayar) + " AND " + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
        }
        daftarLaporanPenjualan.clear();

        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            double jum = 0;
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String kembali = FFunctionKredit.getString(c, "barang");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String jumlah = FFunctionKredit.getString(c, "jumlahjual");
                String harga = FFunctionKredit.getString(c, "hargajual:1");
                double total = FFunctionKredit.strToDouble(harga) * FFunctionKredit.strToDouble(jumlah);

                String campur = faktur + "__" + pelanggan + "__" + kembali + "__" + jumlah + " x " + FFunctionKredit.removeE(harga) + " = " + FFunctionKredit.removeE(total) + "__" + FFunctionKredit.dateToNormal(FFunctionKredit.getString(c, "tgljual"));
                daftarLaporanPenjualan.add(campur);
                jum += total;
            }
            String a = FFunctionKredit.removeE(jum);
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. " + a);
        } else {
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. 0");
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        AdapterLaporanPerJenis.notifyDataSetChanged();
    }

    public void loadList3(String cari) {
        String item = FFunctionKredit.getSpinnerItem(this, R.id.spinner);
        String bayar = "";
        String q = "";
        switch (item) {
            case "Semua":
                q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
                break;
            case "Hutang":
                bayar = "0";
                q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("flagbayar", bayar) + " AND " + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
                break;
            case "Tunai":
                bayar = "1";
                q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("flagbayar", bayar) + " AND " + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
                break;
        }
        daftarLaporanPenjualan.clear();

        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            double jum = 0;
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String kembali = FFunctionKredit.getString(c, "barang");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String jumlah = FFunctionKredit.getString(c, "jumlahjual");
                String harga = FFunctionKredit.getString(c, "hargajual:1");
                double total = FFunctionKredit.strToDouble(harga) * FFunctionKredit.strToDouble(jumlah);

                String campur = faktur + "__" + pelanggan + "__" + kembali + "__" + jumlah + " x " + FFunctionKredit.removeE(harga) + " = " + FFunctionKredit.removeE(total) + "__" + FFunctionKredit.dateToNormal(FFunctionKredit.getString(c, "tgljual"));
                daftarLaporanPenjualan.add(campur);
                jum += total;
            }
            String a = FFunctionKredit.removeE(jum);
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. " + a);
        } else {
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. 0");
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        AdapterLaporanPerJenis.notifyDataSetChanged();
    }

    public void loadList4(String cari) {
        String item = FFunctionKredit.getSpinnerItem(this, R.id.spinner);
        String q = "";
        if (item.equals("Semua")) {
            q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
        } else {
            q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sWhere("kategori", item) + " AND " + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderASC("tgljual");
        }
        daftarLaporanPenjualan.clear();

        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            double jum = 0;
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String pelanggan = FFunctionKredit.getString(c, "pelanggan");
                String kembali = FFunctionKredit.getString(c, "barang");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String jumlah = FFunctionKredit.getString(c, "jumlahjual");
                String harga = FFunctionKredit.getString(c, "hargajual:1");
                double total = FFunctionKredit.strToDouble(harga) * FFunctionKredit.strToDouble(jumlah);

                String campur = faktur + "__" + pelanggan + "__" + kembali + "__" + jumlah + " x " + FFunctionKredit.removeE(harga) + " = " + FFunctionKredit.removeE(total) + "__" + FFunctionKredit.dateToNormal(FFunctionKredit.getString(c, "tgljual"));
                daftarLaporanPenjualan.add(campur);
                jum += total;
            }
            String a = FFunctionKredit.removeE(jum);
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. " + a);
        } else {
            FFunctionKredit.setText(v, R.id.tTotal, "Rp. 0");
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : 0");
        }
        AdapterLaporanPerJenis.notifyDataSetChanged();
    }

    public void dateDari(View view) {
        setDate(1);
    }

    public void dateKe(View view) {
        setDate(2);
    }

    public void submit() {
        try {
            switch (type) {
                case "perpelanggan":
                    loadList(FFunctionKredit.getText(v, R.id.eCari));
                    break;
                case "perjenis":
                    loadList2(FFunctionKredit.getText(v, R.id.eCari));
                    break;
                case "perbarang":
                    loadList3(FFunctionKredit.getText(v, R.id.eCari));
                    break;
                case "perkategori":
                    loadList4(FFunctionKredit.getText(v, R.id.eCari));
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void export(View view) {
        Intent i = new Intent(this, ActivityExportExcelKredit.class);
        i.putExtra("type", "laporanpenjualan");
        startActivity(i);
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
            return new DatePickerDialog(this, edit1, year, month, 1);
        } else if (id == 2) {
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eDari, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
            dari = FFunctionKredit.setDatePicker(thn, bln + 1, 1);
            submit();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eKe, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
            ke = FFunctionKredit.setDatePicker(thn, bln + 1, day);
            submit();
        }
    };
    //end date time picker
}

class AdapterLaporanPerJenis extends RecyclerView.Adapter<AdapterLaporanPerJenis.ViewHolder> {
    private List<String> data;
    Context c;

    AdapterLaporanPerJenis(Context a, List<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_laporan_penjualan_kredit_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur, nma, jumlah, tanggal;
        ConstraintLayout print;

        ViewHolder(View view) {
            super(view);
            nma = view.findViewById(R.id.tHitung);
            tanggal = view.findViewById(R.id.tTanggal);
            faktur = view.findViewById(R.id.tNama);
            jumlah = view.findViewById(R.id.tBarang);
            print = view.findViewById(R.id.wHapus);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.jumlah.setText(row[2] + "\t : " + row[3]);
        viewHolder.nma.setText(row[1]);
        viewHolder.tanggal.setText(row[4]);
        viewHolder.faktur.setText(row[0]);
        viewHolder.print.setTag(row[0]);
    }
}







