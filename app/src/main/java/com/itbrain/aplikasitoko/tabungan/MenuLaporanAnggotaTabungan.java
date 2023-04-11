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

public class MenuLaporanAnggotaTabungan extends AppCompatActivity {

    private EditText mEdtCari;
    private RecyclerView mRecList;
    private TextView mTvJumlahData;

    DatabaseTabungan db;
    String type, keyword = "", idsimpanan = "", simpanan;
    String tab;
    String nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulaporananggotatabungan);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        idsimpanan = "0";
        simpanan = "Semua";
        type = getIntent().getStringExtra("type");

        nama = getText(R.string.laporan_anggota).toString();

        db = new DatabaseTabungan(this);

        ImageButton imageButton = findViewById(R.id.kembaliMenuAnggotaLaporan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String title = "";
        title = ModulTabungan.getResString(this, R.string.laporan_anggota);

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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
        getAnggota(keyword);
    }

    private void getList() {
        getAnggota(keyword);
    }

    private void getAnggota(String keyword) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new MenuAnggotaTabungan.AdapterListAnggota(this, arrayList, true, false);
        mRecList.setAdapter(adapter);
        Cursor c = db.sq(QueryTabungan.selectwhere("tblanggota") + QueryTabungan.sLike("namaanggota", keyword) + " OR " + QueryTabungan.sLike("alamatanggota", keyword) + " OR " + QueryTabungan.sLike("notelpanggota", keyword));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = ModulTabungan.getString(c, "idanggota") + "__" +
                        ModulTabungan.getString(c, "namaanggota") + "__" +
                        ModulTabungan.getString(c, "alamatanggota") + "__" +
                        ModulTabungan.getString(c, "notelpanggota");
                arrayList.add(campur);
            }
        }
        mTvJumlahData.setText(getText(R.string.jumlahdata)+" "+c.getCount());
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mEdtCari = (EditText) findViewById(R.id.cariLaporanAnggota);
        mRecList = (RecyclerView) findViewById(R.id.recLapAnggotaTabungan);
        mTvJumlahData = (TextView) findViewById(R.id.tvLapAnggota);
    }

    public void exportAnggota(View view){
        startActivity(new Intent(this, MenuExportExcelAnggotaTabungan.class));
    }
}