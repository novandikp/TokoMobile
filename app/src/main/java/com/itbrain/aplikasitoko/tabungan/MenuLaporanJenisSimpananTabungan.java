package com.itbrain.aplikasitoko.tabungan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;

public class MenuLaporanJenisSimpananTabungan extends AppCompatActivity {
    private EditText mEdtCari;
    private RecyclerView mRecList;
    private TextView mTvJumlahData;

    DatabaseTabungan db;
    String type, keyword = "", idsimpanan = "", simpanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_laporan_jenis_tabungan);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
        idsimpanan = "0";
        simpanan = "Semua";
        type = getIntent().getStringExtra("type");
        db = new DatabaseTabungan(this);

        ImageButton imageButton = findViewById(R.id.kembaliLaporanJenis);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String title = "";
        title = ModulTabungan.getResString(this, R.string.laporan_jenissimpanan);

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
        getJenis(keyword);
    }

    private void getList() {
            getJenis(keyword);
    }

    private void getJenis(String keyword) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new MenuJenisTabungan.AdapterListSimpanan(this, arrayList,true);
        mRecList.setAdapter(adapter);
        Cursor c = db.sq(QueryTabungan.selectwhere("tbljenissimpanan") +
                QueryTabungan.sLike("jenis", keyword) + " OR " +
                QueryTabungan.sLike("bunga", keyword));
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = ModulTabungan.getString(c, "idjenis") + "__" +
                        ModulTabungan.getString(c, "jenis") + "__" +
                        ModulTabungan.getString(c, "bunga");
                arrayList.add(campur);
            }
        }
        mTvJumlahData.setText(getText(R.string.jumlahdata)+" "+c.getCount());
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mEdtCari = (EditText) findViewById(R.id.edtCariJenisTabungan);
        mRecList = (RecyclerView) findViewById(R.id.recListJenisTabungan);
        mTvJumlahData = (TextView) findViewById(R.id.tvJumlahData);
    }

    public void exportExcelJenis(View view) {
        startActivity(new Intent(this,MenuExportExcelJenisTabungan.class).putExtra("type",type));
    }

}