package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Penjualan_Apotek_ extends AppCompatActivity {
    int year, day, month;
    Calendar calendar;
    ArrayList arrayList = new ArrayList();
    DatabaseApotek db;
    View v;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_penjualan_apotek_);

        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        getjual("");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String tgl = ModulApotek.getDate("dd/MM/yyyy");
        ModulApotek.setText(v, R.id.tgl1, tgl);
        ModulApotek.setText(v, R.id.tgl2, tgl);
        type = getIntent().getStringExtra("type");
        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final EditText eCari = (EditText) findViewById(R.id.dicari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString();
                arrayList.clear();

                getjual(a);
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

    public void getjual(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcLapPenjualan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Adapter_Laporan_Dua_Apotek(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("qdetailjual") + ModulApotek.sLike("fakturjual", cari) + " OR " + ModulApotek.sLike("pelanggan", cari) + " AND " + ModulApotek.sBetween("tgljual", ModulApotek.getText(v, R.id.tgl1), ModulApotek.getText(v, R.id.tgl2));
        double total = 0;
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            double jumlah = ModulApotek.strToDouble(ModulApotek.getString(c, "jumlahjual")) * ModulApotek.strToDouble(ModulApotek.getString(c, "hargajual"));
            total = total + jumlah;
            String campur = ModulApotek.getString(c, "idjual") + "__" + ModulApotek.getString(c, "fakturjual") + "__" + ModulApotek.getString(c, "tgljual") + "__" + ModulApotek.getString(c, "pelanggan") + "__" + ModulApotek.getString(c, "barang") + "__" + ModulApotek.getString(c, "jumlahjual") + "__" + ModulApotek.getString(c, "satuanjual") + "__" + ModulApotek.getString(c, "hargajual") + "__" + ModulApotek.getString(c, "batchnumber");
            arrayList.add(campur);
        }
        setTotal(total);
        adapter.notifyDataSetChanged();

    }

    private void setTotal(double total) {
        ModulApotek.setText(v, R.id.saldoo, "Total: Rp." + ModulApotek.removeE(total));
    }

    public void tgl1(View view) {
        showDialog(1);
    }

    public void tgl2(View view) {
        showDialog(2);
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, date1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulApotek.setText(v, R.id.tgl1, ModulApotek.setDatePickerNormal(thn, bln + 1, day));
            getjual("");

        }
    };

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulApotek.setText(v, R.id.tgl2, ModulApotek.setDatePickerNormal(thn, bln + 1, day));
            getjual("");
        }
    };
    public void export(View view) {
        Intent i = new Intent(this, Menu_Export_Exel_Apotek.class);
        i.putExtra("type", "penjualan");
        startActivity(i);
    }
}

