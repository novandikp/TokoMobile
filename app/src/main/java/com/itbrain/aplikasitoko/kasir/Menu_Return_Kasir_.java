package com.itbrain.aplikasitoko.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
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

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Menu_Return_Kasir_ extends AppCompatActivity {

    View v;
    FConfigKasir config;
    DatabaseKasir db;
    ArrayList arrayList = new ArrayList();
    String dari, ke;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_return_kasir_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        v = this.findViewById(android.R.id.content);
        config = new FConfigKasir(getSharedPreferences("config", this.MODE_PRIVATE));
        db = new DatabaseKasir(this, config);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);



        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final EditText eCari = (EditText) findViewById(R.id.sPencarianReturn);
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
        dari = FFunctionKasir.setDatePicker(year, month + 1, day);
        ke = FFunctionKasir.setDatePicker(year, month + 1, day);
        String now = FFunctionKasir.setDatePickerNormal(year, month + 1, day);
        FFunctionKasir.setText(v, R.id.eKe, now);
        FFunctionKasir.setText(v, R.id.eDari, now);
    }

    public void loadList(String cari) {
        arrayList.clear();
        String q = "";
        if (TextUtils.isEmpty(cari)) {
            q = FQueryKasir.selectwhere("qpenjualan") + FQueryKasir.sBetween("tgljual", dari, ke) + FQueryKasir.sOrderDESC("tgljual");
        } else {
            q = FQueryKasir.selectwhere("qpenjualan") + FQueryKasir.sLike("barang", cari) + " AND " + FQueryKasir.sBetween("tgljual", dari, ke) + FQueryKasir.sOrderDESC("tgljual");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcvReturn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterReturn(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (FFunctionKasir.getCount(c) > 0) {
            FFunctionKasir.setText(v, R.id.tJumlahReturn, "Jumlah Data : " + FFunctionKasir.intToStr(FFunctionKasir.getCount(c)));
            while (c.moveToNext()) {
                String nama = FFunctionKasir.getString(c, "pelanggan");
                String barang = FFunctionKasir.getString(c, "barang");
                String faktur = FFunctionKasir.getString(c, "fakturbayar");
                String jumlah = FFunctionKasir.getString(c, "jumlahjual");
                String id = FFunctionKasir.getString(c, "idpenjualan");

                if (!jumlah.equals("0")) {
                    String campur = nama + "__" + barang + "__" + faktur + "__" + jumlah + "__" + id;
                    arrayList.add(campur);
                }
            }
        } else {
            FFunctionKasir.setText(v, R.id.tJumlahReturn, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void filtertgl() {
        loadList(FFunctionKasir.getText(v, R.id.sPencarianReturn));
    }

    public void dateDari(View view) {
        setDate(1);
    }

    public void dateKe(View view) {
        setDate(2);
    }

    public void bayarutang(View view) {
        String id = view.getTag().toString();
        Intent i = new Intent(this, ActivityReturnProsesKasir.class);
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
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKasir.setText(v, R.id.eDari, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
            dari = FFunctionKasir.setDatePicker(thn, bln + 1, day);
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            FFunctionKasir.setText(v, R.id.eKe, FFunctionKasir.setDatePickerNormal(thn, bln + 1, day));
            ke = FFunctionKasir.setDatePicker(thn, bln + 1, day);
            filtertgl();
        }
    };


    class AdapterReturn extends RecyclerView.Adapter<AdapterReturn.ViewHolder> {
        private ArrayList<String> data;
        Context c;

        public AdapterReturn(Context a, ArrayList<String> kota) {
            this.data = kota;
            c = a;
        }

        @Override
        public AdapterReturn.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_utang_item_kasir, viewGroup, false);
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

                nma = (TextView) view.findViewById(R.id.tBarang);
                faktur = (TextView) view.findViewById(R.id.tNama);
                jumlah = (TextView) view.findViewById(R.id.tHitung);
                wadah = (ConstraintLayout) view.findViewById(R.id.wUtang2);
            }
        }

        @Override
        public void onBindViewHolder(AdapterReturn.ViewHolder viewHolder, int i) {
            String[] row = data.get(i).split("__");

            viewHolder.jumlah.setText("Jumlah : " + row[3]);
            viewHolder.nma.setText(row[1]);
            viewHolder.faktur.setText(row[2] + "\t \t Pelanggan : " + row[0]);
            viewHolder.wadah.setTag(row[4]);


            //end date time picker
        }
    }
}



