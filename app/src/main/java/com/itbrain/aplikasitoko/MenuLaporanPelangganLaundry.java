package com.itbrain.aplikasitoko;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuLaporanPelangganLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;
    String tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_pelanggan_laundry);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);
        v=this.findViewById(android.R.id.content);
        String title = "Laporan Pelanggan";
        tab=getIntent().getStringExtra("tab");
        if (tab.equals("pelanggan")) {
            title="Laporan Pelanggan";
            getpelanggan("");
        }
        final EditText edtCari = (EditText)findViewById(R.id.edtPencarian);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = edtCari.getText().toString();
                if (tab.equals("pelanggan")) {
                    getpelanggan(keyword);
                }
            }
        });

        Modul.btnBack(title,getSupportActionBar());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getpelanggan(String keyword){
        ArrayList DaftarPelanggan = new ArrayList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLaporan);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.Adapter adapter = new AdapterListPelanggan(this,DaftarPelanggan);
        recyclerView.setAdapter(adapter);

        String q;
        if (TextUtils.isEmpty(keyword)){
            q="SELECT * FROM tblpelanggan WHERE idpelanggan>0";
        }else {
            q="SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%' AND idpelanggan>0 "+Query.sOrderASC("pelanggan");
        }

        Cursor c = db.sq(q);
        if (Modul.getCount(c)>0){
            Modul.setText(v,R.id.txtJumlahPelanggan,"Jumlah Pelanggan Terdaftar : "+String.valueOf(Modul.getCount(c)));
            while(c.moveToNext()){
                DaftarPelanggan.add(new getterPelanggan(
                        Modul.getInt(c,"idpelanggan"),
                        Modul.getString(c,"pelanggan"),
                        Modul.getString(c,"alamat"),
                        Modul.getString(c,"notelp")
                ));
            }
        }else {
            Modul.setText(v,R.id.txtJumlahPelanggan,"Jumlah Pelanggan Terdaftar : 0");
        }

        adapter.notifyDataSetChanged();

    }
    public void Excel(View view) {
        Intent i=new Intent(MenuLaporanPelangganLaundry.this,MenuLaporanExcelLaundry.class);
        i.putExtra("tab",tab);
        startActivity(i);
    }
}