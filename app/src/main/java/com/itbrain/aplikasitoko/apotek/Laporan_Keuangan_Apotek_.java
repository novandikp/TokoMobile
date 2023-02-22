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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Keuangan_Apotek_ extends AppCompatActivity {
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arraystat = new ArrayList();
    String saldo;
    int day, month, year;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_keuangan_apotek_);

        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String tgl = ModulApotek.getDate("dd/MM/yyyy");
        ModulApotek.setText(v, R.id.tgl1, tgl);
        ModulApotek.setText(v, R.id.tgl2, tgl);
        getDompet("");
        setText();
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
                getDompet(a);
            }

        });

        Spinner sp = findViewById(R.id.spinnerrr);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDompet("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setText() {
        arraystat.add("Semua");
        arraystat.add("Pemasukan");
        arraystat.add("Pengeluaran");
        Spinner spinner = (Spinner) findViewById(R.id.spinnerrr);
        ArrayAdapter ok = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraystat);
        ok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ok);

    }

    public String whereStat(String cari) {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerrr);
        int id = spinner.getSelectedItemPosition();
        if (id == 0) {
            return ModulApotek.selectwhere("tbltransaksi") + " (" + ModulApotek.sLike("fakturtran", cari) + " OR " + ModulApotek.sLike("kettransaksi", cari) + ") AND " + ModulApotek.sBetween("tgltransaksi", ModulApotek.getText(v, R.id.tgl1), ModulApotek.getText(v, R.id.tgl2));
        } else if (id == 1) {
            return ModulApotek.selectwhere("tbltransaksi") + ModulApotek.sWhere("keluar", "0") + " AND (" + ModulApotek.sLike("fakturtran", cari) + " OR " + ModulApotek.sLike("kettransaksi", cari) + ")" + " AND " + ModulApotek.sBetween("tgltransaksi", ModulApotek.getText(v, R.id.tgl1), ModulApotek.getText(v, R.id.tgl2));
        } else {
            return ModulApotek.selectwhere("tbltransaksi") + ModulApotek.sWhere("masuk", "0") + " AND (" + ModulApotek.sLike("fakturtran", cari) + " OR " + ModulApotek.sLike("kettransaksi", cari) + ")" + " AND " + ModulApotek.sBetween("tgltransaksi", ModulApotek.getText(v, R.id.tgl1), ModulApotek.getText(v, R.id.tgl2));

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDompet(String id) {

        arrayList.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recKeuangan);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Adapter_Dompet_Apotek(this, arrayList);
        recyclerView.setAdapter(adapter);
        String campur = "";
        Cursor c = db.sq(whereStat(id));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                campur = ModulApotek.getString(c, "idtransaksi") + "__" + ModulApotek.getString(c, "tgltransaksi") + "__" + ModulApotek.getString(c, "fakturtran") + "__" + ModulApotek.getString(c, "kettransaksi") + "__" + ModulApotek.getString(c, "masuk") + "__" + ModulApotek.getString(c, "keluar");

                arrayList.add(campur);
            }
        }

        adapter.notifyDataSetChanged();
        setSaldo();

    }

    public void setSaldo() {
        Cursor c = db.sq(ModulApotek.select("tbltransaksi"));
        if (c.getCount() == 0) {
            saldo  = "0";
        } else {
            c.moveToLast();
            saldo = ModulApotek.getString(c, "saldo");
        }
        ModulApotek.setText(v, R.id.saldoo, "Saldo: " + ModulApotek.removeE(saldo));
    }

    public void export(View view) {
        Intent i = new Intent(this, Menu_Export_Exel_Apotek.class);
        i.putExtra("type", "keuangan");
        startActivity(i);
    }

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
            return new DatePickerDialog(this, date1, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, date2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulApotek.setText(v, R.id.tgl1, ModulApotek.setDatePickerNormal(thn, bln + 1, day));
            getDompet("");

        }
    };

    private DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulApotek.setText(v, R.id.tgl2, ModulApotek.setDatePickerNormal(thn, bln + 1, day));
            getDompet("");

        }
    };

    public void tgl1(View view) {
        showDialog(1);
    }

    public void tgl2(View view) {
        showDialog(2);
    }
}

