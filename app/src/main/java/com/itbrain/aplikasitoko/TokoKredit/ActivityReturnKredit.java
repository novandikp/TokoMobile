package com.itbrain.aplikasitoko.TokoKredit;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityReturnKredit extends AppCompatActivity {

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
        setContentView(R.layout.activity_return_kredit);
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
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
                loadList(eCari.getText().toString());
            }
        });

        setText();
        loadList("");
    }

    public void setText() {
        dari = FFunctionKredit.setDatePicker(year, month + 1, 1);
        ke = FFunctionKredit.setDatePicker(year, month + 1, day);
        String dari = FFunctionKredit.setDatePickerNormal(year, month + 1, 1);
        String ke = FFunctionKredit.setDatePickerNormal(year, month + 1, day);

        FFunctionKredit.setText(v, R.id.eDari, dari);
        FFunctionKredit.setText(v, R.id.eKe, ke);
    }

    public void loadList(String cari) {
        arrayList.clear();
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderDESC("tgljual");
        } else {
            q = FQueryKredit.selectwhere("qpenjualan") + FQueryKredit.sLike("barang", cari) + " AND " + FQueryKredit.sBetween("tgljual", dari, ke) + FQueryKredit.sOrderDESC("tgljual");
        }


        RecyclerView recyclerView = findViewById(R.id.recUtang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterReturn(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            FFunctionKredit.setText(v, R.id.tJumlah, "Jumlah Data : " + FFunctionKredit.intToStr(c.getCount()));
            while (c.moveToNext()) {
                String nama = FFunctionKredit.getString(c, "pelanggan");
                String barang = FFunctionKredit.getString(c, "barang");
                String faktur = FFunctionKredit.getString(c, "fakturbayar");
                String jumlah = FFunctionKredit.getString(c, "jumlahjual");
                String id = FFunctionKredit.getString(c, "idpenjualan");

                if (!jumlah.equals("0")) {
                    String campur = nama + "__" + barang + "__" + faktur + "__" + jumlah + "__" + id;
                    arrayList.add(campur);
                }
            }
        } else {

        }
        adapter.notifyDataSetChanged();
    }

    public void filtertgl() {
        loadList(FFunctionKredit.getText(v, R.id.eCari));
    }

    public void dateDari(View view) {
        setDate(1);
    }

    public void dateKe(View view) {
        setDate(2);
    }

    public void bayarutang(View view) {
        String id = view.getTag().toString();
        Intent i = new Intent(this, ActivityReturnProsesKredit.class);
        i.putExtra("idpenjualan", id);
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
            dari = FFunctionKredit.setDatePicker(thn, bln + 1, day);
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKredit.setText(v, R.id.eKe, FFunctionKredit.setDatePickerNormal(thn, bln + 1, day));
            ke = FFunctionKredit.setDatePicker(thn, bln + 1, day);
            filtertgl();
        }
    };
    //end date time picker
}


class AdapterReturn extends RecyclerView.Adapter<AdapterReturn.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterReturn(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_utang_item_kredit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur, nma, jumlah;
        ConstraintLayout wadah;

        public ViewHolder(View view) {
            super(view);

            nma = view.findViewById(R.id.tBarang);
            faktur = view.findViewById(R.id.tNama);
            jumlah = view.findViewById(R.id.tHitung);
            wadah = view.findViewById(R.id.wUtang2);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String[] row = data.get(i).split("__");

        viewHolder.jumlah.setText("Jumlah : " + row[3]);
        viewHolder.nma.setText(row[1]);
        viewHolder.faktur.setText(row[2] + "\t \t Pelanggan : " + row[0]);
        viewHolder.wadah.setTag(row[4]);
    }
}