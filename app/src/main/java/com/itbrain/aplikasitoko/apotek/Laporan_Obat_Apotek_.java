package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Laporan_Obat_Apotek_ extends AppCompatActivity {
    DatabaseApotek db;
    View v;
    ArrayList arrayList = new ArrayList();
    ArrayList arrayStat = new ArrayList();
    String type;
    int year, day, month;
    Calendar calendar;
    Spinner spinner;
    ModulApotek config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laporan_obat_apotek_);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        config = new ModulApotek(getSharedPreferences("config", this.MODE_PRIVATE));
        getBarang("");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

                getBarang(a);
            }
        });
    }

    public void getBarang(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Adapter_Laporan_Obat(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("qbarang") + ModulApotek.sLike("barang", cari) + " ORDER BY barang ASC";
        ;
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String idbarang = ModulApotek.getString(c, "idbarang");
            String hargasatu = "", hargadua = "", satuankecil = "", satuanbesar = "", stok;
            satuanbesar = ModulApotek.getString(c, "satuanbesar");
            satuankecil = ModulApotek.getString(c, "satuankecil");
            Cursor b = db.sq(ModulApotek.selectwhere("qbelidetail") + ModulApotek.sWhere("idbarang", idbarang) + "ORDER BY idbelidetail");
            try {
                if (b.getCount() > 0) {
                    b.moveToLast();
                    hargasatu = ModulApotek.getString(b, "harga_jual_satu");
                    hargadua = ModulApotek.getString(b, "harga_jual_dua");


                } else {
                    hargasatu = "belum";
                }
            } catch (Exception e) {
                hargasatu = "belum";
            }

            String campur = "barang" + "__" + ModulApotek.getString(c, "barang") + "__" + ModulApotek.getString(c, "stok") + "__" + hargasatu + "__" + hargadua + "__" + satuankecil + "__" + satuanbesar + "__" + ModulApotek.getString(c, "nilai");
            arrayList.add(campur);


        }

        adapter.notifyDataSetChanged();
    }
    public void export(View view) {
        Intent i = new Intent(this, Menu_Export_Exel_Apotek.class);
        i.putExtra("type", "obat");
        startActivity(i);
    }
}

