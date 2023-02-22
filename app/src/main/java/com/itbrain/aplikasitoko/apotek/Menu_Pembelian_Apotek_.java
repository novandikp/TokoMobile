package com.itbrain.aplikasitoko.apotek;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.itbrain.aplikasitoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Menu_Pembelian_Apotek_ extends AppCompatActivity implements PemanggilMethod_apotek {
    DatabaseApotek db;
    View v;
    boolean stat;
    String kondisi;
    String idpelanggan,idbarang="default",idsatuan="def",idorderbeli;
    double stok=0,harga=0,nilai=0;
    int year,day,month;
    Calendar calendar;
    ModulApotek config;
    ArrayList arrayKat = new ArrayList();
    ArrayList arrayList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pembelian_apotek_);

        db = new DatabaseApotek(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        config = new ModulApotek(getSharedPreferences("config", this.MODE_PRIVATE));

        ImageButton imageButton = findViewById(R.id.Kembali);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cek();
        setKategori();
        getdata("");
        String tgl = ModulApotek.getDate("dd/MM/yyyy");
        ModulApotek.setText(v, R.id.eTanggal, tgl);
        final Spinner spinner = (Spinner) findViewById(R.id.spPilihSatuanPembelianApotek);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!idbarang.equals("default")) {
                    isihpp();
                    ModulApotek.setText(v, R.id.eHarga, "0");
                    ModulApotek.setText(v, R.id.eStok, "0");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final EditText et = (EditText) findViewById(R.id.eHarga);
        final EditText eCari = (EditText) findViewById(R.id.eStok);
        et.addTextChangedListener(new TextWatcherPengeluaran_Apotek(et, new Locale("id", "id"), 2));
        eCari.addTextChangedListener(new TextWatcherPengeluaran_Apotek(eCari, new Locale("id", "id"), 2));
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isiduahpp();
            }
        });


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isiduahpp();

            }
        });


    }

    public void kalendar(View view) {
        showDialog(1);
    }
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, date, year, month, day);
        }else if (id == 2) {
            return new DatePickerDialog(this, date2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulApotek.setText(v,R.id.eTanggal, ModulApotek.setDatePickerNormal(thn,bln+1,day));
        }
    };

    private DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulApotek.setText(v,R.id.eTglExpired, ModulApotek.setDatePickerNormal(thn,bln+1,day));
        }
    };

    public void cek(){
        Cursor c=db.sq(ModulApotek.selectwhere("qorderbeli")+ ModulApotek.sWhere("stat","0"));
        if (c.getCount()>0){
            c.moveToNext();
            String faktur= ModulApotek.getString(c,"fakturorderbeli");
            if (faktur.equals("kosong")){
                ModulApotek.setText(v,R.id.eFaktur,"");
            }else{
                ModulApotek.setText(v,R.id.eFaktur,faktur);
            }
            idorderbeli= ModulApotek.getString(c,"idorderbeli");
            idpelanggan= ModulApotek.getString(c,"idsupplier");
            ModulApotek.setText(v,R.id.nama, ModulApotek.getString(c,"supplier"));
        }else{
            String q= "INSERT INTO tblorderbeli (idsupplier) VALUES (1)";
            if (db.exc(q)){
                cek();
            }else{
                ModulApotek.showToast(this,"Gagal");
            }
        }
    }

    private void isiduahpp(){
        String a = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eHarga)) ;
        String jumlah= ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eStok));
        if (a.equals("0")||jumlah.equals("0")){
            ModulApotek.setText(v,R.id.hpp2,"0");
            ModulApotek.setText(v,R.id.hpp1,"0");
        }else if (!a.equals("")&&!jumlah.equals("")){
            double hppsatu= (harga+(ModulApotek.strToDouble(a)* ModulApotek.strToDouble(ModulApotek.changeComma(jumlah))))/(stok+ ModulApotek.strToDouble(jumlah));
            if (pilihsat()){
                ModulApotek.setText(v,R.id.hpp2, ModulApotek.sederhana(hppsatu));
                ModulApotek.setText(v,R.id.hpp1, ModulApotek.sederhana(hppsatu*nilai));
            }else{
                ModulApotek.setText(v,R.id.hpp1, ModulApotek.sederhana(hppsatu));
                ModulApotek.setText(v,R.id.hpp2, ModulApotek.sederhana(hppsatu/nilai));
            }
        }else{
            ModulApotek.setText(v,R.id.hpp2,"0");
            ModulApotek.setText(v,R.id.hpp1,"0");
        }
    }

    public void pelanggan(View view) {
        Intent i = new Intent(this, Menu_Pilih_Apotek.class);
        i.putExtra("type","supplier");
        startActivityForResult(i,200);
    }

    public void barang(View view) {
        Intent i = new Intent(this, Menu_Pilih_Apotek.class);
        i.putExtra("type","barang");
        startActivityForResult(i,200);
    }

    private void setTotal(){
        Cursor c=db.sq(ModulApotek.selectwhere("qorderbeli")+ ModulApotek.sWhere("idorderbeli",idorderbeli));
        String total="Rp.0";
        if (c.getCount()>0) {
            c.moveToNext();
            total= ModulApotek.getString(c,"totalorderbeli");
        }
        ModulApotek.setText(v,R.id.eTotal,"Rp."+ ModulApotek.removeE(total));
    }

    public void simpanPembelian(View view){
        simpantran();
        getdata("");
    }

    private boolean limit(String item) {
        int batas = ModulApotek.strToInt(config.getCustom(item,"1"));
        if (batas>5){
            return false;
        }else{
            return true;
        }
    }


    private void transaksi(){
        String faktur = ModulApotek.getText(v,R.id.eFaktur);
        String tgl = ModulApotek.getText(v,R.id.eTanggal);
        Cursor b = db.sq(ModulApotek.select("tbltransaksi"));
        double saldo;
        if (b.getCount()>0){
            b.moveToLast();
            saldo = ModulApotek.strToDouble(ModulApotek.getString(b,"saldo"));
        }else{
            saldo=0;
        }

        Cursor c= db.sq(ModulApotek.selectwhere("tblorderbeli")+ ModulApotek.sWhere("idorderbeli",idorderbeli));
        c.moveToNext();
        double bayartotal = ModulApotek.strToDouble(ModulApotek.getString(c,"totalorderbeli"));
        saldo=saldo-bayartotal;
        String [] isi={tgl,"Pembelian barang",faktur, ModulApotek.doubleToStr(bayartotal), ModulApotek.doubleToStr(saldo)};
        String q= ModulApotek.splitParam("INSERT INTO tbltransaksi (tgltransaksi,kettransaksi,fakturtran,keluar,saldo) VALUES (?,?,?,?,?)",isi);
        db.exc(q);
    }

    public void ganti(View view){
        Cursor c= db.sq(ModulApotek.selectwhere("tblbelidetail")+ ModulApotek.sWhere("idorderbeli",idorderbeli));
        if (c.getCount()<1){
            ModulApotek.showToast(this,"Keranjang masih Kosong");

        }else{


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin menyimpan pesanan ini ?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            db.exc("UPDATE tblorderbeli set stat=1 WHERE idorderbeli="+idorderbeli);
//                            transaksi();
//                            tambahlimit();
//                            if (MenuUtama.status){
//                                cek();
//                                reset();
//                                getdata("");
//                            }else{
//                                if (limit("beli")){
//                                    cek();
//                                    reset();
//                                    getdata("");
//                                }else{
//                                    Intent in = new Intent(MenuPembelian.this,MenuTransaksi.class);
//                                    startActivity(in);
//                                }
//                            }
//                            cek();
//                            reset();
//                            getdata("");
                            Intent a = new Intent(Menu_Pembelian_Apotek_.this, Menu_Bayar_Apotek.class);
                            a.putExtra("idjual",idorderbeli);
                            a.putExtra("type","beli");
                            startActivity(a);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            alert=alertDialog.create();

            alert.setTitle("Simpan Pesanan");
            alert.show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            idpelanggan = data.getStringExtra("idsupplier");
            setSupplier(idpelanggan);
        } else if (resultCode == 200) {
            idbarang = data.getStringExtra("idbarang");
            setBarang(idbarang);
            setKategori();


        }
    }

    private void setSupplier(String idsupplier){
        Cursor c=db.sq("SELECT * FROM tblsupplier WHERE idsupplier="+idsupplier);
        c.moveToNext();
        ModulApotek.setText(v,R.id.ePelanggan, ModulApotek.getString(c,"supplier"));
    }

    private void setBarang(String id){
        Cursor c=db.sq("SELECT * FROM tblbarang WHERE idbarang='"+id+"'");
        c.moveToNext();
        ModulApotek.setText(v,R.id.eBarang, ModulApotek.getString(c,"barang"));
        idsatuan= ModulApotek.getString(c,"idsatuan");

    }

    private void isihpp(){
        if (!idbarang.equals("default")){
            Cursor c=db.sq("SELECT * FROM tblbelidetail WHERE idbarang='"+idbarang+"' ORDER BY idbelidetail");
            harga=0;
            Cursor cur=db.sq("SELECT * FROM tblbarang WHERE idbarang='"+idbarang+"'");
            cur.moveToNext();
            stok= ModulApotek.strToDouble(ModulApotek.getString(cur,"stok"));
            if (c.getCount()>0){
                c.moveToLast();
                if (pilihsat()){
                    stok=stok*nilai;
                    harga+=(stok* ModulApotek.strToDouble(ModulApotek.getString(c,"hpp_dua")));
                }else{
                    harga+=(stok* ModulApotek.strToDouble(ModulApotek.getString(c,"hpp_satu")));
                }
            }
        }

    }

    private boolean pilihsat(){
        Spinner spinner = (Spinner) findViewById(R.id.spPilihSatuanPembelianApotek) ;
        if (spinner.getSelectedItemPosition()==0){
            return false;
        }else{
            return true;
        }

    }




    private void simpantran(){

        Spinner sp = findViewById(R.id.spPilihSatuanPembelianApotek);
        final String satuanjual = sp.getSelectedItem().toString();
        final String hargabeli = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eHarga));
        final String jumlahbeli = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eStok));
        final String faktur = ModulApotek.getText(v,R.id.eFaktur);
        final String hpp1 = ModulApotek.getText(v,R.id.hpp1);
        final String hpp2 = ModulApotek.getText(v,R.id.hpp2);
        final String hb1 = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.hb1));
        final String hb2 = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.hb2));
        final String tgl = ModulApotek.getText(v,R.id.eTanggal);
        final String expired = ModulApotek.getText(v,R.id.eTglExpired);
        final String batchnumber = ModulApotek.getText(v,R.id.eBatch);

        if (!hpp1.equals("0")&&!idbarang.equals("default")&&!TextUtils.isEmpty(faktur)&&!TextUtils.isEmpty(expired)&&!TextUtils.isEmpty(batchnumber)&&!hb1.equals("0")&&!hb2.equals("0")&&!TextUtils.isEmpty(hb1)&&!TextUtils.isEmpty(hb2)){
            if (ModulApotek.strToInt(ModulApotek.unNumberFormat(hpp1))> ModulApotek.strToInt(hb1)&& ModulApotek.strToInt(ModulApotek.unNumberFormat(hpp2))> ModulApotek.strToInt(hb2)){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                AlertDialog alert;
                alertDialog.setMessage("Apakah anda yakin simpan pesanan dengan harga jual kurang dari HPP ?")
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String [] orderbeli ={faktur,tgl,idpelanggan,idorderbeli};
                                String [] belidetail ={idbarang,satuanjual,hargabeli, ModulApotek.unNumberFormat(jumlahbeli), ModulApotek.unNumberFormat(hpp1), ModulApotek.unNumberFormat(hpp2),hb1,hb2,idorderbeli,batchnumber,expired};
                                String q = ModulApotek.splitParam("INSERT INTO tblbelidetail (idbarang,satuanbeli,hargabeli,jumlahbeli,hpp_satu,hpp_dua,harga_jual_satu,harga_jual_dua,idorderbeli,batchnumber,expired) VALUES (?,?,?,?,?,?,?,?,?,?,?)",belidetail) ;
                                String p= ModulApotek.splitParam("UPDATE tblorderbeli SET fakturorderbeli=?,tglorderbeli=?,idsupplier=? WHERE idorderbeli=?   ",orderbeli) ;
                                if(db.exc(p)&&db.exc(q)){
                                    tambahstok();
                                    ModulApotek.showToast(Menu_Pembelian_Apotek_.this,"Berhasil ditambah");
                                }else{
                                    ModulApotek.showToast(Menu_Pembelian_Apotek_.this,"gagal");
                                }

                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                alert=alertDialog.create();

                alert.setTitle("Simpan Pesanan");
                alert.show();
            }else{
                String [] orderbeli ={faktur,tgl,idpelanggan,idorderbeli};
                String [] belidetail ={idbarang,satuanjual,hargabeli, ModulApotek.unNumberFormat(jumlahbeli), ModulApotek.unNumberFormat(hpp1), ModulApotek.unNumberFormat(hpp2),hb1,hb2,idorderbeli,batchnumber,expired};
                String q = ModulApotek.splitParam("INSERT INTO tblbelidetail (idbarang,satuanbeli,hargabeli,jumlahbeli,hpp_satu,hpp_dua,harga_jual_satu,harga_jual_dua,idorderbeli,batchnumber,expired) VALUES (?,?,?,?,?,?,?,?,?,?,?)",belidetail) ;
                String p= ModulApotek.splitParam("UPDATE tblorderbeli SET fakturorderbeli=?,tglorderbeli=?,idsupplier=? WHERE idorderbeli=?   ",orderbeli) ;
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
        ModulApotek.setText(v,R.id.eBarang,"");
        ModulApotek.setText(v,R.id.eHarga,"");
        ModulApotek.setText(v,R.id.eStok,"");
        ModulApotek.setText(v,R.id.hpp1,"0");
        ModulApotek.setText(v,R.id.hpp2,"0");
        ModulApotek.setText(v,R.id.hb1,"");
        ModulApotek.setText(v,R.id.hb2,"");
        ModulApotek.setText(v,R.id.eBatch,"");
        ModulApotek.setText(v,R.id.eTglExpired,"");
        setKategori();

    }

    private void tambahstok(){

        String jumlah = ModulApotek.unNumberFormat(ModulApotek.getText(v,R.id.eStok));
        double konver = ModulApotek.strToDouble(jumlah);
        konver=konver/nilai;
        if (pilihsat()){
            jumlah= ModulApotek.doubleToStr(konver);
        }
        db.exc("UPDATE tblbarang SET stok=stok+"+jumlah+" WHERE idbarang='"+idbarang+"'");


    }

    private void setKategori() {
        arrayKat.clear();
        if (idbarang.equals("default")){
            arrayKat.add("Pilih Satuan");
        }

        Spinner spinner = (Spinner) findViewById(R.id.spPilihSatuanPembelianApotek) ;
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayKat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (!idbarang.equals("default")){
            Cursor c = db.sq(ModulApotek.selectwhere("tblsatuan")+ ModulApotek.sWhere("idsatuan",idsatuan));
            if(c.getCount() > 0){
                while(c.moveToNext()){
                    nilai= ModulApotek.strToDouble(ModulApotek.getString(c,"nilai"));
                    arrayKat.add(ModulApotek.getString(c,"satuanbesar"));
                    arrayKat.add(ModulApotek.getString(c,"satuankecil"));
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    public void expired(View view) {
        showDialog(2);
    }
    public void getdata(String cari) {
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.keranjang) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Master_Qart_Apotek(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulApotek.selectwhere("qbelidetail")+ ModulApotek.sWhere("idorderbeli",idorderbeli) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulApotek.getString(c,"idbelidetail")+"__"+ ModulApotek.getString(c,"barang") + "__" + ModulApotek.getString(c,"jumlahbeli")+ "__" + ModulApotek.getString(c,"hargabeli")+ "__" + ModulApotek.getString(c,"satuanbeli")+ "__" + ModulApotek.getString(c,"nilai")+ "__" + ModulApotek.getString(c,"satuankecil")+ "__" + ModulApotek.getString(c,"idbarang")+ "__" +"pembelian"+ "__" + ModulApotek.getString(c,"batchnumber");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
        setTotal();
}
    }
