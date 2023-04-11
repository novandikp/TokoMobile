package com.itbrain.aplikasitoko.tabungan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MenulaporanSimpananTabungan extends AppCompatActivity {
    private EditText mEdtCari;
    private RecyclerView mRecList;
    private Spinner mSpSort;

    private TextView mTvJumlahData;


    DatabaseTabungan db;
    String type, keyword = "", idsimpanan = "", simpanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_simpanan_tabungan);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        idsimpanan = "0";
        simpanan = "Semua";
        type = getIntent().getStringExtra("type");
        db = new DatabaseTabungan(this);

        ImageButton imageButton = findViewById(R.id.kembaliLaporanSimpanan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String title = "";
            title = ModulTabungan.getResString(this, R.string.laporan_simpanan);
//            mTvSpSort.setVisibility(View.VISIBLE);
//            mTvSpSort.setText(R.string.title_master_simpanan);
//            mSpSort.setVisibility(View.VISIBLE);

            ArrayAdapter<String> dataAdapterKategori = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.getJenis());
            dataAdapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpSort.setAdapter(dataAdapterKategori);

        mEdtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                getList();
            }
        });
        mSpSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3000) {
            idsimpanan = data.getStringExtra("idsimpanan");
            simpanan = data.getStringExtra("namasimpanan");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSimpanan(keyword, idsimpanan);

    }

    private void getList() {
            getSimpanan(keyword, db.getIdJenis().get(mSpSort.getSelectedItemPosition()));
    }

    private void getSimpanan(String keyword, String idjenis) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterTransaksiSimpanan(this, arrayList, false, true);
        mRecList.setAdapter(adapter);
        String q="";
        if (idjenis.equals("0")){
            q=QueryTabungan.selectwhere("qsimpanan") + QueryTabungan.sLike("namaanggota", keyword) + " OR " +
                    QueryTabungan.sLike("jenis", keyword) + " OR " + QueryTabungan.sLike("simpanan", keyword);
        }else{
            q=QueryTabungan.selectwhere("qsimpanan") +"("+ QueryTabungan.sLike("namaanggota", keyword) + " OR " +
                    QueryTabungan.sLike("jenis", keyword) + " OR " + QueryTabungan.sLike("simpanan", keyword)+") AND "+QueryTabungan.sWhere("idjenis",idjenis);
        }
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = ModulTabungan.getString(c, "idsimpanan") + "__" +
                        ModulTabungan.getString(c, "idanggota") + "__" +
                        ModulTabungan.getString(c, "namaanggota") + "__" +
                        ModulTabungan.getString(c, "idjenis") + "__" +
                        ModulTabungan.getString(c, "jenis") + "__" +
                        ModulTabungan.getString(c, "bunga") + "__" +
                        ModulTabungan.getString(c, "simpanan");
                arrayList.add(campur);
            }
        }
        mTvJumlahData.setText(getText(R.string.jumlahdata)+" "+c.getCount());
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mEdtCari = (EditText) findViewById(R.id.edtCariSimpananTabungan);
        mRecList = (RecyclerView) findViewById(R.id.recListSimpananTabungan);
        mSpSort = (Spinner) findViewById(R.id.spSimpananTabungan);
        mTvJumlahData = (TextView) findViewById(R.id.tvJumlahData);
    }

    public void exportExcelSimpanan(View view) {
        startActivity(new Intent(this, MenuExportExcelSimpananTabungan.class));
    }

}