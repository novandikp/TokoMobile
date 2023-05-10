package com.itbrain.aplikasitoko.tabungan;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MenuLaporanTransaksiTabungan extends AppCompatActivity {
    private EditText mEdtCari;
    private ConstraintLayout mCTanggal;
    private Button mBtnTglAwal;
    private Button mBtnTglSampai;
    private RecyclerView mRecList;
    private Spinner mSpSort;
    private ImageButton mIbtnSort;
    private TextView mTvJumlahData;
    private TextView mTvSpSort;
    private ConstraintLayout mCPendapatan;
    private TextView mTvPendapatan;
    private Button mBtnDetail;
    private ImageButton mIbtnSearch;
    private ImageView mIvCari;

    DatabaseTabungan db;
    String type, keyword = "", idsimpanan = "", simpanan;
    Calendar calAwal,calSampai;
    int year,year2,month,month2,day,day2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan_transaksi_tabungan);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        idsimpanan = "0";
        simpanan = "Semua";
        type = getIntent().getStringExtra("type");
        db = new DatabaseTabungan(this);
        
        ImageButton imageButton = findViewById(R.id.kembali_laporan_list);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String title = "";
        if (type.equals("anggota")) {
            title = ModulTabungan.getResString(this, R.string.laporan_anggota);
        } else if (type.equals("jenis")) {
            title = ModulTabungan.getResString(this, R.string.laporan_jenissimpanan);
        } else if (type.equals("simpanan")) {
            title = ModulTabungan.getResString(this, R.string.laporan_simpanan);
            mTvSpSort.setVisibility(View.VISIBLE);
            mTvSpSort.setText(R.string.title_master_simpanan);
            mSpSort.setVisibility(View.VISIBLE);

            ArrayAdapter<String> dataAdapterKategori = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.getJenis());
            dataAdapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpSort.setAdapter(dataAdapterKategori);
        } else if (type.equals("transaksi")) {
            title = ModulTabungan.getResString(this, R.string.laporan_proses_peranggota);
            mCTanggal.setVisibility(View.VISIBLE);
            mTvSpSort.setVisibility(View.VISIBLE);
            mTvSpSort.setText(R.string.keterangan);
            mSpSort.setVisibility(View.VISIBLE);
            mEdtCari.setEnabled(false);
            mEdtCari.setText(simpanan);
            ConstraintLayout.LayoutParams cariParams=(ConstraintLayout.LayoutParams)mEdtCari.getLayoutParams();
            cariParams.leftMargin=16;
            cariParams.topMargin=8;
            cariParams.rightMargin=4;
            mEdtCari.setLayoutParams(cariParams);
            mIvCari.setVisibility(View.GONE);
            mIbtnSearch.setVisibility(View.VISIBLE);
            mIbtnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(MenuLaporanTransaksiTabungan.this, MenuTransaksiCariTabungan.class).putExtra("type", "simpanan").putExtra("fromLaporan", true), 3000);
                }
            });

            String[] kode = {"Semua","Tabungan", "Bunga"};
            ArrayAdapter<String> dataAdapterKategori = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kode);
            dataAdapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpSort.setAdapter(dataAdapterKategori);
        } else if (type.equals("keuangan")) {
            title = getResources().getString(R.string.laporan_keuangan);
            mCTanggal.setVisibility(View.VISIBLE);
            mCPendapatan.setVisibility(View.VISIBLE);
            mTvSpSort.setVisibility(View.VISIBLE);
            mSpSort.setVisibility(View.VISIBLE);
            mTvSpSort.setText(R.string.spSort2);
            String[] spKatKeuangan = {
                    getResources().getString(R.string.kategoriSemua),
                    getResources().getString(R.string.kategoriPemasukan),
                    getResources().getString(R.string.kategoriPengeluaran)
            };

            String[] kode = {"Semua","Tabungan", "Bunga"};
            ArrayAdapter<String> dataAdapterKategori = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kode);
            dataAdapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpSort.setAdapter(dataAdapterKategori);
        }
//        ModulTabungan.btnBack(title, getSupportActionBar());

//        Toolbar toolbar = findViewById(R.id.toolbarTabungan);
//        setSupportActionBar(toolbar);

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

        //tanggal
        if (type.equals("transaksi")||type.equals("keuangan")){
            calAwal=Calendar.getInstance();
            calSampai=Calendar.getInstance();
            mBtnTglAwal.setText(ModulTabungan.getDate2(calAwal,"dd/MM/yyyy"));
            mBtnTglSampai.setText(ModulTabungan.getDate2(calSampai,"dd/MM/yyyy"));
            Cursor cTanggal;
            if (type.equals("transaksi")){
                cTanggal=db.sq(QueryTabungan.select("tbltransaksi")+QueryTabungan.sOrderASC("idtransaksi"));
                if (cTanggal.getCount()>0){
                    cTanggal.moveToLast();
                    calAwal.setTime(ModulTabungan.dateToDate(ModulTabungan.getString(cTanggal,"tgltransaksi")));
                    calSampai.setTime(ModulTabungan.dateToDate(ModulTabungan.getString(cTanggal,"tgltransaksi")));
                }
            }else {
                cTanggal=db.sq(QueryTabungan.select("tblkeuangan")+QueryTabungan.sOrderASC("idtransaksi"));
                if (cTanggal.getCount()>0){
                    cTanggal.moveToLast();
                    calAwal.setTime(ModulTabungan.dateToDate(ModulTabungan.getString(cTanggal,"tgltransaksi")));
                    calSampai.setTime(ModulTabungan.dateToDate(ModulTabungan.getString(cTanggal,"tgltransaksi")));
                }
            }
            year=calAwal.get(Calendar.YEAR);
            month=calAwal.get(Calendar.MONTH);
            day=calAwal.get(Calendar.DAY_OF_MONTH);
            year2=calSampai.get(Calendar.YEAR);
            month2=calSampai.get(Calendar.MONTH);
            day2=calSampai.get(Calendar.DAY_OF_MONTH);
            mBtnTglAwal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog datePicker=new DatePickerDialog(MenuLaporanTransaksiTabungan.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int thn, int bln, int tgl) {
                            year=thn;month=bln;day=tgl;
                            calAwal.set(year,month,day);
                            mBtnTglAwal.setText(ModulTabungan.getDate2(calAwal,"dd/MM/yyyy"));
                            getList();
                        }
                    },year,month,day);
                    datePicker.show();
                }
            });
            mBtnTglSampai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog datePicker=new DatePickerDialog(MenuLaporanTransaksiTabungan.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int thn, int bln, int tgl) {
                            year2=thn;month2=bln;day2=tgl;
                            calSampai.set(year2,month2,day2);
                            mBtnTglSampai.setText(ModulTabungan.getDate2(calSampai,"dd/MM/yyyy"));
                            getList();
                        }
                    },year2,month2,day2);
                    datePicker.show();
                }
            });
        }

        getList();
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
        if (!idsimpanan.equals("0")) {
            mEdtCari.setText(simpanan);
        }
    }

    private void getList() {
        if (type.equals("anggota")) {
            getAnggota(keyword);
        } else if (type.equals("jenis")) {
            getJenis(keyword);
        } else if (type.equals("simpanan")) {
            getSimpanan(keyword, db.getIdJenis().get(mSpSort.getSelectedItemPosition()));
        } else if (type.equals("transaksi")) {
            getTransaksi(idsimpanan, mSpSort.getSelectedItemPosition(),ModulTabungan.getDate2(calAwal,"yyyyMMdd")+"__"+ModulTabungan.getDate2(calSampai,"yyyyMMdd"));
        } else if (type.equals("keuangan")){
            getKeuangan(keyword,String.valueOf(mSpSort.getSelectedItemPosition()),ModulTabungan.getDate2(calAwal,"yyyyMMdd")+"__"+ModulTabungan.getDate2(calSampai,"yyyyMMdd"));
        }
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

    private void getTransaksi(String idsimpanan, int tipe, String tgl) {
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter = new AdapterListTransaksi(this, arrayList,true);
        mRecList.setAdapter(adapter);
        String[] tTgl=tgl.split("__");
        String q="";
        if (idsimpanan.equals("0")){
            if (tipe==0){
                q=QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sBetween("tgltransaksi",tTgl[0],tTgl[1]) + QueryTabungan.sOrderDESC("notransaksi");
            }else if (tipe==1){
                q=QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sWhere("kode","T")+" AND ("+QueryTabungan.sBetween("tgltransaksi",tTgl[0],tTgl[1])+")" + QueryTabungan.sOrderDESC("notransaksi");
            }else {
                q=QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sWhere("kode","B")+" AND ("+QueryTabungan.sBetween("tgltransaksi",tTgl[0],tTgl[1])+")" + QueryTabungan.sOrderDESC("notransaksi");
            }
        }else {
            if (tipe==0){
                q=QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan)+" AND ("+QueryTabungan.sBetween("tgltransaksi",tTgl[0],tTgl[1])+")" + QueryTabungan.sOrderDESC("notransaksi");
            }else if (tipe==1){
                q=QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan)+ " AND " +QueryTabungan.sWhere("kode","T") +" AND ("+QueryTabungan.sBetween("tgltransaksi",tTgl[0],tTgl[1])+")" + QueryTabungan.sOrderDESC("notransaksi");
            }else {
                q=QueryTabungan.selectwhere("tbltransaksi") + QueryTabungan.sWhere("idsimpanan", idsimpanan)+ " AND " +QueryTabungan.sWhere("kode","B") +" AND ("+QueryTabungan.sBetween("tgltransaksi",tTgl[0],tTgl[1])+")" + QueryTabungan.sOrderDESC("notransaksi");
            }
        }
        Cursor c = db.sq(q);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String campur = ModulTabungan.getString(c, "idtransaksi") + "__" +
                        ModulTabungan.getString(c, "idsimpanan") + "__" +
                        ModulTabungan.getString(c, "notransaksi") + "__" +
                        ModulTabungan.getString(c, "tgltransaksi") + "__" +
                        ModulTabungan.getString(c, "masuk") + "__" +
                        ModulTabungan.getString(c, "keluar") + "__" +
                        ModulTabungan.getString(c, "saldo") + "__" +
                        ModulTabungan.getString(c, "kode");
                arrayList.add(campur);
            }
        }
        mTvJumlahData.setText(getText(R.string.jumlahdata)+" "+c.getCount());
        adapter.notifyDataSetChanged();
    }
    private void getKeuangan(String keyword,String kategori,String tgl){
        mRecList.setLayoutManager(new LinearLayoutManager(this));
        mRecList.setHasFixedSize(true);
        ArrayList arrayList=new ArrayList();
        RecyclerView.Adapter adapter=new AdapterListKeuangan(this,arrayList,true);
        mRecList.setAdapter(adapter);
        String[] tanggal=tgl.split("__");
        String q="";
        if (keyword.isEmpty()){
            if (kategori.equals("0")){
                q=QueryTabungan.select("tblkeuangan");
            }else if (kategori.equals("1")){
                q=QueryTabungan.selectwhere("tblkeuangan")+"masuk>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+")";
            }else {
                q=QueryTabungan.selectwhere("tblkeuangan")+"keluar>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+")";
            }
        }else {
            if (kategori.equals("0")){
                q=QueryTabungan.selectwhere("tblkeuangan")+QueryTabungan.sLike("faktur",keyword);
            }else if (kategori.equals("1")){
                q=QueryTabungan.selectwhere("tblkeuangan")+"masuk>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+") AND "+QueryTabungan.sLike("faktur",keyword);
            }else {
                q=QueryTabungan.selectwhere("tblkeuangan")+"keluar>0 AND ("+QueryTabungan.sBetween("tgltransaksi",tanggal[0],tanggal[1])+") AND "+QueryTabungan.sLike("faktur",keyword);
            }
        }
        Cursor c=db.sq(q);
        Cursor cSaldo=db.sq(QueryTabungan.select("tblkeuangan"));
        cSaldo.moveToLast();
        mTvPendapatan.setText(cSaldo.getCount()>0?ModulTabungan.getResString(this,R.string.saldo)+"\n"+ModulTabungan.removeE(ModulTabungan.getString(cSaldo,"saldo")):ModulTabungan.getResString(this,R.string.saldo)+"\n0");
        if (c.getCount()>0){
            while (c.moveToNext()){
                String campur= ModulTabungan.getString(c,"idtransaksi")+"__"+
                        ModulTabungan.getString(c,"tgltransaksi")+"__"+
                        ModulTabungan.getString(c,"faktur")+"__"+
                        ModulTabungan.getString(c,"keterangantransaksi")+"__"+
                        ModulTabungan.getString(c,"masuk")+"__"+
                        ModulTabungan.getString(c,"keluar")+"__"+
                        ModulTabungan.getString(c,"saldo");
                arrayList.add(campur);
            }
        }
        mTvJumlahData.setText(ModulTabungan.getResString(this,R.string.jumlahdata)+" "+c.getCount());
        adapter.notifyDataSetChanged();
    }

    public void exportExcel(View view) {
        startActivity(new Intent(this,MenuExportExcelTransaksiTabungan.class).putExtra("type",type));
    }

    private void initView() {
        mEdtCari = (EditText) findViewById(R.id.edtCari);
        mCTanggal = (ConstraintLayout) findViewById(R.id.cTanggal);
        mBtnTglAwal = (Button) findViewById(R.id.btnTglAwal);
        mBtnTglSampai = (Button) findViewById(R.id.btnTglSampai);
        mRecList = (RecyclerView) findViewById(R.id.recList);
        mSpSort = (Spinner) findViewById(R.id.spSort);
        mIbtnSort = (ImageButton) findViewById(R.id.ibtnSort);
        mTvJumlahData = (TextView) findViewById(R.id.tvJumlahData);
        mTvSpSort = (TextView) findViewById(R.id.tvSpSort);
        mCPendapatan = (ConstraintLayout) findViewById(R.id.cPendapatan);
        mTvPendapatan = (TextView) findViewById(R.id.tvPendapatan);
        mBtnDetail = (Button) findViewById(R.id.btnDetail);
        mIbtnSearch = (ImageButton) findViewById(R.id.ibtnSearch);
        mIvCari = (ImageView) findViewById(R.id.ivCari);
    }
}
