package com.itbrain.aplikasitoko.apotek;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Menu_Penjualan_Apotek extends AppCompatActivity implements PemanggilMethod_apotek {

    String kondisi;
    boolean stat = false;
    DatabaseApotek db;
    View v;
    String idpelanggan, idbarang = "default", idsatuan = "def", idorderbeli;
    double stok = 0, harga = 0, nilai = 0;
    int year, day, month;
    Calendar calendar;
    ArrayList arrayKat = new ArrayList();
    ArrayList arrayList = new ArrayList();
    String tgl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_penjualan_apotek);
        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cek();
        setKategori();
        tgl = ModulApotek.getDate("dd/MM/yyyy");
        ModulApotek.setText(v, R.id.etgl, tgl);


        final Spinner spinner = (Spinner) findViewById(R.id.sp_satuan_penjualan);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!idbarang.equals("default")) {
                    isihpp();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TextView kalendar = findViewById(R.id.etgl);
        kalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        getdata("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getdata(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.keranjangg);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Master_Qart_Apotek(this, arrayList);
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("qdetailjual") + ModulApotek.sWhere("idjual", idorderbeli) + " ORDER BY barang ASC";
        ;
        Cursor c = db.sq(q);
        while (c.moveToNext()) {
            String campur = ModulApotek.getString(c, "iddetailjual") + "__" + ModulApotek.getString(c, "barang") + "__" + ModulApotek.getString(c, "jumlahjual") + "__" + ModulApotek.getString(c, "hargajual") + "__" + ModulApotek.getString(c, "satuanjual") + "__" + ModulApotek.getString(c, "nilai") + "__" + ModulApotek.getString(c, "satuankecil") + "__" + ModulApotek.getString(c, "idbarang") + "__" + "penjualan" + "__" + ModulApotek.getString(c, "batchnumber");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
        setTotal();
    }

    public void kalendar(View view) {
        showDialog(1);
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
            return new DatePickerDialog(this, date, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulApotek.setText(v, R.id.etgl, ModulApotek.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    public void cek() {
        Cursor c = db.sq(ModulApotek.selectwhere("qjual") + ModulApotek.sWhere("bayar", "0"));
        if (c.getCount() > 0) {
            c.moveToNext();
            String faktur = ModulApotek.getString(c, "fakturjual");
            if (faktur.equals("kosong")) {
                faktur = "00000000";
                ModulApotek.setText(v, R.id.fakkur, faktur.substring(0, faktur.length() - ModulApotek.getString(c, "idjual").length()) + ModulApotek.getString(c, "idjual"));
            } else {
                ModulApotek.setText(v, R.id.fakkur, faktur);
            }
            idorderbeli = ModulApotek.getString(c, "idjual");
            idpelanggan = ModulApotek.getString(c, "idpelanggan");
            ModulApotek.setText(v, R.id.ePelanggan, ModulApotek.getString(c, "pelanggan"));
        } else {
            String q = "INSERT INTO tbljual (idpelanggan,tgljual) VALUES (1," + tgl + ")";
            if (db.exc(q)) {
                cek();
            } else {
                ModulApotek.showToast(this, "Gagal");
            }
        }
    }


    public void pelanggan(View view) {
        Intent i = new Intent(this, Menu_Pilih_Apotek.class);
        i.putExtra("type", "pelanggan");
        startActivityForResult(i, 100);
    }

    public void barang(View view) {
        Intent i = new Intent(this, Menu_Pilih_Apotek.class);
        i.putExtra("type", "barang1");
        startActivityForResult(i, 200);
    }

    private void setTotal() {
        Cursor c = db.sq(ModulApotek.selectwhere("tbljual") + ModulApotek.sWhere("idjual", idorderbeli));
        String total = "Rp.0";
        if (c.getCount() > 0) {
            c.moveToNext();
            total = ModulApotek.getString(c, "total");
        }
        ModulApotek.setText(v, R.id.etotal, "Rp." + ModulApotek.removeE(total));
    }

    public void simpan(View view) {
        simpantran();
        getdata("");
    }

    public void ganti(View view) {
        Cursor c = db.sq(ModulApotek.selectwhere("tbldetailjual") + ModulApotek.sWhere("idjual", idorderbeli));
        if (c.getCount() > 0) {
            Intent i = new Intent(Menu_Penjualan_Apotek.this, Menu_Bayar_Apotek.class);
            i.putExtra("idjual", idorderbeli);
            i.putExtra("type", "jual");
            startActivity(i);
        } else {
            ModulApotek.showToast(this, "Keranjang masih kosong");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            idpelanggan = data.getStringExtra("idpelanggan");
            setSupplier(idpelanggan);
        } else if (resultCode == 200) {
            idbarang = data.getStringExtra("idbarang");
            setBarang(idbarang);
            setKategori();


        }
    }

    private void setSupplier(String idsupplier){
        Cursor c=db.sq("SELECT * FROM tblpelanggan WHERE idpelanggan="+idsupplier);
        c.moveToNext();
        ModulApotek.setText(v,R.id.epelangggan, ModulApotek.getString(c,"pelanggan"));
    }

    private void setBarang(String id){
        Cursor c=db.sq("SELECT * FROM qbarang WHERE idbarang='"+id+"'");
        c.moveToNext();
        ModulApotek.setText(v,R.id.ebarang, ModulApotek.getString(c,"barang"));
        idsatuan= ModulApotek.getString(c,"idsatuan");
        nilai= ModulApotek.strToDouble(ModulApotek.getString(c,"nilai"));
        stok= ModulApotek.strToDouble(ModulApotek.getString(c,"stok"));

    }



    private void isihpp(){
        if (!idbarang.equals("default")){
            Cursor c=db.sq("SELECT * FROM qbelidetail WHERE idbarang='"+idbarang+"' ORDER BY idbelidetail");
            if (c.getCount()>0){
                c.moveToLast();
                if (pilihsat()){
                    ModulApotek.setText(v,R.id.eharga, ModulApotek.getString(c,"harga_jual_dua"));
                    harga= ModulApotek.strToDouble(ModulApotek.getString(c,"hpp_dua"));
                }else{
                    ModulApotek.setText(v,R.id.eharga, ModulApotek.getString(c,"harga_jual_satu"));
                    harga= ModulApotek.strToDouble(ModulApotek.getString(c,"hpp_satu"));
                }
            }
            ModulApotek.setText(v,R.id.ejumlah,"0");
        }

    }

    private boolean pilihsat(){
        Spinner spinner = (Spinner) findViewById(R.id.sp_satuan_penjualan) ;
        if (spinner.getSelectedItemPosition()==0){
            return false;
        }else{
            return true;
        }

    }


    private void simpantran(){

        Spinner sp = findViewById(R.id.sp_satuan_penjualan);
        final String satuanjual = sp.getSelectedItem().toString();
        final String hargabeli = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eharga));
        final String jumlahbeli = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.ejumlah));
        final String faktur = ModulApotek.getText(v,R.id.fakkur);
        final String batch = ModulApotek.getText(v,R.id.ebatch);
        final String tgl = ModulApotek.getText(v,R.id.etgl);

        if (!jumlahbeli.equals("0")&&!idbarang.equals("default")&&!TextUtils.isEmpty(batch)&&!TextUtils.isEmpty(jumlahbeli)&&!hargabeli.equals("0")&&!TextUtils.isEmpty(hargabeli)){
            String laba = ModulApotek.doubleToStr(ModulApotek.strToDouble(hargabeli)-harga);
            double jumlah ;
            if (pilihsat()){
                jumlah = stok*nilai;
            }else{
                jumlah=stok;
            }
            if (ModulApotek.strToDouble(jumlahbeli)>jumlah ){
                ModulApotek.showToast(this,"Stok tidak cukup");
            }else{
                String [] orderbeli ={faktur,tgl,idpelanggan,idorderbeli};
                String [] belidetail ={idbarang,satuanjual,hargabeli,jumlahbeli,laba,idorderbeli,batch};
                String q = ModulApotek.splitParam("INSERT INTO tbldetailjual (idbarang,satuanjual,hargajual,jumlahjual,laba,idjual,batchnumber) VALUES (?,?,?,?,?,?,?)",belidetail) ;
                String p= ModulApotek.splitParam("UPDATE tbljual SET fakturjual=?,tgljual=?,idpelanggan=? WHERE idjual=?   ",orderbeli) ;
                if(db.exc(p)&&db.exc(q)){
                    tambahstok();
                    reset();
                    ModulApotek.showToast(this,"Berhasil ditambah");
                }else{
                    ModulApotek.showToast(this,"gagal");
                }
            }

        }else{
            ModulApotek.showToast(this,"Mohon isi dengan benar");
        }
    }

    private void reset(){
        idbarang="default";
        ModulApotek.setText(v,R.id.ebarang,"");
        ModulApotek.setText(v,R.id.eharga,"");
        ModulApotek.setText(v,R.id.ejumlah,"");
        ModulApotek.setText(v,R.id.ebatch,"");
        setKategori();

    }

    private void tambahstok(){

        String jumlah = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.ejumlah));
        double konver = ModulApotek.strToDouble(jumlah);
        konver=konver/nilai;
        if (pilihsat()){
            jumlah= ModulApotek.doubleToStr(konver);
        }
        db.exc("UPDATE tblbarang SET stok=stok-"+jumlah+" WHERE idbarang='"+idbarang+"'");


    }

    private void setKategori() {
        arrayKat.clear();
        if (idbarang.equals("default")){
            arrayKat.add("Pilih Satuan");
        }

        Spinner spinner = (Spinner) findViewById(R.id.sp_satuan_penjualan) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (!idbarang.equals("default")){
            Cursor c = db.sq(ModulApotek.selectwhere("tblsatuan")+ ModulApotek.sWhere("idsatuan",idsatuan));
            if(c.getCount() > 0){
                while(c.moveToNext()){

                    arrayKat.add(ModulApotek.getString(c,"satuanbesar"));
                    arrayKat.add(ModulApotek.getString(c,"satuankecil"));
                }
            }
            adapter.notifyDataSetChanged();
        }

    }
}
