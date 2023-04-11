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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
import com.itbrain.aplikasitoko.R;
//import com.komputerkit.aplikasitabunganplus.Kunci.LisensiBaru;

import java.util.ArrayList;

public class MenuTransaksiSimpananTabungan extends AppCompatActivity {

    private EditText mEdtCari;
    private RecyclerView mRecList;
    DatabaseTabungan db;
    String mmKeyword = "";
    //BillingProcessor bp;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_transaksi_simpanan_tabungan);
        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        bp=new BillingProcessor(this,Pref.licenseKey,this);
//        bp.initialize();
//        checkPurchase();
        ModulTabungan.btnBack(R.string.title_transaksi_simpanan,getSupportActionBar());

        ImageButton imageButton = findViewById(R.id.kembaliDaftarSimpan);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        db = new DatabaseTabungan(this);
        mEdtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mmKeyword = s.toString();
                getSimpanan(mmKeyword);
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
    protected void onResume() {
        super.onResume();
        getSimpanan(mmKeyword);
        //checkPurchase();
    }

    private void getSimpanan(String keyword) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterTransaksiSimpanan(this, arrayList, false, false);
        mRecList.setAdapter(adapter);
        Cursor c = db.sq(QueryTabungan.selectwhere("qsimpanan") + QueryTabungan.sLike("namaanggota", keyword) + " OR " +
                QueryTabungan.sLike("jenis", keyword) + " OR " + QueryTabungan.sLike("simpanan", keyword));
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
        adapter.notifyDataSetChanged();
    }

//    public void checkPurchase(){
//
//        if (LisensiBaru.checkLisence(this)){
//            status =true;
//
//
//        }else {
//            status=false;
//
//        }
//
//
//    }

    public void tambahData(View view) {
        if (ModulTabungan.cekLimit(this, "simpanan") || (!ModulTabungan.cekLimit(this, "simpanan") && status)) {
            startActivity(new Intent(this, MenuTransaksiBuatSimpananTabungan.class));
        } else {
            ModulTabungan modul = new ModulTabungan(this);
            modul.inApp();
        }
    }

    private void initView() {
        mEdtCari = (EditText) findViewById(R.id.edtCari);
        mRecList = (RecyclerView) findViewById(R.id.recList);
    }
}
