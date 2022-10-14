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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MenuLaporanJasaLaundry extends AppCompatActivity {
    DatabaseLaundry db;
    View v;

    int year, month, day;
    Calendar calendar ;
    String tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_jasa_laundry);
        Modul.btnBack("Laporan Jasa",getSupportActionBar());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        db=new DatabaseLaundry(this);
        v=this.findViewById(android.R.id.content);
        String title="judul";
        tab=getIntent().getStringExtra("tab");
        final Spinner sp=(Spinner)findViewById(R.id.spKategori);
        final String[] kat = {"0"};
        List<String> labels = db.getKategori();
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(data);

        if (tab.equals("jasa")){
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kat[0] =db.getIdKategori().get(parent.getSelectedItemPosition());
                    getjasa("",kat[0]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            title="Laporan Jasa";
            getjasa("",kat[0]);
            sp.setVisibility(View.VISIBLE);
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
                if (tab.equals("jasa")){
                    getjasa(keyword,kat[0]);
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

    private void getjasa(String keyword,String kategori){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recLaporan);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList DaftarJasa=new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListJasa(this,DaftarJasa,Boolean.FALSE);
        recyclerView.setAdapter(adapter);
        String q;

        if (TextUtils.isEmpty(keyword)){
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa";
            }else {
                q="SELECT * FROM qjasa WHERE idkategori='"+kategori+"'";
            }
        }else {
            if (kategori.equals("0")){
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%'";
            }else {
                q="SELECT * FROM qjasa WHERE jasa LIKE '%"+keyword+"%' AND idkategori='"+kategori+"'";
            }
        }
        Cursor c=db.sq(q);
        if (Modul.getCount(c)>0){
            Modul.setText(v,R.id.txtJumlahJasa,"Jumlah Jasa : "+String.valueOf(Modul.getCount(c)));
            while(c.moveToNext()){
                DaftarJasa.add(new getterJasa(
                        Modul.getInt(c,"idjasa"),
                        Modul.getInt(c,"idkategori"),
                        Modul.getString(c,"kategori"),
                        Modul.getString(c,"jasa"),
                        Modul.getString(c,"biaya"),
                        Modul.getString(c,"satuan")
                ));
            }
        }else {
            Modul.setText(v,R.id.txtJumlahJasa,"Jumlah Jasa : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void Excel(View view) {
        Intent intent = new Intent(MenuLaporanJasaLaundry.this, MenuLaporanExcelLaundry.class);
        intent.putExtra("tab",tab);
        startActivity(intent);
    }

}