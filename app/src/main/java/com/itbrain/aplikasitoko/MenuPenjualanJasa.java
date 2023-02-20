package com.itbrain.aplikasitoko;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.itbrain.aplikasitoko.Util.Penjualan;
import com.itbrain.aplikasitoko.bengkel.Database_Bengkel_;
import com.itbrain.aplikasitoko.bengkel.ModulBengkel;
import com.itbrain.aplikasitoko.bengkel.Pembayaran_Jual_Oederdil_Bengkel_;

import java.util.ArrayList;
import java.util.Calendar;

public class MenuPenjualanJasa extends AppCompatActivity implements Penjualan {
    Database_Bengkel_ db;
    String idjual;
    double hargabeli = 0;
    View v;
    ArrayList arrayList = new ArrayList();
    int year, day, month;
    Calendar calendar;
    String idpelanggan, idbarang, idkategori = "Modul", idteknisi = "default";
    ImageButton kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_menu_penjualan_jasa);

        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        kembali = findViewById(R.id.kembali);

        db = new Database_Bengkel_(this);
        v = this.findViewById(android.R.id.content);
        String type = getIntent().getStringExtra("type");
        if (type.equals("transaksi")) {
            idjual = getIdOrder();
            refresh();
        } else {
            idjual = getIntent().getStringExtra("idorder");
            setText();
        }

        String tgl   = ModulBengkel.getDate("dd/MM/yyyy") ;
        ModulBengkel.setText(v,R.id.eTanggal,tgl);
        getBarang("");

        final EditText stok = findViewById(R.id.eStok);
        stok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ModulBengkel.strToInt(stok.getText().toString())>ModulBengkel.strToInt(getStok())){
                    stok.setText(getStok());

                }else if(ModulBengkel.strToInt(stok.getText().toString())<0){
                    stok.setText("0");
                }
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idbarang!=null){
            Cursor c = db.sq(ModulBengkel.selectwhere("tblbarang")+ModulBengkel.sWhere("idbarang",idbarang));
            c.moveToNext();
            final String stokya = ModulBengkel.getString(c,"stok");
            ModulBengkel.setText(v,R.id.eHarga,ModulBengkel.unNumberFormat(ModulBengkel.removeE(ModulBengkel.getString(c,"harga"))));
            ModulBengkel.setText(v,R.id.eBarang,ModulBengkel.getString(c,"barang"));
            hargabeli = ModulBengkel.strToDouble(ModulBengkel.getString(c,"hargabeli"));
            hilangStok();


        }
        if (idpelanggan!=null){
            Cursor c = db.sq(ModulBengkel.selectwhere("tblpelanggan")+ModulBengkel.sWhere("idpelanggan",idpelanggan));
            c.moveToNext();
            ModulBengkel.setText(v,R.id.ePelanggan,ModulBengkel.getString(c,"pelanggan"));
        }
        if (idteknisi.equals("default")){
            Cursor c = db.sq(ModulBengkel.selectwhere("tblteknisi")+ModulBengkel.sWhere("idteknisi","1"));
            c.moveToNext();
            ModulBengkel.setText(v,R.id.eTeknisi,ModulBengkel.getString(c,"teknisi"));
            idteknisi="1";
        }else {
            Cursor c = db.sq(ModulBengkel.selectwhere("tblteknisi")+ModulBengkel.sWhere("idteknisi",idteknisi));
            c.moveToNext();
            ModulBengkel.setText(v,R.id.eTeknisi,ModulBengkel.getString(c,"teknisi"));
        }
    }

    public String getStok(){
        Cursor c = db.sq(ModulBengkel.selectwhere("tblbarang")+ModulBengkel.sWhere("idbarang",idbarang));
        c.moveToNext();
        String stokya = ModulBengkel.getString(c,"stok");
        return stokya;
    }

    public void hilangStok(){
        ImageView img,gmbr;
        TextInputLayout txl,barang;

        img=findViewById(R.id.imageView14);
        gmbr=findViewById(R.id.imageView15);
        txl=findViewById(R.id.txl12);
        barang=findViewById(R.id.textInputLayout16);

        if (idkategori.equals("1")){
            img.setVisibility(View.GONE);
            gmbr.setVisibility(View.GONE);
            txl.setVisibility(View.GONE);
            barang.setHint("Nama Jasa");
        }else{
            img.setVisibility(View.VISIBLE);
            gmbr.setVisibility(View.VISIBLE);
            txl.setVisibility(View.VISIBLE);
            barang.setHint("Nama Barang");
            ModulBengkel.setText(v,R.id.eStok,"1");
        }

    }

    public void getBarang(String cari) {
        setTotal();
        arrayList.clear();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.keranjang) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new MasterPenjualan(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        String q = ModulBengkel.selectwhere("qdetailjual") +ModulBengkel.sWhere("idorder",idjual)+" AND "+ ModulBengkel.sLike("barang",cari) + " ORDER BY barang ASC";;
        Cursor c = db.sq(q) ;
        while(c.moveToNext()){
            String campur = ModulBengkel.getString(c,"iddetailorder")+"__"+ModulBengkel.getString(c,"barang") + "__" + ModulBengkel.getString(c,"jumlah")+ "__" + ModulBengkel.getString(c,"hargajual")+"__"+ModulBengkel.getString(c,"idkategori")+"__"+ModulBengkel.getString(c,"teknisi");
            arrayList.add(campur);
        }

        adapter.notifyDataSetChanged();
    }

    public void tambahstok(View view) {
        int stok =ModulBengkel.strToInt(ModulBengkel.getText(v,R.id.eStok))+1;
        ModulBengkel.setText(v,R.id.eStok,ModulBengkel.intToStr(stok));
    }

    public void kurangstok(View view) {
        int stok =ModulBengkel.strToInt(ModulBengkel.getText(v,R.id.eStok))-1;
        ModulBengkel.setText(v,R.id.eStok,ModulBengkel.intToStr(stok));
    }

    public void setTotal(){
        String q = ModulBengkel.selectwhere("qorder") +ModulBengkel.sWhere("idorder",idjual);;
        Cursor c = db.sq(q);
        c.moveToNext();
        ModulBengkel.setText(v,R.id.eTotal,"Rp."+ModulBengkel.removeE(ModulBengkel.getString(c,"total")));
    }

    public  void refresh(){
        idjual= getIdOrder();
        if (idjual.equals("belum")){
            db.exc("INSERT INTO tblorder (idpelanggan,nopol) values (1,'kosong');");
            idjual= getIdOrder();
            refresh();
        }else{
            setText();
            getBarang("");
        }
    }

    public void bayar(View view) {
        String kondisi=cek();
        if (kondisi.equals("belum")){
            ModulBengkel.showToast(this,"Keranjang Masih Kosong");
        }else{
            finish();
            Intent i = new Intent(MenuPenjualanJasa.this, Pembayaran_Jual_Oederdil_Bengkel_.class);
            i.putExtra("idorder",idjual);
            startActivity(i);
        }
    }

    public void setText(){
        Cursor c = db.sq(ModulBengkel.selectwhere("qorder")+ModulBengkel.sWhere("idorder",idjual));
        c.moveToNext();
        String faktur="00000000";
        ModulBengkel.setText(v,R.id.eFaktur,faktur.substring(0,faktur.length()-ModulBengkel.getString(c,"idorder").length())+ModulBengkel.getString(c,"idorder")) ;
        ModulBengkel.setText(v,R.id.ePelanggan,ModulBengkel.getString(c,"pelanggan"));
        idpelanggan=ModulBengkel.getString(c,"idpelanggan");
        String nopol= ModulBengkel.getString(c,"nopol");
        if(nopol.equals("kosong")||nopol.equals(null)){
            EditText nopoli=findViewById(R.id.eNopol);
            nopoli.getText().clear();
        }else{
            ModulBengkel.setText(v,R.id.eNopol,nopol);
        }
    }

    public String cek(){
        String kondisi="belum";
        Cursor c = db.sq("SELECT * FROM tbldetailorder WHERE idorder="+idjual);
        while(c.moveToNext()){
            kondisi=ModulBengkel.getString(c,"idorder");
        }
        return kondisi;
    }

    public String getIdOrder(){
        idjual="belum";
        Cursor c = db.sq("SELECT * FROM tblorder WHERE statusbayar IS NULL");
        while(c.moveToNext()){
            idjual=ModulBengkel.getString(c,"idorder");
        }
        return idjual;
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
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ModulBengkel.setText(v,R.id.eTanggal,ModulBengkel.setDatePickerNormal(thn,bln+1,day));
        }
    };

    public void pelanggan(View view) {
        Intent i = new Intent(MenuPenjualanJasa.this,MenuPilih.class);
        i.putExtra("type","pelanggan");
        startActivityForResult(i,100);
    }

    public void barang(View view) {
        Intent i = new Intent(MenuPenjualanJasa.this,MenuPilih.class);
        i.putExtra("type","jasa");
        startActivityForResult(i,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            idpelanggan = data.getStringExtra("idpelanggan");

        } else if (resultCode == 200) {
            idbarang = data.getStringExtra("idbarang");
            idkategori = data.getStringExtra("idkategori");

        } else if (resultCode == 300) {
            idteknisi = data.getStringExtra("idteknisi");
        }
    }


    private double tambahlaba(){
        String harga = ModulBengkel.getText(v,R.id.eHarga);
        String stok = ModulBengkel.getText(v,R.id.eStok);

        double hargajual = ModulBengkel.strToDouble(harga)* ModulBengkel.strToDouble(stok);
        hargabeli = hargabeli*ModulBengkel.strToDouble(stok);
        return hargajual-hargabeli;
    }

    private void simpan(){
        String barang = ModulBengkel.getText(v,R.id.eBarang);
        String deskripsi = ModulBengkel.getText(v,R.id.eNopol);
        String harga = ModulBengkel.getText(v,R.id.eHarga);
        String faktur=ModulBengkel.getText(v,R.id.eFaktur);
        String tgl = ModulBengkel.getText(v,R.id.eTanggal);
        String stok = ModulBengkel.getText(v,R.id.eStok);boolean status=false;
        String laba = ModulBengkel.doubleToStr(tambahlaba());
        if (idkategori.equals("default")){
            status=false;
        }
        else if (idkategori.equals("1")){
            if (ModulBengkel.strToInt(harga)<=0) {
                status=false;
            }else{
                status=true;
            }
        }else{
            if (ModulBengkel.strToInt(stok)==0 || ModulBengkel.strToInt(harga)<=0) {
                status=false;
            }else{
                status=true;
            }
        }
        if(!TextUtils.isEmpty(barang) && !TextUtils.isEmpty(deskripsi) && !TextUtils.isEmpty(harga) && status ){
            String[] pp = {idpelanggan,faktur,tgl,deskripsi,idjual} ;
            String qq = ModulBengkel.splitParam("UPDATE tblorder SET idpelanggan=?,faktur=?,tglorder=?,nopol=? WHERE idorder=?   ",pp) ;
            db.exc(qq);
            String[] p;
            if (idkategori.equals("1")){
                p = new String[]{idbarang, "1", harga, idjual, idteknisi,laba};
            }else{
                p = new String[]{idbarang,stok, harga, idjual, idteknisi,laba};
            }
            String q = ModulBengkel.splitParam("INSERT INTO tbldetailorder (idbarang,jumlah,hargajual,idorder,idteknisi,laba) values(?,?,?,?,?,?)",p) ;
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil menambah ", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Gagal Menambah "+", Mohon periksa kembali", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Mohon isi dengan Benar", Toast.LENGTH_SHORT).show();
        }


    }

    public void reset(){
        EditText barang,harga,jumlah;
        barang=findViewById(R.id.eBarang);
        harga=findViewById(R.id.eHarga);
        jumlah=findViewById(R.id.eStok);
        barang.getText().clear();
        harga.getText().clear();
        jumlah.getText().clear();
        hargabeli = 0;

    }

    public void simpan(View view) {
        simpan();
        reset();
        getBarang("");
        setText();
    }

    public void ganti(View view) {
        String kondisi=cek();
        if (kondisi.equals("belum")){
            ModulBengkel.showToast(this,"Keranjang Masih Kosong");
        }else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            AlertDialog alert;
            alertDialog.setMessage("Apakah anda yakin untuk menyimpan pesanan ini dan mengganti pesanan lain ?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String qq ="UPDATE tblorder SET statusbayar='belum' WHERE idorder="+idjual;
                            db.exc(qq);
                            refresh();

                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            alert=alertDialog.create();

            alert.setTitle("Ganti Pesanan");
            alert.show();
        }

    }

    public void teknisi(View view) {
        Intent i = new Intent(MenuPenjualanJasa.this,MenuPilih.class);
        i.putExtra("type","teknisi");
        startActivityForResult(i,300);
    }

    public void kembali(View view) {
        finish();
    }

}