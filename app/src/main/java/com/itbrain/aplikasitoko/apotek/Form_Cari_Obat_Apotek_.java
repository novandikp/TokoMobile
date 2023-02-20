package com.itbrain.aplikasitoko.apotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Form_Cari_Obat_Apotek_ extends AppCompatActivity implements PemanggilMethod_apotek {

    ArrayList arrayList = new ArrayList();
    ArrayList arrayKat = new ArrayList();
    ArrayList arrayid = new ArrayList();
    DatabaseApotek db;
    View v;
    ModulApotek config;

    String produkid = "com.komputerkit.aplikasiapotekpluskeuangan.full";
    String produkid2 = "com.komputerkit.aplikasiapotekpluskeuangannew.full";
    private List<String> skuList = Arrays.asList(produkid, produkid2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_cari_obat_apotek);

        ImageButton imageButton = findViewById(R.id.Kembali);
        Button button = findViewById(R.id.TambahObat);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Form_Cari_Obat_Apotek_.this, Form_Tambah_Obat.class);
                startActivity(intent);
            }
        });

        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        Spinner spinner = (Spinner) findViewById(R.id.sp_cari_obat);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getdata("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setKategori();
        getdata("");


        final EditText eCari = (EditText) findViewById(R.id.eCarii);
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
                getdata(a);

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

    private boolean limit(String item) {
        int batas = ModulApotek.strToInt(config.getCustom(item, "1"));
        if (batas > 5) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata("");
    }


    @Override
    public void getdata(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcObat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Adapter_Obat_Apotek(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = getKategori(cari);
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String idbarang = ModulApotek.getString(c, "idbarang");
            Cursor b = db.sq(ModulApotek.selectwhere("qbelidetail") + ModulApotek.sWhere("idbarang", idbarang));
            String hargajual, hargadua = "";
            try {
                if (b.getCount() > 0) {
                    b.moveToLast();
                    hargajual = ModulApotek.removeE(ModulApotek.getString(b, "harga_jual_satu"));
                    hargadua = ModulApotek.removeE(ModulApotek.getString(b, "harga_jual_dua"));
                } else {
                    hargajual = "Belum tersedia";
                }
            } catch (Exception e) {
                hargajual = "Belum tersedia";
            }

            String campur = ModulApotek.getString(c, "idbarang") + "__" + ModulApotek.getString(c, "barang") + "__" + ModulApotek.getString(c, "stok") + "__" + hargajual + "__" + ModulApotek.getString(c, "satuanbesar") + "__" + hargadua + "__" + ModulApotek.getString(c, "satuankecil") + "__" + ModulApotek.getString(c, "nilai");
            arrayList.add(campur);


        }

        adapter.notifyDataSetChanged();
    }

    private void setKategori() {
        arrayKat.clear();
        arrayid.clear();
        arrayKat.add("Semua");
        arrayid.add("0");
        Spinner spinner = (Spinner) findViewById(R.id.sp_cari_obat);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayKat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Cursor c = db.sq(ModulApotek.select("tblkategori"));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                arrayKat.add(ModulApotek.getString(c, "kategori"));
                arrayid.add(ModulApotek.getString(c, "idkategori"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String getKategori(String cari) {
        Spinner spinner = (Spinner) findViewById(R.id.sp_cari_obat);
        String id = arrayid.get(spinner.getSelectedItemPosition()).toString();
        String q;
        if (id.equals("0")) {
            q = ModulApotek.selectwhere("qbarang") + ModulApotek.sLike("barang", cari) + " ORDER BY barang ASC";
        } else {
            q = ModulApotek.selectwhere("qbarang") + ModulApotek.sWhere("idkategori", id) + " AND " + ModulApotek.sLike("barang", cari) + " ORDER BY barang ASC";
        }
        return q;
    }

    public void tambah(View view) {

        if (Aplikasi_Apotek_Plus_Keuangan_Menu_Utama.status) {
            Intent i = new Intent(this, Form_Tambah_Obat.class);
            startActivity(i);
        } else {
            if (limit("obat")) {
                Intent i = new Intent(this, Form_Tambah_Obat.class);
                startActivity(i);
            }
        }
    }
}